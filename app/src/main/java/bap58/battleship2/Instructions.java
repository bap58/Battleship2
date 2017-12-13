package bap58.battleship2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //returns to the previous state the activity was in before another intent was called
        super.onCreate(savedInstanceState);

        //Use XML file activity_main_menu to show buttons on first activity
        setContentView(R.layout.instructions);

        //Creating a toolbar that will collapse as the player scrolls down
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
