import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Josh on 4/29/2015.
 */
public class Test extends JApplet implements Runnable{
    Card one = new Card(1);
    Card two = new Card(2);

    public void init(){
        System.out.println("init");
        setSize(800, 600);
        setContentPane(new JPanel(null));






        one.setLocation(500, 500);
        getContentPane().add(one);
        getContentPane().setBackground(Color.black);
        two.setLocation(0, 0);
        getContentPane().add(two);

    }

    public void start(){
        Thread main = new Thread(this);
        main.start();
    }

    private LinkedList<Point> getPoints(Card start, Card end){
        Point s = new Point(start.getLocation());
        Point e = new Point(end.getLocation());
        LinkedList<Point> points = new LinkedList<Point>();

        float vx = e.x - s.x;
        float vy = e.y - s.y;
        for (double t = 0.0; t < 1.0; t+= .05) {
            double next_point_x = s.x + vx*t;
            double next_point_y = s.y + vy*t;
            Point travelPoint = new Point();
            travelPoint.setLocation(next_point_x,next_point_y);
            points.add(travelPoint);
        }
        return points;
    }

    @Override
    public void run() {
        System.out.println("running");
        LinkedList<Point> travel = getPoints(one,two);
        for(Point p : travel){
            System.out.println(p.x + "\t" + p.y);
            one.setLocation(p.x, p.y);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
