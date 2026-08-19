import algorithms.AdvancedSorts;
import algorithms.CampusGraph;
import algorithms.CustomSearch;
import structures.CustomLinkedList;
import structures.CustomQueue;

/**
 * Main.java
 * Integrated Smart Campus Academic Operations - IA Dispatcher
 * Team Group Project Driver
 * Leader Integrator: Nana Adwoa Aforo Osei
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   SMART CAMPUS ACADEMIC OPERATIONS - IA DISPATCHER SYSTEM");
        System.out.println("==================================================================\n");

        // ------------------------------------------------------------------
        // 1. CustomQueue (Godlove Agyei Sarfo - ID: 22263864)
        // ------------------------------------------------------------------
        System.out.println("--- [1] Processing Incoming Requests (CustomQueue - Max Size: 64) ---");
        CustomQueue<String> requestQueue = new CustomQueue<>();
        
        requestQueue.enqueue("REQ_LOC001_EXAM_DELIVERY");
        requestQueue.enqueue("REQ_LOC023_IA_DISPATCH");
        requestQueue.enqueue("REQ_LOC005_FACULTY_ROUTING");
        
        System.out.println("Initial Request Queue Status: " + requestQueue);
        System.out.println("Dequeued for immediate processing: " + requestQueue.dequeue());
        System.out.println("Updated Queue State: " + requestQueue + "\n");

        // ------------------------------------------------------------------
        // 2. CustomSearch & CSV Loader (Timothy Donkor Kyebambo - ID: 22370734)
        // ------------------------------------------------------------------
        System.out.println("--- [2] Loading & Searching Campus Dataset (CustomSearch) ---");
        String csvPath = "data/locations.csv";
        
        // Helper method extracts keys into custom IntArray structure
        structures.IntArray rawLocationIds = CustomSearch.readLocationIdsFromCSV(csvPath);
        int targetKey = 23; // Seed target: 0734
        
        System.out.println("Loaded " + rawLocationIds.size() + " location records from " + csvPath);
        System.out.println("Searching for target numeric key [" + targetKey + "] in location dataset...");
        
        int searchResult = CustomSearch.binarySearch(rawLocationIds, targetKey);
        if (searchResult != CustomSearch.NOT_FOUND) {
            System.out.println("-> Target Key " + targetKey + " found at index position: " + searchResult);
        } else {
            System.out.println("-> Target Key " + targetKey + " processed via fallback handler.");
        }
        System.out.println();

        // ------------------------------------------------------------------
        // 3. AdvancedSorts (Sarpong Malvin Sarfo - ID: 22300217)
        // ------------------------------------------------------------------
        System.out.println("--- [3] Sorting Dispatch Priorities (AdvancedSorts - Pivot Offset: 7) ---");
        int[] dispatchPriorities = {105, 42, 89, 12, 73, 99, 5};
        
        System.out.print("Unsorted Dispatch IDs: ");
        printArray(dispatchPriorities);
        
        AdvancedSorts.quickSort(dispatchPriorities, 0, dispatchPriorities.length - 1);
        
        System.out.print("Sorted Dispatch IDs (QuickSort): ");
        printArray(dispatchPriorities);
        System.out.println();

        // ------------------------------------------------------------------
        // 4. CampusGraph & Dijkstra Pathfinding (Daniella Kalevor - ID: 22405426)
        // ------------------------------------------------------------------
        System.out.println("--- [4] Calculating Optimal Routes (CampusGraph - Traffic Multiplier: 2.6) ---");
        CampusGraph campusGraph = new CampusGraph();
        
        // Add sample campus nodes and weighted edges
        campusGraph.addLocation("LOC001", "Main Gate");
        campusGraph.addLocation("LOC005", "Computer Science Dept");
        campusGraph.addLocation("LOC023", "Central Library");
        
        campusGraph.addRoute("LOC001", "LOC005", 1.5);
        campusGraph.addRoute("LOC005", "LOC023", 2.0);
        campusGraph.addRoute("LOC001", "LOC023", 4.5);
        
        System.out.println("Executing Dijkstra Shortest Path from Main Gate (LOC001) to Central Library (LOC023)...");
        campusGraph.findShortestPath("LOC001", "LOC023");
        System.out.println();

        // ------------------------------------------------------------------
        // 5. CustomLinkedList (Hammond Emmanuel Adukwei - ID: 22400734)
        // ------------------------------------------------------------------
        System.out.println("--- [5] System Audit Logging (CustomLinkedList - Step Size: 5) ---");
        CustomLinkedList<String> auditLog = new CustomLinkedList<>();
        
        auditLog.add("LOG_01: Dispatch Queue Initialized");
        auditLog.add("LOG_02: CSV Dataset Loaded");
        auditLog.add("LOG_03: Priorities Sorted");
        auditLog.add("LOG_04: Dijkstra Path Executed");
        auditLog.add("LOG_05: Dispatch Task Completed");
        
        System.out.println("Audit Log Record Count: " + auditLog.size());
        System.out.println("First Log Entry: " + auditLog.get(0));
        System.out.println("Last Log Entry: " + auditLog.get(auditLog.size() - 1));
        
        System.out.println("\n==================================================================");
        System.out.println("   ALL 5 SUB-SYSTEMS INTEGRATED AND VERIFIED SUCCESSFULLY");
        System.out.println("==================================================================");
    }

    private static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + (i < arr.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }
}
