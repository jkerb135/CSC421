//File:    RadioPanel.java
//Author:  Dr. Spiegel (based on a concept from Harmony Reppert)
//Course:  CIS 421
//Purpose: To create a JPanel subclass containing JRadioButtons, along with a necessary
//		mutator and facilitator

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RadioPanel extends JPanel implements MouseMotionListener
{   	JRadioButton  buttons[],dummy;   
	ButtonGroup oneGroup = new ButtonGroup();   

	// List the colors, and use this list as the basis for operations below
	Color ColorList[]={Color.red,Color.blue,Color.white,Color.cyan};
	String ColorName[]={"Red","Blue","White","Cyan"};

	//Constructor   
	public RadioPanel()   
	{	buttons = new JRadioButton[ColorList.length];
		for (int i=0;i<ColorList.length;i++) 
		{	buttons[i]=new JRadioButton(ColorName[i],false);
			buttons[i].setBackground(ColorList[i]);
			buttons[i].setSize(65,40);
			buttons[i].setLocation(1,40*i+i);
			buttons[i].addMouseMotionListener(this);
			// Put the little circle in the middle
			buttons[i].setHorizontalAlignment(SwingConstants.CENTER);
			oneGroup.add(buttons[i]);
			add(buttons[i]);   
		}

		// Need a dummy button so we can clear the visible buttons
		dummy=new JRadioButton("",true);
		oneGroup.add(dummy);

		// Set JPanel parameters
		setSize(65,ColorList.length*40+25);
		setBackground(Color.pink);
	}//end constructor

	//Method returns Color of selected button 
	//	or black if invisible dummy radio button is selected when called
	public Color getSelection()   
	{ 	for (int c=0; c<ColorList.length; c++)      
			if (buttons[c].isSelected())          
				return(ColorList[c]);
		return Color.black;   
	}
	
	//Method resets visible buttons by setting invisible dummy
	public void reset()   
	{  	dummy.setSelected(true);
	}

	// MouseMotionListener methods
	public void mouseDragged(MouseEvent mEvent) {}
	
	// Fires when one of the radio buttons is entered by the mouse
	public void mouseMoved(MouseEvent mEvent)
	{ 	for (int c=0; c<ColorList.length; c++)      
			if (mEvent.getSource()==buttons[c])
				setBackground(ColorList[c]);
	}

}//end RadioPanel