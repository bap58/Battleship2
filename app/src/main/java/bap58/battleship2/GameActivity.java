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

/*
Things to do:
- Let the player know when they are connected to another player
- Take turns
 */

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
    int shipCounter = 0;

    Thread t;
    boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);

        //get the extras from launching this intent
        Intent gameIntent = getIntent();

        //created this outside..right?
        //String[] shipStrings = new String[5];

        //instantiate my board
        myBoard = new Board(this, true);
        myBoard.inSetup = false;

        //instatiate opponent board
        opponentBoard = new Board(this, false);
        opponentBoard.inSetup = false;

        //connecting to the server
        try
        {
            ear = new Ear();
            ear.start();

            t.sleep(4000); //give it time to connect and then continue

            for (int i = 0; i < 5; i++){
                shipStrings[i] = gameIntent.getStringExtra("ship" + Integer.toString(i));
            }

            myBoard.fromString(shipStrings);
            myBoard.updateShips();

            (t = new Thread( new Mouth("Ready"))).start();

            myBoard.setOnTouchListener(touchListener);
            opponentBoard.setOnTouchListener(touchListener);

        }
        catch ( Exception e )
        { System.err.println("-----------GameShell setup error="+e); }


        /*
        shipStrings[0] = gameIntent.getStringExtra("ship0");
        shipStrings[1] = gameIntent.getStringExtra("ship1");
        shipStrings[2] = gameIntent.getStringExtra("ship2");
        shipStrings[3] = gameIntent.getStringExtra("ship3");
        shipStrings[4] = gameIntent.getStringExtra("ship4");
        */

        //myBoard.fromString(shipStrings);
        //myBoard.updateShips();

        //not necessary because as soon as the opponent board is created it will show that
        //therefore we have to choose which board we want to show when they are starting game
        //I suggest the opponent board (changed the name to make it more clear)
        //setContentView(myBoard);

        //instatiate opponent board
        //opponentBoard = new Board(this, false);
        //opponentBoard.inSetup = false;
        //opponentBoard.updateShips();

        //myBoard.setOnTouchListener(touchListener);
        //opponentBoard.setOnTouchListener(touchListener);
        setContentView(opponentBoard);


    }

    View.OnTouchListener touchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent m)
        {
            System.out.println("Click");

            int i = ((int)m.getX()-squareSize)/squareSize;
            int j = ((int)m.getY()-squareSize)/squareSize;


            if(myTurn && (!viewMe && i >= 0 && i < 10 && j >= 0 && j < 10))
            {
                opponentBoard.handleTurn(i, j);
                opponentBoard.updateIfSunk();

                String line = "Torpedo " + i + " " + j;
                (t = new Thread(new Mouth(line))).start();
                myTurn = false;
                myBoard.myTurn = false;


            }
            else if(i >= 0 && i < 10 && j >= 11 && j < 13)
            {
                System.out.println("clicked the switch button");
                if(viewMe)
                {
                    viewMe = false;
                }
                else
                {
                    viewMe = true;
                }
            }


            if(opponentBoard.winner() == true)
            {
                System.out.println("Winner Winner Chicken Dinner");
            }
            //setContentView(yourBoard);

            if(!viewMe)
            {
                setContentView(opponentBoard);
                //System.out.println("view my board");
            }
            else
            {
                setContentView(myBoard);
                //System.out.println("view your board");
            }

            //System.out.println("Done with click");

            return false;
        }

        /*
        --! Put this code somewhere in on touch or replace with take turn

        String line = et.getText().toString();
        //String line = scan.nextLine(); // get input from user
        Thread t;
        (t = new Thread( new Mouth(line))).start();
         */

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
                sock = new Socket(ip, port);
                System.out.println("-----------woohoo! socket established.");

                InputStream in = sock.getInputStream();
                bin = new BufferedReader(new InputStreamReader(in));
                pout = new PrintWriter(sock.getOutputStream(), true);
            }
            catch( Exception e )
            { System.out.println("------------ Ear setup error = "+e); }

            while ( keepGoing )
            {
                String line;
                try
                {
                    line = bin.readLine();

                    String[] st = line.split(" ");
                    switch (st[0]){
                        case "Ready":
                            for (int i = 0; i < 5; i++){

                                (t = new Thread( new Mouth(shipStrings[i]))).start();
                                System.out.println("Just sent ship " + i);
                            }
                            (t = new Thread( new Mouth("Yes"))).start();
                        case "Yes":
                            for (int i = 0; i < 5; i++){

                                (t = new Thread( new Mouth(shipStrings[i]))).start();
                                System.out.println("Just sent ship " + i);
                            }

                        case "Board":
                            Log.i("-------", "I heard......" + line);
                            opponentBoard.fromString(line);
                            opponentBoard.updateShips();
                        case "Torpedo":
                            myBoard.torpedo(line);
                            myTurn = true;
                            myBoard.myTurn = true;
                        default:
                            Log.i("-------", "loop not prepared for that message " + line);
                    }

                    /*
                    StringTokenizer st = new StringTokenizer(line);
                    String check = st.nextToken();
                    if(check.equals("Board"))
                    {
                        shipStrings1[shipCounter] = line;
                        shipCounter++;

                        if(shipCounter == 5)
                        {
                            yourBoard.fromString(shipStrings1);
                            yourBoard.updateShips();
                        }

                    }
                    else if(check.equals("Move"))
                    {
                        int i = Integer.parseInt(st.nextToken());
                        int j = Integer.parseInt(st.nextToken());

                        myBoard.handleTurn(i,j);
                        myTurn = true;
                    }
*/
                    //heard.setText(line); // your code replaces this ... does
                    // what you need to do in the game based on this message,
                    // not just print to 'heard' field like I do here.
                    //System.out.println("heard:"+line); write to someplace
                    Thread.sleep(1000);
                    //if (line==null || line.equals("null") ) { keepGoing = false; }

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
