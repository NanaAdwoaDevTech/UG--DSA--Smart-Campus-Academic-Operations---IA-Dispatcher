/*
 * Name: Anefo Israel
 * Student ID: 22299195
 * Assigned Component: src/algorithms/SimpleSorts.java (Simple Sorting Algorithms)
 * ID Derivation Rule: Insertion sort threshold = last 2 digits of Student ID (95) % 20 = 15
 *
 * Contains from-scratch implementations of Bubble Sort, Selection Sort, and
 * Insertion Sort, plus a hybrid sort() that uses the ID-derived threshold to
 * decide when a subarray is small enough to hand off to Insertion Sort
 * (which performs well on small/near-sorted input). No java.util.* collections
 * are used anywhere in the core logic — only primitive int[] arrays.
 */

package algorithms;

public class SimpleSorts {

    // Verification Variable derived from Student ID: 22299195 -> 15
    private static final int INSERTION_SORT_THRESHOLD = 15;

    // ---------------------------------------------------------------
    // Bubble Sort
    // ---------------------------------------------------------------
    public static void bubbleSort(int[] arr) {
        if (arr == null) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    swapped = true;
                }
            }
            // Early exit if already sorted — avoids unnecessary passes
            if (!swapped) {
                break;
            }
        }
    }

    // ---------------------------------------------------------------
    // Selection Sort
    // ---------------------------------------------------------------
    public static void selectionSort(int[] arr) {
        if (arr == null) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(arr, i, minIndex);
            }
        }
    }

    // ---------------------------------------------------------------
    // Insertion Sort
    // ---------------------------------------------------------------
    public static void insertionSort(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // ---------------------------------------------------------------
    // Hybrid Sort — uses the ID-derived INSERTION_SORT_THRESHOLD.
    // Arrays at or below the threshold size are sorted directly with
    // Insertion Sort (fast on small inputs); larger arrays fall back
    // to Selection Sort in this simple-sorts context.
    // ---------------------------------------------------------------
    public static void sort(int[] arr) {
        if (arr == null) {
            return;
        }
        if (arr.length <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr);
        } else {
            selectionSort(arr);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }
        return true;
    }

    private static String toStr(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    // ---------------------------------------------------------------
    // Test / demonstration main() — normal, boundary, and empty/invalid cases
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("== SimpleSorts test run (threshold = " + INSERTION_SORT_THRESHOLD + ") ==\n");

        // 1. Normal case: unsorted array, size well under threshold
        int[] normal = {9, 3, 7, 1, 8, 2, 5};
        System.out.println("Normal case (size " + normal.length + "):");
        System.out.println("  Before: " + toStr(normal));
        bubbleSort(normal.clone());
        int[] normalSel = normal.clone();
        selectionSort(normalSel);
        int[] normalIns = normal.clone();
        insertionSort(normalIns);
        System.out.println("  Bubble:    " + toStr(bubbleSorted(normal)) + " sorted=" + isSorted(bubbleSorted(normal)));
        System.out.println("  Selection: " + toStr(normalSel) + " sorted=" + isSorted(normalSel));
        System.out.println("  Insertion: " + toStr(normalIns) + " sorted=" + isSorted(normalIns));

        // 2. Boundary case A: array exactly at the threshold size (15) -> hybrid should use insertion sort
        int[] atThreshold = new int[INSERTION_SORT_THRESHOLD];
        for (int i = 0; i < atThreshold.length; i++) {
            atThreshold[i] = atThreshold.length - i; // descending, worst case for insertion sort
        }
        int[] atThresholdCopy = atThreshold.clone();
        sort(atThresholdCopy);
        System.out.println("\nBoundary case (size == threshold, " + INSERTION_SORT_THRESHOLD + "):");
        System.out.println("  Before: " + toStr(atThreshold));
        System.out.println("  After hybrid sort(): " + toStr(atThresholdCopy) + " sorted=" + isSorted(atThresholdCopy));

        // 3. Boundary case B: array one element above the threshold -> hybrid should use selection sort
        int[] aboveThreshold = new int[INSERTION_SORT_THRESHOLD + 1];
        for (int i = 0; i < aboveThreshold.length; i++) {
            aboveThreshold[i] = aboveThreshold.length - i;
        }
        int[] aboveThresholdCopy = aboveThreshold.clone();
        sort(aboveThresholdCopy);
        System.out.println("\nBoundary case (size == threshold + 1, " + (INSERTION_SORT_THRESHOLD + 1) + "):");
        System.out.println("  After hybrid sort(): " + toStr(aboveThresholdCopy) + " sorted=" + isSorted(aboveThresholdCopy));

        // 4. Boundary case C: single-element array
        int[] single = {42};
        insertionSort(single);
        System.out.println("\nSingle-element case: " + toStr(single) + " sorted=" + isSorted(single));

        // 5. Empty / invalid cases
        int[] empty = {};
        bubbleSort(empty);
        selectionSort(empty);
        insertionSort(empty);
        System.out.println("\nEmpty array case: " + toStr(empty) + " (no exceptions thrown)");

        int[] nullArr = null;
        bubbleSort(nullArr);
        selectionSort(nullArr);
        insertionSort(nullArr);
        sort(nullArr);
        System.out.println("Null array case: handled gracefully, no exceptions thrown");

        // 6. Already-sorted and reverse-sorted inputs (edge cases for these algorithms)
        int[] alreadySorted = {1, 2, 3, 4, 5};
        bubbleSort(alreadySorted);
        System.out.println("\nAlready-sorted case: " + toStr(alreadySorted) + " sorted=" + isSorted(alreadySorted));

        int[] duplicates = {4, 2, 4, 1, 2, 4};
        selectionSort(duplicates);
        System.out.println("Duplicates case: " + toStr(duplicates) + " sorted=" + isSorted(duplicates));

        System.out.println("\nAll tests completed.");
    }

    // Helper for the demo so bubbleSort's void signature doesn't force
    // repeated cloning boilerplate in main().
    private static int[] bubbleSorted(int[] source) {
        int[] copy = source.clone();
        bubbleSort(copy);
        return copy;
    }
}
