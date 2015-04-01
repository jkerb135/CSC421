/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        EasyComputer.java
 * Purpose:         Extends the computer player object by implementing doTurn
 * to allow a computer AI to play the game. Easy Computer is a rudimentary AI
 * it is very unlikely to win, if you were to place two of them against each
 * other it will take a long time for them to win.
 */

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;

/**
 * Extends the computer player object by implementing doTurn
 * to allow a computer AI to play the game. Easy Computer is a rudimentary AI
 * it is very unlikely to win, if you were to place two of them against each
 * other it will take a long time for them to win.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 * @see Computer
 */
public class EasyComputer extends Computer {
    /**
     * Constructs the easy computer with the defined playerName
     * @param playerName sets the computers name.
     */
    protected EasyComputer(String playerName) {
        super(playerName);
    }

    /**
     *  Simulate a single turn made by the AI player. A decision is made
     *  without concern of what is already present in the rack. The decision
     *  is made by using integer division to determine the slot in the rack.
     * @param theDeck the instance of the deck class used for the AI to make
     * @return Rack().checkForRacko Boolean whether the AI has won the round
     * with the most recent turn.
     */
    @Override
    public boolean doTurn(Deck theDeck) {
        try {
            if(Game.show_rack){
                Rack().printRack();
            }

            Output.Redln("\nWaiting for player " + getPlayerName());

            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Card newCard;


        if(whichPile(theDeck)){
            newCard = theDeck.drawTopCard();
        }
        else{
            newCard = theDeck.drawTopDiscard();
        }

        Integer newCardValue = newCard.cardValue;

        int slotChoice = Math.round((float)newCardValue /
                ((float)theDeck.getCardCount() / (float)9));

        Card oldCard = Rack().getCard(slotChoice);
        Integer oldCardValue = oldCard.cardValue;

        if(oldCardValue < newCardValue && slotChoice != 9){
            oldCard = Rack().getCard(slotChoice + 1);
        }
        else if (oldCardValue > newCardValue && slotChoice != 0){
            oldCard = Rack().getCard(slotChoice - 1);
        }

        theDeck.discard(Rack().replaceCardInRack(oldCard, newCard));

        return Rack().checkForRacko();
    }

    public void doGuiTurn(boolean firstDecision, Deck theDeck){
        Integer newCard;

        if(firstDecision){
            newCard = theDeck.peekTopDraw();
        }
        else{
            newCard = theDeck.peekTopDiscard();
        }

        Integer newCardValue = newCard;

        int slotChoice = Math.round((float)newCardValue /
                ((float)theDeck.getCardCount() / (float)9));

        Card oldCard = Rack().getCard(slotChoice);
        Integer oldCardValue = oldCard.cardValue;

        if(oldCardValue < newCardValue && slotChoice != 9){
            oldCard = Rack().getCard(slotChoice + 1);
        }
        else if (oldCardValue > newCardValue && slotChoice != 0){
            oldCard = Rack().getCard(slotChoice - 1);
        }



        Card c =  Rack().getCard(Rack().getRack().indexOf(oldCard));
        try {
            Thread.sleep(1500);
            c.setBorder(BorderFactory.createLineBorder(Color.cyan, 2));
            Thread.sleep(1500);
            c.doClick();
            c.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param theDeck the instance of the deck class used for the AI to make
     * @return whichPile A decision on which pile the AI should draw from
     */
    public boolean whichPile(Deck theDeck){
        int peekCard, discardSlot, betterSlot;

        discardSlot = Math.round((float)theDeck.peekTopDiscard() /
                ((float)theDeck.getCardCount() / (float)9));

        peekCard = Rack().getCard(discardSlot).cardValue;

        betterSlot = Math.round((float)peekCard /
                ((float)theDeck.getCardCount() / (float)9));



        if(betterSlot == discardSlot)
            return false;
        else
            return true;
    }
}