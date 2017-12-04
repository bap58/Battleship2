package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//Main Activity class
//Starts a thread to run the mobile application
//Is the first activity the player sees when it opens the application
//In the XML file it creates two buttons.
//When either button is pressed, the view is handled in th clickedButton method.
public class MainActivity extends AppCompatActivity {

    //Function is called as soon as the application is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //returns to the previous state the activity was in before another intent was called
        super.onCreate(savedInstanceState);

        //Use XML file activity_main_menu to show buttons on first activity
        setContentView(R.layout.activity_main_menu);

    } //end of onCreate overwritten implementation

    public void clickedButton (View v) {

        //if else if statement to handle button selection in Main Activity
        if(v.getContentDescription().toString().equals("start")) {

            //message to learn that the view's content description was start
            //and the program passed the if statement
            Log.i("-----", "I am in start");

            //creating a new intent to pass onto a new activity to set up the board for the game
            Intent start = new Intent(this, SetupActivity.class);

            //start running the set up of the board and pause the main activity
            startActivity(start);

        } else if(v.getContentDescription().toString().equals("instructions")) {

            //message to learn that the view's content description was instructions
            //and the program passed the else if statement
            Log.i("-----", "I am in instructions");

            //creating a new intent to pass onto a new activity to read the instructions
            Intent instructions = new Intent(this, InstructionsActivity.class);

            //start running instructions of the game and pause the main activity
            //until the player comes back to Main Activity
            startActivity(instructions);

        } //end of if else if statement

    } //end of clickedButton method implementation

} //end of Main Activity class implementation
