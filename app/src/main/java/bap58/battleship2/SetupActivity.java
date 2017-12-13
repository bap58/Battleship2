package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Iterator;

import static bap58.battleship2.BoardSquare.squareSize;

public class SetupActivity extends AppCompatActivity {

    Board myBoard; //This is the player's board
    boolean shipSelected = false; //Flag to let program know if player has selected ship
    int indexSelected = -1; //Will be used to determine which ship a user selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        myBoard = new Board(this, true); //Create new board

        myBoard.setOnTouchListener(touchListener); //Give the board a touch listener

        setContentView(myBoard); //Set the content view to the player's board

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() { //Create new touch listener
        @Override
        public boolean onTouch(View v, MotionEvent event) { //When a user touches the screen


            int i = ((int) event.getX() - squareSize) / squareSize; //Find i user clicked on
            int j = ((int) event.getY() - squareSize) / squareSize; //Find j user clicked on

            System.out.println("Click at: " + i + ", " + j); //For programmers to debug, user can't see

            int indexOfShip = myBoard.whichShip(i, j); //Find which ship user selected
            //indexOfShip will equal -1 if the user did not click on ship

            System.out.println("" + indexOfShip); //For programmers to debug, user can't see
            System.out.println("ship selected is" + shipSelected); //For programmers to debug, user can't see

            //If ship is not currently selected and user clicked on the board
            if (!shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10) {
                System.out.println("in the first ship clicked part"); //For programmers to debug, user can't see

                //Check to see if the user did click on a ship
                //Squares containing a ship will be gray
                if (myBoard.theSquares[i][j].getColor().equals("gray")) {
                    shipSelected = true; //Change shipSelected to gray
                    indexSelected = indexOfShip; //save the index of the ship selected

                    Iterator<Ship> it = myBoard.ships.iterator(); //Need an iterator to go through
                    int counter = 0; //Create a counter           //list of ships

                    //While there is another ship and counter does not go past index of selected ship
                    //This way we can stop at the ship the user selected
                    while (it.hasNext() && counter <= indexSelected) {
                        Ship ship = it.next();

                        if (counter == indexSelected) //If this iteration is when counter equals
                        {                               //index of ship user selected
                            ship.setSelected(true); //Make that ship selected
                            //Changes color to yellow
                        }

                        counter++; //Update counter for while loop
                    }

                    System.out.println("got a ship: " + indexSelected); //For programmers to debug, user can't see
                }
            }
            //A ship is currently selected and the user clicked on the board
            //Therefore, the user is trying to move a selected ship
            else if (shipSelected && i >= 0 && i < 10 && j >= 0 && j < 10) {
                System.out.println("in the second click if thing"); //For programmers to debug, user can't see

                Iterator<Ship> it = myBoard.ships.iterator(); //Need an iterator to go through ships
                int counter = 0; //Counter so we can stop when we get to selected ship
                String o = ""; //String to hold orientation of selected ship
                int s = 0; //int to hold size of selected ship
                //While there is another ship and counter does not go past index of selected ship
                //This way we can stop at the ship the user selected
                while (it.hasNext() && counter <= indexSelected) {
                    Ship ship = it.next();
                    o = ship.getOrientation(); //Last iteration saves selected ship's orientation
                    s = ship.getSize(); //Last iteration saves the selected ship's size
                    counter++; //Update counter for while loop to stop
                }
                System.out.println("counter stopped at: " + counter); //For programmers to debug, user can't see

                //Remember: A ship is already selected at this point, so we need to check if the
                //blue square clicked on by the user is a valid place to move the ship
                //This if checks to make sure the user clicked a blue square and that the ship
                //will not overlap another ship or go off the edge if moved there
                if (myBoard.theSquares[i][j].getColor().equals("blue") &&
                        !myBoard.overlapsAnotherShip(o, s, i, j) &&
                        !myBoard.isOffEdge(o, s, i, j)) {
                    System.out.println("ready to move a ship"); //For programmers to debug, user can't see

                    shipSelected = false; //Change shipSelected to false so user can move another
                    //ship after this one

                    Iterator<Ship> it1 = myBoard.ships.iterator(); //Need an iterator to go through ships
                    int counter1 = 0; //Counter to stop at selected ship in while loop

                    //While there is another ship and counter does not go past index of selected ship
                    //This way we can stop at the ship the user selected
                    while (it1.hasNext() && counter1 <= indexSelected) {
                        Ship ship = it1.next();
                        if (counter1 == indexSelected) //If we are at selected ship
                        {
                            //Within this if statement, the user has selected a valid blue square
                            //to move the selected ship to
                            ship.setI(i); //Change i of ship to i of user click
                            ship.setJ(j); //Change j of ship to j of user click
                            ship.setSelected(false); //The ship should no longer be selected
                        }

                        counter1++; //Update counter for the while loop to stop
                    }

                    //Set ship back to gray because it is no longer selected
                    myBoard.setShipColor(indexSelected, "gray");

                } else {
                    System.out.println("Didn't hit a blue square"); //For programmers to debug, user can't see
                }


            }
            //A ship is currently selected and the user clicked on the Rotate button
            //This means the user wants to rotate the selected ship
            else if (shipSelected && i >= 0 && i < 10 && j >= 11 && j < 13) {
                System.out.println("rotate button loop"); //For programmers to debug, user can't see

                Iterator<Ship> it2 = myBoard.ships.iterator(); //Need an iterator to go through ships
                int counter2 = 0; //counter to stop at selected ship
                String o2 = ""; //String to save orientation of selected ship
                int s2 = 0; //int to save the size of the selected ship
                int i2 = 0; //int to save the i of the selected ship
                int j2 = 0; //int to save the j of the selected ship

                //While there is another ship and counter does not go past index of selected ship
                //This way we can stop at the ship the user selected
                while (it2.hasNext() && counter2 <= indexSelected) {
                    Ship ship = it2.next();
                    o2 = ship.getOrientation(); //Last iteration saves selected ship's orientation
                    s2 = ship.getSize(); //Last iteration saves selected ship's size
                    i2 = ship.getI(); //Last iteration saves selected ship's i
                    j2 = ship.getJ(); //Last iteration saves selected ship's j
                    if (counter2 == indexSelected) { //Once we are at the selected ship
                        if (o2.equals("vertical")) { //Check the orientation of that ship
                            o2 = "horizontal"; //Change temporary orientation to opposite
                        } else {
                            o2 = "vertical";
                        }

                        //Check to see if the selected ship is able to be rotated
                        //It cannot be rotated if it will overlap another ship or go off the edge
                        if (!myBoard.overlapsAnotherShip(o2, s2, i2, j2) &&
                                !myBoard.isOffEdge(o2, s2, i2, j2)) {
                            //If inside this statement, the ship is able to rotate
                            ship.rotate(); //Rotate the ship
                            ship.setSelected(false); //This ship should not be selected anymore
                            shipSelected = false; //There is no longer a ship selected
                        }
                    }
                    counter2++; //Update counter to stop while loop
                }


            }
            //If ship is not currently selected and the user hits the ready for battle button
            //This means that the user is satisfied with the set up of their board and ready to play
            else if (!shipSelected && i >= 0 && i < 10 && j >= 14 && j < 16) {
                myBoard.inSetup = false; //Change this board to no longer in set up phase
                System.out.println("Ready for battle"); //For programmers to debug, user can't see

                startGame(); //Call start game function located and explained below
            }

            myBoard.updateShips(); //Update the state of the ships after each click to make
            //they are consistent with the user's clicks
            setContentView(myBoard); //Repaint the board in its updated state

            return false;
        }
    };

    //This function will be called when the user hits the ready for battle button and will begin the
    //gameActivity intent where the actual gameplay of Battleship will take place
    //It takes in no arguments and returns nothing
    void startGame() {
        //Create new intent
        Intent gameIntent = new Intent(this, GameActivity.class);


        String[] shipStrings = new String[5]; //Create an array of Strings to hold ship values
        Iterator<Ship> it = myBoard.ships.iterator(); //Need iterator to go through ships
        int counter = 0; //Counter to add each ship to the right spot in array
        //While there is another ship
        while (it.hasNext()) {
            Ship ship = it.next();
            shipStrings[counter] = ship.toString(); //Add string version of current ship to array
            counter++; //Update counter
        }

        gameIntent.putExtra("ship0", shipStrings[0]); //Send first ship to GameActivity
        gameIntent.putExtra("ship1", shipStrings[1]); //Send second ship to GameActivity
        gameIntent.putExtra("ship2", shipStrings[2]); //Send third ship to GameActivity
        gameIntent.putExtra("ship3", shipStrings[3]); //Send fourth ship to GameActivity
        gameIntent.putExtra("ship4", shipStrings[4]); //Send fifth ship to GameActivity

        startActivity(gameIntent); //Start GameActivity
    }

}