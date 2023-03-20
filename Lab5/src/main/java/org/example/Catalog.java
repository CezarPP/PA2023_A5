package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Catalog implements Serializable {
    List<Document> documentList = new LinkedList<>();
    static ObjectMapper mapper = new ObjectMapper();

    void addEntry(Document d) {
        documentList.add(d);
    }

    @JsonProperty
    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    /**
     * saves the output to the file catalog.json
     */
    void save() {
        try (FileOutputStream f = new FileOutputStream("catalog.json")) {
            mapper.writeValue(f, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * loads the output from the file catalog.json
     */
    void load() {
        try (FileInputStream f = new FileInputStream("catalog.json")) {
            Catalog catalog = mapper.readValue(f, Catalog.class);
            moveCatalog(catalog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "documentList=" + documentList +
                '}';
    }

    void moveCatalog(Catalog c) {
        documentList = c.documentList;
    }
}
