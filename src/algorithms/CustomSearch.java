package algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Name: Timothy Donkor Kyebambo
 * Student ID: 22370734
 * Assigned Component: Custom Binary Search Algorithm
 * ID Derivation Rule: Last 4 digits of Student ID (22370734) -> 0734
 *
 * M4/M5 Integration Note:
 * Adds CSV-driven key extraction on top of the original M2 logic below,
 * which is otherwise unchanged. locations.csv columns are: locationId,
 * name, area, type, latitude, longitude. Each locationId (e.g. "LOC023")
 * is reduced to its numeric suffix (23) so the existing int[]-based
 * binary search can be reused with no change to the core algorithm.
 */
public class CustomSearch {

    // Verification Variable derived from Student ID: 22370734 -> 0734
    private static final int TARGET_SEED = 734; // 0734

    // ---------------------------------------------------------------
    // Custom growable int array, built from scratch on a primitive
    // int[] backing store. No java.util.* collections are used
    // anywhere in this file.
    // ---------------------------------------------------------------
    static class IntArray {
        private int[] data;
        private int count;

        IntArray() {
            this(4); // small default capacity so growth/resizing is easy to test
        }

        IntArray(int initialCapacity) {
            if (initialCapacity < 1) {
                initialCapacity = 1;
            }
            data = new int[initialCapacity];
            count = 0;
        }

        int size() {
            return count;
        }

        int capacity() {
            return data.length;
        }

        boolean isEmpty() {
            return count == 0;
        }

        void add(int value) {
            if (count == data.length) {
                grow();
            }
            data[count] = value;
            count++;
        }

        int get(int index) {
            if (index < 0 || index >= count) {
                throw new IndexOutOfBoundsException(
                        "Index " + index + " out of bounds for size " + count);
            }
            return data[index];
        }

        // Doubling strategy, implemented manually (this is the "resize event").
        private void grow() {
            int newCapacity = data.length * 2;
            int[] newData = new int[newCapacity];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }

        // Simple insertion sort built from scratch, used so we can hand
        // the binary search a guaranteed-sorted array without touching
        // java.util.Arrays / java.util.Collections.
        void sortInPlace() {
            for (int i = 1; i < count; i++) {
                int key = data[i];
                int j = i - 1;
                while (j >= 0 && data[j] > key) {
                    data[j + 1] = data[j];
                    j--;
                }
                data[j + 1] = key;
            }
        }

        int[] toRawArray() {
            int[] result = new int[count];
            for (int i = 0; i < count; i++) {
                result[i] = data[i];
            }
            return result;
        }
    }

    // Result codes for a slightly richer search outcome than a bare index.
    static final int NOT_FOUND = -1;

    // ---------------------------------------------------------------
    // Iterative binary search over a primitive, sorted int[].
    // Returns the index of target, or NOT_FOUND (-1) if absent or
    // if the array is null/empty.
    // ---------------------------------------------------------------
    static int binarySearchIterative(int[] sortedArr, int target) {
        if (sortedArr == null || sortedArr.length == 0) {
            return NOT_FOUND;
        }

        int low = 0;
        int high = sortedArr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; // avoids overflow vs (low+high)/2
            int midValue = sortedArr[mid];

            if (midValue == target) {
                return mid;
            } else if (midValue < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return NOT_FOUND;
    }

    // Recursive counterpart, kept separate to demonstrate the algorithm
    // two ways using only primitive parameters (no collections involved).
    static int binarySearchRecursive(int[] sortedArr, int target) {
        if (sortedArr == null || sortedArr.length == 0) {
            return NOT_FOUND;
        }
        return recurse(sortedArr, target, 0, sortedArr.length - 1);
    }

    private static int recurse(int[] arr, int target, int low, int high) {
        if (low > high) {
            return NOT_FOUND;
        }
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return recurse(arr, target, mid + 1, high);
        } else {
            return recurse(arr, target, low, mid - 1);
        }
    }

    // ---------------------------------------------------------------
    // M4 CSV INTEGRATION
    // ---------------------------------------------------------------

