/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        4/3/2015
 * Assignment:      Assignment 2
 * Filename:        GlassPane.java
 * Purpose:         This class constructs a transparent JPanel to use as a
 * glass pane when the AI is taking a turn so the user cannot interact with
 * the buttons on the playing field.
 */

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class constructs a transparent JPanel to use as a
 * glass pane when the AI is taking a turn so the user cannot interact with
 * the buttons on the playing field. The MouseListener methods consume mouse
 * events.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @see MouseListener
 * @see JPanel
 * @since 2015-26-2
 */
public class GlassPane extends JPanel implements MouseListener{

    /**
     * Constructs the glass pane to its respectable size and sets the layout
     */
    public GlassPane() {
        setLayout(null);
        setOpaque(false);
        addMouseListener(this);
        setSize(800, 600);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {mouseEvent.consume();}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mouseEvent.consume();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mouseEvent.consume();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        mouseEvent.consume();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        mouseEvent.consume();
    }
}
