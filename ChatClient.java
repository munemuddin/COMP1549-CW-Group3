// handles client-side communication with the server
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;

public class ChatClient {
    private static final String connecthost = "localhost";
    
    private static final int connectport = 6;
    
    private String entername;
    
    private BufferedReader buffR;
    
    private PrintWriter PrW;
    private BufferedReader BuffRi;

    // main method to start the client
    public static void main(String[] methodmain) {
        
        ChatClient Starttheclient = new ChatClient();
        
        Starttheclient.start();
    }

    // initialize and start the client
    private void start() {
        try (Socket FirstSock = new Socket(connecthost, connectport)) {
            
            PrW = new PrintWriter(FirstSock.getOutputStream(), true);
            
            buffR = new BufferedReader(new InputStreamReader(System.in));

            BuffRi = new BufferedReader(new InputStreamReader(FirstSock.getInputStream()));

            System.out.println(BuffRi.readLine()); // get initial message from the server
           
            PrW.println(entername); // send username to the server
            entername = buffR.readLine(); // prompt user to enter username
        
            

            System.out.println(BuffRi.readLine()); // receive greeting message from the server

            // start a separate thread to handle incoming messages from the server
            Thread zonethreadmessage = new Thread(this::messagesolver);
            
            zonethreadmessage.start();

            // listen for user input and send messages to the server
            while (true) {
                String entryuser = buffR.readLine();
                
                if (entryuser == null) {
                    break;
                }
                PrW.println(entryuser);
            }
        } catch (IOException carryout) {
            
            carryout.printStackTrace();
        }
    }

    // method to handle incoming messages from the server
    private void messagesolver() {
        try {
            while (true) {
                String sent1 = BuffRi.readLine();
                
                if (sent1 == null) {
                    break;
                }
                System.out.println("From server: " + sent1); // print messages received from the server
            }
        } catch (IOException carryout) {
            
            carryout.printStackTrace();
        }
    }
}
