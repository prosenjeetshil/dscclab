import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RingElection {

    // Represents a single process in the distributed system
    class Process {
        int id;
        boolean isActive;

        Process(int id) {
            this.id = id;
            this.isActive = true; // All processes start active
        }

        @Override
        public String toString() {
            return "P" + id + (isActive ? " (Active)" : " (Failed)");
        }
    }

    private Process[] processes;
    private int totalProcesses = 5;

    public RingElection() {
        // Initialization is done in the constructor
        System.out.println("Initializing " + totalProcesses + " processes (0 to " + (totalProcesses - 1) + ")");
        processes = new Process[totalProcesses];
        for (int i = 0; i < totalProcesses; i++) {
            processes[i] = new Process(i);
        }
    }

    /**
     * Finds the index of the highest active process ID.
     * @return The array index of the current coordinator.
     */
    public int findCurrentCoordinatorIndex() {
        int index = -1;
        int maxId = -1;
        for (int i = 0; i < processes.length; i++) {
            if (processes[i].isActive && processes[i].id > maxId) {
                maxId = processes[i].id;
                index = i;
            }
        }
        return index;
    }

    /**
     * Simulates the Ring Election Algorithm.
     * @param initiatorIndex The index of the process that detects the failure and starts the election.
     * @param failedIndex The index of the process that is currently failing (the previous coordinator).
     */
    public void runElection(int initiatorIndex, int failedIndex) {
        
        // 1. Failure Simulation
        int oldCoordinatorId = processes[failedIndex].id;
        processes[failedIndex].isActive = false;
        System.out.println("\n----------------------------------------------------");
        System.out.println("FAILURE: Process " + oldCoordinatorId + " (Coordinator) fails.");
        System.out.println("----------------------------------------------------");
        
        if (!processes[initiatorIndex].isActive) {
            System.err.println("Error: Initiator process " + initiatorIndex + " is not active!");
            return;
        }

        System.out.println("ELECTION INITIATED by Process " + processes[initiatorIndex].id);
        
        // The Election Message (list of process IDs involved in the election)
        List<Integer> electionMessage = new ArrayList<>();
        electionMessage.add(processes[initiatorIndex].id);
        
        int currentProcessIndex = initiatorIndex;
        
        // 2. Election Phase: Passing the message around the ring
        while (true) {
            int nextProcessIndex = (currentProcessIndex + 1) % totalProcesses;
            
            // Search for the next active process in the ring
            while (!processes[nextProcessIndex].isActive && nextProcessIndex != initiatorIndex) {
                nextProcessIndex = (nextProcessIndex + 1) % totalProcesses;
            }

            // Check if the message has completed the loop
            if (nextProcessIndex == initiatorIndex) {
                // If the message returns, the election phase is complete.
                break; 
            }

            Process sender = processes[currentProcessIndex];
            Process receiver = processes[nextProcessIndex];

            System.out.println(
                "Process " + sender.id + 
                " sends election message " + electionMessage + 
                " to Process " + receiver.id
            );

            // Logic: The receiving process adds its ID to the message if it's not already there
            if (!electionMessage.contains(receiver.id)) {
                 electionMessage.add(receiver.id);
            }
            
            // Move to the receiver for the next hop
            currentProcessIndex = nextProcessIndex;
        }
        
        // 3. Coordination Phase: Determine and Announce the new coordinator
        
        // Find the maximum ID in the message list (which represents all participants)
        int newCoordinatorId = Collections.max(electionMessage);
        
        System.out.println("\n----------------------------------------------------");
        System.out.println("ELECTION COMPLETE: Process " + newCoordinatorId + " becomes the new coordinator.");
        System.out.println("----------------------------------------------------");

        // 4. Announcement Phase: Coordinator sends the message to all active processes
        int coordinatorIndex = findCurrentCoordinatorIndex();
        int announcementSenderIndex = coordinatorIndex;

        do {
            int nextProcessIndex = (announcementSenderIndex + 1) % totalProcesses;
            
            // Search for the next active process in the ring
            while (!processes[nextProcessIndex].isActive && nextProcessIndex != coordinatorIndex) {
                nextProcessIndex = (nextProcessIndex + 1) % totalProcesses;
            }
            
            if (nextProcessIndex == coordinatorIndex) {
                 // Check if the announcement completed a full cycle back to the coordinator
                 break; 
            }
            
            Process sender = processes[announcementSenderIndex];
            Process receiver = processes[nextProcessIndex];
            
            System.out.println(
                "Process " + sender.id + 
                " passes Coordinator(" + newCoordinatorId + ") message to Process " + 
                receiver.id
            );
            
            announcementSenderIndex = nextProcessIndex;
            
        } while (announcementSenderIndex != coordinatorIndex);

        System.out.println("End of Election and Announcement Cycle.");
    }

    public static void main(String[] args) {
        RingElection simulation = new RingElection();
        
        // Initial Coordinator is P4 (Index 4)
        int initialCoordinatorIndex = simulation.findCurrentCoordinatorIndex();
        
        // We will simulate P4 failing (Index 4)
        int failedIndex = 4; 
        
        // The election will be initiated by P2 (Index 2)
        int initiatorIndex = 2; 

        simulation.runElection(initiatorIndex, failedIndex);
    }
}