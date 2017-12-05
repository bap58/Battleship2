import java.io.*;
import java.net.*;

/**
 * In Android Studio doc to make it easier to share through Github
 * No package
 */

public class Server
{
    ServerSocket ssock; // server socket, place for clients to call in
    boolean keepGoing = true; // set false to stop input loop, game over
    int port = 11013; // which port to make available
    Ear[] ears = new Ear[2]; // these connect to the two clients
    int earCount = 0; // how many are connected so far

    public static void main( String[] args ) //throws IOException
    {
        new Server();
    }

    public Server()
    {
        System.out.println("Battleships starting ...");
        try
        {
            ssock = new ServerSocket(port); // open socket for calls

            // listen for connections
            while (earCount<2) // has no way to stop as written
            {
                // when client calls, set up Ear
                Socket client = ssock.accept(); // this blocks until a client calls
                System.out.println("Game Relay: accepted client connection ");

                ears[earCount] = new Ear(client, earCount );
                earCount++;

                if (earCount == 2)
                {
                    ears[0].start();
                    ears[1].start();
                }

                System.out.println("Player " + earCount + " just connected!");
            }
        }
        catch( Exception e ) { System.err.println("Game Relay: error = "+e); }
        while ( true ) {} // hang
        //System.out.println("GameRelay constructor ending ....");
    }

    // An Ear object listens to client calls and echos whatever comes in
    // on the other Ear's outputs
    public class Ear extends Thread
    {
        int id; // which element are you in the array
        Socket sock; // communicates from/to client
        BufferedReader bin; // object to read from sock
        PrintWriter pout; // object to writ to sock
        boolean keepGoing = true; // loops until this is set false

        public Ear( Socket s, int i )
        {
            System.out.println("Ear constructor starting ...");

            // record args
            id = i;
            sock = s;

            // set up input and output thingies
            try
            {
                InputStream in = sock.getInputStream();
                bin = new BufferedReader( new InputStreamReader(in) );
                pout = new PrintWriter( sock.getOutputStream(), true);
            }
            catch(Exception e)
            { System.out.println("Ear error opening i/o on pipe:"+e); }

        }

        // run ... read messages from this client, send to the other
        @Override
        public void run()
        {
            while (keepGoing)
            {
                try
                {
                    String msg = bin.readLine();
                    System.out.println("Ear read ="+msg);
                    share(msg);
                    //if (msg==null || msg.equals("null") ) { keepGoing = false; }
                }
                catch( Exception e )
                { System.out.println("Ear error="+e); keepGoing = false; }
            }
        }

        // say this thing to the other game shell if there is
        // one.
        public void share( String s )
        {
            if ( earCount==2 )
            {
                int other = (id+1)%2;
                ears[other].send(s);
            }
        }

        // send s out on the socket
        public void send( String s )
        {
            try
            {
                pout.println( s );
                pout.flush();
            }
            catch(Exception e)
            {System.out.println("Ear error trying to output to client="+e);}
        }
    }
}

/*
public class Server {
    private ServerSocket sock;
    //stores a list of each serverListener (each connected to a client
    private ServerListener[] playerArray;
    private int playerCount = 0;
    private int port = 11013;

    public static void main( String[] args )
    {
        new Server();
    }

    public Server() {
        try {
            playerArray = new ServerListener[2];
            sock = new ServerSocket(port); // open socket
            serverConnection(); //rename class
        }

        catch(Exception e) { System.err.println("Server: error = "+e); }
    }

    //continuously waits on the chosen port for a client to call
    private void serverConnection() throws Exception
    {
        while (true) // has no way to stop as written
        {
            System.out.println("Listening on port "+sock.getLocalPort()+"...");
            Socket client = sock.accept(); // this blocks code until a client calls
            System.out.println("Chatter Server accepted a client connection!");
            //creates a ServerListener thread for each client, adds it to the list and starts the thread
            ServerListener sListenerThread = new ServerListener(client);

            playerArray[playerCount] = sListenerThread;
            playerCount++;
            System.out.println("Player " + playerCount + " just connected!");

            sListenerThread.start();
        }

    }

    //Thread that listens to the ChatterClient on that socket and processes what it receives.
    public class ServerListener extends Thread
    {
        public String nickName = "defaultName";
        private Socket clientSocket;
        private BufferedReader bin;
        private BufferedWriter bout;

        //constructor, stores the BufferedReader and BufferedWriter required for the socket
        public ServerListener(Socket s) throws Exception
        {
            clientSocket = s;

            InputStream in = clientSocket.getInputStream();
            bin = new BufferedReader( new InputStreamReader(in) );

            OutputStream out = clientSocket.getOutputStream();
            bout = new BufferedWriter( new OutputStreamWriter( out ) );
        }


        //writes "s" to the client
        public void write(String s) throws IOException
        {
            bout.write(s+"\n");
            bout.flush();
        }

        //reads from the client, blocks until client writes "\n" to the pipe
        public String readLine() throws IOException
        {
            String msg = bin.readLine();
            return msg;
        }

        void tellOpponent(ServerListener sender, String message) throws Exception
        {
            System.out.println("trying to tell others!!");
            if(!playerArray[0].equals(sender)) {
                playerArray[0].write(message);
            }
            else {
                playerArray[1].write(message);
            }
        }

        //takes "line" from client and processes it, seeing if any keywords were written.
        //closes socket if "/quit" is written
        private void processClientLine(String line) throws Exception
        {
            //split string along spaces, to process easier
            String[] arrOfWords = line.split(" ");

            //user wants to change their name
            if (arrOfWords[0].equals("/torpedo") && arrOfWords.length > 3)
            {
                String position = arrOfWords[1] + " " +arrOfWords[2];
                //send to opposite client
                tellOpponent(this, position);
            }

            else if (arrOfWords[0].equals("/setup") && arrOfWords.length > 1)
            {
                String ships = "";
                //send to opposite
                tellOpponent(this, ships);
            }

            //user wants to quit
            else if (arrOfWords[0].equals("/quit"))
            {
                //send message to other player that they know we are quitting
                tellOpponent(this, "Opponent has left the chat room!");
                clientSocket.close();
            }

        }


        //executes when ".start()" is used
        @Override
        public void run()
        {
            try {
                //exits when the socket is closed (the user said /quit)
                while(!clientSocket.isClosed())
                {
                    String inLine = readLine();
                    if (!inLine.isEmpty())
                    {
                        //change***
                        //closes socket (leaves while loop) if /quit is typed
                        processClientLine(inLine);
                    }
                }

            }
            catch (Exception e)
            {e.printStackTrace();}
        }
    }
}
*/