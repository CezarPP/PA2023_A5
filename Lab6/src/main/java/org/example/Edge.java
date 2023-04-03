package org.example;

class Edge {
    private int x, y;
    private Canvas.LineColor lineColor;

    Edge() {
        lineColor = Canvas.LineColor.GRAY;
    }

    Edge(int x, int y) {
        this.x = x;
        this.y = y;
        lineColor = Canvas.LineColor.GRAY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Canvas.LineColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(Canvas.LineColor lineColor) {
        this.lineColor = lineColor;
    }
}