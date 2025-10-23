// intfEqSolve.java
import java.rmi.*;

public interface intfEqSolve extends Remote {
    public int solveEq1(int a, int b) throws RemoteException;
    public int solveEq2(int a, int b) throws RemoteException;
    public int solveEq3(int a, int b) throws RemoteException;
    public int solveEq4(int a, int b) throws RemoteException;

    // Added a method to get the equation's string representation
    public String getEquation(int choice) throws RemoteException;
}