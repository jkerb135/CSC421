import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Josh on 3/31/2015.
 */
public class CheatMenuPane  extends JPanel{
    private JToggleButton debug_Cheat, rack_Cheat, order_Cheat;

    public CheatMenuPane(){

        debug_Cheat = new JToggleButton("OFF");
        debug_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(debug_Cheat.isSelected()){
                    debug_Cheat.setText("ON");
                    NewTestGui.debug = true;
                }
                else {
                    debug_Cheat.setText("OFF");
                    NewTestGui.debug = false;
                }
            }
        });

        JLabel debugLbl = new JLabel("Debug Mode");
        debugLbl.setLocation(0, 0);


        JLabel rackLbl = new JLabel("Show Rack Mode");
        rackLbl.setBounds(0, 110 ,100,25);

        rack_Cheat = new JToggleButton("OFF");
        rack_Cheat.setBounds(debug_Cheat.getX(), debug_Cheat.getHeight() + 10, 60,
                30);
        rack_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (rack_Cheat.isSelected()) {
                    rack_Cheat.setText("ON");
                    Players players = Players.getInstanceOf();
                    for (Player p : players.getPlayers()) {
                        if (p instanceof Computer) {
                            p.Rack().showHideRack(true);
                        }
                    }
                } else {
                    rack_Cheat.setText("OFF");
                    Players players = Players.getInstanceOf();
                    for (Player p : players.getPlayers()) {
                        if (p instanceof Computer) {
                            p.Rack().showHideRack(false);
                        }
                    }
                }
            }
        });

        JLabel orderRack = new JLabel("Order Rack");
        orderRack.setBounds(0, 110, 100, 25);

        order_Cheat = new JToggleButton("OFF");
        order_Cheat.setBounds(debug_Cheat.getX(),debug_Cheat.getHeight() + 10,60,
                30);
        order_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(order_Cheat.isSelected()){
                    Object[] possibilities = {"1", "2", "3","4", "5", "6","7", "8", "9","10"};
                    String orderTo = (String)JOptionPane.showInputDialog(
                            null,
                            "Order Rack To Which Slot?",
                            "Customized Dialog",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            "Order To?");

                    System.out.println(orderTo);

                    if(orderTo != null) {
                        order_Cheat.setText("ON");
                        order_Cheat.setEnabled(false);
                        Player player = Players.getInstanceOf().getPlayer(0);
                        ArrayList<Card> theRack = player.Rack().getRack();
                        ArrayList<Card> sorted = new ArrayList<Card>(theRack);

                        Collections.sort(sorted, new CardCompare());

                        player.Rack().printRack();

                       for(int i = 0; i < theRack.size(); i++){
                           Card old = theRack.get(i);
                           Card newCard = sorted.get(i);

                           System.out.println("Replacing \t" + old + " with " + newCard);
                           player.Rack().replaceCardInRack(old,newCard);
                       }

                    }

                }
            }
        });

        add(debugLbl);
        add(debug_Cheat);
        add(rackLbl);
        add(rack_Cheat);
        add(orderRack);
        add(order_Cheat);
    }

    class CardCompare implements Comparator<Card> {
        public int compare(Card o, Card t1) {
            return o.cardValue.compareTo(t1.cardValue);
        }
    }
}
