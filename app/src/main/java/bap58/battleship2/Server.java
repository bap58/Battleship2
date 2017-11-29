import java.io.*;
import java.net.*;

/**
 * In Android Studio doc to make it easier to share through Github
 * No package
 */

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
