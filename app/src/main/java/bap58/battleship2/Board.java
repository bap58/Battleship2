package bap58.battleship2;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by Brian on 11/13/2017.
 * Beatriz is working on this
 */

//import statements
import android.content.Context;
import android.graphics.Canvas; //graphics
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import static bap58.battleship2.BoardSquare.edgeWidth;
import static bap58.battleship2.BoardSquare.squareSize;

//start implementing public class Board
public class Board extends View
    implements Serializable
{

    BoardSquare[][] theSquares; //Matrix of boardSquares that will make up board
    static int dimension = 10; //Dimension of board that will not change
    static int[] sizes = {2, 3, 3, 4, 5}; //Sizes of 5 game pieces
    boolean inSetup = true; //Flag to tell whether or not the board is in setup mode or not
    boolean myBoard; //Flag to tell whether or not this board is my board or the opponent's
    boolean myTurn = false; //Flag to tell whether it is the turn of the player with this board

    LinkedList<Ship> ships; //Linked list of ships on this board
    String message; //Will be used to send each player messages

    public Board(Context context, boolean mine) {

        super(context);

        //create 100 squares
        myBoard = mine;
        ships = new LinkedList<Ship>();

        message = ""; //Set message to a default value

        //Create the boardSquares that will make up the board
        theSquares = new BoardSquare[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++) {
                theSquares[i][j] = new BoardSquare(i, j);
            }
        }

        //only set ships if it is my Board
        //if it is the opponent's board, I will get the information in the messages
        if (mine) {
            setShips(ships);
        } /*else {
            getShips();
        }*/
        //randomly place ships on personal board
        //create list of possible wins when player pushes play game
    }

    //Function that is invoked every time we draw a board
    @Override public void onDraw(Canvas canvas) {

        Paint myPaint = new Paint();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {

                //draw the border of the square in black
                myPaint.setStrokeWidth(edgeWidth);
                myPaint.setColor(Color.BLACK);
                canvas.drawRect(theSquares[i][j].getX(), theSquares[i][j].getY(),
                        theSquares[i][j].getX() + squareSize,
                        theSquares[i][j].getY() + squareSize, myPaint);

                //determine the color to fill the square
                if ((theSquares[i][j].getColor()).equals("blue")) {
                    myPaint.setColor(Color.BLUE);
                } else if ((theSquares[i][j].getColor()).equals("gray")) {
                    if(myBoard){
                        myPaint.setColor(Color.GRAY);
                    }
                    else{
                        myPaint.setColor(Color.BLUE);
                    }
                } else if ((theSquares[i][j].getColor()).equals("red")) {
                    myPaint.setColor(Color.RED);
                } else if ((theSquares[i][j].getColor()).equals("white")) {
                    myPaint.setColor(Color.WHITE);
                } else if ((theSquares[i][j].getColor()).equals("yellow")) {
                    myPaint.setColor(Color.YELLOW);
                }


                //set the stroke width to 0 for the square filling
                myPaint.setStrokeWidth(0);
                //draw the square
                canvas.drawRect(theSquares[i][j].getX() + edgeWidth,
                        theSquares[i][j].getY() + edgeWidth,
                        theSquares[i][j].getX() + squareSize - edgeWidth,
                        theSquares[i][j].getY() + squareSize - edgeWidth, myPaint);

            }
        }


        if (inSetup) { //If the board is in setup mode, draw setup buttons
            //Draw two rectangles for buttons
            myPaint.setColor(Color.GRAY);
            canvas.drawRect(squareSize + edgeWidth, 12 * squareSize + edgeWidth,
                    11 * squareSize - edgeWidth,
                    14 * squareSize - edgeWidth, myPaint);
            myPaint.setColor(Color.RED);
            canvas.drawRect(squareSize + edgeWidth, 15 * squareSize + edgeWidth,
                    11 * squareSize - edgeWidth,
                    17 * squareSize - edgeWidth, myPaint);

            //put text within the rectangles
            myPaint.setColor(Color.BLACK);
            myPaint.setTextSize(100);
            //First rectangle will be used to rotate ships
            canvas.drawText("ROTATE", 4 * squareSize, 13 * squareSize + squareSize / 2, myPaint);
            myPaint.setTextSize(90);
            //Second rectangle will be used to let the app know that the player is ready for battle
            canvas.drawText("READY FOR BATTLE", 1 * squareSize, 16 * squareSize + squareSize / 2, myPaint);

        }
        else //If game is in play phase, draw button to flip between your board and the opponent's
        {
            myPaint.setColor(Color.GRAY);
            canvas.drawRect(squareSize + edgeWidth, 12 * squareSize + edgeWidth,
                    11 * squareSize - edgeWidth,
                    14 * squareSize - edgeWidth, myPaint);
            myPaint.setColor(Color.BLACK);
            myPaint.setTextSize(90);
            String option = "";
            if(myBoard) { //If this board is my board
                option = "See Enemy Board";
            }
            else //If this board is my opponent's board
            {
                option = "See My Board";
            }
            canvas.drawText(option, 3 * squareSize / 2, 13 * squareSize + squareSize / 2, myPaint);

            //The message that is drawn is based on the moves taken in a game
            //For example, a hit will result in displaying a message to your opponent that
            //one of their ships has been hit
            myPaint.setTextSize(50);
            canvas.drawText(message, 1 * squareSize / 2, 16 * squareSize + squareSize / 2, myPaint);

        }
    }

    //This function creates 5 new default ships and is called from the Board constructor
    public void setShips(LinkedList<Ship> ships) {
        Ship ship;
        int[] sizes = {2, 3, 3, 4, 5};
        for (int i = 0; i < 5; i++){
            ship = new Ship(1, 1 + i*2, sizes[i], "horizontal");
            ships.add(ship);
        }

        updateShips();
    }

    //A ship can be gray or yellow, gray is its default color, but when a player clicks on
    //a ship to select it in set up mode, the ship should turn yellow
    //This function handles the changing of the ships color
    public void setShipColor(int iter, String color)
    {
        Iterator<Ship> it = ships.iterator();
        int counter = 0;
        while(counter <= iter && it.hasNext())
        {
            Ship ship = it.next();
            if(counter == iter)
            {
                if(color.equals("yellow")) //If color passed in is yellow
                {
                    int i = ship.getI();
                    int j = ship.getJ();
                    String o = ship.getOrientation();
                    int s = ship.getSize();

                    if(o.equals("horizontal")) //Check orientation
                    {
                        for(int k = i; k < i + s; k++)
                        {
                            //change the color of the squares of the ship in yellow
                            theSquares[k][j].setColor("yellow");
                        }
                    }
                    else //Do the same thing for vertically oriented ships
                    {
                        for(int k = j; k < j + s; k++)
                        {
                            theSquares[i][k].setColor("yellow");
                        }
                    }
                }
                else if(color.equals("gray")) //If color passed in is gray, follow same steps as
                {                               //for yellow
                    int i = ship.getI();
                    int j = ship.getJ();
                    String o = ship.getOrientation();
                    int s = ship.getSize();

                    if(o.equals("horizontal"))
                    {
                        for(int k = i; k < i + s; k++)
                        {
                            theSquares[k][j].setColor("gray");
                        }
                    }
                    else
                    {
                        for(int k = j; k < j + s; k++)
                        {
                            theSquares[i][k].setColor("gray");
                        }
                    }
                }
            }
            counter++;
        }
    }

    //This function takes in the i and j of a ship and changes its orientation so that it
    //will be rotated when the board is redrawn
    public void rotateShip(int i1, int j1){

        Iterator<Ship> it = ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();
            if(ship.getI() == i1 && ship.getJ() == j1) //Figure out which ship has the i and j
            {                                          //that matches the i and j passed in
                String o = ship.getOrientation();
                String newO = "";
                //Change the orientation of ship
                if(o.equals("vertical"))
                {
                    newO = "horizontal";
                }
                else if(o.equals("horizontal"))
                {
                    newO = "vertical";
                }

                //Make sure that ship will not overlap ship with new orientation
                if(!overlapsAnotherShip(newO, ship.getSize(), ship.getI(), ship.getJ()))
                {
                    ship.rotate(); //Then rotate ship using the ship's built in rotate function
                }
            }
        }

    }

    public void updateShips()
    {
        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                theSquares[i][j].setColor("blue");
            }
        }

        Iterator<Ship> it = ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();
            int i = ship.getI();
            int j = ship.getJ();
            int s = ship.getSize();
            String o = ship.getOrientation();

            if(o.equals("horizontal"))
            {
                for(int a = 0; a < s; a++)
                {
                    if(ship.getSelected())
                    {
                        theSquares[i+a][j].setColor("yellow");
                    }
                    else
                    {
                        theSquares[i + a][j].setColor("gray");
                    }
                }
            }
            else
            {


                for(int a = 0; a < s; a++)
                {
                    if(ship.getSelected())
                    {
                        theSquares[i][j+a].setColor("yellow");
                    }
                    else
                    {
                        theSquares[i][j+a].setColor("gray");
                    }
                }
            }

        }

    }


    //This function takes in an orientation, size, i and a j
    //It checks to see if a ship with these attributes would overlap another existing ship
    //Returns false if it would not overlap another ship and then we know that we
    //can either move a ship or rotate a ship to the desired location
    boolean overlapsAnotherShip(String o, int s, int i1, int j1)
    {
        boolean answer = false;
        String color = "blue";

        if(o.equals("horizontal"))
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
            if(size1 + i1 > dimension)
            {
                answer = true;
            }
        }
        else    //if vertical
        {
            if(size1 + j1 > dimension)
            {
                answer = true;
            }
        }

        return answer;
    }

    public void fromString(String[] shipString)
    {
        int counter = 0;
        Iterator<Ship> it = ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();


            StringTokenizer st = new StringTokenizer(shipString[counter]);
            String dummy = st.nextToken();
            String str = st.nextToken();
            ship.setI(Integer.parseInt(str));
            str = st.nextToken();
            ship.setJ(Integer.parseInt(str));
            str = st.nextToken();
            ship.setOrientation(str);
            str = st.nextToken();
            ship.setSize(Integer.parseInt(str));

            counter++;
        }
    }

    public void fromString(String shipString)
    {
        Ship newShip = new Ship(1, 1, 1 , "horizontal");
        String[] st = shipString.split(" ");
        newShip.setI(Integer.parseInt(st[1]));
        newShip.setJ(Integer.parseInt(st[2]));
        newShip.setOrientation(st[3]);
        newShip.setSize(Integer.parseInt(st[4]));
        ships.add(newShip);

        System.out.println("THe opponent passed " + ships.size() + "ships");
    }


    //Will be used to handle click of opponent that app receives from server
    public void torpedo(String location)
    {
        String[] st = location.split(" ");
        int i = Integer.parseInt(st[1]);
        int j = Integer.parseInt(st[2]);
            /*
           StringTokenizer st = new StringTokenizer(IandJ);
           int i = Integer.parseInt(st.nextToken());
           int j = Integer.parseInt(st.nextToken());
            */
       //Handle the opponent's turn
       handleTurn(i, j);

    }



    //Takes an i and j of either player and changes the color of square based on hit or miss
    //Will work for both offensive turns and when the opponent performs their turn
    public void handleTurn(int i, int j)
    {
        //This should be fine because of the way we set up onDraw
        //Even though we will be drawing a blue square where the opponent's ships are, the square
        //still knows that it is gray
        if(theSquares[i][j].getColor().equals("gray"))
        {
            theSquares[i][j].setColor("red");
        }
        else if(theSquares[i][j].getColor().equals("blue"))
        {
            theSquares[i][j].setColor("white");
        }
    }
    //Update drawing of board after this function is called


    //This function will be called after each turn taken
    //It will go through the list of ships and check to see if any of them are sunk
    //If a ship is sunk, it will update its boolean value to indicate it has been sunk
    public void updateIfSunk()
    {
        Iterator<Ship> it = ships.iterator();
        while(it.hasNext())
        {
            Ship ship = it.next();
            int hitCount = 0;
            if(ship.getSunk() == false) //This is so we don't check ships that are already sunk
            {
                for (int i = 0; i < ship.getSize(); i++)
                {
                    if (ship.orientation.equals("horizontal"))
                    {
                        if (theSquares[ship.getI() + i][ship.getJ()].getColor().equals("red"))
                        {
                            hitCount++;
                        }
                    } else {
                        if (theSquares[ship.getI()][ship.getJ() + i].getColor().equals("red"))
                        {
                            hitCount++;
                        }
                    }
                }

                if (hitCount == ship.getSize()) {
                    ship.setSunk(true);
                }
            }

        }
    }

    //This function goes through the list of ships and finds out if all of them are sunk
    //If true, we have a winner
    //If false, nothing will happen
    public boolean winner()
    {
        Iterator<Ship> it = ships.iterator();
        boolean allSunk = true;
        while(it.hasNext())
        {
            Ship ship = it.next();

            if(ship.getSunk() == false)
            {
                allSunk = false;
            }

        }

        return allSunk;
    }


    public boolean hit(int i, int j)
    {
        boolean hit = false;

        if(theSquares[i][j].getColor().equals("red"))
        {
            hit = true;
        }

        return hit;
    }

    public void setMessage(String msg)
    {
        message = msg;
    }



} //end implementing public class Board