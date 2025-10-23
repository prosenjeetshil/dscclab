import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST, PORT); // Establish connection
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Auto-flush output
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Calculator Server at " + HOST + ":" + PORT);
            System.out.println("\n--- Calculator Operations ---");
            System.out.println("1. Addition (add)");
            System.out.println("2. Subtraction (sub)");
            System.out.println("3. Multiplication (mul)");
            System.out.println("4. Division (div)");
            System.out.println("5. Modulo (mod)"); // Assuming 'mod' was added in server
            System.out.println("-----------------------------");
            System.out.println("Type 'quit' to exit.\n");

            String inputLine;
            while (true) {
                System.out.print("Enter your choice (1-5 or quit): ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("quit")) {
                    out.println("quit"); // Signal server to disconnect
                    System.out.println("Exiting calculator client.");
                    break; // Exit client loop
                }

                String methodName;
                switch (choice) {
                    case "1":
                        methodName = "add";
                        break;
                    case "2":
                        methodName = "sub";
                        break;
                    case "3":
                        methodName = "mul";
                        break;
                    case "4":
                        methodName = "div";
                        break;
                    case "5":
                        methodName = "mod";
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1-5 or 'quit'.");
                        continue; // Ask for choice again
                }

                double num1, num2;

                System.out.print("Enter first number: ");
                try {
                    num1 = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    continue; // Ask for choice again
                }

                System.out.print("Enter second number: ");
                try {
                    num2 = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    continue; // Ask for choice again
                }

                // Construct the request string in the format expected by the server
                String request = methodName + " " + num1 + " " + num2;
                out.println(request); // Send request to server

                String response = in.readLine(); // Read response from server
                System.out.println("Result: " + response);
                System.out.println(); // Add a blank line for readability
            }

        } catch (ConnectException e) {
            System.err.println("Connection refused: Make sure the server is running on " + HOST + ":" + PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + HOST);
        } catch (IOException e) {
            System.err.println("I/O error with server: " + e.getMessage());
            // This can happen if the server unexpectedly closes the connection (e.g., server shutdown)
            if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
                 System.err.println("Server connection was reset. It might have shut down.");
            }
            e.printStackTrace();
        }
    }
}