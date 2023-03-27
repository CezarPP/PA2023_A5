package org.example;

public class ListCommand implements Command {
    final Catalog catalog;

    ListCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        for (Document document : catalog.getDocumentList())
            System.out.println(document);
    }
}
