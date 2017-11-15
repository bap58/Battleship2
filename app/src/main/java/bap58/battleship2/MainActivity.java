package bap58.battleship2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        LinearLayout lay = new LinearLayout(this);

        Button startGame = new Button(this);
        startGame.setText("Start Game");
        startGame.setOnClickListener(this);
        lay.addView(startGame);

        Button instructions = new Button(this);
        instructions.setText("Instructions");
        instructions.setOnClickListener(this);
        lay.addView(instructions);

        setContentView(lay);
    }

    public void onClick (View v) {


    }
}
