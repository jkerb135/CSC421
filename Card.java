import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

/**
 * Created by Josh on 3/6/2015.
 */
public class Card extends JButton implements ActionListener{

    private ImageIcon faceUpPath = new ImageIcon(getClass().getResource("Images/RackoFront.jpg"));
    private ImageIcon faceDownPath = new ImageIcon(getClass().getResource("Images/RackoBack.jpg"));

    public Integer cardValue;


    public Card(Integer value){
        super(value.toString());
        cardValue = value;
        setSize(180, 90);
        setBackground(Color.WHITE);
        setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIcon(faceUpPath);
        setDisabledIcon(faceUpPath);
        setBorder(BorderFactory.createLineBorder(Color.black, 2));

    }

    public void setCardDirection(boolean isUp){
        if (isUp) {
            setIcon(faceUpPath);
            setDisabledIcon(faceUpPath);
        }
        else{
            setIcon(faceDownPath);
            setDisabledIcon(faceDownPath);
        }
        repaint();
    }

    public Card getCard(){return this;}

    @Override
    public String toString(){
        return this.cardValue.toString();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(getIcon() == faceUpPath) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(Color.BLACK);
            int val = 150 / 60;
            int x = Integer.parseInt(getText()) * val + 20;
            g2.drawString(getText(), x, 15);
            g2.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}
