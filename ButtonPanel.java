//File:    ButtonPanel.java
//Author:  Dr. Spiegel 
//Course:  CIS 421
//Purpose: To create a JPanel subclass containing a JButton that kills the program

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonPanel extends JPanel implements ActionListener
{	
	public JButton QuitButton;
	JLabel Msg;
	int timesPushed=0;

	public ButtonPanel()
	{	// First, the button
		QuitButton=new JButton("Quit");
		QuitButton.setBackground(Color.blue);
		QuitButton.setBounds(50,25,1,1);
		QuitButton.addActionListener(this);
		add(QuitButton);

	 	Msg=new JLabel("     ");
		Msg.setForeground(Color.blue);
		Msg.setBackground(Color.red);
		Msg.setSize(65,20);
		Msg.setLocation(1,26);
		add(Msg);

		// Size the JPanel
		setSize(152,55);
		setBackground(Color.orange);
	}

	// Only possible event is button pushed
	public void actionPerformed(ActionEvent event)
	{	// System.exit(0);  this does nothing, now
		String S=(++timesPushed)+" pushes";
		Msg.setText(S);
	}
}
