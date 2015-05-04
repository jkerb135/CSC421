//File:    Test1.java
//Author:  Dr. Spiegel
//Course:  CIS 421
//Purpose: 1.To create a JFrame subclass containing a JPanel of JRadioButtons, 
//		along with a JPanel with a JButton, and a JButton right on the JFrame
//         2. Demonstrate dragging a component, in this case a panel.   

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Test1 extends JApplet implements ActionListener
{   	RadioPanel guess;
	ButtonPanel quit;
	JButton Choose,Quit;
	JTextField ChoiceLabel;
	// Offset from ButtonPanel corner to mouse click
	int bpOffsetX,bpOffsetY;  

	public void init()
	{       setSize(500, 500);
		
		setLayout(null);
		// Must access content pane to change color!?!?!
		getContentPane().setBackground(Color.black);      

		// Panel with radio buttons onto the JFrame
		guess = new RadioPanel();      
		guess.setLocation(10,15);      
		add(guess);

		// Put a Button right on the JFrame
		Choose=new JButton("Choose");
		Choose.setBackground(Color.green);
		Choose.setBounds(80,35,95,25);
		Choose.addActionListener(this);
		add(Choose);

		// Put a JPanel with just a JButton onto the JFrame
		// Can we handle a button push here??
		quit=new ButtonPanel();
		quit.setLocation(289,231);
		add(quit);
		// Listen to the mouse on the ButtonPanel so we can drag it
		quit.addMouseListener(new MouseAdapter() 
                {       public void mousePressed(MouseEvent ME)
                        { // Record offset of mouse pointer inside ButtonPanel
                          bpOffsetX=ME.getX();
                          bpOffsetY=ME.getY();
                          // Show offset
                          System.out.println("("+bpOffsetX+","+bpOffsetY+")");
                        }
                });               
                
		quit.addMouseMotionListener(new MouseAdapter() 
                {       public void mouseDragged(MouseEvent ME)
                        { quit.setLocation((int)(quit.getLocation().getX()+ME.getX()-bpOffsetX),
                                                (int)(quit.getLocation().getY()+ME.getY()-bpOffsetY));
                          // Panel location relative to previous drag 
                          System.out.println("Panel's now at ("+(ME.getX()-bpOffsetX)+","+(ME.getY()-bpOffsetY)+")");
                          // location of mouse pointer relative to previous panel location
                          System.out.println("Mouse drag at ("+ME.getX()+","+ME.getY()+")");
                          // Location of panel at this drag
                          System.out.println("Quit is at "+quit.getLocation());
                        }
		});
                
		// Text field to display chosen color
		ChoiceLabel=new JTextField(" The Background Color is the Choice");
		ChoiceLabel.setForeground(Color.magenta);
		ChoiceLabel.setBackground(Color.black);
		ChoiceLabel.setLocation(50,155);
		ChoiceLabel.setSize(220,40);
		add(ChoiceLabel);
	}  

	public void actionPerformed(ActionEvent event)
	{	// Really don't need the if
		if (event.getActionCommand()=="Choose") {
			ChoiceLabel.setBackground(guess.getSelection());
			guess.reset();
			validate();
		}
	}
}
