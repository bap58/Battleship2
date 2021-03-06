import java.io.*;
import java.net.*;

/**
 * In Android Studio doc to make it easier to share through Github
 * No package
 */

public class Server
{
    ServerSocket ssock; // server socket, place for clients to call in
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

                    System.out.println("Ear read =" + msg);
                    share(msg);
                    if (msg == null || msg.equals("null")) {
                        keepGoing = false;
                    }

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

