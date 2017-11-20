package bap58.battleship2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Iterator;

import static bap58.battleship2.BoardSquare.squareSize;

public class SetupActivity extends AppCompatActivity
    //implements View.OnTouchListener
{

    Board myBoard;
    boolean shipSelected = false;
    int indexSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        myBoard = new Board(this, true);

        myBoard.setOnTouchListener(touchListener);

        setContentView(myBoard);

        /*
        Iterator<Ship> it = myBoard.ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();
            int i = ship.getI();
            int j = ship.getJ();

            myBoard.rotateShip(i,j);
        }
        */

        myBoard.updateShips();
        setContentView(myBoard);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            int i = ((int)event.getX()-squareSize)/squareSize;
            int j = ((int)event.getY()-squareSize)/squareSize;

            System.out.println("Click at: "+i+", "+j);

            int indexOfShip = myBoard.whichShip(i,j);
            System.out.println(""+indexOfShip);

            if(!shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
            {
                System.out.println("in the first ship clicked part");

                if(myBoard.theSquares[i][j].getColor().equals("gray"))
                {
                    shipSelected = true;
                    indexSelected = indexOfShip;
                    System.out.println("got a ship: " + indexSelected);
                }
            }
            else if(shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
            {
                System.out.println("in the second click if thing");

                Iterator<Ship> it = myBoard.ships.iterator();
                int counter = 0;
                String o = "";
                int s = 0;
                while(it.hasNext() && counter <= indexSelected) {
                    Ship ship = it.next();
                    o = ship.getOrientation();
                    s = ship.getSize();
                    counter++;
                }
                System.out.println("counter stopped at: "+counter);

                if(myBoard.theSquares[i][j].getColor().equals("blue") &&
                        !myBoard.overlapsAnotherShip(o, s, i, j) &&
                        !myBoard.isOffEdge(o, s, i, j))
                {
                    System.out.println("ready to move a ship");

                    shipSelected = false;

                    Iterator<Ship> it1 = myBoard.ships.iterator();
                    int counter1 = 0;

                    while(it1.hasNext() && counter1 <= indexSelected) {
                        Ship ship = it1.next();
                        if(counter1 == indexSelected)
                        {
                            ship.setI(i);
                            ship.setJ(j);
                        }

                        counter1++;
                    }
                }
                else
                {
                    System.out.println("Didn't hit a blue square");
                }


            }

            myBoard.updateShips();
            setContentView(myBoard);

            return false;
        }
    };

    /*
    @Override
    public boolean onTouch(View v, MotionEvent event) {



        System.out.println("Click");

        int i = ((int)event.getX()-squareSize)/squareSize;
        int j = ((int)event.getY()-squareSize)/squareSize;

        int indexOfShip = myBoard.whichShip(i,j);

        if(!shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
        {
            System.out.println("in the first ship clicked part");

            if(myBoard.theSquares[i][j].getColor().equals("gray"))
            {
                shipSelected = true;
                indexSelected = indexOfShip;
                System.out.println("got a ship: " + indexSelected);
            }
        }
        else if(shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
        {
            System.out.println("in the second click if thing");

            Iterator<Ship> it = myBoard.ships.iterator();
            int counter = 0;
            String o = "";
            int s = 0;
            while(it.hasNext() && counter <= indexSelected) {
                Ship ship = it.next();
                o = ship.getOrientation();
                s = ship.getSize();
                counter++;
            }

            if(myBoard.theSquares[i][j].getColor().equals("blue") &&
                    !myBoard.overlapsAnotherShip(o, s, i, j) &&
                    !myBoard.isOffEdge(o, s, i, j))
            {
                shipSelected = false;

                Iterator<Ship> it1 = myBoard.ships.iterator();
                int counter1 = 0;

                while(it1.hasNext() && counter1 <= indexSelected) {
                    Ship ship = it1.next();
                    if(counter1 == indexSelected)
                    {
                        ship.setI(i);
                        ship.setJ(j);
                    }

                }
            }
            else
            {
                System.out.println("Didn't hit a blue square");
            }

            setContentView(myBoard);
        }

        return false;
    }
    */
}
