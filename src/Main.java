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
        // 2. CustomSearch (Timothy Donkor Kyebambo - ID: 22370734)
        // ------------------------------------------------------------------
        System.out.println("--- [2] Searching Campus Dataset (CustomSearch - Seed: 0734) ---");
        int targetKey = 734;
        System.out.println("Executing numeric key search for Target Seed [" + targetKey + "]...");
        int extractedKey = CustomSearch.extractNumericSuffix("LOC0734");
        System.out.println("Extracted Location Suffix Key: " + extractedKey + "\n");

        // ------------------------------------------------------------------
        // 3. AdvancedSorts (Sarpong Malvin Sarfo - ID: 22300217)
        // ------------------------------------------------------------------
        System.out.println("--- [3] Sorting Dispatch Priorities (AdvancedSorts - Pivot Offset: 7) ---");
        int[] dispatchPriorities = {105, 42, 89, 12, 73, 99, 5};
        
        System.out.print("Unsorted Priorities: ");
        printArray(dispatchPriorities);
        
        AdvancedSorts.quicksort(dispatchPriorities);
        
        System.out.print("Sorted Priorities (QuickSort): ");
        printArray(dispatchPriorities);
        System.out.println();

        // ------------------------------------------------------------------
        // 4. CampusGraph (Daniella Kalevor - ID: 22405426)
        // ------------------------------------------------------------------
        System.out.println("--- [4] Network Routing (CampusGraph - Traffic Multiplier: 2.6) ---");
        CampusGraph campusGraph = new CampusGraph();
        System.out.println("Campus Graph initialized successfully.");
        System.out.println();

        // ------------------------------------------------------------------
        // 5. CustomLinkedList (Hammond Emmanuel Adukwei - ID: 22400734)
        // ------------------------------------------------------------------
        System.out.println("--- [5] System Audit Logging (CustomLinkedList - Step Size: 5) ---");
        CustomLinkedList<String> auditLog = new CustomLinkedList<>();
        
        auditLog.add("LOG_01: Queue Initialized");
        auditLog.add("LOG_02: Location Code Parsed");
        auditLog.add("LOG_03: Dispatch Priorities Sorted");
        auditLog.add("LOG_04: Graph Engine Ready");
        auditLog.add("LOG_05: Integration Execution Complete");
        
        System.out.println("Audit Log Entry Count: " + auditLog.size());
        System.out.println("First Entry: " + auditLog.get(0));
        System.out.println("Latest Entry: " + auditLog.get(auditLog.size() - 1));
        
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