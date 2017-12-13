package bap58.battleship2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.*;
import android.view.View;

public class BoardSquare
{
    int x;                      //x coordinate of the square from top left
    int y;                      //y coordinate of the square from top left
    int i;
    int j;
    static int squareSize = 80; //length of the side of a square (constant)
    static int edgeWidth = 2;   //width of the border on each square
    String color = "blue";      //color of the square: red for hit, white for miss,
                                //blue for nothing there, gray for unhit ship there

    //constructor
    public BoardSquare(int i1, int j1)
    {
        i = i1;
        j = j1;

        x = squareSize + squareSize*i;
        y = squareSize + squareSize*j;

        //default color is blue, empty square
    }

    //setter functions
    public void setX(int x1){x = x1;}
    public void setY(int y1){y = y1;}
    public void setI(int i1){i = i1;}
    public void setJ(int j1){j = j1;}
    public void setColor(String s){color = s;}

    //getter functions
    public int getI(){return i;}
    public int getJ(){return j;}
    public float getX(){return x;}
    public float getY(){return y;}
    public String getColor(){return color;}



}//END class BoardSquare
