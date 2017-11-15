package bap58.battleship2;

import java.util.LinkedList;

/**
 * Created by Brian on 11/13/2017.
 * Beatriz is working on this 
 */

//import statements
import android.graphics.Canvas; //graphics

//start implementing public class Board
public class Board {

    BoardSquare[][] theSquares;
    static int dimension = 10;
    //LinkedList<Scorable> possibleWins;

    public Board() {
        //create board with randomly-placed ships
        //create list of possible wins when player pushes play game
    }

    public void drawMe(Canvas g) {
        //draw buttons
        //draw 100 squares
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


} //end implementing public class Board


