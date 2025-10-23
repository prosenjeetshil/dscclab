import java.io.*;
import java.net.*;

class RPCClient {
    public RPCClient() {
        DatagramSocket clientSocket = null; // Use a single socket for sending and receiving

        try {
            InetAddress ia = InetAddress.getLocalHost();

            // Client uses a single socket bound to port 1300 for both sending and receiving
            clientSocket = new DatagramSocket(1300); // Now this socket sends FROM 1300 and receives ON 1300

            System.out.println("\nRPC Client - Calculator\n");
            System.out.println("Choose an operation:");
            System.out.println("1. Addition (add)");
            System.out.println("2. Subtraction (sub)");
            System.out.println("3. Multiplication (mul)");
            System.out.println("4. Division (div)");
            System.out.println("Enter 'q' to quit\n");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Enter your choice (1-4 or q): ");
                String choice = br.readLine();

                if (choice.equalsIgnoreCase("q")) {
                    // Send "q" to the server to signal shutdown
                    byte[] quitBytes = "q".getBytes();
                    DatagramPacket quitPacket = new DatagramPacket(quitBytes, quitBytes.length, ia, 1200);
                    clientSocket.send(quitPacket); // Send via the single socket
                    System.out.println("Client shutting down.");
                    break;
                }

                String methodName = "";
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
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or q.");
                        continue; // Go back to the beginning of the loop
                }

                System.out.print("Enter first number: ");
                String num1Str = br.readLine();
                int val1;
                try {
                    val1 = Integer.parseInt(num1Str);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter an integer.");
                    continue;
                }

                System.out.print("Enter second number: ");
                String num2Str = br.readLine();
                int val2;
                try {
                    val2 = Integer.parseInt(num2Str);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter an integer.");
                    continue;
                }

                // Construct the message string for the server
                String request = methodName + " " + val1 + " " + val2;
                byte[] b = request.getBytes();

                // Send the request to the server using the single socket
                DatagramPacket dpSend = new DatagramPacket(b, b.length, ia, 1200);
                clientSocket.send(dpSend);

                // Prepare to receive the response using the same single socket
                byte[] receiveBuffer = new byte[4096]; // Buffer for incoming data
                DatagramPacket dpReceive = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(dpReceive); // Receive on the same socket (port 1300)

                String s = new String(dpReceive.getData(), 0, dpReceive.getLength());
                System.out.println("\nResult = " + s + "\n");
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the single client socket
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        new RPCClient();
    }
}