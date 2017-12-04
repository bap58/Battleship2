package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import static bap58.battleship2.BoardSquare.squareSize;

public class GameActivity extends AppCompatActivity
{

    Board myBoard;
    Board yourBoard;
    Boolean myTurn = false;
    Boolean viewMe = false;

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
}
