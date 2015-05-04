import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by Josh on 4/27/2015.
 */
public class DraggableContent extends JComponent{
    private boolean isDraggable = true;
    protected Point anchorPoint;
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    protected boolean overbearing = false;

    public DraggableContent(){
        addDragListeners();
        setOpaque(true);
    }

    private void addDragListeners() {
        /** This handle is a reference to THIS because in next Mouse Adapter 
         "this" is not allowed */
        final DraggableContent handle = this;
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                anchorPoint = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = e.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x -
                        anchorX, mouseOnScreen.y - parentOnScreen.y - anchorY);
                setLocation(position);

                //Change Z-Buffer if it is "overbearing"
                if (overbearing) {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void removeDragListeners() {
        for (MouseMotionListener listener : this.getMouseMotionListeners()) {
            removeMouseMotionListener(listener);
        }
        setCursor(Cursor.getDefaultCursor());
    }
}
