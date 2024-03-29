import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.Socket;
import java.util.Random;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;



/**
 * manager class handles individual client connections and communication
 * with the server.
 */
public class Manager extends Thread {
    private BufferedReader Buffrin;
   
    private PrintWriter priwout;
   
    private Socket prisock;
   
    private ChatServer s1;

    private String entername;
  
    private String ipAddress; // stores generated IP address
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // timestamps

    /**
     * constructor for Manager class.
     *
     * @param socket the socket associated with the client connection
     * @param server server instance
     */
    public Manager(Socket sock3, ChatServer serv3) {
      
        this.prisock = sock3;
      
        this.s1 = serv3;
    }

    @Override
    public void run() {
        try {
            Buffrin = new BufferedReader(new InputStreamReader(prisock.getInputStream()));
            
            priwout = new PrintWriter(prisock.getOutputStream(), true);

            if (s1.isFirstClient()) {
                // sets the first client as the coordinator
                entername = "Coordinator";
                s1.setCoordinatorUsername(entername);
                System.out.println("Coordinator username has been set to: " + entername);
                // sends coordinator message before prompting for username
                priwout.println("You joined first, so you're the coordinator! Please enter your username/unique ID:");
            } else {
                priwout.println("Welcome to the chatroom! Please enter your username/unique ID:");
                entername = Buffrin.readLine();
                // informs the current user about the current coordinator
                priwout.println("Hello, " + entername + "! The current coordinator is " + s1.getCoordinatorUsername() + ".");
            }

            // prompt user to choose an option
            priwout.println("Please choose an option: 'private', 'public', or 'getip'");

            // generates a random IP address
            ipAddress = generateRandomIPAddress();

            // inform the client about their IP address
            priwout.println("Your IP address is: " + ipAddress);

            s1.addClient(this);

            // message handling loop
            String clientMessage;
            while (true) {
                
                clientMessage = Buffrin.readLine();
                if (clientMessage == null) {
                    break;
                }
                if (clientMessage.equalsIgnoreCase("private")) {
                    handlePrivateOption();
                } else if (clientMessage.equalsIgnoreCase("public")) {
                    handlePublicOption();
                } else if (clientMessage.equalsIgnoreCase("getip")) { // New condition for retrieving IP addresses
                    s1.getip(this);
                } else {
                    priwout.println("Invalid option. Please choose 'private', 'public', or 'getip'");
                }
            }
        } catch (IOException iocexcep1) {
            iocexcep1.printStackTrace();
        } finally {
            try {
                prisock.close();
            } catch (IOException iocexcep3) {
                iocexcep3.printStackTrace();
            }
            s1.removeClient(this);
        }
    }

    private String generateRandomIPAddress() {
        Random random = new Random();
        StringBuilder ipAddress = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ipAddress.append(random.nextInt(256));
            if (i < 3) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

    private void handlePrivateOption() throws IOException {
        priwout.println("Enter the recipients username:");
        String recipientName = Buffrin.readLine();
        priwout.println("Now enter your private message:");
        String privateMessage = Buffrin.readLine();
        String formattedMessage = "[" + dateFormat.format(new Date()) + "] " + entername + " (private): " + privateMessage; // Include timestamp

        // send the message to the sender
        priwout.println(formattedMessage);

        // find the recipient and send the message to them
        boolean recipientFound = false;
        for (Manager client : s1.getClients()) {
            if (client.getUsername().equals(recipientName)) {
                client.sendMessage(formattedMessage);
                recipientFound = true;
                break;
            }
        }

        // if the recipient is not found, notify the sender
        if (!recipientFound) {
            priwout.println("Recipient not found or not available.");
        }
    }

    private void handlePublicOption() {
        try {
            priwout.println("Enter your public message:");
            String publicMessage = Buffrin.readLine();
            String formattedMessage = "[" + dateFormat.format(new Date()) + "] " + entername + " (public): " + publicMessage; // Include timestamp
            s1.broadcast(formattedMessage, this);
            // print the message in the server's console
            System.out.println("Broadcasted message from " + entername + ": " + publicMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retrieveIPAddresses() {
        StringBuilder ips = new StringBuilder("Connected clients' IP addresses:\n");
        for (Manager client : s1.getClients()) {
            if (!client.equals(this)) {
                ips.append(client.getIPAddress()).append("\n");
            }
        }
        priwout.println(ips.toString());
    }

    public void sendMessage(String msg) {
        priwout.println(msg);
    }

    public String getUsername() {
        return entername;
    }

    public String getIPAddress() {
        return ipAddress;
    }
}
