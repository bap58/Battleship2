package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    Board myBoard;
    Board yourBoard;
    Boolean myTurn = false;
    Boolean viewMe = false;

    BufferedReader bin; // object for input from port
    PrintWriter pout;   // object for output to port
    //String ip = "2600:8806:6101:e200:4428:c73a:455f:dd88";
    String ip = "10.0.2.2"; // this computer
    int port = 11011; // if you are on your own machine, this can be whatever.
    Socket sock; // the connection to your server
    boolean keepGoing = true; // set to false to shut down input loop
    Ear ear; // object that sets up network and listens for input from other player.

    String[] shipStrings1 = new String[5];
    int shipCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get the extras from launching this intent
        Intent gameIntent = getIntent();

        String[] shipStrings = new String[5];
        shipStrings[0] = gameIntent.getStringExtra("ship0");
        shipStrings[1] = gameIntent.getStringExtra("ship1");
        shipStrings[2] = gameIntent.getStringExtra("ship2");
        shipStrings[3] = gameIntent.getStringExtra("ship3");
        shipStrings[4] = gameIntent.getStringExtra("ship4");

        //instantiate my board
        myBoard = new Board(this, true);
        myBoard.inSetup = false;

        myBoard.fromString(shipStrings);
        myBoard.updateShips();
        setContentView(myBoard);

        //instatiate your board
        yourBoard = new Board(this, false);
        yourBoard.inSetup = false;

        myBoard.setOnTouchListener(touchListener);
        yourBoard.setOnTouchListener(touchListener);
        setContentView(yourBoard);




    }

    View.OnTouchListener touchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent m)
        {
            System.out.println("Click");

            int i = ((int)m.getX()-squareSize)/squareSize;
            int j = ((int)m.getY()-squareSize)/squareSize;

            //This is just me testing some of the new functions, they seem to be working
            //Did not put in any error checking, so make sure not to click outside the board or else
            //it will explode lol
            if(!viewMe && i >= 0 && i < 10 && j >= 0 && j < 10)
            {
                yourBoard.handleTurn(i, j);
                yourBoard.updateIfSunk();
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


            if(yourBoard.winner() == true)
            {
                System.out.println("Winner Winner Chicken Dinner");
            }
            //setContentView(yourBoard);

            if(!viewMe)
            {
                setContentView(yourBoard);
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

                    //heard.setText(line); // your code replaces this ... does
                    // what you need to do in the game based on this message,
                    // not just print to 'heard' field like I do here.
                    //System.out.println("heard:"+line); write to someplace
                    Thread.sleep(1000);
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
