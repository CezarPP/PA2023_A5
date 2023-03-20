package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Catalog implements Serializable {
    List<Document> documentList;

    Catalog() {
        documentList = new LinkedList<>();
    }

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

    @Override
    public String toString() {
        return "Catalog{" +
                "documentList=" + documentList +
                '}';
    }
}
