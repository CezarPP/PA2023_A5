package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class Document implements Serializable {
    // can be a path or a link
    String URI;
    int id;
    String name;
    Map<String, String> tags;


    boolean isValidURI(String uri) {
        try {
            new URL(uri);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @JsonCreator
    Document(String uri, int id, String name, Map<String, String> tags) {
        setURI(uri);
        setId(id);
        setName(name);
        setTags(tags);
    }

    Document(String uri, int id, String name) {
        setURI(uri);
        setId(id);
        setName(name);
        tags = new TreeMap<>();
    }

    @Override
    public String toString() {
        return "Document{" +
                "URI='" + URI + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }

    @JsonProperty
    public String getURI() {
        return URI;
    }

    public void setURI(String uri) {
        if (!isValidURI(uri)) {
            System.out.println("Not a valid URI");
            System.exit(-1);
        }
        this.URI = uri;
    }

    @JsonProperty

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
