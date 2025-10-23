// implEqSolve.java
import java.rmi.*;
import java.rmi.server.*;
import java.util.stream.Stream;

public class implEqSolve extends UnicastRemoteObject implements intfEqSolve {
    public implEqSolve() throws RemoteException { }

    @Override
    public int solveEq1(int a, int b) throws RemoteException {
        return (a * a) - (2 * a * b) + (b * b);
    }

    @Override
    public int solveEq2(int a, int b) throws RemoteException {
        return (a * a) + (2 * a * b) + (b * b);
    }

    @Override
    public int solveEq3(int a, int b) throws RemoteException {
        return (a * a * a) - (3 * a * a * b) + (3 * a * b * b) - (b * b * b);
    }

    @Override
    public int solveEq4(int a, int b) throws RemoteException {
        return (a * a * a) + (3 * a * a * b) + (3 * a * b * b) + (b * b * b);
    }

    @Override
    public String getEquation(int choice) throws RemoteException {
        // Use a switch expression for a clean way to return the equation string
        return switch (choice) {
            case 1 -> "(a-b)2 = a2 – 2ab + b2";
            case 2 -> "(a+b)2 = a2 + 2ab + b2";
            case 3 -> "(a-b)3 = a3 – 3a2b + 3ab2 – b3";
            case 4 -> "(a+b)3 = a3 + 3a2b + 3ab2 + b3";
            default -> "Invalid equation choice.";
        };
    }
}