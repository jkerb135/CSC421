################################################################################
  ((((//*         (((((        %%@%    .                     ,&@@&.          
 ,         .(     .    &/   %         ( .    &   @    &     .,        @        
 *           @&  @     .@@ @     (*     @    %@@@    @@@@  &     .      @*     
 *    @@@%    @&.       @@#    *@@@@    &    %@&    @@@@  .    #&&&@    *@&    
 /    @@@,   .@@&   ,    @@    @@@  *.. (    %&    @@@@@  @    @@@  @    @&    
 (          .@@@    @/   #@    @@%    @&@         @@@*  *@@    @&%  @    @@%   
 (        &@@@@*    ..    @    @@&  *,,,#    %    %@#&  @@@    @@(  @    @&&   
 #    @    %@,@            .   ,@& &    @    &@    %@   @&@    (@& ,    *@@(   
 %    @@    *@    &@@@@    @           .@    &@@    (@.  . @            @&@    
 %    @@@         @@&//(    @*        @@@    &@@*    .@/    *.        @@@&(    
    @@@@&  &@@@@@@@@#  ..&&@@@%@@@@@@@@@@**@@@@& ./@@@@@,      @@@@@@@@@& .(/ 
################################################################################

Author:	Josh Kerbaugh
Date:	2/26/2015
Assignment: Racko Project 1
Due: 2/26/2014 9:00pm

JavaDoc: http://acad.kutztown.edu/~jkerb135/CSC421/

Setup:	Copy all .java files into directory

Cheats: 
	/d .:Enables Debugging mode
		.: Prints the full deck
		.: Prints the full discard pile
		.: Game is started with one human player vs easy computer
		
	/c .:Enables every players rack to be shown
	/n .:Enables the game to end after n rounds
		.: Example - make run /n 2
		.: Game will end in TWO rounds
		.: Score will be shown.
		
	/o .:Enables you to pass any card to your rack
		.: Example - gmake run /o 1 5 7 9
		.: Your rack will be 	1 5 7 9 ..... randomly dealt after
					A B C D .....
					
	/s .:Enables you to order cards already dealt to your rack
		.: Not fully functional selection sort algorithm is not working
		correctly. Will be done by next project.
		
	Example	.: make run /d /o 1 2 3 9 15 /c
		.: Enables debugging, orders slot 1 to 5 with those numbers and
			shows opponents rack.

Design Decisions:
	.: This game will run until a player reaches a score of 500. If you
	would like the game to end in 1 round and print the score use the /n
	cheat (java Racko /n 1) which will then end the game in 1 round after
	both players have respectively taken their turns.
	

	.: The computer player sleeps for 1.5 after taking its turn making it 
	seem more human like. It also keeps the screen from changing fast
	and have the end user wondering what happened.
	
	.: Instead of just flipping the discard over after the cards have run 
	out I decided to reshuffle the pile so you cannot memorize the card
	sequence in the discard pile.
	
How to Run:
	From the command line you can either run
		.: gmake clean
		.: gmake run <command line args>
		~Or~
		.: javac *.java
		.: java Racko <command line args>
	
How to Play:
	.: Main Menu
		.: Single Player -- Implemented
		.: Multi Player -- Not Implemented
		.: Exit		-- Quit Game
	.: Single Player Mode
		.: Input player Range 2-4 players
		.: Enter Players name
		.: Enter if player is computer
		***NOTE: RACK CHEATS ONLY WORK IF HUMAN PLAYER IS FIRST***
		.: Human players
			.: You are presented with your rack and the top discard
			.: You have a choice to either
				.: 1. Draw from the face down pile
				.: 2. Draw from the discard pile
			.: If you choose 1.
				.: You will be presented with a Card
				.: You will be able to replace the card with	
				one in your Rack or discard the card.
			.: If you choose 2.
				.: You will only be able to replace the card in
				your rack.
		
How to Win:
	.: To win a round a player must go Racko. Racko is when a players rack
	is in consecutive order from low to high. Lowest card must be in the
	A slot and highest card must be in the J slot.
	
	.: To win the game a player must reach a score of 500 points
	
Scoring:
	.: The player who goes Racko receives 75 points and the points for a run
	.: The player who doesnt go Racko receives the points for a run
	
	.:Run points are as follows
	3 Cards in sequential order = 50pts
	4 Cards in sequential order = 100pts
	5 Cards in sequential order = 275pts
	6+ Cards in sequential order = 475pts
	
	NOTE: Only points for the biggest run are kept.
	
	ENJOY PLAYING RACKO
	