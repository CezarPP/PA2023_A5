package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import java.io.*;
import java.util.*;

import static java.lang.Math.max;

public class Catalog implements Serializable {
    List<Document> documentList;
    Graph graph;

    Catalog() {
        documentList = new LinkedList<>();
    }

    void addEntry(Document d) {
        documentList.add(d);
    }

    @JsonProperty
    public List<Document> getDocumentList() {
        return documentList;
    }

    public Document getDocument(int index) {
        return documentList.get(index);
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "documentList=" + documentList +
                '}';
    }

    void buildGraph() {
        final int N = documentList.size();
        GraphBuilder graphBuilder = GraphBuilder.numVertices(N);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                boolean connected = false;
                for (Map.Entry<String, String> entry : getDocument(i).getTags().entrySet()) {
                    if (Objects.equals(getDocument(j).getTags().get(entry.getKey()), entry.getValue())) {
                        connected = true;
                        break;
                    }
                }
                if (connected) {
                    graphBuilder.addEdge(i, j);
                }
            }
        }
        graph = graphBuilder.buildGraph();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * implements the brown coloring algorithm
     * unlike the greedy algorithm, this technique always chooses the vertex with the highest saturation degree
     * that is, the number of distinct colors that its neighbours have. If there is a tie, it chooses the one with the highest degree
     * the vertex is colors with the lowest available color
     *
     * @return -> the number of colors used by the coloring
     */
    public int brownColoringCount() {
        final int N = graph.numVertices();
        int[] colors = new int[N];
        Arrays.fill(colors, -1);

        int[] saturationDegree = new int[N];

        while (uncoloredVerticesExist(colors)) {
            int vertexToColor = selectUncoloredVertexWithHighestSaturationDegree(colors, saturationDegree, graph.degrees());
            colors[vertexToColor] = findSmallestUnusedColor(colors, vertexToColor);

            for (var it = graph.neighborIterator(vertexToColor); it.hasNext(); ) {
                int u = it.next();
                if (colors[u] == -1) {
                    saturationDegree[u]++;
                }
            }
        }
        if (Arrays.stream(colors).max().isPresent())
            return Arrays.stream(colors).max().getAsInt() + 1;
        return 0;
    }

    private static boolean uncoloredVerticesExist(int[] colors) {
        for (int color : colors) {
            if (color == -1) {
                return true;
            }
        }
        return false;
    }

    private static int selectUncoloredVertexWithHighestSaturationDegree(int[] colors, int[] saturationDegree, int[] degree) {
        int maxSaturationDegree = -1;
        int maxDegree = -1;
        int selectedVertex = -1;

        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == -1 &&
                    (saturationDegree[i] > maxSaturationDegree
                            || (saturationDegree[i] == maxSaturationDegree
                            && degree[i] > maxDegree))) {
                maxSaturationDegree = saturationDegree[i];
                maxDegree = degree[i];
                selectedVertex = i;
            }
        }

        return selectedVertex;
    }

    private int findSmallestUnusedColor(int[] colors, int vertex) {
        Set<Integer> usedColors = new HashSet<>();
        for (var it = graph.neighborIterator(vertex); it.hasNext(); ) {
            int neighbor = it.next();
            if (colors[neighbor] != -1) {
                usedColors.add(colors[neighbor]);
            }
        }

        int color = 0;
        while (usedColors.contains(color)) {
            color++;
        }

        return color;
    }

}
