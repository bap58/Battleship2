package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import static bap58.battleship2.BoardSquare.squareSize;


public class GameActivity extends AppCompatActivity
{
    //two boards to go back and forth between
    Board myBoard;       //where the player located his ships, and where his opponent has launched torpedoes
    Board opponentBoard; //where the player has launched torpedoes, and whether or not the guesses where correct

    Boolean myTurn = true; //this mobile game works on a turn basis
    Boolean viewMe = false; //flag to decide which board to show

    BufferedReader bin; // object for input from port
    PrintWriter pout;   // object for output to port

    String ip = "10.0.2.2"; //number depending on the server it is running on

    int port = 11013;  //It can be any number as long as it is the same in the Server.java code

    Socket sock; // the connection to the server
    boolean keepGoing = true; // set to false to shut down input loop
    Ear ear; // object that sets up network and listens for input from other player

    String[] shipStrings = new String[5];

    Thread t;

    int numberOfMineSunk = 0; //Keeps track of how many ships on my board have been sunk
    int numberOfOpponentSunk = 0; //Keeps track of how many ships on opponent board have been sunk

    boolean gameOver; //Flag to let program know if the game is over yet or not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the extras from launching this intent
        Intent gameIntent = getIntent();


        //instantiate my board
        myBoard = new Board(this, true);
        myBoard.inSetup = false;

        //instatiate opponent board
        opponentBoard = new Board(this, false);
        opponentBoard.inSetup = false;

        gameOver = false; //When game starts, set gameOver to false

        //connecting to the server
        try
        {
            ear = new Ear(); //Create new ear to listen from the server
            ear.start(); //Start ear thread

            t.sleep(4000); //give it time to connect and then continue

            //Add five strings from SetupActivity to shipString array
            for (int i = 0; i < 5; i++)
            {
                shipStrings[i] = gameIntent.getStringExtra("ship" + Integer.toString(i));
            }

            //Fill myBoard with five ships using fromString function defined in Board
            myBoard.fromString(shipStrings);
            myBoard.updateShips(); //Update the boards on the ship

            //Send "Ready over the server" to initiate game with other player
            (t = new Thread( new Mouth("Ready"))).start();

            myBoard.setOnTouchListener(touchListener); //Give my board a touch listener
            opponentBoard.setOnTouchListener(touchListener); //Give opponent board a touch listener

        }
        catch ( Exception e )
        { System.err.println("-----------GameShell setup error="+e); }


