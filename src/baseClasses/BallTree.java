package baseClasses;

import java.util.ArrayList;
import java.util.List;

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

    // Build the Ball Tree recursively
    private BallTreeNode buildTree(List<Stop> stops) {
        if (stops.size() == 1) {
            return new BallTreeNode(stops.get(0), 0, stops);
        }
        else if (stops.size() == 0) {
            System.out.println("[ERROR] No stops given, couldn't construct BallTree.");
            return null;
        }

        // Compute the center of the stops
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
            double dist = center.getDistanceToOther(stop);
            maxDist = Math.max(maxDist, dist);
        }

        // Partition the list into two groups based on proximity to the center
        List<Stop> leftGroup = new ArrayList<>();
        List<Stop> rightGroup = new ArrayList<>();
        for (Stop stop : stops) {
            if (center.getDistanceToOther(stop) <= maxDist / 2) {
                leftGroup.add(stop);
            } else {
                rightGroup.add(stop);
            }
        }

        // Create the node and recursively build left and right subtrees
        BallTreeNode node = new BallTreeNode(center, maxDist / 2, stops);
        node.left = buildTree(leftGroup);
        node.right = buildTree(rightGroup);

        return node;
    }

    // Find the closest Stop to the given point
    public Stop findNearestStop(Stop queryStop) {
        return findNearestStop(root, queryStop);
    }

    private Stop findNearestStop(BallTreeNode node, Stop queryStop) {
        if (node == null) {
            return null;
        }

        // Compute the distance to the current center
        double distToCentroid = queryStop.getDistanceToOther(node.center);
        Stop nearestStop = node.center;

        // Search the left or right subtree depending on proximity
        Stop leftNearest = findNearestStop(node.left, queryStop);
        Stop rightNearest = findNearestStop(node.right, queryStop);

        double nearestDist = nearestStop.getDistanceToOther(queryStop);

        if (leftNearest != null && leftNearest.getDistanceToOther(queryStop) < nearestDist) {
            nearestStop = leftNearest;
            nearestDist = nearestStop.getDistanceToOther(queryStop);
        }

        if (rightNearest != null && rightNearest.getDistanceToOther(queryStop) < nearestDist) {
            nearestStop = rightNearest;
        }

        return nearestStop;
    }
}
