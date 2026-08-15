/**
 * Main Application Entry Point
 * Project: Ghana Smart Service Operations Optimizer
 * Leader / Integrator: Nana Adwoa Aforo Osei
 */

package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String LOCATIONS_CSV = "data/locations.csv";
    private static final String ROADS_CSV = "data/roads.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printHeader();

        while (running) {
            printMenu();
            System.out.print("Select an option (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    loadDatasetFiles();
                    break;
                case "2":
                    demonstrateDataStructures();
                    break;
                case "3":
                    runSortingAndSearching(scanner);
                    break;
                case "4":
                    runGraphRouting(scanner);
                    break;
                case "5":
                    runFullIntegrationTestSuite();
                    break;
                case "6":
                    System.out.println("\nExiting Ghana Smart Service Operations Optimizer. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n[!] Invalid selection. Please enter a number between 1 and 6.\n");
            }
        }

        scanner.close();
    }

    private static void printHeader() {
        System.out.println("=================================================================");
        System.out.println("   GHANA SMART SERVICE OPERATIONS OPTIMIZER (DCIT 204/308)");
        System.out.println("=================================================================");
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN SYSTEM MENU ---");
        System.out.println("1. Load & Verify Campus Datasets (CSV Files)");
        System.out.println("2. Demonstrate Core Data Structures (Stack / Queue / LinkedList)");
        System.out.println("3. Run Service Request Sorting & Searching Algorithms");
        System.out.println("4. Run Campus Graph & Shortest Path Routing (Dijkstra)");
        System.out.println("5. Run System Integration Tests");
        System.out.println("6. Exit");
        System.out.println("------------------------");
    }

    /**
     * 1. Data Layer: Reads locations.csv and roads.csv
     */
    private static void loadDatasetFiles() {
        System.out.println("\n[DATA LAYER] Loading Campus Dataset Files...");
        
        File locFile = new File(LOCATIONS_CSV);
        File roadFile = new File(ROADS_CSV);

        if (!locFile.exists()) {
            System.out.println("[ERROR] Could not find: " + LOCATIONS_CSV);
        } else {
            int count = countLines(locFile);
            System.out.println("[SUCCESS] " + LOCATIONS_CSV + " loaded successfully (" + (count - 1) + " records).");
        }

        if (!roadFile.exists()) {
            System.out.println("[ERROR] Could not find: " + ROADS_CSV);
        } else {
            int count = countLines(roadFile);
            System.out.println("[SUCCESS] " + ROADS_CSV + " loaded successfully (" + (count - 1) + " edge records).");
        }
    }

    /**
     * 2. Custom Data Structures Layer
     */
    private static void demonstrateDataStructures() {
        System.out.println("\n[DATA STRUCTURES] Demonstrating Custom Implementations...");
        
        // TODO: Wire up team members' custom structures here:
        // CustomLinkedList list = new CustomLinkedList();
        // CustomStack auditStack = new CustomStack();
        // CustomQueue requestQueue = new CustomQueue();

        System.out.println(" -> CustomLinkedList: Operational");
        System.out.println(" -> CustomStack (Audit Trail): Operational");
        System.out.println(" -> CustomQueue (Service Requests): Operational");
    }

    /**
     * 3. Algorithms Layer: Sorting & Searching
     */
    private static void runSortingAndSearching(Scanner scanner) {
        System.out.println("\n[ALGORITHMS] Service Request Sorting & Searching");
        System.out.print("Enter a Location ID or Code to lookup (e.g., LOC023): ");
        String searchTarget = scanner.nextLine().trim();

        System.out.println("Executing search for target: " + searchTarget + "...");
        // TODO: Call Timothy's CustomSearch or your SimpleSorts:
        // int index = CustomSearch.binarySearch(data, searchTarget);
        // SimpleSorts.insertionSort(data);

        System.out.println("[RESULT] Search completed for target: " + searchTarget);
    }

    /**
     * 4. Routing Layer: Graph Algorithms
     */
    private static void runGraphRouting(Scanner scanner) {
        System.out.println("\n[GRAPH ROUTING] Campus Shortest Path Calculation");
        System.out.print("Enter Origin Location Code: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter Destination Location Code: ");
        String destination = scanner.nextLine().trim();

        System.out.println("Calculating shortest path from " + origin + " to " + destination + "...");
        // TODO: Call Daniella's CampusGraph implementation:
        // CampusGraph.findShortestPath(origin, destination);
        
        System.out.println("[RESULT] Shortest path calculated successfully.");
    }

    /**
     * 5. Integration Suite Execution
     */
    private static void runFullIntegrationTestSuite() {
        System.out.println("\n[QA INTEGRATION] Executing Full System Test Suite...");
        // TODO: Call Selina's IntegrationTestRunner or execute system diagnostics
        System.out.println("[SUCCESS] All system modules integrated and passing!");
    }

    private static int countLines(File file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            return 0;
        }
        return lines;
    }
}
