/*
 * Name: Daniella Kalevor
 * Student ID: 22405426
 * Assigned Component: CampusGraph.java
 * ID Derivation Rule: Dijkstra traffic penalty multiplier = last 2 digits (26) / 10 = 2.6
 */
package algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CampusGraph {

    // ---------------------------------------------------------------
    // Index-derived parameter (Daniella, 22405426)
    // ---------------------------------------------------------------
    static final double TRAFFIC_PENALTY_MULTIPLIER = 2.6;
    static final double CONGESTION_THRESHOLD = 2.5; // roadConditionWeight >= this = penalized

    // ---------------------------------------------------------------
    // Node data -- simple record class, not a collection
    // ---------------------------------------------------------------
    static class Location {
        String locationId, name, area, type;
        double latitude, longitude;

        Location(String locationId, String name, String area, String type,
                  double latitude, double longitude) {
            this.locationId = locationId;
            this.name = name;
            this.area = area;
            this.type = type;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    // ---------------------------------------------------------------
    // Graph state
    // ---------------------------------------------------------------
    int numLocations;
    Location[] locations;          // locations[i] = data for index i
    double[][] adjMatrix;          // adjMatrix[i][j] = weight, or NO_EDGE

    // Adjacency list, hand-rolled with plain arrays instead of a linked
    // list class: adjTo[i] / adjWeight[i] hold node i's neighbors,
    // adjDegree[i] tracks how many are currently filled. Arrays grow by
    // doubling when full
    int[][] adjTo;
    double[][] adjWeight;
    int[] adjDegree;

    static final double NO_EDGE = Double.MAX_VALUE;
    static final int INITIAL_DEGREE_CAPACITY = 4;


    void loadLocations(String filePath) throws IOException {
        // Manual growable array of Location -- avoids needing any
        // external list class for a step that just reads a CSV.
        Location[] temp = new Location[16];
        int count = 0;

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine(); // skip header
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            String[] parts = line.split(",");
            String locationId = parts[0].trim();
            String name = parts[1].trim();
            String area = parts[2].trim();
            String type = parts[3].trim();
            double lat = Double.parseDouble(parts[4].trim());
            double lon = Double.parseDouble(parts[5].trim());

            if (count == temp.length) {
                temp = growArray(temp, temp.length * 2);
            }
            temp[count++] = new Location(locationId, name, area, type, lat, lon);
        }
        br.close();

        numLocations = count;
        locations = new Location[numLocations];
        System.arraycopy(temp, 0, locations, 0, numLocations);

        // Initialize adjacency structures now that numLocations is known
        adjMatrix = new double[numLocations][numLocations];
        for (double[] row : adjMatrix) {
            for (int j = 0; j < row.length; j++) row[j] = NO_EDGE;
        }
        adjTo = new int[numLocations][INITIAL_DEGREE_CAPACITY];
        adjWeight = new double[numLocations][INITIAL_DEGREE_CAPACITY];
        adjDegree = new int[numLocations];
    }

    void loadRoads(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine(); // skip header
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            String[] parts = line.split(",");
            String fromId = parts[0].trim();
            String toId = parts[1].trim();
            double distance = Double.parseDouble(parts[2].trim());
            double roadConditionWeight = Double.parseDouble(parts[4].trim());

            int from = indexOf(fromId);
            int to = indexOf(toId);
            if (from == -1 || to == -1) {
                System.err.println("Skipping road with unknown location: " + line);
                continue;
            }

            double weight = computeEdgeWeight(distance, roadConditionWeight);

            adjMatrix[from][to] = weight;
            adjMatrix[to][from] = weight;
            addToAdjList(from, to, weight);
            addToAdjList(to, from, weight);
        }
        br.close();
    }

    // ---------------------------------------------------------------
    // locationId -> index lookup, self-contained (linear scan).
    // At <= 51 locations this is trivially fast; no hash table needed
    // or depended on.
    // ---------------------------------------------------------------
    int indexOf(String locationId) {
        for (int i = 0; i < numLocations; i++) {
            if (locations[i].locationId.equals(locationId)) return i;
        }
        return -1;
    }

    // ---------------------------------------------------------------
    // Locked penalty-multiplier formula
    // ---------------------------------------------------------------
    double computeEdgeWeight(double distance, double roadConditionWeight) {
        double weight = distance * roadConditionWeight;
        if (roadConditionWeight >= CONGESTION_THRESHOLD) {
            weight *= TRAFFIC_PENALTY_MULTIPLIER;
        }
        return weight;
    }

    // ---------------------------------------------------------------
    // Hand-rolled growable adjacency-list append
    // ---------------------------------------------------------------
    void addToAdjList(int from, int to, double weight) {
        if (adjDegree[from] == adjTo[from].length) {
            adjTo[from] = growArray(adjTo[from], adjTo[from].length * 2);
            adjWeight[from] = growArray(adjWeight[from], adjWeight[from].length * 2);
        }
        adjTo[from][adjDegree[from]] = to;
        adjWeight[from][adjDegree[from]] = weight;
        adjDegree[from]++;
    }

    int[] growArray(int[] arr, int newSize) {
        int[] bigger = new int[newSize];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }

    double[] growArray(double[] arr, int newSize) {
        double[] bigger = new double[newSize];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }

    Location[] growArray(Location[] arr, int newSize) {
        Location[] bigger = new Location[newSize];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }

    // =================================================================
    // Sanity check / printAdjacency
    // =================================================================
    void printAdjacency(String locationId) {
        int idx = indexOf(locationId);
        if (idx == -1) {
            System.out.println(locationId + " not found.");
            return;
        }
        System.out.println(locationId + " (" + locations[idx].name + ") connects to:");
        for (int i = 0; i < adjDegree[idx]; i++) {
            int neighbor = adjTo[idx][i];
            System.out.printf("  -> %s  weight=%.2f%n",
                locations[neighbor].locationId, adjWeight[idx][i]);
        }
    }

    // =================================================================
    // BFS
    // =================================================================
    String[] bfs(String startId) {
        int startIdx = indexOf(startId);
        if (startIdx == -1) {
            System.out.println(startId + " not found.");
            return new String[0];
        }

        boolean[] visited = new boolean[numLocations];
        int[] queue = new int[numLocations];
        int front = 0, rear = 0; // [front, rear) is the live window

        String[] visitOrder = new String[numLocations];
        int visitCount = 0;

        queue[rear++] = startIdx;
        visited[startIdx] = true;

        System.out.println("BFS from " + startId);
        int step = 1;
        while (front < rear) {
            int current = queue[front++];
            visitOrder[visitCount++] = locations[current].locationId;

            for (int i = 0; i < adjDegree[current]; i++) {
                int neighbor = adjTo[current][i];
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue[rear++] = neighbor;
                }
            }

            System.out.printf("Step %d: dequeued %s, queue now = %s%n",
                step++, locations[current].locationId, sliceAsIds(queue, front, rear));
        }

        String[] result = new String[visitCount];
        System.arraycopy(visitOrder, 0, result, 0, visitCount);
        return result;
    }

    // =================================================================
    // DFS
    // =================================================================
    String[] dfs(String startId) {
        int startIdx = indexOf(startId);
        if (startIdx == -1) {
            System.out.println(startId + " not found.");
            return new String[0];
        }

        boolean[] visited = new boolean[numLocations];
        int[] stack = new int[numLocations];
        int top = -1; // stack[0 ... top] is the live window

        String[] visitOrder = new String[numLocations];
        int visitCount = 0;

        stack[++top] = startIdx;

        System.out.println("DFS from " + startId);
        int step = 1;
        while (top >= 0) {
            int current = stack[top--];
            if (visited[current]) continue;

            visited[current] = true;
            visitOrder[visitCount++] = locations[current].locationId;
            System.out.printf("Step %d: visited %s%n", step++, locations[current].locationId);

            for (int i = 0; i < adjDegree[current]; i++) {
                int neighbor = adjTo[current][i];
                if (!visited[neighbor]) {
                    stack[++top] = neighbor;
                }
            }
            System.out.println("  stack now = " + sliceAsIds(stack, 0, top + 1));
        }

        String[] result = new String[visitCount];
        System.arraycopy(visitOrder, 0, result, 0, visitCount);
        return result;
    }

    // helper: print a slice of an index array as location IDs
    String sliceAsIds(int[] arr, int fromInclusive, int toExclusive) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = fromInclusive; i < toExclusive; i++) {
            if (i > fromInclusive) sb.append(", ");
            sb.append(locations[arr[i]].locationId);
        }
        return sb.append("]").toString();
    }

    // =================================================================
    // Dijkstra
    // =================================================================
    static class DijkstraResult {
        double[] dist;
        int[] predecessor;
    }

    DijkstraResult dijkstra(String startId) {
        int startIdx = indexOf(startId);
        double[] dist = new double[numLocations];
        int[] predecessor = new int[numLocations];
        boolean[] finalized = new boolean[numLocations];

        for (int i = 0; i < numLocations; i++) {
            dist[i] = NO_EDGE;
            predecessor[i] = -1;
        }
        dist[startIdx] = 0;

        System.out.println("Dijkstra from " + startId);
        for (int iteration = 0; iteration < numLocations; iteration++) {
            int u = -1;
            double best = NO_EDGE;
            for (int i = 0; i < numLocations; i++) {
                if (!finalized[i] && dist[i] < best) {
                    best = dist[i];
                    u = i;
                }
            }
            if (u == -1) break; // remaining nodes are unreachable

            finalized[u] = true;
            System.out.printf("Extract-min: %s (dist=%.2f)%n", locations[u].locationId, dist[u]);

            for (int i = 0; i < adjDegree[u]; i++) {
                int v = adjTo[u][i];
                double newDist = dist[u] + adjWeight[u][i];
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    predecessor[v] = u;
                    System.out.printf("  relax %s -> dist=%.2f, pred=%s%n",
                        locations[v].locationId, newDist, locations[u].locationId);
                }
            }
        }

        DijkstraResult result = new DijkstraResult();
        result.dist = dist;
        result.predecessor = predecessor;
        return result;
    }

    // =================================================================
    // Prim's MST
    // =================================================================
    void primMST(String startId) {
        int startIdx = indexOf(startId);
        double[] key = new double[numLocations];
        int[] parent = new int[numLocations];
        boolean[] inMST = new boolean[numLocations];

        for (int i = 0; i < numLocations; i++) {
            key[i] = NO_EDGE;
            parent[i] = -1;
        }
        key[startIdx] = 0;

        double totalWeight = 0;
        System.out.println("Prim's MST from " + startId);

        for (int iteration = 0; iteration < numLocations; iteration++) {
            int u = -1;
            double best = NO_EDGE;
            for (int i = 0; i < numLocations; i++) {
                if (!inMST[i] && key[i] < best) {
                    best = key[i];
                    u = i;
                }
            }
            if (u == -1) break; // remaining nodes unreachable from this start

            inMST[u] = true;
            if (parent[u] != -1) {
                totalWeight += key[u];
                System.out.printf("Add edge %s -- %s (weight=%.2f)%n",
                    locations[parent[u]].locationId, locations[u].locationId, key[u]);
            }

            for (int i = 0; i < adjDegree[u]; i++) {
                int v = adjTo[u][i];
                double weight = adjWeight[u][i];
                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parent[v] = u;
                }
            }
        }
        System.out.printf("Total MST weight = %.2f%n", totalWeight);
    }

    // =================================================================
    // Disjoint set for Kruskal's -- kept as a private nested class
    // =================================================================
    static class DisjointSet {
        int[] parent;
        int[] rank;

        DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    // =================================================================
    // Kruskal's MST -- builds a flat edge list from the adjacency
    // list (each undirected edge counted once), sorts it with a
    // hand-written insertion sort, then unions via the disjoint set above.
    // =================================================================
    void kruskalMST() {
        // Build a flat edge list, each undirected edge counted once
        // (only keep it when from < to, since adjacency is mirrored).
        int maxEdges = 0;
        for (int i = 0; i < numLocations; i++) maxEdges += adjDegree[i];

        int[] edgeFrom = new int[maxEdges];
        int[] edgeTo = new int[maxEdges];
        double[] edgeWeight = new double[maxEdges];
        int edgeCount = 0;

        for (int u = 0; u < numLocations; u++) {
            for (int i = 0; i < adjDegree[u]; i++) {
                int v = adjTo[u][i];
                if (u < v) {
                    edgeFrom[edgeCount] = u;
                    edgeTo[edgeCount] = v;
                    edgeWeight[edgeCount] = adjWeight[u][i];
                    edgeCount++;
                }
            }
        }

        // Insertion sort by weight, ascending
        for (int i = 1; i < edgeCount; i++) {
            int fu = edgeFrom[i], tu = edgeTo[i];
            double wu = edgeWeight[i];
            int j = i - 1;
            while (j >= 0 && edgeWeight[j] > wu) {
                edgeFrom[j + 1] = edgeFrom[j];
                edgeTo[j + 1] = edgeTo[j];
                edgeWeight[j + 1] = edgeWeight[j];
                j--;
            }
            edgeFrom[j + 1] = fu;
            edgeTo[j + 1] = tu;
            edgeWeight[j + 1] = wu;
        }

        DisjointSet ds = new DisjointSet(numLocations);
        double totalWeight = 0;
        System.out.println("Kruskal's MST");

        for (int i = 0; i < edgeCount; i++) {
            int u = edgeFrom[i], v = edgeTo[i];
            double w = edgeWeight[i];
            if (ds.union(u, v)) {
                totalWeight += w;
                System.out.printf("Add edge %s -- %s (weight=%.2f)%n",
                    locations[u].locationId, locations[v].locationId, w);
            }
        }
        System.out.printf("Total MST weight = %.2f%n", totalWeight);
    }

    // =================================================================
    // Local test method
    // =================================================================
    public static void main(String[] args) throws IOException {
        CampusGraph graph = new CampusGraph();
        graph.loadLocations("data/locations.csv");
        graph.loadRoads("data/roads.csv");

        System.out.println("=== Adjacency sanity check ===");
        graph.printAdjacency("LOC001");
        graph.printAdjacency("LOC005"); // has a penalized (rcw >= 2.5) neighbor

        System.out.println("\n=== BFS ===");
        graph.bfs("LOC001");

        System.out.println("\n=== DFS ===");
        graph.dfs("LOC001");

        System.out.println("\n=== Dijkstra ===");
        graph.dijkstra("LOC001");

        System.out.println("\n=== Prim's MST ===");
        graph.primMST("LOC001");

        System.out.println("\n=== Kruskal's MST ===");
        graph.kruskalMST();

        // Boundary case: unknown location should fail gracefully, not crash
        System.out.println("\n=== Invalid input test ===");
        graph.printAdjacency("LOC999");
        graph.bfs("LOC999");
    }
}
