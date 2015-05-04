/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        4/3/2015
 * Assignment:      Assignment 2
 * Filename:        Racko.java
 * Purpose:         Creates the GUI for user interaction between the backend
 * code. This game is in the form of an applet and is hosted online. The game
 * can be played with 2-4 players. Only one human player is allowed right now
 * but the change necessary to make more humans is minuscule.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Creates the GUI for user interaction between the backend
 * code. This game is in the form of an applet and is hosted online. The game
 * can be played with 2-4 players. Only one human player is allowed right now
 * but the change necessary to make more humans is minuscule.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 * @see JApplet
 * @see ActionListener
 */
public class Racko extends JApplet implements ActionListener, Runnable {
    public volatile static boolean
            debug = false,
            round_cheat = false;

    public volatile static int round_limit = 0;
    public int roundCounter = 0;

    private Timer drawTimer;
    private DragSource source;
    private TransferHandler transferHandler;

    private final int winningScore = 500;
    private int maxScore = 0;
    private final Players players = Players.getInstanceOf();
    private Player currentPlayer = null;
    private Deck theDeck;
    private boolean isRoundDone = false;
    private boolean turnTaken = false;
    private boolean discardClicked;
    private boolean drawClicked;
    private int alpha = 255;
    private int increment = -5;

    private final MouseListener drawML = new CardListeners();
    private final MouseListener discML = new CardListeners();

    private final CheatMenuPane cheatMenu;
    private final JPanel computerPanel;
    private final JPanel playerPanel;
    private final JPanel deckPanel;
    private final JPanel sidePanel;
    public JPanel glassPane;
    private final JPanel waitScreen;
    private JButton ready  = new JButton("Ready");
    private Card discardPileBtn;
    private JTextArea chatBox = new JTextArea();
    private JTextField messageBox = new JTextField();
    private Card drawPileBtn;
    private JLabel[] names;
    private JLabel[] scores;
    public static final int rack_size = 10;
    private boolean isOnline;

    private InetAddress ipAddress;
    private Socket socket;
    private Thread clientThread;
    private static String username;

    private final String hostName = "acad.kutztown.edu";
    private final int port = 15012;
    PrintWriter out = null;
    BufferedReader in = null;

    /**
     * Initializes the Panels to their respective elements.
     */
    public Racko() {
        cheatMenu = new CheatMenuPane();
        computerPanel = new JPanel(new CardLayout());
        playerPanel = new JPanel(null);
        sidePanel = new JPanel(null);
        deckPanel = new JPanel(null);
        waitScreen = new JPanel(null);
    }

