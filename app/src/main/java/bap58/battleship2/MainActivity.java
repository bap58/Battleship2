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

        BoardSquare square1 = new BoardSquare(this, 1, 1);
        square1.setColor("gray");
        setContentView(square1);



        /*
        theSquares = new BoardSquare[10][10];
        for(int i = 1; i <= 10; i++)
        {
            for(int j = 1; j <= 10; j++)
            {
                theSquares[i][j] = new BoardSquare(this, i, j);
            }
        }
        */
    }
}
