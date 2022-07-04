import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br; // To read data
    PrintWriter out; // T O write the data

    // Constructor...
    public Server() {

        try {
            server = new ServerSocket(7778);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();

            /*
             * It will take input stream from socket and give it to inputstream reader,
             * InputStreamReader will change data in character from byte
             */
            br = new BufferedReader(new InputStreamReader((socket.getInputStream())));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Using Multithreading for reading and writing data
    // Start Writing [Method]
    public void startWriting() {
        // thread will read data

        // Create Thread for Reading
        Runnable r1 = () -> {
            System.out.println("Reader Started...");

            try {
                while (true) {
                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated The Chat!");

                        socket.close();

                        break;
                    }

                    System.out.println("Client: " + msg);

                }
            } catch (IOException e) {
                // e.printStackTrace(); // print everything about error
                System.out.println("Connection Closed");
            }

        };
        new Thread(r1).start();
    }

    // Start Reading [Method]
    public void startReading() {
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
                // e.printStackTrace(); // print everything about error
                System.out.println("Connection Closed");
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server");
        new Server();
    }
}