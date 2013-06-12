package net.hub.chat;

import net.hub.chat.ui.ChatClientWindow;
import sun.awt.windows.ThemeReader;

import javax.naming.InitialContext;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The ChatClient class which represents the client that connects to the Chat Server.
 * First it establishes a connection with the chat server and then listens for messages.
 * If the user submits a new message using the user interface then this will be submitted.
 */
public class ChatClient extends Thread {

    private Socket connection;
    private DataOutputStream out;
    private DataInputStream in;
    private String nickName;
    private ChatClientWindow chatClientWindow;

    /**
     * Creates a new ChatClient who tries to connect to the provided host and port.
     * When the connection is established the client transmits it's nick name to the server.
     * Afterwards the run method of the thread is called and the client waits for incoming messages
     * @param serverHost the host on which the chat server is deployed
     * @param serverPort the port that the chat server listens
     * @param chatClientWindow the user interface client window to which this thread is bound
     * @throws IOException if the port is in use and can no be bound
     */
    public ChatClient (String serverHost, int serverPort, String nickName, ChatClientWindow chatClientWindow) throws IOException {

        this.nickName = nickName;
        this.chatClientWindow = chatClientWindow;

        this.connection = new Socket(serverHost, serverPort);
        this.in = new DataInputStream(this.connection.getInputStream());
        this. out = new DataOutputStream(this.connection.getOutputStream());
        this.out.writeUTF(this.nickName);

    }

    /**
     * Run method inherited from the Thread class, which described the way the thread will execute.
     * The client blocks and waits for any incoming messages from the server. If any come these are
     * appended to the user interface text area. This is an never ending loop.
     */
    public void run() {

        try {

            while (true) {

                String message = in.readUTF();
                this.chatClientWindow.getTextArea().append(message + "\n");
            }

        } catch(IOException ie) {

            System.out.println("There was an error while waiting for a message.");

        }
    }

    /**
     * Method to transmit a message from the client to the server.
     * @param message the message to transmit
     */
    public void transmit(String message) {

        try {

            this.out.writeUTF(message);

        } catch(IOException ie) {

            System.out.println("There was an error while transmitting the message : " + message + ".");

        }

    }

}
