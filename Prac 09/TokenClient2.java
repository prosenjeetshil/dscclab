// TokenClient2.java

import java.net.*;
import java.io.*;

class TokenClient2 {
    // Port 200 is for Client 2
    private static final int CLIENT_PORT = 200;
    private static final int NEXT_CLIENT_PORT = 100; // Client 1
    private static final int SERVER_PORT = 1000;
    private static final String TOKEN_MSG = "Token";

    public static void main(String[] args) throws Exception {
        DatagramSocket ds = null;
        BufferedReader br = null;
        boolean hasToken = true; // Start Client2 with the token to initiate the ring.

        try {
            ds = new DatagramSocket(CLIENT_PORT);
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("TokenClient2 (Port 200) running.");
            System.out.println("Client 2 starts with the token.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            if (hasToken) {
                System.out.println("\n------------------------------------------");
                System.out.println("TOKEN RECEIVED. Do you want to enter data? (yes/no):");
                String ans = br.readLine().trim();
                System.out.println("------------------------------------------");

                if (ans.equalsIgnoreCase("yes")) {
                    System.out.print("Enter data to send to server: ");
                    String data = "Client-2===> " + br.readLine();

                    // 1. Send Data to Server (Shared Resource)
                    byte[] dataBuff = data.getBytes();
                    ds.send(new DatagramPacket(dataBuff, dataBuff.length, InetAddress.getLocalHost(), SERVER_PORT));
                    System.out.println("Data sent to server (Port " + SERVER_PORT + ").");

                    // 2. Client still has the token, can send again or pass it.
                    // We'll immediately ask if they want to pass the token after sending data.
                }

                // --- Token Passing Logic ---
                System.out.println("Passing token to Client 1 (Port " + NEXT_CLIENT_PORT + ").");
                byte[] tokenBuff = TOKEN_MSG.getBytes();

                // Send Token to Client 1
                ds.send(new DatagramPacket(tokenBuff, tokenBuff.length, InetAddress.getLocalHost(), NEXT_CLIENT_PORT));
                hasToken = false;

            } else {
                // Client does not have the token, so it must be in receiving mode.
                System.out.println("\nWaiting/Receiving mode... (Port " + CLIENT_PORT + ")");

                byte[] bf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(bf, bf.length);
                ds.receive(dp); // This is a blocking call, waiting for the token or data.

                String incomingMsg = new String(dp.getData(), 0, dp.getLength()).trim();
                System.out.println("Received message: " + incomingMsg);

                if (incomingMsg.equals(TOKEN_MSG)) {
                    hasToken = true;
                    System.out.println("!!! TOKEN ACQUIRED. !!!");
                } else {
                    // This handles unexpected data sent directly between clients
                    System.out.println("Received unexpected data: " + incomingMsg);
                }
            }
        }
    }
}