package net.hub.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class implementing the Chat Server functionality. The chat server will listen to a given port for new connections.
 * When a new request for connection is received from a client a new thread will be created and assigned to this connection.
 * Methods for transmitting messages, removing disconnected clients and providing advanced features are implemented.
 */

public class ChatServer {

    private int connectionPort = 6667;
    private ServerSocket serverSocket;
    private List<ConnectionThread> connectedClients = new ArrayList<ConnectionThread>();

    /**
     * Creates a new ChatServer who listens for connections on the default port, 6667.
     * @throws IOException if the port is in use and can no be bound
     */
    public ChatServer() throws IOException {

        this.serverSocket = new ServerSocket(this.connectionPort);
        listenForConnections();

    }

    /**
     * Creates a new ChatServer who listens for connections on the provided port
     * @param port the port which will be used by the server.
     * @throws IOException if the port is in use or invalid and can no be bound
     */
    public ChatServer(int port) throws IOException {

        this.connectionPort = port;
        this.serverSocket = new ServerSocket(this.connectionPort);
        listenForConnections();

    }

    /**
     * The chat server listens for new requests for connection from clients.
     * This method has a repeating loop which blocks on the ServerSocket accept() method.
     * Whenever a new request for connection comes a new thread is created and assigned to this connection.
     * @throws IOException if an error occurs while waiting
     */
    private void listenForConnections() throws IOException {

        System.out.println("Chat Server listening at port : " + this.connectionPort);

        while (true) {

            Socket clientConnection = serverSocket.accept();
            System.out.println("Received connection from " + clientConnection);
            this.connectedClients.add(new ConnectionThread(this, clientConnection));

        }

    }

    /**
     * Method that invalidates a connection for the Chat Server after the client has closed it.
     * The synchronize keyword is used to verify that no other thread is trying to access and change
     * the structure of the List that holds the active connections.
     * First the client is removed from the list and then the socket is closed.
     * Finally all clients are notified that the given connection has been closed.
     * @param client the client to invalidate
     */
    public void invalidate(ConnectionThread client) {

        synchronized(connectedClients) {

            connectedClients.remove( client );

            try {

                client.getConnection().close();
                transmit(client.getNickName() + " has left the chat room!");
                System.out.println("Client disconnected --- connection : " +  client.getConnection());

            } catch(IOException ie) {

                System.out.println("There was an error while closing the connection : " +  client.getConnection());

            }

        }
    }

    /**
     * Method that handles all messages transmitted from the clients to the server.
     * Based on the content it will decide if the message should be handled as an advanced feature command or
     * as a simple message to be transmitted to all clients.
     * @param message the message received
     * @param client the client thread that received the message
     */
    public void handleMessage(String message, ConnectionThread client) {

        System.out.println("Received message : " + message + " from client : " + client);

        if (message.startsWith("$\\")) {

            message = advancedFeatures((message.replace("$\\", "")).trim());
            transmitBackToClient(message, client);

        } else {

            transmit(client.getNickName() + " : " + message);

        }

    }

    /**
     * Method that transmits a message to all clients connected to the server.
     * @param message the message to be transmitted
     */
    public void transmit(String message) {

        synchronized(connectedClients) {

            System.out.println("Transmitting to all clients : " + message);

            for (ConnectionThread client : connectedClients) {

                DataOutputStream out = client.getOut();

                try {

                    out.writeUTF(message);

                } catch(IOException ie) {

                    System.out.println("There was an error while transmitting the message : " +  message);

                }

            }
        }

    }

    /**
     * Method that transmits a message to a single client connected to the server.
     * @param message the message to be transmitted
     * @param client the client who is supposed to receive the message
     */
    public void transmitBackToClient(String message, ConnectionThread client) {

        System.out.println("Transmitting message : " + message + " to client : " + client.getConnection());
        DataOutputStream out = client.getOut();

        try {

            out.writeUTF(message);

        } catch(IOException ie) {

            System.out.println("There was an error while transmitting the message : " +  message);

        }

    }

    /**
     * Method that transmits a default welcome message to a client.
     * @param client the client who is supposed to receive the message
     */
    public void welcomeClient(ConnectionThread client) {

        System.out.println("Transmitting welcome message to client : " + client.getConnection());
        String message = "Welcome to HUB Chat Server!\n";
        message += "Connected with name " + client.getNickName() +"\n";
        message += "Try using the string '$\\ help' to get instructions on how to use advanced features\n\n";
        transmitBackToClient(message, client);
        transmit(client.getNickName() + " has entered the chat room!");

    }

    /**
     * Method that handles the advanced features command. Based on the use of the string for advanced commands ($\) and
     * the command that follows it the appropriate response is returned. If no command matches then an
     * error message is returned.
     * @param command the command that was sent to the server
     * @return the string to return to the client
     */
    private String advancedFeatures(String command) {

        if ( command.startsWith("help") ) {

            String helpMessage = "You can use the following advanced commands\n";
            helpMessage += "$\\ reverse <message> : to get your message back reversed\n";
            helpMessage += "$\\ capitalize <message> : to get your message back in capital letters\n";
            helpMessage += "$\\ participants : to get the list of participants in the chat room\n";
            helpMessage += "$\\ date : to get the current date\n";
            helpMessage += "$\\ time : to get the current time\n";
            return helpMessage;

        } else if ( command.startsWith("reverse") ) {

            return new StringBuffer(command.replace("reverse ","")).reverse().toString();

        } else  if ( command.startsWith("capitalize") ) {

            return command.replace("capitalize ","").toUpperCase();

        } else if (  "participants".equals(command) ) {

            return createParticipantsList();

        } else if ( "date".equals(command) ) {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            return "Current date : " + dateFormat.format(date);

        } else if ( "time".equals(command) ) {

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            return "Current time : " +  dateFormat.format(date);

        } else {

            return "Oops, unrecognized command!";

        }

    }

    /**
     * Method that returns a string with the nick names of all participants in the chat room.
     * @return the string with the participants info
     */
    public String createParticipantsList() {

        String participants = "The following are currently in the chat room :\n";

        for (ConnectionThread client : connectedClients) {

            participants+=client.getNickName()+"\n";

        }

        return participants;

    }

}
