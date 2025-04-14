/**
 * This class supports the options that are displayed in Graphs.
 * Author:	    Olivia Crespo, Karen Moriguchi
 * Course:	    COMP 2100
 * Assignment:	Project 4
 * Date:	    12/6/2024
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph {
    private int[][] matrix;
    private int size;

    /**
     * It reads in the file the user gives.
     * It finds how many nodes there are, the edges and the weight.
     * It closes the file after that.
     *
     * @param file the specified graph the user gives.
     * @throws FileNotFoundException if the file cannot be found
     */
    public Graph(String file) throws FileNotFoundException {
        Scanner fileReader = new Scanner(new File(file));
        size = fileReader.nextInt();
        matrix = new int[size][size];

        for (int i = 0; i < size; ++i) {
            int edges = fileReader.nextInt();
            int j = 0;
            while (j < edges) {
                int column = fileReader.nextInt();
                matrix[i][column] = fileReader.nextInt();
                ++j;
            }

        }
        fileReader.close();

    }

    /**
     * The constructor that is being used to print out the graph for the MST.
     *
     * @param matrix the matrix to print out
     */
    public Graph(int[][] matrix) {
        size = matrix.length;
        this.matrix = matrix;
    }

    /**
     * This method sees if the specific graph is connected or not connected.
     */

    public boolean isConnected() {
        int[] number = new int[size + 1];
        number[size] = 1;
        DFS(0, number);
        for (int i = 0; i < size; i++) {
            if (number[i] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * A recursive algorithm that uses backtracking principle.
     * @param v the value where to start
     * @param number how many times it runs
     */
    private void DFS(int v, int[] number) {
        number[v] = number[size];
        ++number[size];
        for (int u = 0; u < size; u++) {
            if (matrix[v][u] > 0 && number[u] == 0) {
                DFS(u, number);
            }
        }
    }

    /**
     * The size of the graph
     *
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * generate a String representation of an object.
     *
     * @return a String
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(size).append("\n"); // inserts total num of nodes
        // this loop will count edges and insert num of edges for each node
        for (int i = 0; i < size; ++i) {
            StringBuilder data = new StringBuilder();
            int edges = 0;
            for (int j = 0; j < size; ++j) { // counting num of edges for each nod
                if (matrix[i][j] > 0) { // greater than zero indicates a connection
                    data.append(j).append(" "); //inserts node that current is connected to
                    data.append(matrix[i][j]).append(" "); // weight of that node
                    ++edges; // counting the connection
                }
            }
            output.append(edges).append(" ").append(data).append("\n");
        }
        return String.valueOf(output);


    }

    /**
     * Prints the graph of the MST in the same format that is used fo the graph input file.
     *
     * @return the graph of the minimum spanning tree
     */
    public Graph mst() {
        int[][] mst = new int[size][size];
        boolean[] visited = new boolean[size];
        visited[0] = true;
        for (int node = 0; node < size - 1; ++node) {
            int start = -1;
            int end = -1;
            int weight = Integer.MAX_VALUE;
            for (int i = 0; i < size; ++i) {

                if (visited[i]) {
                    for (int j = 0; j < size; ++j) {
                        if (matrix[i][j] > 0 && !visited[j]) { //finding smallest NOT visited

                            if (weight > matrix[i][j]) {
                                weight = matrix[i][j];
                                start = i;
                                end = j;
                            }
                        }
                    }
                }
            }
            mst[start][end] = matrix[start][end];
            visited[end] = true;
            mst[end][start] = matrix[end][start];
        }

        return new Graph(mst);
    }

    /**
     * Prints the lengths of the shortest path from a node to another node.
     * For the starting node it prints 0 and for unreachable nodes it prints infinity.
     *
     * @param start the users choice of what node to start from
     * @return the shortest path from the starting node
     */
    public int[][] shortestPath(int start) {
        boolean[] s = new boolean[size];
        int[] distance = new int[size];
        int[] pred = new int[size];

        for (int i = 0; i < size; ++i) {
            distance[i] = Integer.MAX_VALUE; // setting all nodes to max distance/Infinity
        }
        distance[start] = 0;
        boolean done = false;
        while (!done) {
            int bestIndex = -1;
            int bestWeight = Integer.MAX_VALUE;

            for (int i = 0; i < distance.length; ++i) {
                if (!s[i] && distance[i] < bestWeight) {
                    bestIndex = i;
                    bestWeight = distance[i];
                }

            }
            if (bestIndex == -1) {
                done = true;
            } else {
                for (int i = 0; i < size; ++i) {
                    if ((matrix[bestIndex][i] > 0 && distance[i] > distance[bestIndex] + matrix[bestIndex][i])) {
                        distance[i] = distance[bestIndex] + matrix[bestIndex][i];
                        pred[i] = bestIndex;
                    }
                }
                s[bestIndex] = true;
            }
        }
        return new int[][]{distance, pred};
    }

    /**
     * tests to see if all nodes are connected to every other node (besides itself.)
     */
    public boolean isComplete() {
        for (int node = 0; node < size; ++node) { // current node
            for (int end = 0; end < size; ++end) { // end node
                if (matrix[node][end] < 1 && node != end) { // if there is an edge missing to all nodes (except itself)
                    return false;
                }
            }
        }
        return true; // all nodes are completely connected
    }

    /**
     * This method assumes graph is complete and tests for the triangle inequality.
     */
    public boolean isMetric() {
        for (int current = 0; current < size; ++current) { // current node
            for (int end = 0; end < size; ++end) { // node connected to current
                if (current != end) { // current won't be connected to itself
                    int weight = matrix[current][end]; // value of edge
                    for (int i = 0; i < size; ++i) {
                        int[][] values = shortestPath(i); // finding the shortest path from all nodes
                        if (weight > values[0][end] + values[0][current] && i != end) { // ensuring there is no better path from the direct weight obtained
                            return false; // if there is it fails triangle inequality
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method will change any connected graph to a metric graph.
     */
    public void makeMetric() {
        for (int current = 0; current < size; ++current) { //for each node
            int[][] values = shortestPath(current); // shortest path of that node to each edge
            for (int end = 0; end < size; ++end) { // the end being compared
                matrix[current][end] = values[0][end]; // changing weight to the shortest path, (current)-> end
                matrix[end][current] = values[0][end]; // undirected graph so changing for,  (end) -> current
            }
        }


    }

    /**
     * This method attempts brute force to try all possible tours starting at node 0.
     */
    public int[] tsp() {
        int[] path = new int[size + 1]; // last space is weight of path
        int[] bestPath = new int[size + 1]; // last space is weight of path
        bestPath[size] = Integer.MAX_VALUE;
        boolean[] visited = new boolean[size];
        tsp(0, path, 0, visited, bestPath);
        return bestPath;


    }

    /**
     * recursively checks path and compares the best path.
     *
     * @param current node that we are starting from
     * @param path the current path
     * @param index location in the path that we are at
     * @param visited if node is visited
     * @param bestPath current path with the lowest value
     */
    private void tsp(int current, int[] path, int index, boolean[] visited, int[] bestPath) {
        if (index == size) {
            if (current == path[0] && path[size] < bestPath[size]) {
                System.arraycopy(path, 0, bestPath, 0, bestPath.length);
            }
        } else if (!visited[current]) {
            visited[current] = true;
            path[index] = current;
            for (int i = 0; i < size; ++i) {
                if (matrix[current][i] > 0) { // has edge
                    path[size] += matrix[current][i]; // adding to current weight of path
                    tsp(i, path, index + 1, visited, bestPath); // check next
                    path[size] -= matrix[current][i];
                }
            }
            visited[current] = false;
        }
    }

    /**
     * If the graph is metric it uses the MST method finding the approximation TSP.
     *
     * @return the path
     */
    public int[] approxTsp() {
        Graph mst = this.mst(); //min span tre graph
        int[] number = new int[size + 1]; // array for DFS results
        int[] path = new int[size + 1];// path of nodes
        number[size] = 1; // DFS starts from 1
        mst.DFS(0, number);
        int totalWeight = 0;
        for (int i = 0; i < size; ++i) {
            path[number[i] - 1] = i;
        }
        boolean hasTour = true; // if every edge has a value on path
        for (int i = 0; i < size - 1; ++i) {
            int weight = matrix[path[i]][path[i + 1]];
            if (weight <= 0) {
                hasTour = false;
            }
            totalWeight += weight;
        }
        if (hasTour) {
            totalWeight += matrix[path[size - 1]][path[0]];
            path[size] = totalWeight;
        } else {
            path[size] = Integer.MAX_VALUE;
        }

        return path;
    }


}
