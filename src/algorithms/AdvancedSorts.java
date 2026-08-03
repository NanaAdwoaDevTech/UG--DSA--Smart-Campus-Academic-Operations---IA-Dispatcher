/*
 * Name: Sarpong Malvin Sarfo
 * Student ID: 22300217
 * Assigned Component: src/algorithms/AdvancedSorts.java
 * ID Derivation Rule: Quicksort pivot offset = Last digit of Student ID (7)
 */

public class AdvancedSorts {
    // Verification variable derived from Student ID: 22300217 -> 7
    private static final int PIVOT_OFFSET = 7;

    /* Public API: sorts the array in-place using Quicksort. */
    public static void quicksort(int[] a) {
        if (a == null || a.length <= 1)
            return;
        qs(a, 0, a.length - 1);
    }

    private static void qs(int[] a, int low, int high) {
        if (low >= high)
            return;
        int p = choosePivotIndex(low, high);
        int pi = partition(a, low, high, p);
        qs(a, low, pi - 1);
        qs(a, pi + 1, high);
    }

    private static int choosePivotIndex(int low, int high) {
        int len = high - low + 1;
        return low + (PIVOT_OFFSET % len);
    }

    private static int partition(int[] a, int low, int high, int pivotIndex) {
        int tmp = a[pivotIndex];
        a[pivotIndex] = a[high];
        a[high] = tmp;
        int pivot = a[high];
        int i = low;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
            }
        }
        tmp = a[i];
        a[i] = a[high];
        a[high] = tmp;
        return i;
    }

    /* Printing helpers (no imports). */
    private static void printArray(String prefix, int[] a) {
        if (a == null) {
            System.out.println(prefix + "null");
            return;
        }
        System.out.print(prefix + "[");
        for (int i = 0; i < a.length; i++) {
            if (i > 0)
                System.out.print(",");
            System.out.print(a[i]);
        }
        System.out.println("]");
    }

    private static void printSortedFlag(int[] a) {
        boolean sorted = true;
        if (a != null) {
            for (int i = 1; i < a.length; i++) {
                if (a[i - 1] > a[i]) {
                    sorted = false;
                    break;
                }
            }
        }
        System.out.println("sorted=" + sorted);
    }

    public static void main(String[] args) {
        System.out.println("AdvancedSorts Quicksort demo — pivot offset = " + PIVOT_OFFSET);

        int[] normal = { 5, 3, 8, 1, 2, 7, 4, 6 };
        printArray("Before normal: ", normal);
        quicksort(normal);
        printArray("After  normal: ", normal);
        printSortedFlag(normal);

        int[] single = { 42 };
        printArray("Before single: ", single);
        quicksort(single);
        printArray("After  single: ", single);
        printSortedFlag(single);

        int[] small = { 9, 3, 7 };
        printArray("Before small: ", small);
        quicksort(small);
        printArray("After  small: ", small);
        printSortedFlag(small);

        int[] empty = {};
        printArray("Before empty: ", empty);
        quicksort(empty);
        printArray("After  empty: ", empty);
        printSortedFlag(empty);

        int[] nul = null;
        printArray("Before null: ", nul);
        quicksort(nul);
        printArray("After  null: ", nul);
        printSortedFlag(nul);
    }
}
