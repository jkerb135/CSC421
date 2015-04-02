/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Computer.java
 * Purpose:         This is the abstract class of a computer. It is used
 * for sub-classing different difficulties of computer players. As of now
 * there is only one difficulty which is Easy Player which implements
 * the abstract method doTurn. This class also extends the Player Class
 * allowing it to access the methods inside that class for manipulating
 * the computer as a player.
 */

import javax.swing.*;

/**
 * Extends the Player object to provide a level of abstraction between the
 * Human Player and the Computer Player implementations
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 * @see Player
 */
public abstract class Computer extends Player{

    /**
     * Constructs the computer with the defined playerName
     * @param playerName sets the computers name.
     */
    protected Computer(String playerName) {
        super(playerName);
        isComputer = true;
    }

    /**
     Abstract class method implemented in the EasyCompute.java class used
     to simulate a single turn made by the AI player.
     @see EasyComputer
     @param theDeck the instance of the deck class used for the AI to make
                    a decision based upon the top discard and the draw pile.
     **/
    public abstract boolean doTurn(Deck theDeck);
    public abstract void doGuiTurn(Card draw, Card discard, Deck theDeck);
    public abstract boolean whichPile(Deck theDeck);

    public static void waitForAI(int time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

}
