/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        4/3/2015
 * Assignment:      Assignment 2
 * Filename:        EasyComputer.java
 * Purpose:         Extends the computer player object by implementing doTurn
 * to allow a computer AI to play the game. Easy Computer is a rudimentary AI
 * it is very unlikely to win, if you were to place two of them against each
 * other it will take a long time for them to win.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Extends the computer player object by implementing doTurn
 * to allow a computer AI to play the game. Easy Computer is a rudimentary AI
 * it is very unlikely to win, if you were to place two of them against each
 * other it will take a long time for them to win.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @see Computer
 * @since 2015-26-2
 */
public class EasyComputer extends Computer {
    private int alpha = 255;
    private int increment = -5;

    /**
     * Constructs the easy computer with the defined playerName
     *
     * @param playerName sets the computers name.
     */
    protected EasyComputer(String playerName) {
        super(playerName);
    }

    /**
     * Simulate a single turn made by the AI player. A decision is made
     * without concern of what is already present in the rack. The decision
     * is made by using integer division to determine the slot in the rack.
     *
     * @param theDeck the instance of the deck class used for the AI to make
     * @return Rack().checkForRacko Boolean whether the AI has won the round
     * with the most recent turn.
     */
    @Override
    public boolean doTurn(Deck theDeck) {
        int maxCards = theDeck.getCardCount();
        try {
            if (Game.show_rack) {
                Rack().printRack();
            }

            Output.Redln("\nWaiting for player " + getPlayerName());

            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Card newCard;


        if (whichPile(theDeck)) {
            System.out.println("DRAWING TOP DRAW");
            newCard = theDeck.drawTopCard();
        } else {
            System.out.println("DRAWING TOP DISCARD");
            newCard = theDeck.drawTopDiscard();
        }

        int slotChoice = newCard.cardValue / (maxCards / 10);

        if (slotChoice == 10) {
            slotChoice = 9;
        }

        Card oldCard = Rack().getCard(slotChoice);

        System.out.println("REPLACED \t" + oldCard.cardValue + " with " +
                newCard.cardValue);

        theDeck.discard(Rack().replaceCardInRack(oldCard, newCard));

        return Rack().checkForRacko();
    }

    /**
     * Simulate a single turn made by the AI player. A decision is made
     * without concern of what is already present in the rack. The decision
     * is made by using integer division to determine the slot in the rack.
     * This method can interact with the action listeners on cards.
     * @param draw
     * @param discard
     * @param theDeck
     */
    public void doGuiTurn(Card draw, Card discard, Deck theDeck) {
        waitForAI();
        Integer newCard;
        int maxCards = theDeck.getCardCount();

        if (whichPile(theDeck)) {
            System.out.println("Peeking TOP DRAW");
            draw.doClick();
            newCard = theDeck.peekTopDraw();
        } else {
            System.out.println("Peeking TOP DISCARD");
            discard.doClick();
            newCard = theDeck.peekTopDiscard();
        }

        int slotChoice = newCard / (maxCards / 10);

        if (slotChoice == 10) {
            slotChoice = 9;
        }

        Card c = the_rack.getRack().get(slotChoice);

        waitForAI();
        Timer rackChange = flashBorder(c);
        rackChange.start();
        waitForAI();
        c.doClick();
        rackChange.stop();
        c.setBorder(BorderFactory.createLineBorder(Color.black, 2));

    }

    /**
     * @param theDeck the instance of the deck class used for the AI to make
     * @return whichPile A decision on which pile the AI should draw from
     */
    public boolean whichPile(Deck theDeck) {
        int rackCard, discardSlot, betterSlot;
        int maxCards = theDeck.getCardCount();

        discardSlot = theDeck.peekTopDiscard() / (maxCards / 10);

        if (discardSlot == 10) {
            discardSlot = 9;
        }

        rackCard = Rack().getCard(discardSlot).cardValue;

        betterSlot = rackCard / (maxCards / 10);

        if (betterSlot == 10) {
            betterSlot = 9;
        }

        return betterSlot != discardSlot;
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
                        0,  alpha), 4));
            }
        });
    }
}