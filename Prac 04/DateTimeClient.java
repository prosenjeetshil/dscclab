// DateTimeClient.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DateTimeClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5001; // Must match server's port

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST, PORT); // Establish connection
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Auto-flush output
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Date/Time Server at " + HOST + ":" + PORT);
            System.out.println("\n--- Date/Time Service Operations ---");
            System.out.println("1. Get Current Date (date)");
            System.out.println("2. Get Current Time (time)");
            System.out.println("3. Get Current Date and Time (datetime)");
            System.out.println("------------------------------------");
            System.out.println("Type 'quit' to exit.\n");

            String inputLine;
            while (true) {
                System.out.print("Enter your choice (1-3 or quit): ");
                String choice = scanner.nextLine();

                String commandToSend;

                switch (choice.toLowerCase()) { // Client still uses switch for user input parsing
                    case "1":
                        commandToSend = "date";
                        break;
                    case "2":
                        commandToSend = "time";
                        break;
                    case "3":
                        commandToSend = "datetime";
                        break;
                    case "quit":
                        commandToSend = "quit";
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, 3, or 'quit'.");
                        System.out.println(); // Add blank line for readability
                        continue; // Ask for choice again
                }

                out.println(commandToSend); // Send request to server

                if (commandToSend.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting Date/Time client.");
                    break; // Exit client loop
                }

                String response = in.readLine(); // Read response from server
                System.out.println("Server response: " + response);
                System.out.println(); // Add a blank line for readability
            }

        } catch (ConnectException e) {
            System.err.println("Connection refused: Make sure the server is running on " + HOST + ":" + PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + HOST);
        } catch (IOException e) {
            System.err.println("I/O error with server: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
                 System.err.println("Server connection was reset. It might have shut down.");
            }
            e.printStackTrace();
        }
    }
}