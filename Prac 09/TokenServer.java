// TokenServer.java

import java.net.*;
import java.io.*;

class TokenServer {
    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(1000); // Server listens on port 1000
            System.out.println("TokenServer (Logger) running on port 1000. Waiting for data...");

            while (true) {
                byte buff[] = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buff, buff.length);
                ds.receive(dp);

                String str = new String(dp.getData(), 0, dp.getLength()).trim();

                // Display the received data
                System.out.println("------------------------------------------");
                System.out.println("SHARED RESOURCE ACCESS: " + str);
                System.out.println("------------------------------------------");
            }
        } catch (SocketException e) {
            System.err.println("Error creating socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}