    /**
     * Extracts the trailing numeric portion of a locationId string.
     * e.g. "LOC023" -> 23, "LOC001" -> 1
     * Returns -1 if no numeric suffix is found (defensive, mirrors the
     * NOT_FOUND convention used elsewhere in this file).
     */
    static int extractNumericSuffix(String locationId) {
        if (locationId == null || locationId.isEmpty()) {
            return NOT_FOUND;
        }
        int end = locationId.length();
        int start = end;
        while (start > 0 && Character.isDigit(locationId.charAt(start - 1))) {
            start--;
        }
        if (start == end) {
            return NOT_FOUND; // no trailing digits found
        }
        String digits = locationId.substring(start, end);
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return NOT_FOUND;
        }
    }

    /**
     * Reads locations.csv and builds an IntArray of numeric keys
     * extracted from the locationId column (assumed to be column 0).
     * Skips the header row. No java.util collections used.
     */
    static IntArray readLocationIdsFromCSV(String csvFilePath) throws IOException {
        IntArray keys = new IntArray();
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        try {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length == 0) {
                    continue;
                }
                String locationId = fields[0].trim();
                int key = extractNumericSuffix(locationId);
                if (key != NOT_FOUND) {
                    keys.add(key);
                }
            }
        } finally {
            reader.close();
        }
        return keys;
    }

    // ---------------------------------------------------------------
    // Test runner / trace evidence (main method)
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        int passCount = 0;
        int totalCount = 0;

        System.out.println("=== CustomSearch Test Evidence ===");
        System.out.println("TARGET_SEED (ID-derived) = " + TARGET_SEED);
        System.out.println();

        // -------------------------------------------------------
        // 0. M4 CSV-DRIVEN CASE (real campus data)
        // -------------------------------------------------------
        System.out.println("-- M4: CSV-driven case (locations.csv) --");
