package org.example;

import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public enum LineColor {GRAY, RED, BLUE}

    GameState gameState = new GameState();

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

    // human player can only color blue edges
    private void handleClick(Point clickPoint) {
        if (gameState.getCurrentPlayer() == Player.RED)
            return;
        Edge selectedEdge = findClosestEdge(clickPoint);
        if (selectedEdge != null && selectedEdge.getLineColor() == LineColor.GRAY) {
            selectedEdge.setLineColor(LineColor.BLUE);
            if (gameState.checkForTriangle(this, gameState.getCurrentPlayer())) {
                JOptionPane.showMessageDialog(this,
                        gameState.getCurrentPlayer() + " player wins!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
                setEnabled(false);
                return;
            } else {
                gameState.changeCurrentPlayer();
            }
            repaint();
        }
        if (edges.stream().noneMatch(e -> e.getLineColor() == LineColor.GRAY)) {
            JOptionPane.showMessageDialog(this,
                    "DRAW", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            setEnabled(false);
        }
        gameState.play(this);
        repaint();
        if (gameState.checkForTriangle(this, Player.RED)) {
            JOptionPane.showMessageDialog(this,
                    Player.RED + " player wins!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            setEnabled(false);
        }
        if (edges.stream().noneMatch(e -> e.getLineColor() == LineColor.GRAY)) {
            JOptionPane.showMessageDialog(this,
                    "DRAW", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            setEnabled(false);
        }
    }

    // Method to find the closest edge to a click point
    private Edge findClosestEdge(Point clickPoint) {
        return ClosestEdgeFinder.findClosestEdge(clickPoint, edges, pointList);
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
            g.setColor(edge.getLineColor() == LineColor.RED ? Color.RED
                    : edge.getLineColor() == LineColor.BLUE ? Color.BLUE : Color.GRAY);
            g.drawLine(pointList.get(edge.getX()).x, pointList.get(edge.getX()).y,
                    pointList.get(edge.getY()).x, pointList.get(edge.getY()).y);
        }
    }

    public void saveToPNG(String filename) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();

        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e) {
            System.out.println("Error transforming to PNG");
            System.exit(-1);
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
