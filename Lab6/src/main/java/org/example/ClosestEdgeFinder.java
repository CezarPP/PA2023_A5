package org.example;

import java.awt.Point;
import java.util.List;

public class ClosestEdgeFinder {
    public static Edge findClosestEdge(Point p, List<Edge> edges, List<Point> pointList) {
        double minDistance = Double.MAX_VALUE;
        Edge closestEdge = null;

        for (Edge edge : edges) {
            double distance = pointToLineDistance(p, pointList.get(edge.x), pointList.get(edge.y));
            if (distance < minDistance) {
                minDistance = distance;
                closestEdge = edge;
            }
        }
        return closestEdge;
    }

    private static double pointToLineDistance(Point p, Point a, Point b) {
        double lineLength = distanceBetweenPoints(a, b);
        if (lineLength == 0) {
            return distanceBetweenPoints(p, a);
        }

        double t = ((p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y)) / (lineLength * lineLength);
        t = Math.max(0, Math.min(1, t));
        Point projection = new Point((int) (a.x + t * (b.x - a.x)), (int) (a.y + t * (b.y - a.y)));
        return distanceBetweenPoints(p, projection);
    }

    private static double distanceBetweenPoints(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }
}
