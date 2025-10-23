import java.io.*; // Added for IOException
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;

class RPCServer1 {
    private DatagramSocket ds; // Socket for receiving client requests

    public RPCServer1() {
        try {
            ds = new DatagramSocket(1200); // Server listens on port 1200
            byte[] b = new byte[4096]; // Buffer for incoming data

            System.out.println("RPC Date/Time Server started on port 1200...");
            System.out.println("Waiting for client requests...\n");

            while (true) {
                DatagramPacket dp = new DatagramPacket(b, b.length);
                ds.receive(dp); // Receive client request

                String clientRequest = new String(dp.getData(), 0, dp.getLength()).trim(); // Trim whitespace

                if (clientRequest.equalsIgnoreCase("q")) {
                    System.out.println("Client requested shutdown. Server is terminating.");
                    // No need to send a reply for 'q' for a clean exit, as client exits locally.
                    // If you want to acknowledge 'q', you could send a "Server shutting down" message
                    // before breaking. For this practical, exiting is fine.
                    break;
                }

                String methodName;
                String result = "Error: Unknown command"; // Default error message

                try {
                    // In this specific RPC, the request is just the method name ("date" or "time")
                    methodName = clientRequest.toLowerCase();

                    Calendar c = Calendar.getInstance();
                    Date d = c.getTime(); // Get current date and time

                    System.out.println("Received request: '" + methodName + "' from " + dp.getAddress() + ":" + dp.getPort());

                    if (methodName.equals("date")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        result = dateFormat.format(d);
                    } else if (methodName.equals("time")) {
                        // Using SimpleDateFormat for time as well for better formatting options
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // 24-hour format
                        result = timeFormat.format(d);
                    } else {
                        // Handled by the default "Error: Unknown command"
                    }
                } catch (Exception e) {
                    // Catch any unexpected parsing or date/time formatting errors
                    result = "Server error processing request: " + e.getMessage();
                    System.err.println("Server internal error: " + e.getMessage());
                }

                // Send the result back to the client
                byte[] responseBytes = result.getBytes();
                // Send reply to the client's address and the port it sent from (which is now 1300)
                DatagramPacket replyPacket = new DatagramPacket(responseBytes, responseBytes.length, dp.getAddress(), dp.getPort());
                ds.send(replyPacket);

                System.out.println("Sent response: '" + result + "'\n");
            }
        } catch (IOException e) { // Catch more specific IOException
            System.err.println("I/O error in server: " + e.getMessage());
        } catch (Exception e) { // Catch any other unexpected exceptions
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close(); // Close the server socket
            }
        }
    }

    public static void main(String[] args) {
        new RPCServer1();
    }
}