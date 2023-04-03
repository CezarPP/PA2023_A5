package org.example;

import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.jetbrains.annotations.NotNull;

public class GameState {

    Player currentPlayer = Player.RED;

    Graph getGraphWithEdgesOfPlayer(@NotNull Canvas canvas, Player player) {
        GraphBuilder graphBuilder = GraphBuilder.numVertices(canvas.getNumberOfDots());
        for (Edge edge : canvas.edges)
            if (edge.getLineColor().toString().equals(player.toString())) {
                graphBuilder.addEdge(edge.getX(), edge.getY());
            }
        return graphBuilder.buildGraph();
    }

    boolean checkForTriangle(Canvas canvas, Player player) {
        Graph graphForTriangles = getGraphWithEdgesOfPlayer(canvas, player);
        return TriangleCounter.countTriangles(graphForTriangles.adjacencyMatrix()) > 0;
    }


    // AI plays RED, so position is evaluated from its perspective
    // a positive score is good for the AI, a negative one is bad
    int evaluatePosition(Canvas canvas, Player playerToMove) {
        Graph graphWithEdgesOfPlayer = getGraphWithEdgesOfPlayer(canvas, playerToMove);
        int mxPotential = -100;
        for (Edge edge : canvas.edges)
            if (edge.getLineColor() == Canvas.LineColor.GRAY) {
                graphWithEdgesOfPlayer.addEdge(edge.getX(), edge.getY());
                int playerPotentialTriangles =
                        TriangleCounter.countTriangles(graphWithEdgesOfPlayer.adjacencyMatrix());
                mxPotential = Math.max(mxPotential, playerPotentialTriangles);
                graphWithEdgesOfPlayer.removeEdge(edge.getX(), edge.getY());
            }
        if (playerToMove == Player.RED)
            return mxPotential * 100;
        else
            return mxPotential * (-100);
    }

    void play(Canvas canvas) {
        int mxScore = -1000000;
        Edge edgeToColor = null;
        for (Edge edge : canvas.edges)
            if (edge.getLineColor() == Canvas.LineColor.GRAY) {
                edge.setLineColor(Canvas.LineColor.RED);
                int crtScore = evaluatePosition(canvas, Player.BLUE);
                if (crtScore > mxScore) {
                    mxScore = crtScore;
                    edgeToColor = edge;
                }
                edge.setLineColor(Canvas.LineColor.GRAY);
            }
        assert(edgeToColor != null);
        edgeToColor.setLineColor(Canvas.LineColor.RED);
        changeCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void changeCurrentPlayer() {
        currentPlayer = currentPlayer == Player.RED ? Player.BLUE : Player.RED;
    }
}
