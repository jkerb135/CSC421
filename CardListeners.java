import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Josh on 4/1/2015.
 */
public class CardListeners implements MouseListener{
        public void mouseClicked(MouseEvent mouseEvent) {}

        public void mousePressed(MouseEvent mouseEvent) {}

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
