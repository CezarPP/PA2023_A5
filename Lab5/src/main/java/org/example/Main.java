package org.example;

import javax.print.Doc;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        Document d1 = null, d2 = null, d3 = null, d4 = null;
        try {
            d1 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_05.html", 1, "d1");
            d2 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_04.html", 2, "d2");
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
        catalogManager.save();

        // catalogManager.load();
        for (Document document : catalogManager.getCatalog().getDocumentList())
            System.out.println(document);
        ViewCommand viewCommand = new ViewCommand(d4);
        viewCommand.execute(); // opens a document
        ReportCommand reportCommand = new ReportCommand(catalog);
        reportCommand.execute();
    }
}