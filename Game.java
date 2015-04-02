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
public class Game{
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
            Players.getInstanceOf().addPlayer(new EasyComputer("Josh"));
            Players.getInstanceOf().addPlayer(new EasyComputer("Bob"));
        } else {
            Display.getInstanceOf().mainMenu();
        }

        Players players = Players.getInstanceOf();
        players.reset();

        theDeck = new Deck(Players.getInstanceOf().getPlayers().size());
        theDeck.dealDeck(Players.getInstanceOf());

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
}
