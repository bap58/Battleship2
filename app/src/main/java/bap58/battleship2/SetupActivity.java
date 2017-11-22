package bap58.battleship2;

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

        myBoard.updateShips();
        setContentView(myBoard);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //Get the i and j location of the square that the user clicked on
            int i = ((int)event.getX()-squareSize)/squareSize;
            int j = ((int)event.getY()-squareSize)/squareSize;

            System.out.println("Click at: "+i+", "+j);

            //If ship has not been selected yet and user clicked on board
            if(!shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
            {
                System.out.println("in the first ship clicked part");
                //Check the square that the user pressed and see if it has a ship/is gray
                if(myBoard.theSquares[i][j].getColor().equals("gray"))
                {
                    shipSelected = true; //A ship has now been selected
                    indexSelected = myBoard.whichShip(i,j); //Get index of ship clicked
                    System.out.println("got a ship: " + indexSelected);
                }
            }
            //If ship has been selected already and user clicks on another square on the board
            else if(shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10)
            {
                System.out.println("in the second click if thing");


                Iterator<Ship> it = myBoard.ships.iterator(); //Iterator for list of ships
                int counter = 0; //Counter to be stopped at ship user selected
                String o = ""; //Will be used to hold orientation of ship that user selected
                int s = 0; //Will be used to hold size of ship that user selected
                //Iterate through list of ships and stop at ship user selected
                while(it.hasNext() && counter <= indexSelected) {
                    Ship ship = it.next(); //By last iteration of loop, this is ship user clicked
                    o = ship.getOrientation(); //Save its orientation
                    s = ship.getSize(); //Save its size
                    counter++; //Update counter
                }
                System.out.println("counter stopped at: "+counter);

                //If square user clicked on is open to move ship to/is blue, if the ship would
                //not overlap another ship if moved there, and if ship would not go off the edge
                //of the board if moved there
                if(myBoard.theSquares[i][j].getColor().equals("blue") &&
                        !myBoard.overlapsAnotherShip(o, s, i, j) &&
                        !myBoard.isOffEdge(o, s, i, j))
                {
                    System.out.println("ready to move a ship");

                    shipSelected = false; //Ship is going to be moved, so we do not want it to be
                                            //selected after this if statement completes to allow
                                            //the user to move another ship

                    Iterator<Ship> it1 = myBoard.ships.iterator(); //Iterator for list of ships
                    int counter1 = 0; //Will be used to stop at ship user selected

                    //Iterate through list of ships and stop at ship user selected
                    while(it1.hasNext() && counter1 <= indexSelected)
                    {
                        Ship ship = it1.next(); //At last iteration, this will be ship user clicked
                        //If we are at the ship that the user selected in the list of ships
                        if(counter1 == indexSelected)
                        {
                            ship.setI(i); //set i value of user click to i value of ship's 1st square
                            ship.setJ(j); //set j value of user click to j value of ship's 1st square
                        }

                        counter1++; //Update counter to reach ship that user clicked on
                    }
                }
            }

            //Need to update location of ships to reflect changes occured above
            myBoard.updateShips();
            //Redraw the Board to display the changes made to the ships locations and board
            setContentView(myBoard);


            return false;
        }
    };

}
