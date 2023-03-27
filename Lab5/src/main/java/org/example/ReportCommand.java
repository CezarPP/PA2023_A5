package org.example;
// ReportCommand.java

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReportCommand implements Command {
    final Catalog catalog;

    public ReportCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        try {
            // Initialize FreeMarker configuration
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassForTemplateLoading(ReportCommand.class,
                    "/Reports");

            // Load FreeMarker template
            Template template = cfg.getTemplate("report.ftl");

            // Prepare data model
            Map<String, Object> data = new HashMap<>();
            // data.put("catalog", catalog);
            data.put("documents", catalog.getDocumentList());

            // Merge data model with template
            StringWriter writer = new StringWriter();

            template.process(data, writer);


            // Write merged content to file
            File file = new File("report.html");
            PrintWriter fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            fileWriter.println(writer);
            fileWriter.close();

            // Open report file using the native operating system application
            Document d = null;
            try {
                d = new Document("report.html", 1, "");
            } catch (InvalidUriException e) {
                System.out.println("The file report.html is not a valid uri" + e.getMessage());
            }
            ViewCommand viewCommand = new ViewCommand(d);
            viewCommand.execute();
        } catch (IOException | TemplateException e) {
            System.out.println("Failed to create the report: " + e.getMessage());
        }
    }
}
