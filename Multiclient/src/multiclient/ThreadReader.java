package multiclient;
import java.io.*;
import java.io.*;
import java.net.*;
public class ThreadReader extends Thread implements Runnable{

    Socket socket;
    public ThreadReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while(true){
                
                /* Read basic Java data types from underlying input streams in 
                a machine-independent manner.
                */
                DataInputStream din=new DataInputStream(socket.getInputStream());
               
                // the received packet will be imported into rcvCTX
                String rcvCTX = din.readUTF();
                
                //Since the necessary message is encapsulated in the format:
                //"IP address/+{on}+[words replaced with underline <length>]
                //enframe the packet IP by method getCTX to extract destination IP 
                String rcvIP = Multiclient.getCTX(rcvCTX,"/",")");
                String localIP =socket.getLocalAddress().toString()+")";
                localIP = Multiclient.getCTX(localIP,"/",")");
                
                /* Compare the ip address from the received message and the local
                client ip. If the ip address from the received message is the same
                as the client ip address, then execute enframing the received 
                message and get the result of the game. If the message contains 
                "end", then the socket will be closed and exit automatically.
                If the ip address from the received the message is different
                from the client ip address, then the message will be dropped.           
                */
                if(rcvIP.equals(localIP))
                {
                    String endLabel = Multiclient.getCTX(rcvCTX,"{","}");
                    
                    if(endLabel.equals("end")){
                        String ctx = Multiclient.getCTX(rcvCTX,"[","]");
                        System.out.println(ctx);
                        socket.close();
                        System.exit(0);
                    }
                    else{
                        String ctx = Multiclient.getCTX(rcvCTX,"[","]");
                        System.out.println(ctx);
                    }
                 
                }
                
            }
        } catch (IOException e) {

            e.printStackTrace();
        } 
    }
    
}