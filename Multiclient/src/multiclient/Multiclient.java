package multiclient;
import java.net.*;
import java.io.*;
import java.util.*;
/**
 * Runs the client as an application. First it displays on the command line
 * asking for the IP address of the server, then connects to the server on 
 * the port 9999 and starts the game.
 */
public class Multiclient {
    public static void main(String[] args) throws UnknownHostException, IOException {
        //Since the ip address of the server varies when accessing to different network. 
        //Ask for the client to input the ip address to connect to the specific server.
        System.out.println("Welcome to HangMan Game! Please enter the server address:");
        
        Scanner sc = new Scanner(System.in);
        String serverAddr = sc.next();
        //create a socket and connect it to the server on the port 9999.
        Socket socket = new Socket(serverAddr,9999);
            //start a new thread for client to input the guessing
            new ThreadWriter(socket).start();
            //start a new thread for receiving message from the server
            new ThreadReader(socket).start();    
    }
    /*
    This static method is used for extracting content from a original pakaged message.
    */
    public static String getCTX(String originalCTX,String firstSplit,String secondSplit)
    {
        String resultCTX = originalCTX.substring(originalCTX.lastIndexOf(firstSplit), 
                originalCTX.lastIndexOf(secondSplit));
        resultCTX = resultCTX.substring(1,resultCTX.length());
        return resultCTX;
    }
    
}
