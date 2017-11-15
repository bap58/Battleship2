package bap58.battleship2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        LinearLayout lay = new LinearLayout(this);
        TextView newTextHeader = new TextView(this);
        newTextHeader.setText("Instructions");
        lay.addView(newTextHeader);

        TextView newTextInstructions = new TextView(this);
        newTextInstructions.setText("Battleship. This is a 2-Player game in which players " +
                "compete " + "to win. Each player locates 5 ships in a 10 by 10 board, and the " +
                "adversaries " + "alternate taking turns to guess the coordinates where they " +
                "believe an opponent’s " + "piece is. The player who first finds (sinks) the " +
                "competition’s fleet wins.  \n" + "\n" + "Contents of the game. In this mobile " +
                "application version, each player has two " + "boards. The first displays where " +
                "the player’s ships are located and where it has " + "been hit by the opponent. " +
                "The second shows where the player has played its moves, " + "where he has " +
                "missed, and where he has hit. \n" + "\n" + "Game preparation. Each player has " +
                "to arrange the five ships on a 10 by 10 board. " +
                "The board will have the ships placed in randomized locations, then it " +
                "is up to the " + "user to make further changes before beginning.\n" + "\n" +
                "Ships. There are five ships of different sizes. Aircraft carrier (length 5), " +
                "battleship (length 4), submarine (length 3), cruiser (length 3), destroyer " +
                "(length 2).\n" + "\n" + "Player Board. Displays where the player set the ships. " +
                "If the opponent has played, " + "it shows the success (red) and failed attempts" +
                " (white) to guess the " + "set ships. \n" + "\n" + "Opponent Board. " +
                "Displays the moves the player has made. " + "It shows the success (red) and " +
                "failed attempts (white) to guess the set ships. \n" + "\n" + "Decide which " +
                "player is Player 1 and which is Player 2. The game will consider the player " +
                "who first completes his board and presses the “ready for battle” button to " +
                "be Player 1.\n" + "\n" + "Listen for the game's prompt: \"Awaiting orders from " +
                "Player (1 or 2).\"\n" + "\n" + "Place a white marking peg on the target grid " +
                "coordinate where you intend to fire. The target grid is the upright grid in " +
                "between the 2 players.\n" + "\n" + "Play. The player selects a square on the " +
                "opponent’s board where he or she believes the opponent’s ship is located. " +
                "The players will alternate turns after their own selection.  Additionally, the " +
                "player can toggle between board views, displaying their own ships or the " +
                "opponent’s board with  hits and misses.\n" + "\n" + "Continue until all 5 ships " +
                "on one side are sunk. At this point, the game announces, \"Enemy fleet " +
                "destroyed. Congratulations, Admiral.\"");
        lay.addView(newTextInstructions);

        setContentView(lay);
    }
}
