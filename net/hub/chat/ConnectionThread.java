package net.hub.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * The class that represents the thread assigned to each connection established from a client to the server.
 * Ths thread listens from messages from the clients and then requests from the chat server to handle them.
 */

public class ConnectionThread extends Thread {

    private ChatServer chatServer;
    private Socket connection;
    private DataOutputStream out;
    private String nickName;

    /**
     * Creates a new tread that represents and handles each connection to the server.
     */
    public ConnectionThread(ChatServer chatServer, Socket connection) {

        this.chatServer = chatServer;
        this.connection = connection;

        try {

            this.out = new DataOutputStream( connection.getOutputStream() );

        } catch (IOException e) {

            System.out.println("There was an error while getting the data stream.");

        }

        start();

    }

    /**
     * Run method inherited from the Thread class, which described the way the thread will execute.
     * When the connection is first established the nick name of the connected client is retrieved.
     * Following thant the connected client receives a welcome message from the chat server.
     * Then the thread blocks while waiting from messages from the client. If a message arrives this
     * is handled by the chat server. If there is an error while the thread is blocked or reading the data
     * then the given thread is considered dead and it is requested from the chat server to invalidate it.
     */
    public void run() {

        try {

            DataInputStream in = new DataInputStream( connection.getInputStream() );
            this.nickName = in.readUTF();
            chatServer.welcomeClient(this);

            while (true) {

                String message = in.readUTF();
                chatServer.handleMessage(message, this);

            }

        } catch( EOFException ie ) {

            System.out.println("There was an error while working with the stream.");

        } catch( IOException ie ) {

            System.out.println("There was an error while working with the stream.");

        } finally {

            chatServer.invalidate(this);

        }

    }

    /**
     * Getter methods for the internal variables.
     */
    public Socket getConnection() {
        return connection;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public String getNickName() {
        return nickName;
    }

}
