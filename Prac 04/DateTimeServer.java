// DateTimeServer.java

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateTimeServer {
    private static final int PORT = 5001; // Using a different port than the calculator
    private static final int THREAD_POOL_SIZE = 5; 
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Date/Time Server is running on port " + PORT + " (TCP/IP Stream Socket)");
            System.out.println("Waiting for client connections...");

            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                // Hand over client handling to a new thread from the pool
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shut down the thread pool when the server exits (e.g., on error or manual termination)
            executor.shutdown();
        }
    }

    // Inner class to handle each client connection in a separate thread
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // 'true' for auto-flush
            ) {
                String request;
                // Keep reading requests from this client until they send "quit"
                while ((request = in.readLine()) != null) {
                    System.out.println("Request received from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + ": " + request);

                    if (request.equalsIgnoreCase("quit")) {
                        System.out.println("Client " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " disconnected.");
                        break; // Exit loop, closing streams and socket
                    }

                    String response = handleRequest(request);
                    out.println(response);
                }
            } catch (IOException e) {
                // Log client-specific connection issues without crashing the server
                System.err.println("Error handling client " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + ": " + e.getMessage());
            } finally {
                try {
                    clientSocket.close(); // Ensure client socket is closed
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        // This method acts as the "remote procedure" dispatcher
        // --- REFACTORED TO USE IF-ELSE IF-ELSE START ---
        private String handleRequest(String request) {
            // Trim whitespace from the request
            String command = request.trim().toLowerCase();

            // Get current date and time
            Date now = new Date();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss"); // 24-hour format

            if (command.equals("date")) {
                return dateFormatter.format(now);
            } else if (command.equals("time")) {
                return timeFormatter.format(now);
            } else if (command.equals("datetime")) {
                return dateFormatter.format(now) + " " + timeFormatter.format(now);
            } else {
                return "Error: Unknown command. Supported commands: date, time, datetime.";
            }
        }
    }
}