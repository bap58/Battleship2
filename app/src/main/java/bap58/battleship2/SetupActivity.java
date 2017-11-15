package bap58.battleship2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Board theBoard = new Board(this, true);
        setContentView(theBoard);

        theBoard.rotateShip();
        theBoard.updateShips();
        setContentView(theBoard);
    }
}
