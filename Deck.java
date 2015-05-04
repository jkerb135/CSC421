/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Deck.java
 * Purpose:         The Deck class represents the deck of cards used to play
 * the game of Racko. It encapsulates the draw and discard pile for gameplay.
 * The deck initially contains all the cards inside the draw pile until the
 * players are dealt the cards.
 *
 * Assumptions:     60 cards will always be the max value for gameplay.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * The Deck class represents the deck of cards used to play
 * the game of Racko. It encapsulates the draw and discard pile for gameplay.
 * The deck initially contains all the cards inside the draw pile until the
 * players are dealt the cards.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Deck{
    protected static int number_of_cards;
    protected int number_of_players = 1;
    protected int deal_count = 40;
    protected ArrayList<Card> draw_pile, discard_pile;

    /**
     * Constructs the deck to the number of players in the game.
     * @param numPlayers Number of players
     */
    public Deck(int numPlayers){
        initializeDeck(numPlayers);
    }

    /**
     * Initializes the predefined variables so the number_of_cards and
     * deal_count have the correct values.
     * @param numPlayers Number of players
     */
    private void initializeDeck(int numPlayers){
        number_of_players = numPlayers;
        number_of_cards =
                (Racko.rack_size * numPlayers) + (Racko.rack_size * 2);
        deal_count = (Racko.rack_size * number_of_players);

        draw_pile = new ArrayList<Card>();

        for(int i = 1; i <= number_of_cards; i++){
            draw_pile.add(new Card(i));
        }

        shuffleDeck();
    }

    /**
     * Resets the deck back to its original state when the game was initialized.
     */
    public void reset(){
        initializeDeck(number_of_players);
    }

    /**
     * Shuffles the deck. If debug flag is set in command line (/d) the seed
     * of the deck will be set so the same pseudo random numbers are generated
     */
    private void shuffleDeck(){
        if (Racko.debug){
            //Sets Seed in Debug so the same Deck is dealt
            Collections.shuffle(draw_pile, new Random(1000));
        }else {
            Collections.shuffle(draw_pile);
        }
    }

    /**
     * Deals the deck out card by card to each of the players rack. If the
     * card_order cheat is set (/s) the rack will be sorted to the nth index
     * passed in the command line.
     * @see SelectionSort#doSelectionSort
     * @param thePlayers Object is used to get the players rack and add a
     *                   card to it.
     */
    public void dealDeck(Players thePlayers) {
        ArrayList<Card> tmp = new ArrayList<Card>(draw_pile);
        for (int i = 0; i < deal_count; i++) {
            thePlayers.getPlayer(i % number_of_players).Rack().addCardToRack(new Card(tmp.get(i).cardValue));
            draw_pile.remove(tmp.get(i));
        }

        discard_pile = new ArrayList<Card>(draw_pile.size());

        Card firstCard = draw_pile.get(0);
        draw_pile.remove(firstCard);
        discard_pile.add(new Card(firstCard.cardValue));
    }

    /**
     * Gets the top card off the draw pile. For use with the GUI.
     * @return top card
     */
    public Card getTopDraw(){
        return draw_pile.get(0);
    }

    /**
     * Gets the top card off the discard pile. For use with the GUI.
     * @return top discard
     */
    public Card getTopDisc(){
        return discard_pile.get(discard_pile.size() - 1);
    }

    /**
     * Simulates a Player drawing a card off the top of the draw pile.
     * @return theCard The top card off the draw pile.
     */
    public Card drawTopCard(){
        Card theCard = null;
        if(draw_pile.size() != 0){
            theCard = draw_pile.iterator().next();
            draw_pile.remove(theCard);
        }
        return theCard;
    }

    /**
     * Peeks at the top card on the draw pile. Used for decision making in
     * the Easy Computer class. The top card is not removed from the draw pile.
     * @see EasyComputer
     * @return top card in the discard
     */
    public Integer peekTopDraw(){
        return draw_pile.get(0).cardValue;
    }

    /**
     * Peeks at the top card on the draw pile. Used for decision making in
     * the Easy Computer class. The top card is not removed from the draw pile.
     * @see EasyComputer
     * @return top card in the discard
     */
    public Integer peekTopDiscard(){
        return discard_pile.get(0).cardValue;
    }

    /**
     * Simulates a Player drawing a card off the top of the discard pile.
     * @return theCard The top card off the discard pile.
     */
    public Card drawTopDiscard(){
        Card theCard = discard_pile.get(discard_pile.size() - 1);
        discard_pile.remove(theCard);
        return theCard;
    }

    /**
     * Simulates a Player discarding a card to the discard pile. If the draw
     * pile is empty the deck will be reshuffled.
     * @param card The card drawn either from the discard or draw pile
     */
    public void discard(Card card){
        if(draw_pile.size() == 0){
            draw_pile = new ArrayList<Card>(discard_pile);
            discard_pile.clear();

            Output.Green("\nDraw Pile Empty Reshuffling.\n");

            Collections.reverse(draw_pile);
            Collections.shuffle(draw_pile);
        }
        discard_pile.add(card);
        draw_pile.remove(card);
    }

    /**
     * Used to get the total number of cards.
     * @return number_of_cards The total value of cards
     */
    public int getCardCount(){
        return number_of_cards;
    }

    /**
     * Used for debugging prints the whole draw pile to see if cards are
     * being removed correctly
     */
    public void printDeck(){
        Output.Yellowln("Printing Full Deck:\t" + Arrays.toString(draw_pile
                .toArray()));
    }

    /**
     * If the debugging flag is set (/d) prints the whole discard pile. If it
     * isn't set the top discarded card is show on the screen.
     */
    public void printDiscard(){
        if(Game.debug){
            Output.Yellowln("Full Discard Pile:\t" + Arrays.toString
                    (discard_pile
                            .toArray()));
        } else{
            Output.Blueln("Top Discarded Card: " +
                    discard_pile.get(discard_pile.size() - 1));
        }
        System.out.println();
    }

}