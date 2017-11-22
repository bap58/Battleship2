package bap58.battleship2;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity
        implements OnClickListener {

    Button startGame;
    Button instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);

        startGame = new Button(this);
        startGame.setText("Start Game");
        startGame.setContentDescription("start");
        startGame.setOnClickListener(this);
        lay.addView(startGame);

        instructions = new Button(this);
        instructions.setText("Instructions");
        instructions.setContentDescription("instructions");
        instructions.setOnClickListener(this);
        lay.addView(instructions);

        //setContentView(lay);
    }

    public void onClick (View v) {

        /*if(v.getContentDescription() == startGame.getContentDescription())
        {
            Intent current1 = new Intent(this, SetupActivity.class);
            startActivity(current1);
        }
        else if(v.getContentDescription() == instructions.getContentDescription())
        {
            Intent current2 = new Intent(this, InstructionsActivity.class);
            startActivity(current2);
        }*/

    }

    public void clickedButton (View v) {
        Log.i("-----", v.getContentDescription().toString());

        if(v.getContentDescription().toString().equals("start"))
        {
            Log.i("-----", "I am in start");
            Intent current1 = new Intent(this, SetupActivity.class);
            startActivity(current1);
        }
        else if(v.getContentDescription().toString().equals("instructions"))
        {
            Log.i("-----", "I am in instructions");
            Intent current2 = new Intent(this, InstructionsActivity.class);
            //Intent current2 = new Intent(this, SetupActivity.class);
            startActivity(current2);
        }

    }
}
