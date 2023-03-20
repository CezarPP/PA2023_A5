package org.example;

public class Main {
    public static void main(String[] args) {
        Document d1 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_05.html", 1, "d1");
        Document d2 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_04.html", 2, "d2");
        Document d3 = new Document("https://profs.info.uaic.ro/~acf/java/labs/lab_03.html", 3, "d3");
        Catalog catalog = new Catalog();
        catalog.addEntry(d1);
        catalog.addEntry(d2);
        catalog.addEntry(d3);
        catalog.save();
        // catalog.load();
    }
}