package bap58.battleship2;

import android.util.Log;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created by Nico on 11/22/17.
 */

public class Client extends Thread{
    //list of 2
    //main activity
    //oncreate fx, call, open socket, launch thread
    //pass address around after opening socket

    private ClientListener listener;
    private Board theGame;
    private BufferedWriter bout;
    private Socket socket;
    String localhost = "";
    int port = 0;


    //Starts a  client with inputted hostname and portnumber, or with defaults 127.0.0.1 and 11013
    /*
    public static void main( String[] args )
    {
            new Client("127.0.0.1", 11013);

    }
    */

    public Client(String name, int p) {
        localhost = name;
        port = p;

        /*
        try {
        //this command will throw an exception if "port" is not open
            socket = new Socket("127.0.0.1", 11013);

            OutputStream out = socket.getOutputStream();
            bout = new BufferedWriter( new OutputStreamWriter( out ) );

            theGame = game;

            listener = new ClientListener(socket);
            listener.start();

            //The main thread stays here and checks for keyboard commands (and sends them to the server) continuously
            //until the user types /quit
            while(!socket.isClosed())
            {
                String userCommand = getUserInput(); //adapt to user logged on
                write(userCommand); //push to server
                if (userCommand.split(" ")[0].equals("/quit")) //quit = a button instead for game
                {
                    socket.close();
                }
            }
            System.exit(0);

        }
        catch ( Exception e )
        { System.err.println(e); }
         */
    }


    public Client(Board game) {
        try {
        //this command will throw an exception if "port" is not open
            socket = new Socket("127.0.0.1", 11013);

            OutputStream out = socket.getOutputStream();
            bout = new BufferedWriter( new OutputStreamWriter( out ) );

            theGame = game;

            listener = new ClientListener(socket);
            listener.start();

            //The main thread stays here and checks for keyboard commands (and sends them to the server) continuously
            //until the user types /quit
            while(!socket.isClosed())
            {
                String userCommand = getUserInput(); //adapt to user logged on
                write(userCommand); //push to server
                if (userCommand.split(" ")[0].equals("/quit")) //quit = a button instead for game
                {
                    socket.close();
                }
            }
            System.exit(0);

        }
        catch ( Exception e )
        { System.err.println(e); }
    }

    //Returns keyboard input, blocks until enter is hit
    private String getUserInput()
    {
        String line = "";//theGame.getMove();
        return line;
    }

    //writes to the server using a BufferedWriter, adding a "\n" so that the server can use "readline()" ***fix
    public void write(String s) throws IOException
    {
        bout.write(s + "\n");
        bout.flush();
    }

    //Thread that continuously listens on the socket for ChatterServer messages
    public class ClientListener extends Thread
    {
        private BufferedReader bin;

        //constructor, sets up the BufferedReader "bin
        public ClientListener(Socket s) throws Exception
        {
            InputStream in = s.getInputStream();
            bin = new BufferedReader( new InputStreamReader(in) );
        }

        //Blocks until the server flushes a message to the socket
        public String readLine() throws IOException
        {
            String line = bin.readLine();
            return line;
        }

        //runs continuously
        @Override
        public void run()
        {
            Log.i("-----", "im hereeeeeee in run inside subclass");
            try {
                while(true)
                {
                    String serverLine = readLine();
                    if (!serverLine.isEmpty())
                        System.out.println(serverLine);
                }

            } catch ( Exception e )
            { System.err.println(e); }
        }
    }

    public void run() {
        Log.i("-----", "im hereeeeeee 1");
        try {
            //this command will throw an exception if "port" is not open
            Log.i("-----", "im in try");
            socket = new Socket(localhost, port);
            Log.i("-----", "im after socket");

            OutputStream out = socket.getOutputStream();
            bout = new BufferedWriter(new OutputStreamWriter(out));

            listener = new ClientListener(socket);
            listener.start();

            //The main thread stays here and checks for keyboard commands (and sends them to the server) continuously
            //until the user types /quit
            while (!socket.isClosed()) {
                String userCommand = getUserInput(); //adapt to user logged on
                write(userCommand); //push to server
            }
            System.exit(0);

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
