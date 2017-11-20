package bap58.battleship2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;

import static bap58.battleship2.BoardSquare.squareSize;

public class SetupActivity extends AppCompatActivity
    implements View.OnTouchListener
{

    Board myBoard;
    boolean shipSelectd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        myBoard = new Board(this, true);
        setContentView(myBoard);

        Iterator<Ship> it = myBoard.ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();
            int i = ship.getI();
            int j = ship.getJ();

            myBoard.rotateShip(i,j);
        }

        myBoard.updateShips();
        setContentView(myBoard);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int i = ((int)event.getX()-squareSize)/squareSize;
        int j = ((int)event.getY()-squareSize)/squareSize;

        if(!shipSelectd && i >= 0 && i < 10 && j >= 0 && j < 10)
        {
            if(myBoard.theSquares[i][j].getColor().equals("gray"))
            {

            }
        }
        else if(shipSelectd && i >= 0 && i < 10 && j >= 0 && j < 10)
        {

        }


        return false;
    }
}
