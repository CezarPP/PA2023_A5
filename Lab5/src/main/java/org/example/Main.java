package org.example;

import org.graph4j.alg.coloring.GreedyColoring;

public class Main {
    public static void main(String[] args) {
/*        Document d1 = null, d2 = null, d3 = null, d4 = null;
        try {
            d1 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_05.html", 1, "d1");
            d2 = new Document("C:\\Users\\Cezar\\Desktop\\Java\\Lab5\\src\\main\\java\\org\\example\\Command.java", 2, "d2");
            d3 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_03.html", 3, "d3");
            d4 = new Document("C:\\Users\\Cezar\\Desktop\\Java\\Lab5\\.idea\\.gitignore", 4, "d4");
        } catch (InvalidUriException exception) {
            System.out.println("Invalid URI exception " + exception.getMessage());
            exit(-1);
        }
        Catalog catalog = new Catalog();
        catalog.addEntry(d1);
        catalog.addEntry(d2);
        catalog.addEntry(d3);
        catalog.addEntry(d4);
        CatalogManager catalogManager = new CatalogManager(catalog);
        catalogManager.save();*/

        // catalogManager.load();
/*        for (Document document : catalogManager.getCatalog().getDocumentList())
            System.out.println(document);
        ViewCommand viewCommand = new ViewCommand(d4);
        viewCommand.execute(); // opens a document
        ReportCommand reportCommand = new ReportCommand(catalog);
        reportCommand.execute();*/

        final int CNT_DOCS = 1000;
        Catalog catalog = new Catalog();
        for (int i = 0; i < CNT_DOCS; i++) {
            Document document = Document.getRandomDocument(i);
            catalog.addEntry(document);
        }

        catalog.buildGraph();
        long startTime = System.nanoTime();
        int brownColoringResult = catalog.brownColoringCount();
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double elapsedTimeInMilliseconds = elapsedTime / 1_000_000.0;
        System.out.println("Results for " + CNT_DOCS + " documents");
        System.out.println("The coloring result for brown coloring is "
                + brownColoringResult + " in " + elapsedTimeInMilliseconds + " ms.");
        startTime = System.nanoTime();
        GreedyColoring greedyColoring = new GreedyColoring(catalog.getGraph());
        int graph4JResult = greedyColoring.findColoring().numUsedColors();
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        elapsedTimeInMilliseconds = elapsedTime / 1_000_000.0;
        System.out.println("The coloring result for greedy from graph4j is "
                + graph4JResult + " in " + elapsedTimeInMilliseconds + " ms.");
/*
        Map<String, String> m = d2.getMetadata();
        for (Map.Entry<String, String> entry : m.entrySet()) {
            System.out.println(entry.getKey() + ' ' + entry.getValue());
        }*/
    }
}