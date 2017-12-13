package bap58.battleship2;

import android.graphics.Canvas;

import java.util.Random;

import static bap58.battleship2.Board.dimension;
import static bap58.battleship2.BoardSquare.squareSize;

public class Ship
{
    int i;  //position i in the array
    int j;  //position j in the array
    int x;  //Represents x coordinate of beginning of ship, maybe first square?
    int y;  //Represents y coordinate of beginning of ship, maybe first square?
    int size; //Represents the amount of squares in shape
    //BoardSquare[] spots; //Represents the squares that make up the ship
    String orientation = "horizontal"; //Vertical or horizontal
    int hitCount; //How many times this ship has been hit
    boolean selected; //Flag to tell if ship is selected or not
    boolean sunk; //Flag to tell if ship is sunk or not

    //constructor for Ship class
    public Ship(int i1, int j1, int s, String o)
    {
        i = i1;
        j = j1;

        x = squareSize + squareSize*i;
        y = squareSize + squareSize*j;

        size = s;
        orientation = o;
        hitCount = 0;
        sunk = false;
        selected = false;
    }

    //getters - Accessor functions
    int getI(){return i;}
    int getJ(){return j;}
    int getSize(){return size;}
    String getOrientation(){return orientation;}
    boolean getSelected(){return selected;}
    boolean getSunk() {return sunk;}



    //setters - Mutator Functions
    void setI(int a)
    {
        i=a;
        x = squareSize + squareSize*i;
    }
    void setJ(int a)
    {
        j=a;
        y = squareSize + squareSize*j;
    }
    void setSize(int s) {size = s;}
    void setOrientation(String s){orientation = s;}
    void setSelected(boolean b){selected = b;}
    void setSunk(boolean b) {sunk = b;}

    //Mutator function for orientation of ship. Changes will be displayed to user in drawMe
    public void rotate()
    {

        if(orientation.equals("vertical")&& !isOffEdge("horizontal",size,i,j)) //If orientation is currently vertical
        {
            orientation = "horizontal"; //change to horizontal
        }
        else if(orientation.equals("horizontal") && !isOffEdge("vertical",size,i,j)) //If orientation is currently horizontal
        {
            orientation = "vertical"; //change to vertical
        }

        //Cannot rotate a ship unless meets one of two conditions above
    }


    //This function will take in an orientation, size, i and j
    //It checks to see if a ship with specified values will go off the edge of the board
    //Returns true if ship goes off edge
    //Returns false if ship remains on the board
    public boolean isOffEdge(String orientation1, int size1, int i1, int j1)
    {
        boolean answer = false;

        if(orientation1.equals("horizontal")) //If orientation is horizontal
        {
            //If size of ship plus its starting i position is greater than width of board
            if(size1 + i1 > dimension)
            {
                answer = true; //Then the ship is off the edge, set answer to true
            }
        }
        else    //if vertical
        {
            //If ship size + starting j position is greater than length of the board
            if(size1 + j1 > dimension)
            {
                answer = true; //Then the ship is off the edge, set answer to true
            }
        }

        return answer; //return the answer
    }


    //This function turns a ship object into a String
    //Will be used to send ships over the server
    public String toString()
    {
        String returnString = "";

        //Format: Board i j orientation size
        //Example: Board 2 2 horizontal 3
        returnString = "Board " + i + " " + j + " " + orientation + " " + size;

        return returnString;
    }


}
