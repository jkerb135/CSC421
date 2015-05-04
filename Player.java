/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Player.java
 * Purpose:         The player class is an abstract class designed to allow
 * either a human or computer player play the game of Racko. This class can
 * and will be extended by any number of player objects
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * The player class is an abstract class designed to allow
 * either a human or computer player play the game of Racko. This class can
 * and will be extended by any number of player objects
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public abstract class Player extends JPanel{
    public final int rack_size = Racko.rack_size;
    private final String player_name;
    private int player_score;
    protected Rack the_rack;
    public boolean won_round;
    public boolean won_game;

    /**
     * Constructs the player with the defined playerName and sets won round
     * to false
     * @param playerName the players name
     */
    protected Player(String playerName){
        player_name = playerName;
        won_round = false;
        initializeRack();
        setLayout(null);

    }

    /**
     * Resets a player;
     */
    public void reset(){
        won_round = false;
        initializeRack();
        if(this instanceof Computer){
            the_rack.showHideRack(false);
        }
        removeAll();
        repaint();
    }

    /**
     * Sets the rack to its respective size specified by Racko.rack_size;
     * @see Racko
     */
    public void initializeRack(){
        the_rack = new Rack(rack_size);
    }

    /**
     * Gets the players name
     * @return player_name
     */
    public String getPlayerName(){
        return player_name;
    }

    /**
     * Gets the players score
     * @return player_score
     */
    public int getPlayerScore(){
        return player_score;
    }

    /**
     * Sets the players score
     * @param points the score of the players rack after a round being over
     */
    public void setPlayerScore(int points){
        player_score += points;
    }

    /**
     * Returns the instance of the players rack.
     * @return the_rack
     */
    public Rack Rack(){
        return the_rack;
    }

    /**
     * @see HumanPlayer#doTurn
     * @see Computer#doTurn
     * @param theDeck Used for decision making for the AI and used for the
     *                human player to interact with the deck.
     * @return Rack().checkForRacko Boolean flag to see if the round it over
     * @throws IOException Invalid User Menu Input
     */
    public abstract boolean doTurn(Deck theDeck) throws
            IOException;

    /**
     * Repaints the rack to the JLayered Pane after the cards have been dealt
     * to the rack.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        the_rack.setBounds(15,0,380,400);
        add(the_rack);
    }
}
