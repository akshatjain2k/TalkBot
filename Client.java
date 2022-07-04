import java.io.*;
import java.net.Socket;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

class Client extends JFrame {

    Socket socket;

    BufferedReader br; // To read data
    PrintWriter out; // T O write the data

    // Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor
    public Client() {
        try {

            System.out.println("Sending Request to Server");
            socket = new Socket("192.168.43.249", 7778);
            System.out.println("Connection Done...");

            /*
             * It will take input stream from socket and give it to inputstream reader,
             * InputStreamReader will change data in character from byte
             */
            br = new BufferedReader(new InputStreamReader((socket.getInputStream())));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key released "+e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("You have pressed ENTER button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    System.out.println();
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }

        });
    }

    private void createGUI() {
        // gui code

        this.setTitle("Client Messager[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // coding for Component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        // heading.setIcon(new ImageIcon("icon.png"));
        // heading.setHorizontalTextPosition(SwingConstants.CENTER);
        // heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        // Layout of Frame
        this.setLayout(new BorderLayout());

        // Adding the Components to Frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        // messageArea.setCaretPosition(messageArea.getDocument().getLength());

        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);
    }

    // Start Reading [Method]
    public void startReading() {
        // thread will read data

        // Create Thread for Reading
        Runnable r1 = () -> {
            System.out.println("Reader Started...");

            try {
                while (true) {

                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server Terminated The Chat!");
                        JOptionPane.showMessageDialog(this, "Server Terminated The Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    System.out.println();
                    // System.out.println("Server : " + msg);
                    messageArea.append("Server: " + msg + "\n");

                }
            } catch (IOException e) {
                // e.printStackTrace(); // print everything about error
                System.out.println("Connection Closed");

            }

        };
        new Thread(r1).start();
    }

    // Start Writing [Method]
    public void startWriting() {
        // thread will take data from user and send it to client

        // Create Thread for Writing
        Runnable r2 = () -> {
            System.out.println("Writer Started...");

            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                    /*
                     * flush() writes the content of the buffer to the destination and
                     * makes the buffer empty for further data to store,
                     * but it does not closes the stream permanently
                     */

                }
            } catch (Exception e) {
                // e.printStackTrace(); // print everything about error/
                System.out.println("Connection Closed");

            }

        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Client");
        new Client();
    }
}
