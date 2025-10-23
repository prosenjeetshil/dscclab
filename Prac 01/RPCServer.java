import java.util.*;
import java.net.*;
import java.io.*; // <-- Add this import

class RPCServer {
    private DatagramSocket ds; // Socket for receiving client requests

    // Define an enum for calculator operations
    private enum Operation {
        ADD, SUB, MUL, DIV, UNKNOWN
    }

    public RPCServer() {
        try {
            ds = new DatagramSocket(1200); // Server listens on port 1200
            byte[] b = new byte[4096]; // Buffer for incoming data

            System.out.println("RPC Server started on port 1200...");
            System.out.println("Waiting for client requests...\n");

            while (true) {
                DatagramPacket dp = new DatagramPacket(b, b.length);
                ds.receive(dp); // Receive client request

                String str = new String(dp.getData(), 0, dp.getLength()); // e.g., "add 10 20"

                if (str.equalsIgnoreCase("q")) {
                    System.out.println("Client requested shutdown. Server is terminating.");
                    break; // Exit the loop and terminate the server
                }

                String methodNameStr;
                Operation operation;
                int val1, val2;
                String result = "Error: Invalid request format"; // Default error message

                try {
                    StringTokenizer st = new StringTokenizer(str, " ");
                    methodNameStr = st.nextToken().toUpperCase(); // Convert to uppercase for enum matching

                    // Convert string to Operation enum
                    try {
                        operation = Operation.valueOf(methodNameStr);
                    } catch (IllegalArgumentException e) {
                        operation = Operation.UNKNOWN;
                    }

                    val1 = Integer.parseInt(st.nextToken());
                    val2 = Integer.parseInt(st.nextToken());

                    System.out.println("Received request: " + methodNameStr + " " + val1 + " " + val2);

                    switch (operation) {
                        case ADD:
                            result = String.valueOf(add(val1, val2));
                            break;
                        case SUB:
                            result = String.valueOf(sub(val1, val2));
                            break;
                        case MUL:
                            result = String.valueOf(mul(val1, val2));
                            break;
                        case DIV:
                            if (val2 == 0) {
                                result = "Error: Division by zero";
                            } else {
                                result = String.valueOf(div(val1, val2));
                            }
                            break;
                        case UNKNOWN:
                        default: // Should be caught by UNKNOWN, but good practice
                            result = "Error: Unknown method " + methodNameStr;
                            break;
                    }
                } catch (NoSuchElementException e) {
                    result = "Error: Malformed request. Expected 'method val1 val2'.";
                    System.err.println("Error parsing request: " + e.getMessage());
                } catch (NumberFormatException e) {
                    result = "Error: Invalid number format. Parameters must be integers.";
                    System.err.println("Error converting numbers: " + e.getMessage());
                }

                // Send the result back to the client
                byte[] b1 = result.getBytes();
                // Use the address and port from the incoming packet to send the reply back
                DatagramPacket dp1 = new DatagramPacket(b1, b1.length, dp.getAddress(), dp.getPort());
                ds.send(dp1);

                System.out.println("Sent result: " + result + "\n");
            }
        } catch (IOException e) { // This line now compiles
            System.err.println("I/O error in server: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close(); // Close the server socket
            }
        }
    }

    public int add(int val1, int val2) { return val1 + val2; }
    public int sub(int val3, int val4) { return val3 - val4; }
    public int mul(int val3, int val4) { return val3 * val4; }
    public int div(int val3, int val4) { return val3 / val4; }

    public static void main(String[] args) {
        new RPCServer();
    }
}