import java.util.ArrayList;
import java.util.Vector;
import java.util.Collections; //to sort the vectors
import java.util.Arrays;
import java.lang.Math;

/**
 * Created by Josh on 4/1/2015.
 */
public class MediumComputer extends Computer {
    private int range;
    /**
     * Constructs the computer with the defined playerName
     *
     * @param playerName sets the computers name.
     */
    protected MediumComputer(String playerName) {
        super(playerName);
    }

    private boolean[] isRackSlotInOrder(){
        boolean order = false;
        boolean[] orders = new boolean[rack_size];
        for(int i = 0, len = orders.length; i < len; i++){
            if(i == 0 || the_rack.getRack().get(i).cardValue > the_rack.getRack().get(i - 1).cardValue){
                order = true;
            }
            else{
                order = false;
            }
            orders[i] = order;
        }
        return orders;
    }

    private int moveCardPlacementDown(Card card, int estimatedSlot, boolean[] orders){
        Card c = the_rack.getCard(estimatedSlot);

        do{
            estimatedSlot--;
        }
        while (c.cardValue > card.cardValue && orders[estimatedSlot]);

        return estimatedSlot;
    }

    private int moveCardPlacementUp(Card card, int estimatedSlot, boolean[] orders){
        Card c = the_rack.getCard(estimatedSlot);

        do{
            estimatedSlot++;
        }
        while (c.cardValue > card.cardValue && orders[estimatedSlot]);

        return estimatedSlot;
    }

    private int calculateRackSlotChoice(boolean[] rackOrder, int cardValue){
        int estimatedSlot = cardValue / range;

        if(estimatedSlot != 1 && estimatedSlot != 1){

        }

        System.out.println(estimatedSlot);

        return 1;
    }

    @Override
    public boolean doTurn(Deck theDeck) {
        the_rack.printRack();
        System.out.println(Arrays.toString(isRackSlotInOrder()));

        boolean[] orders = isRackSlotInOrder();
        Integer topDiscardValue = theDeck.peekTopDiscard();


        int estimatedSlotChoice = calculateRackSlotChoice(orders, topDiscardValue);


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void doGuiTurn(Card draw, Card discard, Deck theDeck) {

    }

    @Override
    public boolean whichPile(Deck theDeck) {
        return false;
    }
}
