package structures;

import objects.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @brief BallTree class used to partition stops into a tree structure in order
 *        to optimize the search for the nearest stop by having quick access to
 *        the neighbours of a Stop.
 */
public class BallTree {
    private final BallTreeNode root;
    private final int leafSize;

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

    public BallTree(Collection<Stop> stops, int leafSize) {
        this.leafSize = leafSize;
        this.root = buildTree(stops);
    }

    /**
     * @brief Builds the Ball Tree recursively based on a list of unordered stops.
     *
     * @param stops The list of stops to be used for building the tree.
     * @return The root node of the constructed Ball Tree.
     */
    private BallTreeNode buildTree(Collection<Stop> stops) {
        if (stops == null || stops.isEmpty()) {
            System.out.println("[\033[92mINFO\033[0m] Empty stop collection received, returning null.");
            return null;
        }
        // Base case for recursion
        // If there is only one stop, create a leaf node
        if (stops.size() <= this.leafSize) {
            return new BallTreeNode(stops);
        }
        Stop[] pivots = findFarthest(stops);
        Stop pivot1 = pivots[0], pivot2 = pivots[1];

        Collection<Stop> left = new ArrayList<>();
        Collection<Stop> right = new ArrayList<>();

        for (Stop s : stops) {
            double d1 = pivot1.getDistanceToOther(s);
            double d2 = pivot2.getDistanceToOther(s);
            if (d1 < d2)
                left.add(s);
            else
                right.add(s);
        }
        Stop center = computeCenter(stops);
        double radius = computeRadius(stops, center);

        BallTreeNode node = new BallTreeNode(center, radius);
        node.left = buildTree(left);
        node.right = buildTree(right);
        return node;
    }

    /**
     * @brief Finds the two farthest stops in a collection of stops.
     * 
     * @param stops The collection of stops to search.
     * @return An array containing the two farthest stops.
     */
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
            double d = stopB.getDistanceToOther(s);
            if (d > maxDist) {
                maxDist = d;
                stopC = s;
            }
        }
        return new Stop[] { stopB, stopC };
    }

    /**
     * @brief Computes the center of a collection of stops.
     * 
     * @param stops The collection of stops to compute the center.
     * @return A Stop object representing the center of the collection.
     */
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

    /**
     * @brief Computes the radius of a collection of stops based on the center Stop
     *        of the stops collection.
     * 
     * @param stops  The collection of stops to compute the radius.
     * @param center The center of the collection.
     * @return The radius of the collection.
     */
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
     * @brief Queries the Ball Tree for stops within a given range from a query
     *        stop.
     * 
     * @param query   The query stop.
     * @param maxDist The maximum range to search for neighbours.
     * @return A collection of stops within the specified range from the query stop.
     */
    public Collection<Stop> query_neighbours(Stop query, double maxDist) {
        Collection<Stop> result = new ArrayList<>();
        knn_search(root, query, maxDist, result);
        return result;
    }

    /**
     * @brief Searches for stops within a given range from a query stop in the Ball
     *        Tree using the KNN method.
     * 
     * @param node    The current node in the Ball Tree.
     * @param q       The query stop.
     * @param maxDist The maximum range to search for neighbours.
     * @param out     The collection to store the found stops.
     */
    private void knn_search(BallTreeNode node, Stop q, double maxDist, Collection<Stop> out) {
        if (node == null) // base case of recursion
            return;

        if (node.isLeaf()) { // if leaf is node, we retrieve all the stops within the given range
            for (Stop s : node.stops) {
                if (q.getDistanceToOther(s) <= maxDist) {
                    out.add(s);
                }
            }
        } else { // we continue searching in subtrees
            double distToCenter = q.getDistanceToOther(node.center);
            if (distToCenter - node.radius > maxDist) { // if the distance to the center is greater than the radius, we
                                                        // can prune this branch
                return;
            }
            knn_search(node.left, q, maxDist, out);
            knn_search(node.right, q, maxDist, out);
        }
    }
}
