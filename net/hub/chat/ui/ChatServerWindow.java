package net.hub.chat.ui;

import net.hub.chat.ChatServer;

import javax.swing.*;
import java.io.IOException;

/**
 * A simple Swing user interface used to set the port in which the server will listen to and then start the server.
 */
public class ChatServerWindow {

    /**
     * First create a pop up with Swing to get the port number that the server will listen to.
     * The default value is 6667 and can be changed by the user if needed.
     * In the case that the port number is changed a check is made to see if it is
     * in the range of TCP port (from 0 to 65535). If not an error message is displayed and the application terminates.
     */
    public ChatServerWindow() {

        String port = (String) JOptionPane.showInputDialog(null, "Enter the host to connect to : ", "Welcome to HUB Chat!", JOptionPane.QUESTION_MESSAGE, null, null, "6667");
        int portNumber = -1;

        try {

            portNumber = Integer.parseInt(port);

            if( portNumber > 0 && portNumber < 65535) {

                try {

                    new ChatServer( portNumber);

                } catch (IOException e) {

                    System.out.println("There was an error listening to  port : " + portNumber);
                    JOptionPane.showMessageDialog(null, "Could not start server at port : " + portNumber + ". The client will now terminate!", "HUB Chat information", 1);

                }
            } else {

                JOptionPane.showMessageDialog(null, "You entered an invalid port number : " + portNumber + ". The client will now terminate!", "HUB Chat information", 1);

            }
        } catch (NumberFormatException ex){

            System.out.println("There was an error while parsing the provided port : " + port + ".");
            JOptionPane.showMessageDialog(null, "This is not a valid port number : " + port + ". The client will now terminate!", "HUB Chat information", 1);

        }

    }

    /**
     * Main method to start the chat server.
     */
    public static void main( String args[] ) {

        new ChatServerWindow();

    }

}
