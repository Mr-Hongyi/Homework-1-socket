package multiclient;
import java.util.*;
import java.io.*;
import java.net.*;

public class ThreadWriter extends Thread implements Runnable{

    Socket socket;
    public ThreadWriter(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);
            while(true){
                // Get the word which user typed
                String sendCTX = sc.next();
                // Write basic Java data types into the output stream in the proper way
                DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
                // Package the message into an agreed format
                sendCTX = socket.getLocalAddress().toString()+")"+"["+sendCTX+"]";
                // Send the message to the server
                dout.writeUTF(sendCTX);
                
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

}
