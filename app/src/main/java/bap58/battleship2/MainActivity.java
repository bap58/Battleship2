package bap58.battleship2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    BoardSquare[][] theSquares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Board theBoard = new Board(this, true);
        setContentView(theBoard);

        theBoard.theSquares[2][2].setColor("gray");
        theBoard.theSquares[5][6].setColor("gray");
        theBoard.theSquares[3][9].setColor("red");
        setContentView(theBoard);
    }
}
