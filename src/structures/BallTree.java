package structures;

import objects.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @brief BallTree class used to partition stops into a tree structure in order
 *        to optimize the search for the nearest stop by having quick access to
 *        the neighbours of a Stop.
 * @details Its implementation is based on the Ball Tree Wikipedia page:
 *          https://en.wikipedia.org/wiki/Ball_tree
 *          Will be further developed in report.
 * 
 */
public class BallTree {
    private final BallTreeNode root;
    private final int leaf_size;
    private int recursiveCallCount = 0; // Add counter as class field

    // Node class that represents a node in the Ball Tree
    private class BallTreeNode {
        Stop center;
        double radius;
        Collection<Stop> stops;
        BallTreeNode left;
        BallTreeNode right;

        /**
         * @brief Constructor for a leaf.
         * 
         * @param stops
         */
        BallTreeNode(Collection<Stop> stops) {
            this.stops = stops;
        }

        /**
         * @brief Constuctor for a partitionning node.
         * 
         * @param center
         * @param radius
         */
        BallTreeNode(Stop center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        /**
         * Checks if current node is a leaf node in the ball tree.
         * 
         * @return true if node contains stops data, false if it's an internal node
         */
        boolean isLeaf() {
            return this.stops != null;
        }
    }

    public BallTree(Collection<Stop> stops, int leaf_size) {
        this.leaf_size = leaf_size;
        this.root = buildTree(stops);
    }

    /**
     * @brief Builds the Ball Tree recursively based on a list of unordered stops.
     *
     * @param stops The list of stops to be used for building the tree.
     * @return The root node of the constructed Ball Tree.
     */
    private BallTreeNode buildTree(Collection<Stop> stops) {
        // try {
        // TimeUnit.SECONDS.sleep(1);
        recursiveCallCount++; // Increment counter at start of each call
        // System.out.println("[\033[92mINFO\033[0m] Recursive call #" +
        // recursiveCallCount + " on buildTree method with " + stops.size()
        // + " stops.");
        if (stops == null || stops.isEmpty()) {
            System.out.println("[\033[92mINFO\033[0m] Empty stop collection received, returning null.");
            return null;
        }
        // Base case for recursion
        // If there is only one stop, create a leaf node
        if (stops.size() <= this.leaf_size) {
            // System.out.println("[\033[92mINFO\033[0m] Base case of tree building reached,
            // returning...");
            return new BallTreeNode(stops);
        }
        // System.out.println("[\033[92mINFO\033[0m] Searching for farthest stop.");
        Stop[] pivots = findFarthest(stops);
        Stop pivot1 = pivots[0], pivot2 = pivots[1];

        Collection<Stop> left = new ArrayList<>();
        Collection<Stop> right = new ArrayList<>();

        // Partition selon la proximité aux deux pivots
        // System.out.println("[\033[92mINFO\033[0m] Creating two subsets of stops.");
        for (Stop s : stops) {
            double d1 = pivot1.getDistanceToOther(s);
            double d2 = pivot2.getDistanceToOther(s);
            if (d1 < d2)
                left.add(s);
            else
                right.add(s);
        }
        // System.out.println("[\033[92mINFO\033[0m] Left subset size: " + left.size());
        // System.out.println("[\033[92mINFO\033[0m] Right subset size: " +
        // right.size());

        // Noeud interne avec centre et rayon
        // System.out.println("[\033[92mINFO\033[0m] Computing center.");
        Stop center = computeCenter(stops);
        // System.out.println("[\033[92mINFO\033[0m] Computing radius.");
        double radius = computeRadius(stops, center);

        BallTreeNode node = new BallTreeNode(center, radius);
        // System.out.println("[\033[92mINFO\033[0m] Left tree recursion call.");
        node.left = buildTree(left);
        // System.out.println("[\033[92mINFO\033[0m] Right tree recursion call.");
        node.right = buildTree(right);
        return node;
        // }
        // catch(InterruptedException e) {
        // System.out.println("[\033[91mERROR\033[0m] " + e);
        // return null;
        // }
    }

    private Stop[] findFarthest(Collection<Stop> stops) {
        // First pass: find farthest point from arbitrary first point
        Stop stopA = stops.iterator().next();
        Stop stopB = stopA;
        double maxDist = Double.MIN_VALUE;

        for (Stop s : stops) {
            double d = stopA.getDistanceToOther(s);
            if (d > maxDist) {
                maxDist = d;
                stopB = s;
            }
        }

        // Second pass: find farthest point from stopB
        Stop stopC = stopB;
        maxDist = Double.MIN_VALUE;
        for (Stop s : stops) {
            double d = stopB.getDistanceToOther(s); // <-- Fixed: Using stopB instead of stopA
            if (d > maxDist) {
                maxDist = d;
                stopC = s;
            }
        }
        return new Stop[] { stopB, stopC };
    }

    private Stop computeCenter(Collection<Stop> stops) {
        double sumLat = 0, sumLon = 0;
        for (Stop s : stops) {
            sumLat += s.getStopLat();
            sumLon += s.getStopLon();
        }
        double avgLat = sumLat / stops.size();
        double avgLon = sumLon / stops.size();
        return new Stop("", "", avgLat, avgLon); // creating a virtual Stop used only for partitioning
    }

    private double computeRadius(Collection<Stop> stops, Stop center) {
        double maxDist = 0;
        for (Stop s : stops) {
            double d = center.getDistanceToOther(s);
            if (d > maxDist)
                maxDist = d;
        }
        return maxDist;
    }

    /**
     * Retourne tous les stops à distance ≤ maxDist (en mètres)
     * 
     * @param query   le Stop requête
     * @param maxDist rayon maximum en mètres
     */
    public Collection<Stop> range(Stop query, double maxDist) {
        Collection<Stop> result = new ArrayList<>();
        rangeSearch(root, query, maxDist, result);
        return result;
    }

    private void rangeSearch(BallTreeNode node, Stop q, double maxDist, Collection<Stop> out) {
        if (node == null)
            return;

        if (node.isLeaf()) {
            // Dans une feuille, test direct pour chaque Stop
            for (Stop s : node.stops) {
                if (q.getDistanceToOther(s) <= maxDist) {
                    out.add(s);
                }
            }
        } else {
            // Calcul de la distance de la requête au centre du nœud
            double distToCenter = q.getDistanceToOther(node.center);
            // Si la boule est trop loin (distance au centre – rayon > maxDist), on prune
            if (distToCenter - node.radius > maxDist) {
                return;
            }
            // Sinon on descend dans les deux branches (l’une pourrait encore contenir des
            // solutions)
            rangeSearch(node.left, q, maxDist, out);
            rangeSearch(node.right, q, maxDist, out);
        }
    }

    // Add getter to access the count
    public int getRecursiveCallCount() {
        return recursiveCallCount;
    }

}
