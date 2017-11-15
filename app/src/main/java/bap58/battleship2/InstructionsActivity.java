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
        TextView newText = new TextView(this);
        newText.setText("Hello...");
        lay.addView(newText);
        setContentView(lay);
    }
}
