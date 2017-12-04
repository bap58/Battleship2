package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Iterator;

import static bap58.battleship2.BoardSquare.squareSize;

public class SetupActivity extends AppCompatActivity
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

        //Not necessary, remember this only executes when it is created
        //and we already are displaying the board
        //myBoard.updateShips();
        //setContentView(myBoard);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            int i = ((int)event.getX()-squareSize)/squareSize;
            int j = ((int)event.getY()-squareSize)/squareSize;

            System.out.println("Click at: "+i+", "+j);

            int indexOfShip = myBoard.whichShip(i,j);
            System.out.println(""+indexOfShip);
            System.out.println("ship selected is"+shipSelected);

            if(!shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
            {
                System.out.println("in the first ship clicked part");

                if(myBoard.theSquares[i][j].getColor().equals("gray"))
                {
                    shipSelected = true;
                    indexSelected = indexOfShip;

                    Iterator<Ship> it = myBoard.ships.iterator();
                    int counter = 0;

                    while(it.hasNext() && counter <= indexSelected) {
                        Ship ship = it.next();

                        if(counter == indexSelected)
                        {
                            ship.setSelected(true);
                        }

                        counter++;
                    }

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
                            ship.setSelected(false);
                        }

                        counter1++;
                    }

                    myBoard.setShipColor(indexSelected, "gray");

                }

                else
                {
                    System.out.println("Didn't hit a blue square");
                }


            }

            else if(shipSelected &&  i >= 0 && i < 10 && j >= 11 && j < 13)
            {
                System.out.println("rotate button loop");

                Iterator<Ship> it2 = myBoard.ships.iterator();
                int counter2 = 0;
                String o2 = "";
                int s2 = 0;
                int i2 = 0;
                int j2 = 0;
                while(it2.hasNext() && counter2 <= indexSelected) {
                    Ship ship = it2.next();
                    o2 = ship.getOrientation();
                    s2 = ship.getSize();
                    i2 = ship.getI();
                    j2 = ship.getJ();
                    if(counter2 == indexSelected) {
                        if (o2.equals("vertical")) {
                            o2 = "horizontal";
                        } else {
                            o2 = "vertical";
                        }

                        if (!myBoard.overlapsAnotherShip(o2, s2, i2, j2) &&
                                !myBoard.isOffEdge(o2, s2, i2, j2)) {
                            ship.rotate();
                            ship.setSelected(false);
                            shipSelected = false;
                        }
                    }
                    counter2++;
                }


            }
            else if(!shipSelected &&  i >= 0 && i < 10 && j >= 14 && j < 16)
            {
                myBoard.inSetup = false;
                System.out.println("Ready for battle");

                startGame();
            }

            myBoard.updateShips();
            setContentView(myBoard);

            return false;
        }
    };

    void startGame()
    {
        Intent gameIntent = new Intent(this, GameActivity.class);

        String[] shipStrings = new String[5];
        Iterator<Ship> it = myBoard.ships.iterator();
        int counter = 0;
        while(it.hasNext())
        {
            Ship ship = it.next();
            shipStrings[counter] = ship.toString();
            counter++;
        }

        gameIntent.putExtra("ship0", shipStrings[0]);
        gameIntent.putExtra("ship1", shipStrings[1]);
        gameIntent.putExtra("ship2", shipStrings[2]);
        gameIntent.putExtra("ship3", shipStrings[3]);
        gameIntent.putExtra("ship4", shipStrings[4]);

        startActivity(gameIntent);
    }

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
