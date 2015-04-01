/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Game.java
 * Purpose:         This class will setup and govern the gameplay of racko.
 * The cheats are taken care of in the constructor of the class which sets
 * public boolean flags for package wide use.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class will setup and govern the gameplay of racko.
 * The cheats are taken care of in the constructor of the class which sets
 * public boolean flags for package wide use.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Game extends JPanel implements MouseListener{
    public static boolean
            debug = true,
            show_rack = false,
            end_turns = false,
            load_deck = false,
            card_order = false,
            isDrawPileClicked = false,
            isDiscardPileClicked = false;

    public static int
            end_turn_value = 0,
            card_order_value = 0,
            round_counter = 0;

    public static List<Integer> loaded_cards = new ArrayList<Integer>();

    private Deck theDeck;

    private int max_score,
            winning_score = 500;

    private boolean isRoundDone, isGameDone;

    private Player current;

    private JPanel currentPlayers = new JPanel(null);
    private JPanel leftPanel = new JPanel(null);
    private JPanel rightPanel = new JPanel(new CardLayout());
    private JPanel[] cards;
    private JLabel[] playerNames;
    private JLayeredPane leftLayer, rightLayer;
    private Players players = Players.getInstanceOf();
    private Card drawPileBtn, discardPileBtn;
    private MouseListener drawPileListener = new drawPileMouseListener();
    private MouseListener discardPileListener = new discardPileListener();


    public Game(String[] player, String[] type){
        System.out.print(player.length);

        for(int i = 0, len = player.length; i < len; i++){
            if(type[i] == "0"){
                players.addPlayer(new HumanPlayer(player[i]));
            }
            else{
                players.addPlayer(new EasyComputer(player[i]));
            }
        }

        players.reset();

        theDeck = new Deck(Players.getInstanceOf().getPlayers().size());
        theDeck.dealDeck(Players.getInstanceOf());
        createGui();
    }
    /**
     * The constructor sets the boolean flags for the cheats and then calls
     * the Display class to prompt the user for their input. After the user
     * specifies their input playRound is called to start the game.
     * @see Display
     * @see Game#playRound
     * @param args These are the passed in command line cheat codes.
     * @throws IOException Invalid User Menu Input
     */
    public Game(String[] args) throws IOException {
        if (args.length != 0) {
            debug = Arrays.asList(args).contains("/d");
            show_rack = Arrays.asList(args).contains("/c");
            end_turns = Arrays.asList(args).contains("/n");
            card_order = Arrays.asList(args).contains("/s");
            load_deck = Arrays.asList(args).contains("/o");

            if (debug) {
                DebugStream.activate();
                Output.Yellowln
                        ("DEBUGGING ON ~DEBUG TEXT SHOWN IN YELLOW");
                Output.Yellowln
                        ("DEBUGGING STREAM ACTIVE PRINTING CLASS AND LINE " +
                                "NUMBERS");
                Output.Yellowln
                        ("\t SEED for Random Number Generator  is " +
                                "also set");
            }
            if (show_rack) {
                Output.Blueln("SHOWING OPPONENTS RACK");
            }
            if (end_turns) {
                int idx = Arrays.asList(args).indexOf("/n");
                end_turn_value = Integer.parseInt(args[idx + 1]);
                Output.Blueln("GAME OVER IN " + end_turn_value +
                        " TURN(S)");
            }
            if (card_order) {
                int idx = Arrays.asList(args).indexOf("/s");
                card_order_value = Integer.parseInt(args[idx + 1]);
                Output.Blueln("RACK ORDER CHEAT");
            }
            if (load_deck) {
                int idx = Arrays.asList(args).indexOf("/o");
                for (int i = (idx + 1), len = args.length; i < len; i++) {
                    if (args[i].contains("/")) break;
                    loaded_cards.add(Integer.parseInt(args[i]));
                }
                Output.Blueln("LOADED DECK CHEAT");
            }
        }
        System.out.println();

        if (Game.debug) {
            Players.getInstanceOf().addPlayer(new HumanPlayer("Josh"));
            Players.getInstanceOf().addPlayer(new EasyComputer("Bob"));
        } else {
            Display.getInstanceOf().mainMenu();
        }

        Players players = Players.getInstanceOf();
        players.reset();

        theDeck = new Deck(Players.getInstanceOf().getPlayers().size());
        theDeck.dealDeck(Players.getInstanceOf());

        createGui();

        do {
            max_score = playRound(players,theDeck);

            if (max_score >= winning_score) {
                Output.Cyanln("\nGame Winner: \t" + Players
                        .getInstanceOf().getWinner() + " with a score of " +
                        Players.getInstanceOf().getHighestScore());
            }

            if(isRoundDone){
                players.reset();
                theDeck.reset();
                theDeck.dealDeck(Players.getInstanceOf());
            }
        }while(max_score < winning_score);
    }

    /**
     * Initilizes the players Racks and then creates a new Deck Object.
     * Simulates a round of play. The round is over when the doTurn method
     * returns that the player went racko. A flag is then set inside the
     * player class that that player went racko.
     * @see Players#reset
     * @see Deck
     * @see Deck#dealDeck
     * @see Player#doTurn
     * @return HighestScore
     * @throws IOException Invalid User Menu Input
     * @param players the list of players
     * @param theDeck the current games deck
     */
    private int playRound(Players players, Deck theDeck) throws
    IOException {
        for (int currentPlayer = 0, len = Players.getInstanceOf()
                .getPlayers
                ().size(); currentPlayer < len; currentPlayer++) {
            current = players.getPlayer(currentPlayer);

            Output.Greenln("Current Player: " +
                    current.getPlayerName() + "\n");

            if(end_turns && end_turn_value == round_counter){
                Score.scoreRound();
                Output.Cyanln("\nRound Limit Hit");
                Output.Cyanln("\n" + Players.getInstanceOf()
                        .getGameScores());
                System.exit(0);
            }

            isRoundDone = current.doTurn(theDeck);
            if(isRoundDone){
                current.won_round = true;
                Score.scoreRound();

                Output.Cyanln("\nRound Winner: \t" + Players
                        .getInstanceOf().getWinner() + " with a score of " +
                        Players.getInstanceOf().getHighestScore());

                Output.Cyanln("\nGame Score Is " + Players.getInstanceOf()
                        .getGameScores());

                return Players.getInstanceOf().getHighestScore();
            }
        }
        round_counter++;
        return Players.getInstanceOf().getHighestScore();
    }



    public void createGui(){
        setLayout(null);
        leftPanel.setBounds(0, 0, 400, 600);
        leftPanel.setBackground(Color.YELLOW);

        rightPanel.setBounds(400, 0, 400, 600);
        rightPanel.setBackground(Color.CYAN);

        playerNames = new JLabel[players.getPlayers().size()];
        for(int i = 0, len = players.getPlayers().size(); i < len; i++){
            playerNames[i] = new JLabel(players.getPlayer(i).getPlayerName());
            if(i == 0) {
                playerNames[i].setBounds(10, 10, 50, 20);
            }
            else{
                playerNames[i].setBounds(playerNames[i - 1].getWidth() + 10, 10,
                        100, 20);
            }
            currentPlayers.add(playerNames[i]);
        }
        currentPlayers.setBounds(0,0,400,50);
        leftPanel.add(currentPlayers);

        cards = new JPanel[players.getPlayers().size()];
        Color[] color = {Color.GREEN,Color.blue,Color.CYAN};
        for(int i = 0, len = players.getPlayers().size(); i < len; i++){
            if(i == 0){
                //leftLayer = players.getPlayer(i).Rack();
                leftLayer.setBounds(0,325,400,600);
                leftPanel.add(leftLayer);
            }
            else{
                JPanel card;
                card = new JPanel(null);
                card.setBackground(color[i - 1]);
               // rightLayer = players.getPlayer(i).Rack();
                players.getPlayer(i).Rack().showHideRack(false);
                rightLayer.setBounds(50, 10, 400, 600);
                card.add(rightLayer, players.getPlayer(i).getPlayerName());
                JLabel curr = new JLabel(players.getPlayer(i).getPlayerName
                                ());
                curr.setBounds(0,0,100,100);
                curr.setBackground(Color.white);
                        card.add(curr);
                rightPanel.add(card);
            }
        }

        add(leftPanel);
        add(rightPanel);

        theDeck.printDeck();
        theDeck.printDiscard();


        drawPileBtn = theDeck.getTopDraw();
        discardPileBtn = theDeck.getTopDisc();

        drawPileBtn.setBounds(10, 75, 180, 90);
        discardPileBtn.setBounds(210, 75, 180, 90);

        drawPileBtn.addMouseListener(drawPileListener);
        discardPileBtn.addMouseListener(discardPileListener);

        leftPanel.add(drawPileBtn);
        leftPanel.add(discardPileBtn);


        setSize(800, 600);
        setVisible(true);

        }

    public void updateGui(){
        leftPanel.remove(drawPileBtn);
        leftPanel.remove(discardPileBtn);
        leftPanel.remove(leftLayer);
        leftPanel.invalidate();

        discardPileBtn = theDeck.getTopDisc();
        drawPileBtn = theDeck.getTopDraw();

        drawPileBtn.setBounds(10, 75, 180, 90);
        discardPileBtn.setBounds(210, 75, 180, 90);

        updateRack();

        removeMouseListeners(drawPileBtn);
        removeMouseListeners(discardPileBtn);

        drawPileBtn.addMouseListener(drawPileListener);
        discardPileBtn.addMouseListener(discardPileListener);

        leftPanel.add(drawPileBtn);
        leftPanel.add(discardPileBtn);

        leftPanel.revalidate();
        leftPanel.repaint();

    }

    public void updateRack(){
        for(int i = 0, len = players.getPlayers().size(); i < len; i++){
            if(i == 0){
                //leftLayer = players.getPlayer(i).Rack();
                leftLayer.setBounds(0,325,400,600);
                leftPanel.add(leftLayer);
            }
            else{
                JPanel card;
                card = new JPanel(null);
                //leftLayer = players.getPlayer(i).Rack();
                players.getPlayer(i).Rack().showHideRack(false);
                leftLayer.setBounds(50, 10, 400, 600);
                card.add(leftLayer, players.getPlayer(i).getPlayerName());
                JLabel curr = new JLabel(players.getPlayer(i).getPlayerName
                        ());
                curr.setBounds(0,0,100,100);
                curr.setBackground(Color.white);
                card.add(curr);
                rightPanel.add(card);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getSource());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class drawPileMouseListener implements MouseListener{
            @Override
            public void mouseClicked(MouseEvent e) {
                isDrawPileClicked = true;
                //players.getPlayer(0).Rack().setCardInUse(drawPileBtn.getCard
                        //(), );
                drawPileBtn.setCardDirection(true);
                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                drawPileBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                drawPileBtn.setBorder(BorderFactory.createLineBorder
                        (Color.RED, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                drawPileBtn.setBorder(BorderFactory.createLineBorder(Color
                        .black, 2));
            }
    }

    private class discardPileListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(isDrawPileClicked){
                theDeck.discard(theDeck.drawTopCard());
                updateGui();
            }
            isDrawPileClicked = false;
            e.consume();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            discardPileBtn.setBorder(BorderFactory.createLineBorder
                    (Color.RED, 2));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            discardPileBtn.setBorder(BorderFactory.createLineBorder(Color
                    .black, 2));
        }
    }

    private void removeMouseListeners(Card theCard){
        for(MouseListener m : theCard.getMouseListeners()){
            theCard.removeMouseListener(m);
        }
    }
}
