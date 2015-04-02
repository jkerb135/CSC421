/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Rack.java
 * Purpose:         The Rack class is where each Player will hold their own
 * cards. A Player will be able to replace the cards in the rack. The rack
 * will also be able to Score itself at the end of a winning round.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.*;

/**
 * The Rack class is where each Player will hold their own
 * cards. A Player will be able to replace the cards in the rack. The rack
 * will also be able to Score itself at the end of a winning round.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Rack extends JLayeredPane{
    protected volatile boolean wasCardReplaced = false;
    private int card_counter = 0;
    private boolean cardInHand = false;
    protected Card cardInUse, oldCard;
    private ArrayList<Card> theRack;
    public static int rack_size;
    private static char[] rack_slots =
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private boolean isRackShown = true;
    /**
     * Constructs the rack object to a specified size
     * @param size Size of the Rack
     */
    public Rack(int size) {
        rack_size = size;
        theRack = new ArrayList<Card>();

        setBackground(Color.DARK_GRAY);
    }

    public void setCardInUse(Card card){
        cardInUse = card;
        cardInHand = true;
    }

    public void showHideRack(boolean shown){
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        System.out.println(MessageFormat.format("({0}) : ", element));
        isRackShown = shown;
        for(int i = 0, len = theRack.size(); i < len; i++){
            theRack.get(i).getCard().setCardDirection(shown);
            removeListeners(theRack.get(i));
        }
    }

    private void removeListeners(Card c){
        for(MouseListener m : c.getMouseListeners()){
            c.removeMouseListener(m);
        }
    }
    /**
     * Used for dealing cards into the rack as well as replacement of cards
     * inside the rack
     * @param card The card value
     */
    public void addCardToRack(final Card card) {
        JLabel slotLbl = new JLabel(Integer.toString((theRack.size() + 1) * 5));
        card.setLocation((theRack.size() * 20), 280 - (theRack.size() * 25));
        slotLbl.setBounds(185 + (theRack.size() * 20), 350 - (theRack.size() * 25), 150, 25);
        slotLbl.setForeground(Color.WHITE);
        card.setActionCommand("Rack");
        if(!isRackShown){
            card.setCardDirection(false);
        }
        else {
            card.addMouseListener(new CardListeners());
        }
        card.addActionListener(new cardListener());
        add(card, new Integer(rack_size));
        add(slotLbl, new Integer(rack_size));
        theRack.add(card);
    }


    /**
     * Returns the Rack
     * @return theRack Object
     */
    public ArrayList<Card> getRack() {
        return theRack;
    }

    /**
     * Used in the Player class doTurn method to replace a card with the new
     * card acquired.
     * @param oldCard value of the select card of replacement inside the rack
     * @param newCard value of the newly acquired card
     * @return oldCard Used in discarding
     * @see Player#doTurn
     */
    public Card replaceCardInRack(Card oldCard, Card newCard) {
        int v1 = oldCard.cardValue;
        int v2 = newCard.cardValue;

        oldCard.cardValue = v2;
        newCard.cardValue = v1;

        oldCard.setText(Integer.toString(v2));
        newCard.setText(Integer.toString(v1));

        return oldCard;
    }

    public void updateCardsOnGui(Card newCard){
        System.out.println("Card Counter is \t" + card_counter);
        Card oldCard = theRack.get(card_counter);
        oldCard.setText(newCard.getText());
        oldCard.cardValue = newCard.cardValue;
        card_counter++;
    }

    /**
     * Gets the card at a specific rack slot.
     * Used in HumanPlayer doTurn
     * @param rackSlot [A-J] char value
     * @return converted rackSlot value to the index in the rack
     * @see HumanPlayer#doTurn
     */
    public Card getCard(char rackSlot) {
        int idx = new String(rack_slots).indexOf(rackSlot);
        return theRack.get(idx);
    }

    /**
     * Gets the card at a specific rack slot.
     * Used in Computer doTurn
     * @param rackSlot [A-J] char value
     * @return converted rackSlot value to the index in the rack
     * @see Computer#doTurn
     */
    public Card getCard(int rackSlot) {
        return theRack.get(rackSlot);
    }

    /**
     * Uses the Sortable Class to check if this object of a Rack is in
     * consecutive order.
     * @return isSorted Boolean value
     * @see Sortable#isSorted
     */
    public boolean checkForRacko() {
        return Sortable.isSorted(theRack);
    }

    /**
     * Checks the rack for a run and returns how long the run was based off
     * of sequential order.
     * @return runLength [0-9] value
     */
    public int checkRackForSuccession()
    {
        int runLength = 0;
        int maxRun = 0;
        int nextCard;
        int lastCard = theRack.get(0).cardValue;
        for (int i = 1; i < Racko.rack_size; i++)
        {
            nextCard = theRack.get(i).cardValue;
            if (lastCard + 1 == nextCard)
            {
                runLength++;
            }
            else
            {
                runLength = 0;
            }
            lastCard = nextCard;
            if(maxRun < runLength) maxRun = runLength;
        }
        return maxRun;
    }

    /**
     * Prints the current rack object
     */
    public void printRack() {
        Iterator<Card> itr = theRack.iterator();
        Output.Greenln("Your Rack: ");
        while (itr.hasNext()) {
            System.out.print(itr.next() + "\t");
        }
        System.out.println();
        for (int i = 0; i < rack_slots.length; i++){
            System.out.print(rack_slots[i] + "\t");
        }
        System.out.println();
        System.out.println();
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponents(g);
    }


    class cardListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                Card c = (Card) e.getSource();
                System.out.println(e.getActionCommand());
                if(cardInHand && cardInUse != null) {
                    cardInHand = false;
                    replaceCardInRack(c, cardInUse);
                    wasCardReplaced = true;
                }
            }
    }
}