String csvPath = "data/locations.csv"; // path relative to project root, matches repo layout               
try {
            IntArray csvKeys = readLocationIdsFromCSV(csvPath);
            csvKeys.sortInPlace();
            int[] csvArr = csvKeys.toRawArray();
            System.out.println("Loaded " + csvKeys.size() + " location keys from " + csvPath);
            System.out.print("Sorted CSV keys: ");
            printArray(csvArr);

            int csvTarget = 23; // corresponds to LOC023
            totalCount++;
            int csvIdxIter = binarySearchIterative(csvArr, csvTarget);
            int csvIdxRec = binarySearchRecursive(csvArr, csvTarget);
            boolean passCsv = csvIdxIter >= 0 && csvArr[csvIdxIter] == csvTarget
                    && csvIdxIter == csvIdxRec;
            System.out.println("Search for LOC023 (key=" + csvTarget + ") -> iterative index "
                    + csvIdxIter + ", recursive index " + csvIdxRec
                    + " | " + (passCsv ? "PASS" : "FAIL"));
            if (passCsv) passCount++;
        } catch (IOException e) {
            System.out.println("Could not read " + csvPath + " (" + e.getMessage() + ").");
            System.out.println("Skipping CSV case; hardcoded cases below still run.");
        }
        System.out.println();

        // -------------------------------------------------------
        // 1. NORMAL INPUTS
        // -------------------------------------------------------
        System.out.println("-- Normal inputs --");

        IntArray normal = new IntArray();
        int[] seedValues = {45, 12, 734, 89, 3, 67, 210, 5, 99, 15, 620, 1, 8};
        for (int v : seedValues) {
            normal.add(v);
        }
        normal.sortInPlace();
        int[] normalArr = normal.toRawArray();
        System.out.print("Sorted array: ");
        printArray(normalArr);

        // 1a. Search for the ID-derived seed value, which was inserted above.
        totalCount++;
        int idx = binarySearchIterative(normalArr, TARGET_SEED);
        boolean pass1 = idx >= 0 && normalArr[idx] == TARGET_SEED;
        System.out.println("Search for TARGET_SEED (734) -> index " + idx
                + " | " + (pass1 ? "PASS" : "FAIL"));
        if (pass1) passCount++;

        // 1b. Search for a value known to exist, using the recursive version.
        totalCount++;
        int idx2 = binarySearchRecursive(normalArr, 67);
        boolean pass2 = idx2 >= 0 && normalArr[idx2] == 67;
        System.out.println("Recursive search for 67 -> index " + idx2
                + " | " + (pass2 ? "PASS" : "FAIL"));
        if (pass2) passCount++;

        System.out.println();

        // -------------------------------------------------------
        // 2. BOUNDARY CASES
        // -------------------------------------------------------
        System.out.println("-- Boundary cases --");

        // 2a. Single-element structure.
        IntArray single = new IntArray();
        single.add(TARGET_SEED);
        int[] singleArr = single.toRawArray();
        totalCount++;
        int idxSingle = binarySearchIterative(singleArr, TARGET_SEED);
        boolean passSingle = idxSingle == 0;
        System.out.println("Single-element array " + arrayToString(singleArr)
                + ", search TARGET_SEED -> index " + idxSingle
                + " | " + (passSingle ? "PASS" : "FAIL"));
        if (passSingle) passCount++;

        // 2b. Structure at capacity, forcing a resize event, then re-check contents.
        IntArray growth = new IntArray(2); // capacity 2, so 3rd add() must resize
        System.out.println("Initial capacity: " + growth.capacity());
        growth.add(10);
        growth.add(20);
        System.out.println("At capacity (" + growth.capacity() + "), size=" + growth.size());
        growth.add(30); // triggers grow()
        System.out.println("After forced resize -> new capacity: " + growth.capacity()
                + ", size=" + growth.size());
        growth.sortInPlace();
        int[] growthArr = growth.toRawArray();
        totalCount++;
        int idxGrowth = binarySearchIterative(growthArr, 30);
        boolean passGrowth = idxGrowth >= 0 && growthArr[idxGrowth] == 30;
        System.out.println("Post-resize search for 30 -> index " + idxGrowth
                + " | " + (passGrowth ? "PASS" : "FAIL"));
        if (passGrowth) passCount++;

        // 2c. Searching for the first and last elements (edge boundaries of range).
        totalCount++;
        int firstIdx = binarySearchIterative(normalArr, normalArr[0]);
        int lastIdx = binarySearchIterative(normalArr, normalArr[normalArr.length - 1]);
        boolean passEdges = firstIdx == 0 && lastIdx == normalArr.length - 1;
        System.out.println("First-element index=" + firstIdx
                + ", last-element index=" + lastIdx
                + " | " + (passEdges ? "PASS" : "FAIL"));
        if (passEdges) passCount++;

        System.out.println();

        // -------------------------------------------------------
        // 3. INVALID / EMPTY INPUTS
        // -------------------------------------------------------
        System.out.println("-- Invalid / empty inputs --");

        // 3a. Searching an empty structure.
        IntArray empty = new IntArray();
        int[] emptyArr = empty.toRawArray();
        totalCount++;
        int idxEmpty = binarySearchIterative(emptyArr, TARGET_SEED);
        boolean passEmpty = idxEmpty == NOT_FOUND;
        System.out.println("Search on empty array -> " + idxEmpty
                + " | " + (passEmpty ? "PASS" : "FAIL"));
        if (passEmpty) passCount++;

        // 3b. Searching a null reference (defensive handling).
        totalCount++;
        int idxNull = binarySearchIterative(null, TARGET_SEED);
        boolean passNull = idxNull == NOT_FOUND;
        System.out.println("Search on null array -> " + idxNull
                + " | " + (passNull ? "PASS" : "FAIL"));
        if (passNull) passCount++;

        // 3c. Searching for a value that does not exist in a populated array.
        totalCount++;
        int idxMissing = binarySearchIterative(normalArr, 9999);
        boolean passMissing = idxMissing == NOT_FOUND;
        System.out.println("Search for absent value 9999 -> " + idxMissing
                + " | " + (passMissing ? "PASS" : "FAIL"));
        if (passMissing) passCount++;

        // 3d. Accessing an out-of-bounds index on the custom structure directly.
        totalCount++;
        boolean threw = false;
        try {
            empty.get(0);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
            System.out.println("get(0) on empty structure correctly threw: "
                    + e.getMessage());
        }
        System.out.println("Out-of-bounds access handling | " + (threw ? "PASS" : "FAIL"));
        if (threw) passCount++;

        // 3e. extractNumericSuffix on malformed / missing input (M4 addition).
        totalCount++;
        int badSuffix1 = extractNumericSuffix("LOC");
        int badSuffix2 = extractNumericSuffix("");
        int badSuffix3 = extractNumericSuffix(null);
        boolean passSuffix = badSuffix1 == NOT_FOUND && badSuffix2 == NOT_FOUND && badSuffix3 == NOT_FOUND;
        System.out.println("extractNumericSuffix on malformed input (\"LOC\", \"\", null) -> "
                + badSuffix1 + ", " + badSuffix2 + ", " + badSuffix3
                + " | " + (passSuffix ? "PASS" : "FAIL"));
        if (passSuffix) passCount++;

        System.out.println();
        System.out.println("=== Summary: " + passCount + "/" + totalCount + " tests passed ===");
    }

    private static void printArray(int[] arr) {
        System.out.println(arrayToString(arr));
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}