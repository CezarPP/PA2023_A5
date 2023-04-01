package org.example;

import java.awt.*;
import java.util.ArrayList;

public class CanvasData {
    int numberOfDots;
    int width;
    int height;
    float lineProbability;
    ArrayList<Edge> edges;

    public CanvasData(int numberOfDots, int width, int height, float lineProbability, ArrayList<Edge> edges) {
        this.numberOfDots = numberOfDots;
        this.width = width;
        this.height = height;
        this.lineProbability = lineProbability;
        this.edges = edges;
    }

    CanvasData() {

    }

    public int getNumberOfDots() {
        return numberOfDots;
    }

    public void setNumberOfDots(int numberOfDots) {
        this.numberOfDots = numberOfDots;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

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

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
}
