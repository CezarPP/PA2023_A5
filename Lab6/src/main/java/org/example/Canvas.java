package org.example;

import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;

public class Canvas extends JPanel {
    int numberOfDots, width, height;
    float lineProbability;
    Random random;
    ArrayList<Point> pointList;

    ArrayList<Edge> edges;
    final int centerX, centerY, radius;
    final int DOT_SIZE = 10;

    public enum Player {RED, BLUE}

    public enum LineColor {GRAY, RED, BLUE}

    // Fields and methods from the original class ...

    // Add field for the current player
    Player currentPlayer;

    static class Edge {
        // dots connected
        int x, y;
        LineColor lineColor;

        Edge() {
            lineColor = LineColor.GRAY;
        }

        Edge(int x, int y) {
            this.x = x;
            this.y = y;
            lineColor = LineColor.GRAY;
        }
    }

    Canvas(int numberOfDots, int width, int height, float lineProbability) {
        this(numberOfDots, width, height, lineProbability, null);
        edges = new ArrayList<>();
        for (int i = 0; i < numberOfDots; i++) {
            for (int j = i + 1; j < numberOfDots; j++) {
                if (Math.random() <= lineProbability) {
                    edges.add(new Edge(i, j));
                }
            }
        }

        // init game
        currentPlayer = Player.RED;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getPoint());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void handleClick(Point clickPoint) {
        Edge selectedEdge = findClosestEdge(clickPoint);
        if (selectedEdge != null && selectedEdge.lineColor == LineColor.GRAY) {
            selectedEdge.lineColor = currentPlayer == Player.RED ? LineColor.RED : LineColor.BLUE;
            if (checkForTriangle(currentPlayer)) {
                JOptionPane.showMessageDialog(this,
                        currentPlayer + " player wins!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                setEnabled(false);
            } else {
                currentPlayer = currentPlayer == Player.RED ? Player.BLUE : Player.RED;
            }
            repaint();
        }
    }

    // Method to find the closest edge to a click point
    private Edge findClosestEdge(Point clickPoint) {
        return ClosestEdgeFinder.findClosestEdge(clickPoint, edges, pointList);
    }

    // Method to check if the current player has formed a triangle
    private boolean checkForTriangle(Player player) {
        GraphBuilder graphBuilder = GraphBuilder.numVertices(numberOfDots);
        for (Edge edge : edges)
            if (edge.lineColor.toString().equals(player.toString())) {
                graphBuilder.addEdge(edge.x, edge.y);
            }
        Graph graphForTriangles = graphBuilder.buildGraph();
        return TriangleCounter.countTriangles(graphForTriangles.adjacencyMatrix()) > 0;
    }

    Canvas(int numberOfDots, int width, int height, float lineProbability, ArrayList<Edge> edges) {
        this.numberOfDots = numberOfDots;
        this.width = width;
        this.height = height;
        this.lineProbability = lineProbability;
        random = new Random();
        pointList = new ArrayList<>();
        centerX = width / 2;
        centerY = height / 2;
        radius = min(width, height) / 2 - DOT_SIZE;
        for (int i = 0; i < numberOfDots; i++) {
            double angle = 2 * Math.PI * i / numberOfDots;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            pointList.add(new Point(x, y));
        }
        this.edges = edges;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2)); // sets line thickness

        for (Point point : pointList) {
            int x = point.x, y = point.y;
            g.fillOval(x - DOT_SIZE / 2, y - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
        }

        for (Edge edge : edges) {
            g.setColor(edge.lineColor == LineColor.RED ? Color.RED
                    : edge.lineColor == LineColor.BLUE ? Color.BLUE : Color.GRAY);
            g.drawLine(pointList.get(edge.x).x, pointList.get(edge.x).y,
                    pointList.get(edge.y).x, pointList.get(edge.y).y);
        }
    }

    public int getNumberOfDots() {
        return numberOfDots;
    }

    public void setNumberOfDots(int numberOfDots) {
        this.numberOfDots = numberOfDots;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getLineProbability() {
        return lineProbability;
    }

    public void setLineProbability(float lineProbability) {
        this.lineProbability = lineProbability;
    }

}
