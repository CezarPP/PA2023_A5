package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;

public class Canvas extends JPanel {
    int numberOfDots, width, height;
    float lineProbability;
    Random random;
    ArrayList<Point> pointList;

    ArrayList<Pair> edges;
    final int centerX, centerY, radius;
    final int DOT_SIZE = 10;

    static class Pair {
        Point x, y;

        Pair() {
            x = new Point();
            y = new Point();
        }

        Pair(Point x, Point y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    Canvas(int numberOfDots, int width, int height, float lineProbability) {
        this(numberOfDots, width, height, lineProbability, null);
        edges = new ArrayList<>();
        for (int i = 0; i < numberOfDots; i++) {
            for (int j = i + 1; j < numberOfDots; j++) {
                if (Math.random() <= lineProbability) {
                    edges.add(new Pair(pointList.get(i), pointList.get(j)));
                }
            }
        }
    }

    Canvas(int numberOfDots, int width, int height, float lineProbability, ArrayList<Pair> edges) {
        this.numberOfDots = numberOfDots;
        this.width = width;
        this.height = height;
        this.lineProbability = lineProbability;
        random = new Random();
        pointList = new ArrayList<>();
        centerX = width / 2;
        centerY = height / 2;
        radius = min(width, height) / 2;
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
        for (Point point : pointList) {
            int x = point.x, y = point.y;
            g.fillOval(x - DOT_SIZE / 2, y - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
        }

        for (Pair edge : edges) {
            g.drawLine(edge.x.x, edge.x.y,
                    edge.y.x, edge.y.y);
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
