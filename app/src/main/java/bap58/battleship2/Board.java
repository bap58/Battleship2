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
    static int[] sizes = {2, 3, 3, 4, 5};
    boolean myBoard;
    //on own board list ships that player will place
    //on opponent board list is not drawn unless hit
    LinkedList<Ship> ships;

    public Board(boolean mine) {
        //create 100 squares
        myBoard = mine;
        theSquares = new BoardSquare[dimension][dimension];
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++) {
                theSquares[i][j] = new BoardSquare();
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

    public LinkedList<Ship> getShips() {
        LinkedList<Ship> solutions = new LinkedList<Ship>();
        return solutions;
    }

    public void setShips(LinkedList<Ship> ships) {
        //randomly set ships
        //for loop, one for each ship
        for (int i = 0; i < sizes.length; i++){
            int
            Ship s = new Ship();
        }
    }

    public void moveShip(){

    }

    public void rotateShip(){

    }


} //end implementing public class Board


