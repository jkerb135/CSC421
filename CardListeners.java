import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The Card listener class is used for interaction between the human player
 * and the card object itself. On mouse hover the card shows a green border
 * and on mouse out the card border is set to its original color.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @see MouseListener
 * @since 2015-26-2
 */
public class CardListeners implements MouseListener{
        public void mouseClicked(MouseEvent mouseEvent) {}

        public void mousePressed(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {}

        public void mouseEntered(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            c.setBorder(BorderFactory.createLineBorder(new Color(0,204,0), 3));
            mouseEvent.consume();
        }

        public void mouseExited(MouseEvent mouseEvent)
        {
            Card c = (Card)mouseEvent.getSource();
            c.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            mouseEvent.consume();
        }
}
