/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        HumanPlayer.java
 * Purpose:         Extends the Player class to allow for a human player to
 * participate in the game of Racko.
 */

import Input.InputReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Extends the Player class to allow for a human player to participate in the
 * game of Racko.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 * @see Player
 */
public class HumanPlayer extends Player
{
    /**
     * Constructs the human player with the defined playerName
     * @param playerName the players name
     */
    protected HumanPlayer(String playerName) {
        super(playerName);
    }

    /**
     * Prints out the users rack as well as the top discard pile. If
     * debugging is on the full deck is shown as well as the full discard pile.
     * Prompts the user using the display class what they would like to do
     * with their turn. Once a choice is made the card is shown and they can
     * decide to discard it or put it inside their rack.
     * @param theDeck the instance of the deck class for the player to use.
     * @return Rack().checkForRacko Boolean whether the player has won the round
     * @throws IOException Invalid User Menu Input
     * @see Display#playMenu
     * @see Display#cardChoice
     * @see Deck#discard
     * @see Rack#checkForRacko
     */
    @Override
    public boolean doTurn(Deck theDeck) throws IOException {
        char cardChoice;
        Card newCard;

        if(Game.debug){
            theDeck.printDeck();
        }

        theDeck.printDiscard();

        Rack().printRack();

        char playChoice = Display.getInstanceOf().playMenu();

        newCard = playChoice == '1' ?
                theDeck.drawTopCard() : theDeck.drawTopDiscard();

        System.out.println();
        System.out.println("You Drew the Card: " + newCard.toString());
        System.out.println();

        cardChoice = Display.getInstanceOf().cardChoice(
                Character.getNumericValue(playChoice));

        if(cardChoice == '1'){
            Output.Green("Replace which card (A-J): ");
            char cardSlot = InputReader.getChoice("ABCDEFGHIJ");
            Card oldCard = Rack().getCard(cardSlot);

            theDeck.discard(Rack().replaceCardInRack(oldCard, newCard));
        }
        else{
            theDeck.discard(newCard);
        }

        return Rack().checkForRacko();
    }
}