package org.example;

import java.util.Map;

public class InfoCommand implements Command {
    Document document;

    InfoCommand(Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        Map<String, String> m = document.getMetadata();
        for (Map.Entry<String, String> entry : m.entrySet()) {
            System.out.println(entry.getKey() + ' ' + entry.getValue());
        }
    }
}
