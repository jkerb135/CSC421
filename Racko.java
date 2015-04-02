/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Racko.java
 * Purpose:         Creates a new instance of the game and passes the Game
 * object the command line arguments.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Creates a new instance of the game and passes the Game object the command
 * line arguments.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Racko extends JApplet implements ActionListener {
    public volatile static boolean
            debug = false,
            show_rack = false,
            end_turns = false,
            load_deck = false,
            card_order = false,
            isDrawPileClicked = false,
            isDiscardPileClicked = false;

    private Timer drawTimer;

    private int winningScore = 500;
    private int maxScore = 0;
    private Players players = Players.getInstanceOf();
    private Player currentPlayer = null;
    private Deck theDeck;
    private boolean isRoundDone = false;
    private Thread gameThread;
    private boolean turnTaken = false;
    private boolean discardClicked;
    private boolean drawClicked;
    private int alpha = 255;
    private int increment = -5;

    private final MouseListener drawML = new CardListeners();
    private final MouseListener discML = new CardListeners();

    protected final CheatMenuPane cheatMenu = new CheatMenuPane();
    protected JPanel computerPanel = new JPanel(new CardLayout());
    protected JPanel playerPanel = new JPanel(null);
    protected JPanel deckPanel = new JPanel(null);
    protected JPanel sidePanel = new JPanel(null);
    protected JPanel glassPane;
    private Card discardPileBtn;
    private Card drawPileBtn;
    private JLabel[] names;
    private JLabel[] scores;
    public static int rack_size = 10;

    public void init() {
        setLayout(null);
        System.out.println("App Init");

        setLayout(null);
        setFocusable(true);
        addMouseListener(new paneListener());
        setBounds(0, 0, 800, 600);
        initDeck(getParameter("players").split(","));

        setBackground(Color.DARK_GRAY);

        glassPane = new GlassPane();
        setGlassPane(glassPane);

        addPlayers();
        initComponents();
        revalidate();
        repaint();

        setSize(800, 600);
    }

    public void start() {
        System.out.println("App Start");
        playGame();
    }

    public void initDeck(String[] args) {
        for (int i = 0, len = args.length; i < len; i++) {
            if (i == 0) {
                players.addPlayer(new HumanPlayer(args[i]));
            } else {
                players.addPlayer(new EasyComputer(args[i]));
            }
        }

        players.reset();
        theDeck = new Deck(players.getPlayers().size());
        theDeck.dealDeck(players);
    }

    public void initComponents() {
        deckPanel.setBackground(Color.DARK_GRAY);
        deckPanel.setBounds(0, 0, 550, 200);

        addDeck();

        sidePanel.setBounds(550, 0, 250, 200);
        sidePanel.setBackground(Color.DARK_GRAY);

        addSidePanelLabels();

        add(deckPanel);
        add(sidePanel);
    }

    public void addDeck() {
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
    }

    public void addSidePanelLabels() {
        names = new JLabel[players.getPlayers().size()];
        scores = new JLabel[names.length];

        int i = 0;
        for (int len = players.getPlayers().size(); i < len; i++) {
            Player p = players.getPlayer(i);
            JLabel name = new JLabel(p.getPlayerName());
            JLabel score = new JLabel(Integer.toString(p.getPlayerScore()));

            name.setFont(new Font("Serif", 1, 20));
            score.setFont(new Font("Serif", 1, 20));
            if (i == 0) {
                name.setBounds(20, 20, 110, 25);
                score.setBounds(name.getWidth() + name.getX(), 20, 110, 25);
            } else {
                Rectangle b = names[(i - 1)].getBounds();

                name.setBounds(b.x, b.y + (b.height + 10), 110, 25);
                score.setBounds(b.x + b.width, b.y + (b.height + 10), 110, 25);
            }
            names[i] = name;
            scores[i] = score;

            sidePanel.add(names[i]);
            sidePanel.add(scores[i]);
        }
    }

    public void addPlayers() {
        playerPanel.setBounds(0, 200, 400, 600);
        computerPanel.setBounds(400, 200, 400, 600);
        //Collections.shuffle(players.getPlayers());
        for (Player p : players.getPlayers()) {
            if ((p instanceof Computer)) {
                p.setBackground(Color.DARK_GRAY);
                p.Rack().showHideRack(false);
                computerPanel.add(p, p.getPlayerName());
            } else if ((p instanceof HumanPlayer)) {
                p.setBounds(0, 0, 400, 600);
                p.setBackground(Color.DARK_GRAY);
                playerPanel.add(p);
            }
            p.repaint();
        }
        add(playerPanel);
        add(computerPanel);
    }

    public void updateDeck() {
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

    public void updateSidePanel(int currentPlayerIdx) {
        int i = 0;
        for (int len = names.length; i < len; i++) {
            JLabel label = names[i];
            JLabel labelscr = scores[i];
            label.setText(label.getText().replace("\u27AD ", ""));
            if (i == currentPlayerIdx) {
                label.setText("\u27AD " + label.getText());
                label.setForeground(new Color(0, 204, 0));
                labelscr.setForeground(new Color(0, 204, 0));
            } else {
                label.setText(label.getText().replace("\u27AD ", ""));
                label.setForeground(Color.WHITE);
                labelscr.setForeground(Color.WHITE);
            }
        }
    }

    public void updateScores() {
        for (int i = 0, len = names.length; i < len; i++) {
            JLabel labelscr = scores[i];
            labelscr.setText(Integer.toString(players.getPlayer(i)
                    .getPlayerScore()));
        }
    }

    public void updateAfterRound() {
        deckPanel.removeAll();
        theDeck.reset();
        players.reset();
        theDeck = new Deck(players.getPlayers().size());
        theDeck.dealDeck(players);
        addDeck();
        repaint();
    }

    public void playGame() {
        gameThread = new Thread() {
            public void run() {
                while(maxScore < winningScore){
                    while (!isRoundDone) {
                        int i = 0;
                        int len = Players.getInstanceOf().getPlayers().size();
                        for (; i < len; i++) {
                            glassPane.setVisible(false);
                            CardLayout cl = (CardLayout) computerPanel.getLayout();

                            currentPlayer = players.getPlayer(i);
                            updateSidePanel(i);

                            if ((currentPlayer instanceof Computer)) {
                                glassPane.setVisible(true);
                                ((Computer) currentPlayer).doGuiTurn(drawPileBtn, discardPileBtn, theDeck);
                                cl.next(computerPanel);
                            }

                            while (!turnTaken) {
                                if (currentPlayer.the_rack.wasCardReplaced) {
                                    if (discardClicked) {
                                        Card c = theDeck.drawTopDiscard();
                                        theDeck.discard(c);
                                    } else {
                                        Card c = theDeck.drawTopCard();
                                        theDeck.discard(c);
                                    }
                                    turnTaken = true;
                                    currentPlayer.the_rack.wasCardReplaced = false;
                                    currentPlayer.the_rack.setCardInUse(null);
                                }
                            }

                            drawTimer.stop();
                            updateDeck();
                            repaint();

                            isRoundDone = currentPlayer.Rack().checkForRacko();

                            if (isRoundDone) {
                                currentPlayer.won_round = true;
                                Score.scoreRound();
                                maxScore = players.getHighestScore();
                                break;
                            }
                            resetFlags();
                        }
                    }
                    if(maxScore >= winningScore){
                        currentPlayer.won_game = true;
                        showWinningScreen();
                    }else {
                        updateScores();
                        resetFlags();
                        updateAfterRound();
                    }
                }
            }
        };
        gameThread.start();
    }

    private void resetFlags() {
        drawClicked = false;
        discardClicked = false;
        turnTaken = false;
        isRoundDone = false;
    }

    public void showWinningScreen(){
        getContentPane().removeAll();
        JPanel winningScreen = new JPanel(null);
        winningScreen.setBounds(0,0,800,600);
        setContentPane(winningScreen);
        winningScreen.setBackground(Color.DARK_GRAY);

        JLabel gameOver = new JLabel("Game Over");
        gameOver.setForeground(Color.WHITE);
        gameOver.setBounds(0, 0, 800, 50);
        gameOver.setFont(new Font("Serif", 1, 55));
        gameOver.setHorizontalAlignment(SwingConstants.CENTER);

        Dimension d = new Dimension();
        JLabel[][] winNames = new JLabel[players.getPlayers().size()][2];
        for(int i = 0, len = players.getPlayers().size(); i < len; i++) {
            Player p = players.getPlayer(i);
            JLabel name = new JLabel(p.getPlayerName());
            JLabel score = new JLabel(Integer.toString(p.getPlayerScore()));

            name.setBounds(325, 150 + (i * 35), 100, 25);
            score.setBounds(435, 150 + (i * 35), 100, 25);
            name.setFont(new Font("Serif", 1, 25));
            score.setFont(new Font("Serif", 1, 25));
            name.setForeground(Color.WHITE);
            score.setForeground(Color.WHITE);

            winNames[i][0] = name;
            winNames[i][1] = score;

        }

        Arrays.sort(winNames, new Comparator<JLabel[]>() {
            @Override
            public int compare(JLabel[] o1, JLabel[] o2) {
                final Integer score1 = Integer.parseInt(o1[1].getText());
                final Integer score2 = Integer.parseInt(o2[1].getText());
                return score2.compareTo(score1);
            }

        });

        winNames[0][0].setForeground(Color.GREEN);
        winNames[0][1].setForeground(Color.GREEN);

        for (final JLabel[] s : winNames) {
            getContentPane().add(s[0]);
            getContentPane().add(s[1]);
        }

        getContentPane().add(gameOver);
        repaint();


    }

    public void actionPerformed(ActionEvent e) {
        final Card c = (Card) e.getSource();
        if (e.getActionCommand() == "Deck Clicked") {
            drawClicked = true;
            c.setCardDirection(true);
            currentPlayer.Rack().setCardInUse(theDeck.getTopDraw());
            drawTimer = flashBorder(c);
            drawTimer.start();
        } else if (e.getActionCommand() == "Draw Clicked") {
            if (drawClicked) {
                Card top = theDeck.drawTopCard();
                theDeck.discard(top);
                turnTaken = true;
                currentPlayer.Rack().setCardInUse(null);
                drawTimer.stop();
            } else {
                discardClicked = true;
                drawPileBtn.setEnabled(false);
                drawPileBtn.removeMouseListener(drawML);
                currentPlayer.Rack().setCardInUse(theDeck.getTopDisc());
                drawTimer = flashBorder(c);
                drawTimer.start();
            }
        }
    }

    private Timer flashBorder(final Card c) {
        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += increment;
                if (alpha >= 255) {
                    alpha = 255;
                    increment = -increment;
                }
                if (alpha <= 0) {
                    alpha = 0;
                    increment = -increment;
                }
                c.setBorder(BorderFactory.createLineBorder(new Color(0, 204, 0, alpha), 4));
            }
        });
        return timer;
    }

    class paneListener implements MouseListener {

        public void mouseClicked(MouseEvent mouseEvent) {
            if (SwingUtilities.isRightMouseButton(mouseEvent)) {
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

        public void mousePressed(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
        }

        public void mouseExited(MouseEvent mouseEvent) {
        }
    }
}
