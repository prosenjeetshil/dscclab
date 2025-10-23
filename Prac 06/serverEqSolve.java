// serverEqSolve.java
import java.io.*;
import java.rmi.*;

public class serverEqSolve {
    public static void main(String[] args) {
        try {
            // Set the hostname for the RMI registry lookup.
            // This is crucial if the client and server are on different machines.
            // System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            implEqSolve obj = new implEqSolve();
            Naming.rebind("EquationSolver", obj);

            System.out.println("RMI Server for Equation Solving is ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}