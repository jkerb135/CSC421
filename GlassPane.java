import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Josh on 4/1/2015.
 */
public class GlassPane extends JPanel implements MouseListener{
    public GlassPane() {
        setLayout(null);
        setOpaque(false);
        addMouseListener(this);
        setSize(800, 600);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("Mouse Consumed");
        mouseEvent.consume();
    }

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
