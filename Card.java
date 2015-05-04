/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/20/2015
 * Due Date:        4/3/2015
 * Assignment:      Assignment 2
 * Filename:        Card.java
 * Purpose:         The Card class is a representation of a card help by a
 * player in their rack, and a card that are in the deck. A card consists of
 * an image, value, and whether it is face up or face down.
 */

import javax.swing.*;
import java.awt.*;

/**
 * The Card class is a representation of a card help by a
 * player in their rack, and a card that are in the deck. A card consists of
 * an image, value, and whether it is face up or face down.
 *
 * @author Josh Kerbaugh
 * @version 1.0
 * @see JButton
 * @since 2015-26-2
 */
public class Card extends JButton{

    private final ImageIcon faceUpPath =  new ImageIcon(getClass()
            .getResource("Images/RackoFront.jpg"));
    private final ImageIcon faceDownPath =  new ImageIcon(getClass()
            .getResource("Images/RackoBack.jpg"));
    public Integer cardValue;

    public Card(Integer value){
        super(value.toString());
        cardValue = value;
        setText(Integer.toString(value));
        setSize(180, 90);
        setBackground(Color.WHITE);
        setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIcon(faceUpPath);
        setDisabledIcon(faceUpPath);
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }

    /**
     * Sets the cards up or down according to the boolean flag
     * @param isUp is the card up or down
     */
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

    /**
     * Returns the card object
     * @return this
     */
    public Card getCard(){
        return this;
    }

    /**
     * Overridden toString to show the cards value on .toString()
     * @return this.cardValue
     */
    @Override
    public String toString(){
        return this.cardValue.toString();
    }

    /**
     * Overridden paintComponent to show the cards integer value at a
     * specific point on the Card.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        cardValue = Integer.parseInt(getText());
        if(getIcon() == faceUpPath) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(Color.BLACK);
            int val = 150 / 60;
            int x = Integer.parseInt(getText()) * val + 20;
            g2.drawString(getText(), x, 15);
            g2.dispose();
        }
    }
}
