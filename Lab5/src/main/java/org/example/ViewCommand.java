package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

public class ViewCommand implements Command {
    final Document document;

    ViewCommand(Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        File file = new File(document.uri);
        if (!file.exists()) {
            System.out.println("The file does not exist");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
            exit(-1);
        }
    }
}
