import java.io.*;
import java.net.*;

class RPCClient1 {
    public RPCClient1() {
        DatagramSocket clientSocket = null; // Use a single socket for sending and receiving

        try {
            InetAddress ia = InetAddress.getLocalHost();

            // Bind the client's single socket to a known port (1300) for both sending and receiving
            clientSocket = new DatagramSocket(1300);

            System.out.println("\nRPC Client - Date/Time Service\n");
            System.out.println("Available commands:");
            System.out.println("  'date' - To get the current date from the server");
            System.out.println("  'time' - To get the current time from the server");
            System.out.println("  'q'    - To quit the client and signal server shutdown\n");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Enter command (date, time, or q): ");
                String command = br.readLine();

                // Send the command to the server
                byte[] sendBuffer = command.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, ia, 1200);
                clientSocket.send(sendPacket);

                if (command.equalsIgnoreCase("q")) {
                    System.out.println("Client shutting down.");
                    break; // Exit loop if 'q' is entered
                }

                // Prepare to receive the response
                byte[] receiveBuffer = new byte[4096]; // Increased buffer size for safety
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                // Receive the response on the same socket
                clientSocket.receive(receivePacket);

                String result = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("\nServer response: " + result + "\n");
            }
        } catch (SocketException e) {
            System.err.println("Socket error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close(); // Ensure the socket is closed
            }
        }
    }

    public static void main(String[] args) {
        new RPCClient1();
    }
}