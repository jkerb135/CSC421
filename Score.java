/*
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Score.java
 * Purpose:         Static class used to calculate the score after a player
 * has gone Racko. This class with set the points per player for going racko
 * and then set the points for each individual players run scores.
 */

/**
 * Static class used to calculate the score after a player has gone Racko.
 * This class with set the points per player for going racko and then set the
 * points for each individual players run scores.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Score {
    private static int points_for_racko = 75;

    /**
     * Scores the round after a player has gone racko. Places 75 points plus
     * the run score inside the winning players object and then places the
     * run score into the remaining player objects
     * @see Players#getPlayers
     * @see Player#Rack
     * @see Player#setPlayerScore
     * @see Score#calcRunScore
     */
    public static void scoreRound(){
        if(Racko.debug) points_for_racko = 500;
        Players players = Players.getInstanceOf();
        for(int i = 0, len = players.getPlayers().size(); i < len; i++){
            Player player = players.getPlayer(i);
            int run = player.Rack().checkRackForSuccession();
            if(player.won_round){
                player.setPlayerScore(calcRunScore(run) + points_for_racko);
            }
            else{
                player.setPlayerScore(calcRunScore(run));
            }
        }
    }

    /**
     * Takes the run length and returns the points based on that value
     * @param run runLength
     * @return points for run
     */
    private static int calcRunScore(int run){
        if (run == 3)
            return 50;
        else if (run == 4)
            return 100;
        else if (run == 5)
            return 200;
        else if (run >= 6)
            return 400;
        return 0;
    }

}
