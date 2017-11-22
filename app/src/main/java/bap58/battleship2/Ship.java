package bap58.battleship2;

import android.graphics.Canvas;

import java.util.Random;

import static bap58.battleship2.Board.dimension;
import static bap58.battleship2.BoardSquare.squareSize;


/**
 * Created by joe on 11/13/17.
 */

public class Ship
{
    int i;  //position i in the array
    int j;  //position j in the array
    int x; //Represents x coordinate of beginning of ship, maybe first square?
    int y; //Represents y coordinate of beginning of ship, maybe first square?
    int size; //Represents the amount of squares in shape
    //BoardSquare[] spots; //Represents the squares that make up the ship
    String orientation = "horizontal"; //Vertical or horizontal
    int hitCount;


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
    }

    //getters
    int getI(){return i;}
    int getJ(){return j;}
    int getX(){return x;}
    int getY(){return y;}
    int getSize(){return size;}
    String getOrientation(){return orientation;}
    int getHitCount(){return hitCount;}

    //setters
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
    void incrementHitCount(){hitCount++;}

    //Draws a ship
    public void drawMe()
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

        if(orientation.equals("vertical")&& !isOffEdge("horizontal",size,i,j)) //If orientation is currently vertical
        {
            orientation = "horizontal"; //change to horizontal
        }
        else if(orientation.equals("horizontal") && !isOffEdge("vertical",size,i,j)) //If orientation is currently horizontal
        {
            orientation = "vertical"; //change to vertical
        }
        else
        {
            //can't do this. add a warning
        }
    }


    public boolean isSunk()
    {
        return (hitCount == size);
    }

    //Need to figure out how to do this efficiently
    //Moves the shape when the user moves the ship using his/her finger
    public void move(int i1, int j1)
    {
        if(!isOffEdge(orientation, size, i1, j1)) {
            i = i1;
            j = j1;
        }
        else
        {
            //ERROR! moved it off the edge
        }
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


}
