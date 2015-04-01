import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by Josh on 3/31/2015.
 */
public class CheatMenuPane  extends JPanel{
    private JToggleButton debug_Cheat, rack_Cheat;

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
        debugLbl.setLocation(0,0);


        final JLabel rackLbl = new JLabel("Show Rack Mode");
        rackLbl.setBounds(0, 110 ,100,25);

        rack_Cheat = new JToggleButton("OFF");
        rack_Cheat.setBounds(debug_Cheat.getX(),debug_Cheat.getHeight() + 10,60,
                30);
        rack_Cheat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(rack_Cheat.isSelected()){
                    rack_Cheat.setText("ON");
                    Players players = Players.getInstanceOf();
                    for(Player p : players.getPlayers()) {
                        if (p instanceof Computer) {
                            p.Rack().showHideRack(true);
                        }
                    }
                }
                else {
                    rack_Cheat.setText("OFF");
                    Players players = Players.getInstanceOf();
                    for(Player p : players.getPlayers()) {
                        if (p instanceof Computer) {
                            p.Rack().showHideRack(false);
                        }
                    }
                }
            }
        });

        add(debugLbl);
        add(debug_Cheat);
        add(rackLbl);
        add(rack_Cheat);
    }
}
