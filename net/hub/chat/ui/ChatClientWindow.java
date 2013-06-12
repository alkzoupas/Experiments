package net.hub.chat.ui;

import net.hub.chat.ChatClient;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * A simple Swing user interface used to set the host, port and nick name of the client.
 * After the submission of these information and a successful connection to the server the chat
 * client window is created with a text area holding the messages and a text input waiting
 * for new messages from the user.
 */
public class ChatClientWindow  extends JFrame {

    private JTextField textField;
    private JTextArea textArea;
    private ChatClient chatClient;

    /**
     * First create a pop up with Swing to get the host and the port of the server. Following that the user has
     * to provide a nick name that will be used for participating in the chat room. If the inputs are valid then
     * a new connection to the server is created else the appropriate error messages are displayed and the
     * client terminates. After that a new JFrame is created and inside it a JPanel
     * with a JTextArea holding the messages received from the server and a JTextField under it, which is used to
     * capture the users input. With the JTextField a method is bound to handle any new messages submitted by the user.
     */
    public ChatClientWindow () {

        int portNumber = 0;

        String host = (String)JOptionPane.showInputDialog(null, "Enter the host to connect to : ", "Welcome to HUB Chat!", JOptionPane.QUESTION_MESSAGE, null, null, "localhost" );
        String port = (String)JOptionPane.showInputDialog(null, "Enter the port to connect to : ", "Welcome to HUB Chat!", JOptionPane.QUESTION_MESSAGE, null, null, "6667" );
        String nickName = (String)JOptionPane.showInputDialog(null, "Enter the host to connect to : ", "Welcome to HUB Chat!", JOptionPane.QUESTION_MESSAGE, null, null, "visitor" );

        try {

            portNumber = Integer.parseInt(port);

        } catch (NumberFormatException ex){

            System.out.println("There was an error while parsing the provided port : " + port + ".");

        }

        if( null == host || "".equals(host) ) {

            JOptionPane.showMessageDialog(null, "You entered an invalid host : " + host + ". The client will now terminate!", "HUB Chat information", 1);

        } else {

            if( portNumber <= 0 || portNumber > 65535 ) {

                JOptionPane.showMessageDialog(null, "You entered the port : " + port + ". The client will now terminate!", "HUB Chat information", 1);

            } else {

                if( null == nickName || "".equals(nickName) ) {

                    JOptionPane.showMessageDialog(null, "You entered an invalid nick name : " + nickName + ". The client will now terminate!", "HUB Chat information", 1);

                } else {

                    try {

                        chatClient = new ChatClient( host, portNumber, nickName, this );

                        JFrame frame = new JFrame("Chat Client");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        JPanel panel = new JPanel();
                        panel.setLayout( new BorderLayout() );

                        textField = new JTextField();

                        textArea = new JTextArea(14, 80);
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setFont(new Font("Arial", Font.PLAIN, 11 ));

                        JScrollPane scrollingResult = new JScrollPane(textArea);

                        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
                        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

                        panel.add("South", textField);
                        panel.add( "Center", scrollingResult );

                        textField.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                processMessage(e.getActionCommand());
                            }
                        });

                        frame.getContentPane().add(panel);
                        frame.pack();

                        textField.requestFocusInWindow();

                        frame.setVisible(true);

                        chatClient.start();

                    } catch (IOException e) {

                        System.out.println("There was an error connection to host : " + host + " and port : " + port);
                        JOptionPane.showMessageDialog(null, "Could not connect to server with host : " + host + " and port : " + portNumber +". The client will now terminate!", "HUB Chat information", 1);

                    }
                }
            }
        }

    }

    /**
     * Method bound with an action listener to the text field where the user types the messages.
     * Whenever something new is typed followed by carriage return, this method is invoked and
     * the messages is passed to the server.
     * @param message the message to transmit to the server
     */
    private void processMessage( String message ) {

        chatClient.transmit( message );
        textField.setText("");

    }

    /**
     * Getter method for the text area where the messages from the server are printed.
     * It is used from the client thread to update the user interface.
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * Main method to start the chat client.
     */
    public static void main( String args[] ) {

        new ChatClientWindow();

    }
}
