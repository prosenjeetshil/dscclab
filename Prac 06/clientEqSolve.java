// clientEqSolve.java
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.Scanner;

public class clientEqSolve {
    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            // A more descriptive name for the RMI object lookup
            intfEqSolve object = (intfEqSolve) Naming.lookup("rmi://127.0.0.1/EquationSolver");

            scanner = new Scanner(System.in);
            System.out.println("RMI Equation Solver Client is running.");

            while (true) {
                System.out.println("\nEquations:");
                System.out.println("1. (a-b)2");
                System.out.println("2. (a+b)2");
                System.out.println("3. (a-b)3");
                System.out.println("4. (a+b)3");
                System.out.println("Type 'quit' to exit.");

                System.out.print("Choose the equation (1-4): ");
                String choiceStr = scanner.nextLine();

                if (choiceStr.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting client.");
                    break;
                }

                int choice;
                try {
                    choice = Integer.parseInt(choiceStr);
                    if (choice < 1 || choice > 4) {
                        System.out.println("Invalid option. Please choose a number from 1 to 4.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                String equationString = object.getEquation(choice);
                System.out.println("You chose: " + equationString);

                System.out.print("Enter the value of 'a': ");
                int num1;
                try {
                    num1 = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for 'a'. Please enter an integer.");
                    continue;
                }

                System.out.print("Enter the value of 'b': ");
                int num2;
                try {
                    num2 = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for 'b'. Please enter an integer.");
                    continue;
                }
                
                int res = 0;
                // Use a switch expression to make the code more concise
                res = switch (choice) {
                    case 1 -> object.solveEq1(num1, num2);
                    case 2 -> object.solveEq2(num1, num2);
                    case 3 -> object.solveEq3(num1, num2);
                    case 4 -> object.solveEq4(num1, num2);
                    default -> 0; // This case is unreachable due to the validation above
                };

                System.out.println("The answer is: " + res);
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}