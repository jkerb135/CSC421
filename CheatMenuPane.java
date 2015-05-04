/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/20/2015
 * Due Date:        4/3/2015
 * Assignment:      Assignment 2
 * Filename:        Rack.java
 * Purpose:         The CheatMenauPane class is where each cheat will be
 * implemented and the toggle buttons that allow the cheats
 * to be enabled or disabled
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The CheatMenauPane class is where each cheat will be
 * implemented and the toggle buttons that allow the cheats
 * to be enabled or disabled. Extends JPanel for ease of access for
 * JOptionDialog
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class CheatMenuPane extends JPanel {
    private final JToggleButton debug_Cheat;
    private final JToggleButton rack_Cheat;
    private final JToggleButton order_Cheat;
    private final JToggleButton end_Cheat;

    public CheatMenuPane() {
        debug_Cheat = new JToggleButton("OFF");
        debug_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (debug_Cheat.isSelected()) {
                    debug_Cheat.setText("ON");
                    Racko.debug = true;
                } else {
                    debug_Cheat.setText("OFF");
                    Racko.debug = false;
                }
            }
        });

        JLabel debugLbl = new JLabel("Debug Mode");
        debugLbl.setLocation(0, 0);


        JLabel rackLbl = new JLabel("Show Rack Mode");
        rackLbl.setBounds(0, 110, 100, 25);

        rack_Cheat = new JToggleButton("OFF");
        rack_Cheat.setBounds(debug_Cheat.getX(), debug_Cheat.getHeight() +
                10, 60, 30);
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
        order_Cheat = new JToggleButton("CLICK");
        order_Cheat.setBounds(debug_Cheat.getX(), debug_Cheat.getHeight() +
                        10, 60, 30);

        order_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (order_Cheat.isSelected()) {
                    Object[] possibilities = {"1", "2", "3", "4", "5", "6",
                            "7", "8", "9", "10"};
                    String orderTo = (String) JOptionPane.showInputDialog(
                            null,
                            "Order Rack To Which Slot?",
                            "Customized Dialog",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            "Order To?");

                    if (orderTo != null) {
                        order_Cheat.setText("CLICK");
                        order_Cheat.setSelected(false);
                        Players players = Players.getInstanceOf();
                        for (Player player : players.getPlayers()) {
                            if (player instanceof HumanPlayer) {

                                ArrayList<Card> rack = player.Rack().getRack();
                                ArrayList<Integer> sorted = new ArrayList<Integer>();

                                for (Card aRack : rack) {
                                    sorted.add(aRack.cardValue);
                                }

                                Collections.sort(sorted);
                                Collections.shuffle(sorted.subList(Integer
                                        .parseInt(orderTo), sorted.size()));

                                for (int i = 0, len = rack.size(); i < len; i++)
                                {
                                    Card c = rack.get(i);
                                    c.setText(Integer.toString(sorted.get(i)));
                                    c.cardValue = sorted.get(i);
                                }
                            }
                        }
                    }
                }
            }
        });

        JLabel endRoundsLbl = new JLabel("End Rounds In");
        endRoundsLbl.setBounds(0, 110, 100, 25);
        end_Cheat = new JToggleButton("CLICK");
        end_Cheat.setBounds(order_Cheat.getX(), order_Cheat.getHeight() +
                10, 60, 30);

        end_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (end_Cheat.isSelected()) {
                    String orderTo = (String) JOptionPane.showInputDialog(
                            null,
                            "End Game In (n) Turns?",
                            "Customized Dialog",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "How Many Turns?");

                    if (orderTo != null) {
                        order_Cheat.setText("CLICK");
                        order_Cheat.setSelected(false);
                        Racko.round_cheat = true;
                        Racko.round_limit = Integer.parseInt(orderTo);
                    }
                }
            }
        });

        //add(debugLbl);
        //add(debug_Cheat);
        add(rackLbl);
        add(rack_Cheat);
        add(orderRack);
        add(order_Cheat);
        add(endRoundsLbl);
        add(end_Cheat);
    }
}
