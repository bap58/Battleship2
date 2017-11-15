package bap58.battleship2;

import android.graphics.Canvas;


/**
 * Created by joe on 11/13/17.
 */

public class Ship
{
    int x; //Represents x coordinate of beginning of ship, maybe first square?
    int y; //Represents y coordinate of beginning of ship, maybe first square?
    int size; //Represents the amount of squares in shape
    BoardSquare[] spots; //Represents the squares that make up the ship
    String orientation; //Vertical or horizontal


    //constructor for Ship class
    public Ship(int x1, int y1, int s, BoardSquare[] sp, String o)
    {
        x = x1;
        y = y1;
        size = s;
        BoardSquare[] spots = new BoardSquare[size];
        for(int i = 0; i < size; i++)
        {
            //spots[i].setColor(s.getColor());
            //spots[i].setX(s.getX());
            //spots[i].setY(s.getY());
        }
        orientation = o;
    }

    //Draws a ship
    public void drawMe(Canvas g)
    {
        //Check to see if you will draw ship vertically or horizontally
        if(orientation.equals("vertical")) //If shape is vertical
        {

        }
        else
        {

        }

    }

    //Mutator function for orientation of ship. Changes will be displayed to user in drawMe
    public void rotate()
    {

        if(orientation.equals("vertical")) //If orientation is currently vertical
        {
            orientation = "horizontal"; //change to horizontal
        }
        else //If orientation is currently horizontal
        {
            orientation = "vertical"; //change to vertical
        }
    }


    public boolean isSunk()
    {
        boolean sunk = true; //Flag to tell if ship is sunk or not
                              //False, if not sunk. True, if sunk.
                            //Will be set to false, if any of the squares are still black

        for(int i = 0; i < size; i++) //Go through each of the squares that makes up shape
        {
            if(spots[i].getColor() == "black") //If square is black, it has not been hit and
            {                                  //therefore, the ship is not sunk
                sunk = false; //Set sunk to false because ship is not sunk
            }
        }

        return sunk; //return whether or not the ship is sunk
    }

    //Need to figure out how to do this efficiently
    //Moves the shape when the user moves the ship using his/her finger
    public void move(int x, int y)
    {

    }



}
