import java.text.SimpleDateFormat;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Date;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this is the server class handling client connections and facilitates communication
 * between clients in the chatroom.
 */
public class ChatServer {
    private static final int portserver = 6;
    
    private List<Manager> privclients = new ArrayList<>();
    
    private String coordinatorUsername;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // timestamp format

    /**
     * starts server and listens for client connections
     */
    public static void main(String[] serverarg) {
        ChatServer threeserv = new ChatServer();
        threeserv.start();
    }

    private void start() {
        try {
            ServerSocket socksev = new ServerSocket(portserver);
            System.out.println("The server has now begun on the port " + portserver);

            while (true) {
                Socket d3soc = socksev.accept();
                System.out.println("Update: new client has joined.");

                Manager manager = new Manager(d3soc, this);
                privclients.add(manager);
                manager.start();
            }
        } catch (IOException ceoexc) {
            ceoexc.printStackTrace();
        }
    }

    /**
     * checks if the client is the first one to connect.
     *
     * @return true if the client is the first one; or else false.
     */
    public synchronized boolean isFirstClient() {
        return privclients.isEmpty();
    }

    /**
     * gets the username of coordinator
     *
     * @return username of the coordinator.
     */
    public synchronized String getCoordinatorUsername() {
        return coordinatorUsername;
    }

    /**
     * sets the username of the coordinator.
     *
     * @param username the username of the coordinator.
     */
    public synchronized void setCoordinatorUsername(String username) {
        this.coordinatorUsername = username;
    }



    public synchronized void addClient(Manager manager) {
        privclients.add(manager);
    }

    public synchronized void removeClient(Manager client) {
        privclients.remove(client);
        if (client.getUsername().equals(coordinatorUsername)) {
            // if the removed client is the coordinator
            if (!privclients.isEmpty()) {
                // sssign the coordinator role to the next client in line
                coordinatorUsername = privclients.get(0).getUsername();
                // inform the new coordinator about their role
                privclients.get(0).sendMessage("the old coordinator left, now you are the coordinator.");
            } else {
                // if there are no clients left, reset the coordinatorUsername
                coordinatorUsername = null;
            }
        }
    }

    public void broadcast(String sentm, Manager complsender) {
        String formattedMessage = "[" + dateFormat.format(new Date()) + "] " + sentm; // Include timestamp
        for (Manager mannyclient : privclients) {
            if (mannyclient != complsender) {
                mannyclient.sendMessage(formattedMessage);
            }
        }
        System.out.println(formattedMessage); // print to server console with timestamp
    }

    public synchronized void privateMessage(String fourmsg, Manager fourthsend, String famerec) {
        String formattedMessage = "[" + dateFormat.format(new Date()) + "] " + fourmsg; // Include timestamp
        boolean recipientFound = false;
        for (Manager thirdcl : privclients) {
            if (thirdcl.getUsername().equals(famerec)) {
                thirdcl.sendMessage(formattedMessage); // Include timestamp in the message
                recipientFound = true;
                break;
            }
        }
        if (!recipientFound) {
            fourthsend.sendMessage("Recipient not found or not available.");
        }
        System.out.println(formattedMessage); // print to server console with timestamp
    }


    public synchronized void getip(Manager client) {
        StringBuilder ipAddresses = new StringBuilder("Connected clients' IP addresses:\n");
        for (Manager m : privclients) {
            if (m != client) {
                ipAddresses.append(m.getUsername()).append(": ").append(m.getIPAddress()).append("\n");
            }
        }
        client.sendMessage(ipAddresses.toString());
    }

    public List<Manager> getClients() {
        return privclients;
    }
}
