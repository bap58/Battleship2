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

    BoardSquare[][] theSquares;
    static int dimension = 10;
    static int[] sizes = {2, 3, 3, 4, 5};
    boolean inSetup = true;
    boolean myBoard;
    boolean myTurn = false;
    //on own board list ships that player will place
    //on opponent board list is not drawn unless hit
    LinkedList<Ship> ships;

    public Board(Context context, boolean mine) {

        super(context);

        //create 100 squares
        myBoard = mine;
        ships = new LinkedList<Ship>();

        theSquares = new BoardSquare[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++) {
                theSquares[i][j] = new BoardSquare(i, j);
            }
        }

        //setShips(ships);

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

    @Override public void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

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

        if (inSetup) {
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
            canvas.drawText("ROTATE", 4 * squareSize, 13 * squareSize + squareSize / 2, myPaint);
            myPaint.setTextSize(90);
            canvas.drawText("READY FOR BATTLE", 1 * squareSize, 16 * squareSize + squareSize / 2, myPaint);

        }
        else
        {
            myPaint.setColor(Color.GRAY);
            canvas.drawRect(squareSize + edgeWidth, 12 * squareSize + edgeWidth,
                    11 * squareSize - edgeWidth,
                    14 * squareSize - edgeWidth, myPaint);
            myPaint.setColor(Color.BLACK);
            myPaint.setTextSize(90);
            String option = "";
            if(myBoard) {
                option = "See Enemy Board";
            }
            else
            {
                option = "See My Board";
            }
            canvas.drawText(option, 3 * squareSize / 2, 13 * squareSize + squareSize / 2, myPaint);

            String message = "";
            if(myTurn)
            {
                message = "It's your turn!";
            }
            else
            {
                message = "Waiting for opponent...";
            }
            myPaint.setTextSize(70);
            canvas.drawText(message, 3 * squareSize / 2, 16 * squareSize + squareSize / 2, myPaint);

        }
        //System.out.println("done drawing board");
    }

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

    public void setShips(LinkedList<Ship> ships) {
        Ship ship;
        int[] sizes = {2, 3, 3, 4, 5};
        for (int i = 0; i < 5; i++){
            ship = new Ship(1, 1 + i*2, sizes[i], "horizontal");
            ships.add(ship);
        }

        /*
        Ship ship1 = new Ship(1, 1, 2, "horizontal");
        Ship ship2 = new Ship(1, 3, 3, "horizontal");
        Ship ship3 = new Ship(1, 5, 3, "horizontal");
        Ship ship4 = new Ship(1, 7, 4, "horizontal");
        Ship ship5 = new Ship(1, 9, 5, "horizontal");

        ships.add(ship1);
        ships.add(ship2);
        ships.add(ship3);
        ships.add(ship4);
        ships.add(ship5);

        */

        updateShips();
    }

    public void setShipColor(int iter, String color)
    {
        Iterator<Ship> it = ships.iterator();
        int counter = 0;
        while(counter <= iter && it.hasNext())
        {
            Ship ship = it.next();
            if(counter == iter)
            {
                if(color.equals("yellow"))
                {
                    int i = ship.getI();
                    int j = ship.getJ();
                    String o = ship.getOrientation();
                    int s = ship.getSize();

                    if(o.equals("horizontal"))
                    {
                        for(int k = i; k < i + s; k++)
                        {
                            theSquares[k][j].setColor("yellow");
                        }
                    }
                    else
                    {
                        for(int k = j; k < j + s; k++)
                        {
                            theSquares[i][k].setColor("yellow");
                        }
                    }
                }
                else if(color.equals("gray"))
                {
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

            /*
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

            */
            Log.i("____________", shipString[counter]);
            String[] st = shipString[counter].split(" ");
            ship.setI(Integer.parseInt(st[1]));
            ship.setJ(Integer.parseInt(st[2]));
            ship.setOrientation(st[3]);
            ship.setSize(Integer.parseInt(st[4]));

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



} //end implementing public class Board