        setContentView(opponentBoard); //Begin player's by looking at opponent board
        //Player's will only see blue squares even though the program knows where the opponent's
        //ships are located
    }

    View.OnTouchListener touchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent m)
        { //create new touchlistener
            System.out.println("Click"); //For programmer to debug, user cannot see

            int i = ((int)m.getX()-squareSize)/squareSize; //Get i of user click
            int j = ((int)m.getY()-squareSize)/squareSize; //Get j of user click

            //If it is this player's turn, the player is not currently looking at his/her own board,
            //the player clicked on the board, and the game is not over
            if(myTurn && (!viewMe && i >= 0 && i < 10 && j >= 0 && j < 10) && (gameOver == false))
            {
                //If the player clicked on a square that he/she has not clicked on yet
                //Therefore, the color would be blue rather than white or red
                if(!opponentBoard.theSquares[i][j].getColor().equals("red") &&
                        !opponentBoard.theSquares[i][j].getColor().equals("white"))
                {
                    opponentBoard.handleTurn(i, j); //Handle the turn of user (defined in board)
                    opponentBoard.updateIfSunk(); //Update ships to see if any more have been sunk
                                                    //(Defined in board)

                    String line = "Torpedo " + i + " " + j; //Create string to send to other player
                    (t = new Thread(new Mouth(line))).start(); //Send string to other player
                    myTurn = false; //It is not longer this player's turn
                    //Used to change the display to let user know it is no longer their turn
                    myBoard.myTurn = false;
                    opponentBoard.myTurn = false;

                    //Strings that will be used to alert user of various things below
                    String msg1;
                    String msg2;


                    if(myTurn == false) //If it is not my turn
                    {
                        //Put message on screen telling user to wait for opponent
                        msg2 = "Waiting for opponent to take turn.";
                        myBoard.setMessage2(msg2);
                        opponentBoard.setMessage2(msg2);
                    }

                    if(opponentBoard.winner())
                    {
                        //This user wins, let the user know on the screen
                        msg1 = "You sunk your opponent's ships. YOU WIN!!";
                        myBoard.setMessage1(msg1);
                        opponentBoard.setMessage1(msg1);

                        msg2 = "";
                        myBoard.setMessage2(msg2);
                        opponentBoard.setMessage2(msg2);

                        gameOver = true; //The game is now over
                    }
                    else if(opponentBoard.numberOfSunk() > numberOfOpponentSunk) //Tell User that opponent sunk a ship
                    {
                        numberOfOpponentSunk = opponentBoard.numberOfSunk(); //update how many ships have been sunk

                        //User sunk a ship, let the user know on screen
                        msg1 = "You sunk the opponent's Battleship!";
                        myBoard.setMessage1(msg1);
                        opponentBoard.setMessage1(msg1);
                    }
                    else
                    {
                        //This means nothing has happened that reqires a message
                        msg1 = " ";
                        myBoard.setMessage1(msg1);
                        opponentBoard.setMessage1(msg1);
                    }



                }

            }
            //User clicked the switch button, if currently viewing opponent board, user will now be
            //viewing myBoard
            else if(i >= 0 && i < 10 && j >= 11 && j < 13)
            {
                System.out.println("clicked the switch button");//For programmer to debug, user cannot see
                if(viewMe) //If user is looking at myBoard
                {
                    viewMe = false; //Switch flag so that user can view opponent board
                }
                //Do the same if user is currently looking at opponent board to switch to myBoard
                else
                {
                    viewMe = true;
                }
            }

            //Purely for programmer debugging, the user is not able to see these outputs
            if(opponentBoard.winner() == true)
            {
                System.out.println("You win");
            }
            else if(myBoard.winner() == true)
            {
                System.out.println("Your opponent wins");
            }

            //Set content view based on the viewMe booleab
            //If it is true, set content view to myBoard
            //If it os false, set content view to opponentBoard
            if(!viewMe)
            {
                setContentView(opponentBoard);
            }
            else
            {
                setContentView(myBoard);
            }


            return false;
        }
    };



    // makes the call (opens the socket), and then oepns the read
    // and writ objects.  Then goes into a loop to listen to any
    // messages coming over the network.  NOTE: in this test shell
    // version, I'm just writing whatever came over the network to
    // the 'heard' field.  You will want to take the message and
    // perform whatever game function is specified by it.
    public class Ear extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                System.out.println("-----------about to try to call " + ip + "/" + port);
                sock = new Socket(ip, port); //Create new socket
                System.out.println("-----------woohoo! socket established.");

                InputStream in = sock.getInputStream(); //set inputStream for socket
                bin = new BufferedReader(new InputStreamReader(in)); //create new bufferedReader
                pout = new PrintWriter(sock.getOutputStream(), true);
                //create new printWriter
            }
            catch( Exception e )
            { System.out.println("------------ Ear setup error = "+e); }

            while ( keepGoing )
            {
                String line; //String to hold line from bufferedReader
                try
                {
                    line = bin.readLine(); //set line to next line from bufferedReader

                    StringTokenizer st = new StringTokenizer(line); //Create new stringTokenizer
                    String flag = st.nextToken(); //set flag equal to first word in line
                    if(flag.equals("Ready")) //If first word is Ready
                    {
                        //Send Yes over server, this will make sure that the two games are connected
                        (t = new Thread( new Mouth("Yes"))).start();
                    }
                    else if(flag.equals("Yes")) //If flag equals Yes
                    {
                        //Means that the other game already received ready and responded with Yes

                        //Send my ships and their attributes in string form to other game
                        for (int i = 0; i < 5; i++) {

                            (t = new Thread(new Mouth(shipStrings[i]))).start();
                            System.out.println("Just sent ship " + i);
                        }
                    }
                    else if(flag.equals("Board")) //If flag equals Board
                    {
                        //This line must be a ship because ship strings have the Board indicator

                        //Take the line and add it to opponentBoard
                        opponentBoard.fromString(line);
                        //Update current state of ships of opponentboard
                        opponentBoard.updateShips();
                    }
                    else if(flag.equals("Torpedo")) //If flag equals torpedo
                    {
                        //This means that the opponent sent a move over the server

                        int i = Integer.parseInt(st.nextToken()); //i of move will be next token
                        int j = Integer.parseInt(st.nextToken()); //j of move will be next token

                        myBoard.handleTurn(i,j); //Handle the move by opponent
                        myBoard.updateIfSunk(); //Check to see if any new ships have been sunk
                        myTurn = true; //It is now my turn because the user just went
                        myBoard.myTurn = true;
                        opponentBoard.myTurn = true;

                        //Strings that will be used to display messages to the user
                        String msg;
                        String msg1;
                        String msg2;
                        if(myBoard.hit(i,j)) //If the opponent's move was a hit
                        {
                            //Tell the user that the opponent hit a ship
                            msg = "Opponent's Last Move: Hit at " + i + ", " + j + "!";
                            myBoard.setMessage(msg);
                            opponentBoard.setMessage(msg);

                        }
                        else if(!myBoard.hit(i,j)) //If the opponent's move was a miss
                        {
                            //Tell the user that the opponent missed
                            msg = "Opponent's Last Move: Miss at " + i + ", " + j + "!";
                            myBoard.setMessage(msg);
                            opponentBoard.setMessage(msg);

                        }

                        if(myTurn) //If it is my turn
                        {
                            //Tell the user it is their turn to go
                            msg2 = "It's Your Turn!";
                            myBoard.setMessage2(msg2);
                            opponentBoard.setMessage2(msg2);
                        }

                        if(myBoard.winner()) //If there is now a winner, the opponent won
                        {
                            //Tell the user that the opponent won the game
                            msg1 = "Your last ship was sunk, the opponent wins!";
                            myBoard.setMessage1(msg1);
                            opponentBoard.setMessage1(msg1);

                            msg2 = "";
                            myBoard.setMessage2(msg2);
                            opponentBoard.setMessage2(msg2);

                            gameOver = true; //Game is now over
                        }
                        else if(myBoard.numberOfSunk() > numberOfMineSunk) //Tell User that opponent sunk a ship
                        {
                            numberOfMineSunk = myBoard.numberOfSunk(); //Update number of sunk ships

                            //Tell the user that the opponent sunk your ship
                            msg1 = "The Opponent Sunk Your BattleShip!";
                            myBoard.setMessage1(msg1);
                            opponentBoard.setMessage1(msg1);
                        }
                        else
                        {
                            //Means that there is nothing to output to the user at the moment
                            msg1 = " ";
                            myBoard.setMessage1(msg1);
                            opponentBoard.setMessage1(msg1);
                        }



                    }
                    this.sleep(100); //A very short pause to make sure boards come over server
                                            //in the correct manner
                    if (line==null || line.equals("null") ) { keepGoing = false; }

                }
                catch(Exception e )
                { System.out.println("Ear error reading from pipe="+e); }
            }
        }
    }

    // separate thread for sending messages.  In Java on terminal this does
    // not need a separate thread, but in Android it does .
    // This is intended to be launched in anonymous thread, and since the
    // run() function takes no argument, we send the message first to the
    // constructor which stores it in msg, and then (presumably) the
    // thread is started and run() uses and sends msg immediately
    // (and then the anonymous thread disappears).
    // If you have coded the actions in your game as one line string
    // commands, you can use this class without alteration I think.
    public class Mouth implements Runnable
    {
        String msg;

        public Mouth( String s )
        {
            msg = s;
        }

        // send s out on the socket
        public void run()
        {
            try
            {
                pout.println( msg );
                pout.flush();
            }
            catch(Exception e)
            {System.out.println("Mouth error trying to output to client="+e);}
        }
    }
}
