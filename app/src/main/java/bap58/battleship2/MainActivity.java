package bap58.battleship2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ClientTalker clientTalker;
    ClientListener clientListener;

    private String hostIP = "141.161.88.4";
    private int port = 11013;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientTalker = new ClientTalker(hostIP, port);
        clientTalker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //clientListener = new ClientListener(clientTalker.getSocket());
        //clientListener.start();
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
