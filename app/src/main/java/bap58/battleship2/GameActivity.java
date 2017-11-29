package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    Board myBoard;
    Board yourBoard;
    Boolean myTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get the extras from launching this intent
        Intent gameIntent = getIntent();

        String[] shipStrings = new String[5];
        shipStrings[0] = gameIntent.getStringExtra("ship0");
        shipStrings[1] = gameIntent.getStringExtra("ship1");
        shipStrings[2] = gameIntent.getStringExtra("ship2");
        shipStrings[3] = gameIntent.getStringExtra("ship3");
        shipStrings[4] = gameIntent.getStringExtra("ship4");

        //instantiate my board
        myBoard = new Board(this, true);
        myBoard.inSetup = false;

        myBoard.fromString(shipStrings);
        myBoard.updateShips();
        setContentView(myBoard);

        //instatiate your board
        yourBoard = new Board(this, false);
        yourBoard.inSetup = false;
        setContentView(yourBoard);


    }
}
