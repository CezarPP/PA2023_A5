package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CatalogManager {
    Catalog catalog;
    static ObjectMapper mapper = new ObjectMapper();

    CatalogManager(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * saves the output to the file catalog.json
     */
    void save() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try (FileOutputStream f = new FileOutputStream("catalog.json")) {
            mapper.writeValue(f, catalog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * loads the output from the file catalog.json
     */
    void load() {
        try (FileInputStream f = new FileInputStream("catalog.json")) {
            this.catalog = mapper.readValue(f, Catalog.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Catalog getCatalog() {
        return catalog;
    }
}
