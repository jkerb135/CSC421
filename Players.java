/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Player.java
 * Purpose:         The players class holds the the list of players for the game
 * This class allows players to be added to the game and allows the players
 * racks to be reinitialized after a game is complete. The players class in
 * general allows manipulation and searching throughout all the players.
 */

import java.util.LinkedList;

/**
 * Created by Josh on 2/17/2015.
 */
public class Players {
    public static final Players players = new Players();

    private static final LinkedList<Player> thePlayers = new LinkedList<Player>();

    /**
     * Returns the Player object.
     * Allows access to all public methods below
     * @return players
     */
    public static Players getInstanceOf(){
        return players;
    }

    /**
     * Returns the Linked List of Player objects.
     * @return thePlayers LinkedList of Player
     * @see Player
     */
    public LinkedList<Player> getPlayers(){
        return thePlayers;
    }

    /**
     * Gets a specific player in the linked list
     * @param idx Index of the player
     * @return thePlayers.get(idx) A specific player
     * @see Player
     */
    public Player getPlayer(int idx){
        return thePlayers.get(idx);
    }

    /**
     * Adds a new player to the player object
     * @param newPlayer A new Player
     * @see Player
     */
    public void addPlayer(Player newPlayer){
        thePlayers.add(newPlayer);
    }

    /**
     * Empties the players rack and recreates it to the rack size variable
     * Sets won_round to false
     * @see Player
     * @see Player#initializeRack
     * @see Racko#rack_size
     */
    public void reset(){
        for(Player player : thePlayers){
            player.reset();
        }
    }

    /**
     * Returns the highest score
     * Used to check if the highest score is over the game winning value
     * @return max_score
     * @see Game#winning_score
     * @see Player
     * @see Player#getPlayerScore
     */
    public int getHighestScore(){
        int max_score = 0;
        for(Player player : thePlayers){
            if(player.getPlayerScore() > max_score){
                max_score = player.getPlayerScore();
            }
        }
        return max_score;
    }

    /**
     * Prints out the scoreboard after the round and game is over
     * @return output The game scoreboard
     * @see Player
     * @see Player#getPlayerName
     * @see Player#getPlayerScore
     */
    public String getGameScores(){
        String output = "";
        for(Player player : thePlayers){
            output += player.getPlayerName() + ": " +
                    player.getPlayerScore() + " \t";
        }
        return output;
    }

    /**
     * Returns the name of the game winning player or No Winner if there was
     * no winner
     * @return The Game Winner
     */
    public String getWinner(){
        for(Player player : thePlayers){
            if(player.won_game){
                return player.getPlayerName();
            }
        }
        return "No winner";
    }
}