    /**
     * Initializes the applet and sets the glassPane for use with AI turns.
     * Initializes the GUI components needed to interact with the game.
     */
    public void init() {
        setLayout(null);
        setFocusable(true);
        addMouseListener(new paneListener());
        setBounds(0, 0, 800, 600);

        System.out.println(getParameter("online"));
        isOnline = getParameter("online").equalsIgnoreCase("true") ? true : false;

        String players[] = null;
        if(isOnline){
            this.setContentPane(waitScreen);
            Container content = this.getContentPane();
            content.setSize(800, 600);
            content.setBackground(Color.DARK_GRAY);

            this.messageBox.addActionListener(this);
            this.ready.addActionListener(this);

            this.chatBox.setBounds(0, 0, 800, 300);
            this.chatBox.setEditable(false);
            this.messageBox.setBounds(0, 300, 800, 25);
            this.ready.setBounds(350,400,125,50);

            try {
                this.socket = new Socket();
                this.ipAddress = InetAddress.getByName(hostName);

                System.out.println(ipAddress);
                clientThread = new Thread(this);
                clientThread.start();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            //tryLogin(true);

            content.add(this.chatBox);
            content.add(this.messageBox);
            content.add(this.ready);
            //Show wait screen

            //Get players from server

            //Get seed from server

        }
        else {
            players = getParameter("players").split(",");
            initDeck(players);
            glassPane = new GlassPane();
            setGlassPane(glassPane);
            addPlayers();
            initComponents();
            revalidate();
            repaint();
        }

        setSize(800, 600);
    }

    /**
     * Starts the applet and game by calling the playGame() method.
     */
    public void start() {

        playGame();
    }

    /**
     * Initializes the deck with the passed in parameters through the HTML form.
     * Deals the deck to the players in the game.
     * @param args
     */
    private void initDeck(String[] args) {
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

    /**
     * Initializes the look and feel of the GUI components.
     */
    private void initComponents() {
        deckPanel.setBackground(Color.DARK_GRAY);
        deckPanel.setBounds(0, 0, 550, 200);

        addDeck();

        sidePanel.setBounds(550, 0, 250, 200);
        sidePanel.setBackground(Color.DARK_GRAY);

        addSidePanelLabels();

        add(deckPanel);
        add(sidePanel);
    }

    /**
     * Sets up the JButtons for the Draw and Discard GUI elements. Add their
     * respective action listeners, mouse listeners and Action Commands.
     */
    private void addDeck() {
        drawPileBtn = theDeck.getTopDraw();
        drawPileBtn.setCardDirection(false);
        drawPileBtn.setBounds(90, 55, 180, 90);
        drawPileBtn.setActionCommand("Deck Clicked");
        drawPileBtn.addActionListener(this);
        drawPileBtn.addMouseListener(drawML);
        drawPileBtn.setTransferHandler(new TransferHandler(drawPileBtn.getText()));

        discardPileBtn = theDeck.getTopDisc();
        Rectangle b = drawPileBtn.getBounds();
        discardPileBtn.setBounds(b.x + (b.width + 10), b.y, 180, 90);
        discardPileBtn.setActionCommand("Draw Clicked");
        discardPileBtn.addActionListener(this);
        discardPileBtn.addMouseListener(discML);
        discardPileBtn.setTransferHandler(new TransferHandler(discardPileBtn.getText()));

        deckPanel.add(drawPileBtn);
        deckPanel.add(discardPileBtn);
    }

    /**
     * Adds the score panel to the side off the applet. This is updated after
     * every round to show who has the highest score.
     */
    private void addSidePanelLabels() {
        names = new JLabel[players.getPlayers().size()];
        scores = new JLabel[names.length];

        int i = 0;
        for (int len = players.getPlayers().size(); i < len; i++) {
            Player p = players.getPlayer(i);
            JLabel name = new JLabel(p.getPlayerName());
            JLabel score = new JLabel(Integer.toString(p.getPlayerScore()));

            name.setFont(new Font("Serif", Font.BOLD, 20));
            score.setFont(new Font("Serif", Font.BOLD, 20));
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

    /**
     * Adds the players racks to the GUI. The human player is added to the
     * bottom left and every other computer player is added to the bottom
     * right inside of a card layout which changes based on whose turn it is.
     */
    private void addPlayers() {
        playerPanel.setBounds(0, 200, 400, 600);
        computerPanel.setBounds(400, 200, 400, 600);
        Collections.shuffle(players.getPlayers());
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

    /**
     * Updates the draw and discard pile to their correct values after
     * interaction has occurred.
     */
    private void updateDeck() {
        deckPanel.invalidate();
        discardPileBtn.invalidate();

        drawPileBtn.setText(theDeck.getTopDraw().cardValue.toString());
        drawPileBtn.setEnabled(true);
        drawPileBtn.setCardDirection(false);
        drawPileBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        drawPileBtn.addMouseListener(drawML);

        discardPileBtn.setText(theDeck.getTopDisc().cardValue.toString());
        discardPileBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK,
                2));
        discardPileBtn.addMouseListener(discML);

        drawPileBtn.repaint();
        discardPileBtn.repaint();
    }

    /**
     * Updates the sidepanel to show the current player who is currently
     * taking their turn. Current player is highlighted in green and has an
     * arrow in front.
     * @param currentPlayerIdx
     */
    private void updateSidePanel(int currentPlayerIdx) {
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

    /**
     * Updates the scores in the side panel after a player has gone racko
     */
    private void updateScores() {
        for (int i = 0, len = names.length; i < len; i++) {
            JLabel labelscr = scores[i];
            labelscr.setText(Integer.toString(players.getPlayer(i)
                    .getPlayerScore()));
        }
    }

    /**
     * Reinitializes the GUI after a round has ended. This method redeals the
     * cards resets the rack and adds the deck back onto the gui.
     */
    private void updateAfterRound() {
        deckPanel.removeAll();
        theDeck.reset();
        players.reset();
        theDeck = new Deck(players.getPlayers().size());
        theDeck.dealDeck(players);
        addDeck();
        repaint();
    }

    /**
     * Simulates a round of play. The round is over when the doGuiTurn method
     * returns that the player went racko. A flag is then set inside the
     * player class that that player went racko.
     * @see Players#reset
     * @see Deck
     * @see Deck#drawTopCard
     * @see Deck#drawTopDiscard
     * @see Computer#doGuiTurn
     */
    private void playGame() {
        Thread gameThread = new Thread() {
            public void run() {
                while (maxScore < winningScore) {
                    while (!isRoundDone) {
                        int i = 0;
                        int len = Players.getInstanceOf().getPlayers().size();
                        for (; i < len; i++) {
                            glassPane.setVisible(false);
                            CardLayout cl = (CardLayout) computerPanel
                                    .getLayout();

                            currentPlayer = players.getPlayer(i);
                            updateSidePanel(i);

                            if ((currentPlayer instanceof Computer)) {
                                glassPane.setVisible(true);
                                ((Computer) currentPlayer).doGuiTurn
                                        (drawPileBtn, discardPileBtn, theDeck);
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
                                currentPlayer = players.getPlayer(0);
                                break;
                            }
                            resetFlags();
                        }
                        roundCounter++;
                        if((round_cheat && roundCounter != round_limit)){
                            showWinningScreen();
                        }
                    }
                    if(maxScore >= winningScore){
                        currentPlayer.won_game = true;
                        showWinningScreen();
                    }
                    else{
                        updateScores();
                        resetFlags();
                        updateAfterRound();
                    }
                }
            }
        };
        gameThread.start();
    }

    /**
     * Resets the game state flags after a round is over
     */
    private void resetFlags() {
        drawClicked = false;
        discardClicked = false;
        turnTaken = false;
        isRoundDone = false;
    }

    /**
     * On Game end erases the content pane and shows a winning screen with
     * the scores of the played game. Has the option to play again
     */
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

        /*JButton playAgain = new JButton("Play Again?");
        playAgain.setBounds(325,400,100,25);
        playAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().repaint();
                players.reset();

                initComponents();
                addPlayers();
                start();
                try {
                    getAppletContext().showDocument(new URL("http:acad" +
                            ".kutztown.edu/~jkerb135/CSC421/"));
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
            }
        });*/

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
        //getContentPane().add(playAgain);
        repaint();


    }

    /**
     * Actoin Listeners for the draw and deck pile. Check the action command
     * of the card and allows the user to interact with the gui according to
     * the game rules.
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof Card){
            final Card c = (Card) e.getSource();
            if (e.getActionCommand().equals("Deck Clicked")) {
                drawClicked = true;
                c.setCardDirection(true);
                currentPlayer.Rack().setCardInUse(theDeck.getTopDraw());
                drawTimer = flashBorder(c);
                drawTimer.start();
            } else if (e.getActionCommand().equals("Draw Clicked")) {
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
        else if(e.getSource() instanceof JTextField){
            String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
            out.println("[ " + username + " " + timeStamp + " ]:\t" + messageBox.getText());
            messageBox.setText("");
        }
        else if(e.getSource() instanceof JButton){
            out.println(username + " is ready to play");
        }
    }

    /**
     * Shows a flashing border after the AI's move to let the players know
     * what the AI did during its turn.
     * @param c
     * @return timer
     */
    private Timer flashBorder(final Card c) {
        return new Timer(30, new ActionListener() {
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
                c.setBorder(BorderFactory.createLineBorder(new Color(0, 204,
                        0, alpha), 4));
            }
        });
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(hostName,port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Right Click listener to show the cheats option menu.
     */
    private class paneListener implements MouseListener {

        public void mouseClicked(MouseEvent mouseEvent) {
            if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                Object[] options = {"OK"};
                JOptionPane.showOptionDialog(null,
                        cheatMenu,
                        "Cheats Menu",
                        JOptionPane.DEFAULT_OPTION,
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
