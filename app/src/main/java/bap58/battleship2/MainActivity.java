package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
    }

    public void clickedButton (View v) {
        Log.i("-----", v.getContentDescription().toString());

        if(v.getContentDescription().toString().equals("start"))
        {
            Log.i("-----", "I am in start");
            Intent start = new Intent(this, SetupActivity.class);
            startActivity(start);
        }
        else if(v.getContentDescription().toString().equals("instructions"))
        {
            Log.i("-----", "I am in instructions");
            Intent instructions = new Intent(this, InstructionsActivity.class);
            startActivity(instructions);
        }

    }
}
