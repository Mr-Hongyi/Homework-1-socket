package multithread;
import java.io.*;
import java.net.*;
import java.util.*;

public class Multithread {
    
    static String[] WORD_BASE;
    private static final ArrayList<Socket> SOCKET_LIST = new ArrayList<Socket>();
    static ArrayList<String> USER_BASE = new ArrayList<String>();
    static boolean USER_FLG;
    public static void main(String[] args) throws Exception {
        // On the server side, it will run on port 9999.
        ServerSocket server = new ServerSocket(9999);
        /*
        Extracting the txt file "word.txt", and importing it into an string array 
        "WORD_BASE" to store all the words as a library.
        */
        File file = new File("/Users/harry/Desktop/KTH Course/Period 2/ID1212/Task 1/words.txt");
        FileReader reader = new FileReader(file);
        int fileLen = (int)file.length();
        char[] chars = new char[fileLen];
        reader.read(chars);
        String txt = String.valueOf(chars);
        WORD_BASE = txt.split("\n"); 
       
        
        while(true){
            /*
            Accepting and saving all the socket connection into arraylist 
            "SOCKET_LIST".
            Building up a database "USER_BASE" to store the client IP, word that
            the client is guessing, the word length and word replacing by underline,
            which will be updated after every guessing.
            */
            Socket socket = server.accept();
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            SOCKET_LIST.add(socket);

            System.out.println("Client:"+socket.getInetAddress());
            String rcvIP = socket.getInetAddress().toString()+")";
            rcvIP = Multithread.getCTX(rcvIP,"/",")");
            USER_FLG = true;
                    for(int i=0;i<Multithread.USER_BASE.size();i++)
                    {
                        String temp = Multithread.USER_BASE.get(i);
                        String baseIP = Multithread.getCTX(temp,"/",")");
                        /*
                        If a client stops the program by mistake and the game is not correctly
                        closed, the user data is still stored in USER_BASE. If the client connect
                        with the server again, the game will continue from where it stopped.
                        The USER_FLG will be set false in order to skip the initialization of
                        the game.
                        */
                        if(rcvIP.equals(baseIP))
                        {
                            String userAttempt = Multithread.getCTX(temp,"<",">");
                            String userUnderline = Multithread.getCTX(temp,"[","]");
                            String userWord = Multithread.getCTX(temp,"{","}");
                            int wordLength = userWord.length();
                            String sendCTX = "/"+baseIP+")"+"{on}"+"["+"Game continue: "+
                                    userUnderline+" <"+wordLength+" letters>"+" You can quit the game just type \"QUIT\"]";
                             //sending welcome message to the client
                            dout.writeUTF(sendCTX);
                            sendCTX = "("+socket.getInetAddress()+")"+"{on}"+"[Please guess a letter: ]";
                            dout.writeUTF(sendCTX);
                            sendCTX = null;
                            USER_FLG = false;
                            
                            System.out.println(USER_BASE);
                            break;
                        }
                    }
            
            if(USER_FLG){
                //Randomly picking up a word from the library, and replace the word
                //with underline for display. 
                DataProcessing.initial();
                int wordLength = DataProcessing.GUESS_WORD.length();
                System.out.println("Guessing word:"+DataProcessing.GUESS_WORD+" "+wordLength+" letters");

                //Encapsulating the welcome message that will be sent to the client
                String sendCTX = "("+socket.getInetAddress()+")"+"{on}"+"["+"Game starts!"+
                        ":"+multithread.DataProcessing.SEND_UNDERLINE+" <"+
                        wordLength+" letters>"+" You can quit the game just type \"QUIT\"]";

                //Storing the user data into USER_BASE
                String userIP = socket.getInetAddress().toString()+")";
                userIP = getCTX(userIP,"/",")");
                int userAttempt = wordLength;
                String userData = "/"+userIP+")"+"<"+userAttempt+">"+"!0?"+"["+
                        DataProcessing.SEND_UNDERLINE+"]"+"{"+DataProcessing.GUESS_WORD+"}";
                USER_BASE.add(userData);
                System.out.println(USER_BASE);

                dout.writeUTF(sendCTX);
                
                sendCTX = "("+socket.getInetAddress()+")"+"{on}"+"[Please guess a letter: ]";
                
                dout.writeUTF(sendCTX);
            }
            
            new ServerThread(socket,SOCKET_LIST).start();
        }
         
    }
    /*
    This static method is used for extracting content from an original packaged message
    */
    public static String getCTX(String originalCTX,String firstSplit,String secondSplit)
    {
        String resultCTX = originalCTX.substring(originalCTX.lastIndexOf(firstSplit), 
                originalCTX.lastIndexOf(secondSplit));
        resultCTX = resultCTX.substring(1,resultCTX.length());
        return resultCTX;
    }
}


