/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Racko.java
 * Purpose:         Creates a new instance of the game and passes the Game
 * object the command line arguments.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Creates a new instance of the game and passes the Game object the command
 * line arguments.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Racko extends JApplet{
    String players;
    private NewTestGui mainContentPane;
    /**
     * Used for players racksize
     */
    public static int rack_size = 10;

    public void init(){
        System.out.println("App Init");
        //Get Params
        players = getParameter("players");

        mainContentPane = new NewTestGui(players.split(","));
        setContentPane(mainContentPane);

        String args[] = {"/d"};
        setSize(800,600);

    }

    public void start(){
        System.out.println("App Start");
        mainContentPane.playGame();
    }
}
