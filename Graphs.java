/**
 * This class opens the file specified by the user and reads in the representation of a graph.
 * It displays a menu for the user to make a choice that will either give information about
 * the loaded graph or transform it depending on the users choice.
 * Author:	    Olivia Crespo, Karen Moriguchi
 * Course:	    COMP 2100
 * Assignment:	Project 4
 * Date:	    12/6/2024
 */

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graphs {
    /**
     * @param tsp the path
     *            if all nodes have been visited
     */
    private static boolean hasPath(int[] tsp) {
        for (int i = 0; i < tsp.length; ++i) {
            if (tsp[i] == Integer.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param tsp the path
     *            Formatting the path to print with Total weight : start -> nodes - > start
     */
    private static void printTsp(int[] tsp) {
        System.out.println();
        System.out.print(tsp[tsp.length - 1] + ": ");

        for (int i = 0; i < tsp.length - 1; ++i) {
            System.out.print(tsp[i] + " -> ");
        }
        System.out.print(tsp[0]);
        System.out.println();
    }

    /**
     * Prints path for current. start -> pred -> current
     *
     * @param start   node we began with
     * @param pred    previous node
     * @param current current node
     */
    private static void path(int start, int[] pred, int current) {
        if (current == start) {
            System.out.print(current);
        } else {
            path(start, pred, pred[current]);
            System.out.print(" -> " + current);
        }
    }

    /**
     * The main method that displays the choices the user can choose from 1 to 8.
     *
     * @throws FileNotFoundException if the file cannot be found
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.print("Input File Name: ");
        String file = in.next();
        Graph graph = new Graph(file);
        String input = "";
        // prompts user for selection and executes command.
        do {
            System.out.println();
            System.out.println("""
                    1. Is Connected
                    2. Minimum Spanning Tree
                    3. Shortest Path
                    4. Is Metric
                    5. Make Metric
                    6. Traveling Salesman Problem
                    7. Approximate TSP
                    8. Quit
                    """);
            System.out.print("Make your choice (1 - 8): ");
            input = in.next();


            //calls methods based on user selection
            switch (input) {
                case "1" -> {
                    System.out.println();
                    if (graph.isConnected()) {
                        System.out.println("Graph is connected.");
                    } else {
                        System.out.println("Graph is not connected.");
                    }

                }
                case "2" -> {
                    System.out.println();
                    if (!graph.isConnected()) {
                        System.out.println("Error: Graph is not connected.");
                    } else {
                        System.out.print(graph.mst());
                    }

                }
                case "3" -> {
                    int node = 0;
                    do {
                        System.out.println();
                        System.out.print("From which node would you like to find the shortest paths (0 - " + (graph.getSize() - 1) + "): ");
                        node = in.nextInt(); // user input for starting node

                        if (node > graph.getSize() - 1) {
                            System.out.println();
                            System.out.print("Please enter legal value. . . (0 - " + (graph.getSize() - 1) + ")");
                            System.out.println();
                        } else {
                            System.out.println();
                        }

                    } while (node > graph.getSize() - 1);
                    int[][] values = graph.shortestPath(node); //storing array of the shortest paths.

                    for (int i = 0; i < graph.getSize(); ++i) {
                        if (values[0][i] != Integer.MAX_VALUE) {
                            System.out.print(i + ": (" + values[0][i] + ")\t");
                            path(node, values[1], i);
                        } else {
                            System.out.print(i + ": (Infinity)"); // Edge from node to i did not exist.
                        }
                        System.out.println();
                    }


                }
                case "4" -> {
                    System.out.println();
                    if (!graph.isComplete()) {
                        System.out.println("Graph is not metric: Graph is not completely connected.");
                    } else if (!graph.isMetric()) {
                        System.out.println("Graph is not metric: Edges do not obey the triangle inequality.");
                    } else {
                        System.out.println("Graph is metric.");
                    }

                }
                case "5" -> {
                    System.out.println();
                    if (!graph.isConnected()) {
                        System.out.print("Error: Graph is not connected.");
                    } else {
                        graph.makeMetric(); // to make graph follow triangle inequality
                        System.out.print(graph);

                    }

                }
                case "6" -> {
                    if (!graph.isConnected()) {
                        System.out.println("Error: Graph is not connected.");
                    } else {
                        int[] tsp = graph.tsp(); // obtains possible optimal path
                        if (hasPath(tsp)) { // checks values in path for unvisited nodes
                            printTsp(tsp);

                        } else {
                            System.out.println();
                            System.out.println("Error: Graph has no tour.");
                        }
                    }
                }
                case "7" -> {
                    if (!graph.isMetric()) {
                        System.out.println("Error: Graph is not metric.");
                    } else {
                        int[] tsp = graph.approxTsp();
                        if (tsp[graph.getSize()] != Integer.MAX_VALUE) { // MAXVALUE is the value that tsp path weight is set to, if a disconnection is found.
                            printTsp(tsp);

                        } else {
                            System.out.println();
                            System.out.println("Error: Graph has no tour.");
                        }
                    }
                }

            }
        } while (!input.equals("8")); // 8 is the value that quits the process
    }

}
