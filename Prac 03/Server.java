import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors; // For thread pool

public class Server {
    private static final int PORT = 5000;
    // Use a thread pool to handle multiple client connections concurrently
    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Calculator Server is running on port " + PORT + " (TCP/IP Stream Socket)");
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
            // Shut down the thread pool when the server exits (e.g., on error)
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

        private String handleRequest(String request) {
            String[] tokens = request.split(" ");
            if (tokens.length != 3) {
                return "Invalid format. Use: operation operand1 operand2 (e.g., add 10.5 2.3)";
            }

            String operation = tokens[0].toLowerCase();
            double num1, num2;

            try {
                num1 = Double.parseDouble(tokens[1]);
                num2 = Double.parseDouble(tokens[2]);
            } catch (NumberFormatException e) {
                return "Invalid numbers. Operands must be numeric.";
            }

            switch (operation) {
                case "add": return String.valueOf(num1 + num2);
                case "sub": return String.valueOf(num1 - num2);
                case "mul": return String.valueOf(num1 * num2);
                case "div":
                    if (num2 == 0) return "Division by zero error.";
                    return String.valueOf(num1 / num2);
                case "mod": // Added modulo operation
                    if (num2 == 0) return "Modulo by zero error.";
                    return String.valueOf(num1 % num2);
                default: return "Unsupported operation: " + operation + ". Supported: add, sub, mul, div, mod.";
            }
        }
    }
}