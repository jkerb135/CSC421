/**
 * Author:          Josh Kerbaugh
 * Creation Date:   2/17/2015
 * Due Date:        2/26/2015
 * Assignment:      Assignment 1
 * Filename:        Display.java
 * Purpose:         The display class facilitates the display of the command
 * line interface. This class will allow easy editing of the user
 * interface if it is desired.
 */

import Input.InputReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * The display class facilitates the display of the command
 * line interface. This class will allow easy editing of the user
 * interface if it is desired.
 * @author Josh Kerbaugh
 * @version 1.0
 * @since 2015-26-2
 */
public class Display {
    private static final Display display = new Display();
    private Scanner input = new Scanner(System.in);
    private String num_players = "234";

    private static String[] menu_opts = {
            " Single Player Mode",
            " Multi Player Mode",
            " Exit"
    };
    private static String[] play_opts = {
            " Draw from face down pile?",
            " Draw from top of the discard pile?",
    };

    private static String[] card_opts =
            {" Replace a card in your rack?"," Discard the card?"};

    /**
     * Empty consturctor
     */
    public Display(){}

    /**
     * Gets the instance of the Display class, used for accessing the
     * functions of the class.
     * @return display The display class object
     */
    public static Display getInstanceOf(){
        return display;
    }

    /**
     * Prompts the user to either start a Single Player/Multi Player Game or
     * exit the game all together
     * @throws IOException Invalid User Menu Input
     */
    public void mainMenu() throws IOException {
        String choices = null;

        Output.Greenln("~~~~~Welcome To Racko~~~~~");
        Output.Greenln("Please Choose A Menu Option.");

        for (int i = 0, len = menu_opts.length; i < len; i++) {
            Output.Greenln("\t" + (i + 1)
                    + menu_opts[i]);
            choices += Integer.toString(i + 1);
        }
        Output.Green("Choice -> ");
        char menuChoice;

        menuChoice = InputReader.getChoice(choices);

        if (menuChoice == '1') {
            playersMenu();
        }
        else if (menuChoice == '2') {
            Output.Greenln("\nMultiplayer Not Implemented\n");
            mainMenu();
        }
        else if(menuChoice == '3'){
            System.exit(0);
        }
    }

    /**
     * Prompts the user how many players for the game and asks which ones
     * will be human or computer.
     * @throws IOException Invalid User Menu Input
     */
    public void playersMenu() throws IOException {
        Output.Greenln("How Many Players (2-4)? ");
        Output.Green("Choice -> ");
        int num = InputReader.getChoice(num_players);
        for(int i = 0; i < Character.getNumericValue(num); i++) {
            Output.Green("Enter Name for Player " + (i + 1) + ": ");
            String username = input.nextLine();
            Output.Green("Do you want " + username +
                    " to be a computer (Y,N)" + "? ");
            char select = InputReader.getChoice("YN");
            if (select == 'Y') {
                Players.getInstanceOf().addPlayer(new EasyComputer(username));
            } else {
                Players.getInstanceOf().addPlayer(new HumanPlayer(username));
            }
        }
        System.out.println();
    }

    /**
     * Prompts the user if he/she would like to take the top card from the
     * draw or discard pile.
     * @return menuChoice
     * @throws IOException Invalid User Menu Input
     */
    public char playMenu() throws IOException {
        String choices = null;

        Output.Greenln("Would You Like To");

        for (int i = 0, len = play_opts.length; i < len; i++) {
            Output.Greenln("\t" + (i + 1) + play_opts[i]);
            choices += Integer.toString(i + 1);
        }
        Output.Green("Choice -> ");
        char menuChoice = InputReader.getChoice(choices);
        return (menuChoice);
    }

    /**
     * Prompts the user what they want to do with the card they have just
     * acquired.
     * @param numericValue used to get either the 1 option or 2 option array
     *                     value.
     * @return menuChoice
     * @throws IOException Invalid User Menu Input
     */
    public char cardChoice(int numericValue) throws IOException {
        String choices = null;

        if(numericValue == 2){
            return (char)49;
        }
        Output.Greenln("Would You Like To");
        for (int i = 0, len = card_opts.length; i < len; i++) {
            Output.Greenln("\t" + (i + 1) +
                    card_opts[i]);
            choices += Integer.toString(i + 1);
        }
        Output.Green("Choice -> ");
        char menuChoice = InputReader.getChoice(choices);
        return menuChoice;

    }

}