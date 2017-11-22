package bap58.battleship2;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Brian on 11/13/2017.
 * Beatriz is working on this
 */

//import statements
import android.content.Context;
import android.graphics.Canvas; //graphics
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import static bap58.battleship2.BoardSquare.edgeWidth;
import static bap58.battleship2.BoardSquare.squareSize;

//start implementing public class Board
public class Board extends View
{

    BoardSquare[][] theSquares; //Matrix of squares that make up the board
    static int dimension = 10; //Will be the width and length of the board
    static int[] sizes = {2, 3, 3, 4, 5}; //Represent sizes of game pieces
    boolean myBoard;
    //on own board list ships that player will place
    //on opponent board list is not drawn unless hit
    LinkedList<Ship> ships;

    public Board(Context context, boolean mine) {

        super(context);

        myBoard = mine;
        ships = new LinkedList<Ship>(); //Create new linked list of game pieces/ships

        //Create the matrix of squares by filling it with 100 new squares
        theSquares = new BoardSquare[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++) {
                theSquares[i][j] = new BoardSquare(i, j);
            }
        }

        if (mine) {
            setShips(ships);
        } else {
            getShips();
        }
        //randomly place ships on personal board
        //create list of possible wins when player pushes play game
    }

    @Override public void onDraw(Canvas canvas) {

        Paint myPaint = new Paint();

        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++) {
                //draw the border of the square in black

                myPaint.setStrokeWidth(edgeWidth);
                myPaint.setColor(Color.WHITE);
                canvas.drawRect(theSquares[i][j].getX(), theSquares[i][j].getY(),
                        theSquares[i][j].getX()+squareSize,
                        theSquares[i][j].getY()+squareSize, myPaint);

                //determine the color to fill the square
                if((theSquares[i][j].getColor()).equals("blue"))
                {
                    myPaint.setColor(Color.BLUE);
                }
                else if((theSquares[i][j].getColor()).equals("gray"))
                {
                    myPaint.setColor(Color.GRAY);
                }
                else if((theSquares[i][j].getColor()).equals("red"))
                {
                    myPaint.setColor(Color.RED);
                }
                else if((theSquares[i][j].getColor()).equals("white"))
                {
                    myPaint.setColor(Color.WHITE);
                }

                //set the stroke width to 0 for the square filling
                myPaint.setStrokeWidth(0);
                //draw the square
                canvas.drawRect(theSquares[i][j].getX()+edgeWidth,
                        theSquares[i][j].getY()+edgeWidth,
                        theSquares[i][j].getX()+squareSize-edgeWidth,
                        theSquares[i][j].getY()+squareSize-edgeWidth, myPaint);

            }
        }

    }

    //Not sure if we need this the way we set it up -Joe
    public BoardSquare findSquare(){

        BoardSquare square = null;
        //get where player clicked
        //is this necessary if it's a button

        return square;
    }

    /*
    Other functions worth writing: whose turn, winner, play, save file, load file,
     */

    // get function for member variable dimension
    public static int getDimension() {return  dimension;}

    public LinkedList<Ship> getShips() {
        LinkedList<Ship> solutions = new LinkedList<Ship>();
        return solutions;
    }

    //This function creates the initial set up of ships on the board and adds ship to linked list
    public void setShips(LinkedList<Ship> ships) {
        //Create each of the 5 individual game pieces
        Ship ship1 = new Ship(1, 1, 2, "horizontal");
        Ship ship2 = new Ship(1, 3, 3, "horizontal");
        Ship ship3 = new Ship(1, 5, 3, "horizontal");
        Ship ship4 = new Ship(1, 7, 4, "horizontal");
        Ship ship5 = new Ship(1, 9, 5, "horizontal");

        //Add each of the newly created ships to the linked list of ships
        ships.add(ship1);
        ships.add(ship2);
        ships.add(ship3);
        ships.add(ship4);
        ships.add(ship5);

        //Update ships and the board to reflect changes made above
        updateShips();
    }

    //Not sure if we still need this function the way we set it up -Joe
    public void moveShip(){

    }


    public void rotateShip(int i1, int j1){

        Iterator<Ship> it = ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();
            if(ship.getI() == i1 && ship.getJ() == j1)
            {
                String o = ship.getOrientation();
                String newO = "";
                if(o.equals("vertical"))
                {
                    newO = "horizontal";
                }
                else if(o.equals("horizontal"))
                {
                    newO = "vertical";
                }

                if(!overlapsAnotherShip(newO, ship.getSize(), ship.getI(), ship.getJ()))
                {
                    ship.rotate();
                }
            }
        }

    }

    //This function updates the ships and the boards to reflect their current state
    //It will be called any time the game pieces are moved, rotated or changed in any way
    public void updateShips()
    {
        //Make all of the squares of the board blue, so that it is a blank slate to redraw
        //updated ships
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                theSquares[i][j].setColor("blue");
            }
        }


        Iterator<Ship> it = ships.iterator(); //Iterator for linked list of ships
        //Iterate through entire list of ships
        while(it.hasNext())
        {
            Ship ship = it.next(); //Current ship we are looking at
            int i = ship.getI(); //Save i of current ship
            int j = ship.getJ(); //Save j of current ship
            int s = ship.getSize(); //Save size of current ship
            String o = ship.getOrientation(); //Save orientation of current ship

            if(o.equals("horizontal")) //If ships's orientation is horizontal
            {
                //Draw ship on the board horizontally
                for(int a = 0; a < s; a++)
                {
                    theSquares[i+a][j].setColor("gray");
                }
            }
            else //If ship's orientation if vertical
            {
                //Draw ship on the board vertically
                for(int a = 0; a < s; a++)
                {
                    theSquares[i][j+a].setColor("gray");
                }
            }

        }

    }

    //Function checks to see if a ship will overlap another ship if moved to a new square
    boolean overlapsAnotherShip(String o, int s, int i1, int j1)
    {
        boolean answer = false; //Return value
        String color = "blue";

        if(o.equals("horizontal")) //If orientation is horizontal
        {
            for(int a = 1; a < s && i1+a < 10; a++)
            {
                color = theSquares[i1+a][j1].getColor();
                if(color.equals("gray"))
                {
                    answer = true;
                }
            }
        }
        else    //orientation is vertical
        {
            for(int a = 1; a < s && j1+a < 10; a++)
            {
                color = theSquares[i1][j1+a].getColor();
                if(color.equals("gray"))
                {
                    answer = true;
                }
            }
        }

        return answer;
    }

    int whichShip(int i1, int j1)
    {
        int index = -1;
        int counter = 0;
        Iterator<Ship> it = ships.iterator();
        while(it.hasNext())
        {
            Ship itShip = it.next();
            for(int a = 0; a < itShip.getSize(); a++)
            {
                if((itShip.getOrientation().equals("horizontal")))
                {
                    if(i1 == itShip.getI()+a && j1 == itShip.getJ())
                    {
                        index = counter;
                    }
                }
                else
                {
                    if(i1 == itShip.getI() && j1 == itShip.getJ()+a)
                    {
                        index = counter;
                    }
                }

            }//end for loop iterating through all the squares of one ship

            counter++;
        }//end while iterating through ship list

        return index;
    }

    public boolean isOffEdge(String orientation1, int size1, int i1, int j1)
    {
        boolean answer = false;

        if(orientation1.equals("horizontal"))
        {
            if(size1 + i1 >= dimension)
            {
                answer = true;
            }
        }
        else    //if vertical
        {
            if(size1 + j1 >= dimension)
            {
                answer = true;
            }
        }

        return answer;
    }

} //end implementing public class Board