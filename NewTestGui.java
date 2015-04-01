import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class NewTestGui extends JPanel implements ActionListener
{
    public static boolean
            debug = false,
            show_rack = false,
            end_turns = false,
            load_deck = false,
            card_order = false,
            isDrawPileClicked = false,
            isDiscardPileClicked = false;

    private int winningScore = 500;
    private int maxScore = 0;
    private Players players = Players.getInstanceOf();
    private Player currentPlayer = null;
    private Deck theDeck;
    private boolean isRoundDone = false;
    private Thread gameThread;
    private volatile boolean turnTaken = false;
    private boolean discardClicked;
    private boolean drawClicked;

    private final MouseListener drawML = new drawMouseListener();
    private final MouseListener discML = new discardMouseListener();

    protected final CheatMenuPane cheatMenu = new CheatMenuPane();
    protected JPanel computerPanel = new JPanel(new CardLayout());
    protected JPanel playerPanel = new JPanel(null);
    protected JPanel deckPanel = new JPanel(null);
    protected JPanel sidePanel = new JPanel(null);
    private Card discardPileBtn;
    private Card drawPileBtn;
    private JLabel[] names;
    private JLabel[] scores;

    public NewTestGui(String[] args)
    {
        setLayout(null);
        setFocusable(true);
        addMouseListener(new paneListener());
        setBounds(0, 0, 800, 600);
        initDeck(args);
        addPlayers();
        addDeckPanel();
        addSidePanel();
    }

    public void initDeck(String[] args)
    {
        for(int i = 0, len = args.length; i < len; i++){
            if(i == 0){
                players.addPlayer(new HumanPlayer(args[i]));
            }
            else{
                players.addPlayer(new EasyComputer(args[i]));
            }
        }

        players.reset();
        theDeck = new Deck(this.players.getPlayers().size());
        theDeck.dealDeck(this.players);
    }

    public void addDeckPanel()
    {
        deckPanel.setBackground(Color.DARK_GRAY);
        deckPanel.setBounds(0, 0, 550, 200);

        drawPileBtn = theDeck.getTopDraw();
        drawPileBtn.setCardDirection(false);
        drawPileBtn.setBounds(90, 55, 180, 90);
        drawPileBtn.setActionCommand("Deck Clicked");
        drawPileBtn.addActionListener(this);
        drawPileBtn.addMouseListener(drawML);

        discardPileBtn = theDeck.getTopDisc();
        Rectangle b = drawPileBtn.getBounds();
        discardPileBtn.setBounds(b.x + (b.width + 10), b.y, 180, 90);
        discardPileBtn.setActionCommand("Draw Clicked");
        discardPileBtn.addActionListener(this);
        discardPileBtn.addMouseListener(discML);

        deckPanel.add(drawPileBtn);
        deckPanel.add(discardPileBtn);
        add(deckPanel);
    }

    public void addSidePanel()
    {
        sidePanel.setBounds(550, 0, 250, 200);
        sidePanel.setBackground(Color.DARK_GRAY);

        names = new JLabel[players.getPlayers().size()];
        scores = new JLabel[names.length];

        int i = 0;
        for (int len = players.getPlayers().size(); i < len; i++)
        {
            Player p = players.getPlayer(i);
            JLabel name = new JLabel(p.getPlayerName());
            JLabel score = new JLabel(Integer.toString(p.getPlayerScore()));

            name.setFont(new Font("Serif", 1, 20));
            score.setFont(new Font("Serif", 1, 20));
            if (i == 0)
            {
                name.setBounds(20, 20, 110, 25);
                score.setBounds(name.getWidth() + name.getX(), 20, 110, 25);
            }
            else
            {
                Rectangle b = names[(i - 1)].getBounds();

                name.setBounds(b.x, b.y + (b.height + 10), 110, 25);
                score.setBounds(b.x + b.width, b.y + (b.height + 10), 110, 25);
            }
            names[i] = name;
            scores[i] = score;

            sidePanel.add(names[i]);
            sidePanel.add(scores[i]);
        }
        add(sidePanel);
    }

    public void addPlayers()
    {
        playerPanel.setBounds(0, 200, 400, 600);
        computerPanel.setBounds(400, 200, 400, 600);
        for (Player p : players.getPlayers())
        {
            if ((p instanceof Computer))
            {
                p.setBackground(Color.DARK_GRAY);
                p.Rack().showHideRack(false);
                computerPanel.add(p, p.getPlayerName());
            }
            else if ((p instanceof HumanPlayer))
            {
                p.setBounds(0, 0, 400, 600);
                p.setBackground(Color.DARK_GRAY);
                playerPanel.add(p);
            }
            p.repaint();
        }
        add(playerPanel);
        add(computerPanel);
    }

    public void updateDeck()
    {
        deckPanel.invalidate();
        discardPileBtn.invalidate();

        drawPileBtn.setText(theDeck.getTopDraw().cardValue.toString());
        drawPileBtn.setEnabled(true);
        drawPileBtn.setCardDirection(false);
        drawPileBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        drawPileBtn.addMouseListener(drawML);

        discardPileBtn.setText(theDeck.getTopDisc().cardValue.toString());
        discardPileBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        discardPileBtn.addMouseListener(discML);

        drawPileBtn.repaint();
        discardPileBtn.repaint();
    }

    public void updateSidePanel(int currentPlayerIdx)
    {
        int i = 0;
        for (int len = names.length; i < len; i++)
        {
            JLabel label = names[i];
            JLabel labelscr = scores[i];
            if (i == currentPlayerIdx)
            {
                label.setText("\u27AD " + label.getText());
                label.setForeground(Color.CYAN);
                labelscr.setForeground(Color.CYAN);
            }
            else
            {
                label.setText(label.getText().replace("\u27AD ", ""));
                label.setForeground(Color.BLACK);
                labelscr.setForeground(Color.BLACK);
            }
        }
    }

    public void playGame()
    {
        gameThread = new Thread()
        {
            public void run() {
                while (maxScore < winningScore) {
                    while (!isRoundDone) {
                        int i = 0;
                        int len = Players.getInstanceOf().getPlayers().size();
                        for (; i < len; i++) {
                            CardLayout cl = (CardLayout) computerPanel.getLayout();

                            theDeck.printDeck();
                            theDeck.printDiscard();

                            currentPlayer = players.getPlayer(i);
                            updateSidePanel(i);
                            if ((currentPlayer instanceof Computer)) {
                                removeMouseListeners(drawPileBtn);
                                removeMouseListeners(discardPileBtn);

                                boolean decision = ((Computer) currentPlayer).whichPile(theDeck);
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (decision) {
                                    drawPileBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                                    drawPileBtn.doClick();
                                } else {
                                    discardPileBtn.doClick();
                                }
                                ((Computer) currentPlayer).doGuiTurn(decision, theDeck);

                                cl.next(computerPanel);
                            }

                            while (!turnTaken) {
                                if (currentPlayer.the_rack.wasCardReplaced) {
                                    if (discardClicked) {
                                        Card c = theDeck.drawTopDiscard();
                                        System.out.println(c.cardValue);
                                        theDeck.discard(c);
                                    } else {
                                        Card c = theDeck.drawTopCard();
                                        System.out.println(c.cardValue);
                                        theDeck.discard(c);
                                    }
                                    turnTaken = true;
                                    currentPlayer.the_rack.wasCardReplaced = false;
                                    currentPlayer.the_rack.setCardInUse(null);
                                }
                            }

                            isRoundDone = currentPlayer.Rack().checkForRacko();

                            resetFlags();
                            updateDeck();
                            repaint();
                        }
                    }
                }
            }
        };
        gameThread.start();
    }

    private void resetFlags()
    {
        drawClicked = false;
        discardClicked = false;
        turnTaken = false;
    }

    private void removeMouseListeners(Card c)
    {
        for (MouseListener m : c.getMouseListeners()) {
            if ((m == discML) || (m == drawML)) {
                c.removeMouseListener(m);
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        Card c = (Card)e.getSource();
        if (e.getActionCommand() == "Deck Clicked")
        {
            drawClicked = true;
            c.setCardDirection(true);
            currentPlayer.Rack().setCardInUse(theDeck.getTopDraw());
        }
        else if (e.getActionCommand() == "Draw Clicked")
        {
            if (drawClicked)
            {
                Card top = theDeck.drawTopCard();
                theDeck.discard(top);
                turnTaken = true;
                currentPlayer.Rack().setCardInUse(null);
            }
            else
            {
                discardClicked = true;
                drawPileBtn.setEnabled(false);
                drawPileBtn.removeMouseListener(drawML);
                currentPlayer.Rack().setCardInUse(theDeck.getTopDisc());
            }
        }
    }
    class paneListener implements MouseListener
    {

        public void mouseClicked(MouseEvent mouseEvent)
        {
            if(SwingUtilities.isRightMouseButton(mouseEvent)){
                    Object[] options = {"OK"};
                    JOptionPane.showOptionDialog(null,
                        cheatMenu,
                            "Cheats Menu",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                mouseEvent.consume();
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {}

        public void mouseReleased(MouseEvent mouseEvent) {}

        public void mouseEntered(MouseEvent mouseEvent) {}

        public void mouseExited(MouseEvent mouseEvent)
        {
        }
    }

    class drawMouseListener implements MouseListener
    {

        public void mouseClicked(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            c.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            mouseEvent.consume();
        }

        public void mousePressed(MouseEvent mouseEvent) {}

        public void mouseReleased(MouseEvent mouseEvent) {}

        public void mouseEntered(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            if (!drawClicked) {
                c.setBorder(BorderFactory.createLineBorder(Color.cyan, 2));
            }
            mouseEvent.consume();
        }

        public void mouseExited(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            if (!drawClicked) {
                c.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
            mouseEvent.consume();
        }
    }

    class discardMouseListener implements MouseListener
    {
        public void mouseClicked(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            c.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            mouseEvent.consume();
        }

        public void mousePressed(MouseEvent mouseEvent) {}

        public void mouseReleased(MouseEvent mouseEvent) {}

        public void mouseEntered(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            if (!discardClicked) {
                c.setBorder(BorderFactory.createLineBorder(Color.cyan, 2));
            }
            mouseEvent.consume();
        }

        public void mouseExited(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            if (!discardClicked) {
                c.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
            mouseEvent.consume();
        }
    }
}
