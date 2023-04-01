# Lab 5

* [x] Compulsory
* [x] Homework
    * [x] Use classes for the new commands instead of functions
    * [x] Implement the list command and view command
    * [x] Report command using FreeMaker that generates html report
    * [x] Build a JAR file
* [ ] Bonus
    * [x] Info command that uses Apache Tika

```java
public class InfoCommand implements Command {
    Document document;

    InfoCommand(Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        Map<String, String> m = document.getMetadata();
        // implemented using tika library
        for (Map.Entry<String, String> entry : m.entrySet()) {
            System.out.println(entry.getKey() + ' ' + entry.getValue());
        }
    }
}
```

* [x] Draw graph from documents, connect to documents if they have a common tag. This can be done in O(N^2)

```java
class Catalog {
    // ...
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
    // ...
}
```

* [x] Get a minimal coloring

```java
class Catalog {
    // ...
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
    // ...
}
```

* [x] Generate random instances and test the implementation
```java
class Main {
    public static void main(String[] args) {
        final int CNT_DOCS = 10000;
        Catalog catalog = new Catalog();
        for (int i = 0; i < CNT_DOCS; i++) {
            Document document = Document.getRandomDocument(i);
            catalog.addEntry(document);
        }
    }
}
```
Example output:
```
Results for 1000 documents
The coloring result for brown coloring is 43 in 13.7132 ms.
The coloring result for greedy from graph4j is 43 in 16.4289 ms.
```