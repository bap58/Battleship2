package bap58.battleship2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.*;
import android.view.View;

/**
 * Created by joe on 11/13/17.
 */

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

    /*
    //drawMe lets the square draw itself on the screen
    @Override public void onDraw(Canvas c)
    {
        //First get a new paint
        Paint myPaint = new Paint();

        //draw the border of the square in black
        myPaint.setStrokeWidth(edgeWidth);
        myPaint.setColor(Color.BLACK);
        c.drawRect(x, y, x+squareSize, y+squareSize, myPaint);

        //determine the color to fill the square
        if(color.equals("blue"))
        {
            myPaint.setColor(Color.BLUE);
        }
        else if(color.equals("gray"))
        {
            myPaint.setColor(Color.GRAY);
        }
        else if(color.equals("red"))
        {
            myPaint.setColor(Color.RED);
        }
        else if(color.equals("white"))
        {
            myPaint.setColor(Color.WHITE);
        }

        //set the stroke width to 0 for the square filling
        myPaint.setStrokeWidth(0);
        //draw the square
        c.drawRect(x+edgeWidth, y+edgeWidth, x+squareSize-edgeWidth,
                y+squareSize-edgeWidth, myPaint);

    }
    */

}//END class BoardSquare
