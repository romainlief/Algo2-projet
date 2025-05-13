package baseClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @brief BallTree class used to partition stops into a tree structure in order
 *        to optimize the search for the nearest stop by having quick access to
 *        the neighbours of a Stop.
 * @details Its implementation is based on the Ball Tree Wikipedia page:
 *          https://en.wikipedia.org/wiki/Ball_tree
 *          Will be further developed in report.
 * 
 */
class BallTree {
    private BallTreeNode root;

    // Node class that represents a node in the Ball Tree
    private class BallTreeNode {
        Stop center;
        double radius;
        List<Stop> points;
        BallTreeNode left;
        BallTreeNode right;

        BallTreeNode(Stop center, double radius, List<Stop> points) {
            this.center = center;
            this.radius = radius;
            this.points = points;
            this.left = null;
            this.right = null;
        }
    }

    public BallTree(List<Stop> stops) {
        this.root = buildTree(stops);
    }

    /**
     * @brief Builds the Ball Tree recursively based on a list of unordered stops.
     *
     * @param stops The list of stops to be used for building the tree.
     * @return The root node of the constructed Ball Tree.
     */
    private BallTreeNode buildTree(List<Stop> stops) {
        if (stops == null || stops.isEmpty()) {
            return null;
        }
        // Base case for recursion
        // If there is only one stop, create a leaf node
        if (stops.size() == 1) {
            return new BallTreeNode(stops.get(0), 0, stops);
        }

        // else
        // Compute the center of the stops
        // TODO: create tree right after reading the stops and sum the lat and lon at
        // the same time to save67k iterations
        double lat_sum = 0;
        double lon_sum = 0;
        for (Stop stop : stops) {
            lat_sum += stop.getStopLat();
            lon_sum += stop.getStopLon();
        }

        // creating a center stop for reference
        Stop center = new Stop("", "", lat_sum / stops.size(), lon_sum / stops.size());

        // Find the farthest stop to calculate the radius
        double maxDist = 0;
        for (Stop stop : stops) {
            double dist = center.getDistanceToOther(stop); // should be O(1), execution ≃ 100ns (based on a benchamrk on
                                                           // 10**9 iterations)
            maxDist = Math.max(maxDist, dist); // O(1)
        }

        // Partition the list into two groups based on proximity to the center
        List<Stop> leftGroup = new ArrayList<>();
        List<Stop> rightGroup = new ArrayList<>();
        for (Stop stop : stops) { // O(n), n being the number of stops
            if (center.getDistanceToOther(stop) <= maxDist / 2) {
                leftGroup.add(stop);
            } else {
                rightGroup.add(stop);
            }
        }

        // Create the node and recursively build left and right subtrees with n/2 stops
        // for each child
        BallTreeNode node = new BallTreeNode(center, maxDist / 2, stops);
        node.left = buildTree(leftGroup);
        node.right = buildTree(rightGroup);

        return node;
    }

    /**
     * @brief Finds the nearest stop to a given stop using knn search.
     * 
     * @param query_stop The stop to which the nearest stops are to be found.
     * @return T
     */
    public PriorityQueue<Stop> findNearestStop(Stop query_stop) {
        PriorityQueue<Stop> neighbouring_stops = new PriorityQueue<>(
                (stop1, stop2) -> Double.compare(
                        query_stop.getDistanceToOther(stop1),
                        query_stop.getDistanceToOther(stop2)));
        return findNearestStop(neighbouring_stops, root, query_stop);
    }

    private PriorityQueue<Stop> findNearestStop(PriorityQueue<Stop> neighbouring_stops, BallTreeNode node, Stop query_stop) {
        if (node == null) {
            return null;
        }
        if (query_stop.getDistanceToOther(node.center) - node.radius >= query_stop(ne))
    }
}
