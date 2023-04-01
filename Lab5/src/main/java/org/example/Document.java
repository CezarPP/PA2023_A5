package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.ParseContext;

import java.io.*;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Document implements Serializable {
    // can be a path or a link
    String uri;
    int id;
    String name;
    Map<String, String> tags;

    static Faker faker = new Faker();

    static Document getRandomDocument(int id) {
        String uri = "/" + faker.name().name();
        String name = faker.name().title();
        Map<String, String> tags = new HashMap<>();
        tags.put("Content-Type", faker.file().extension());

        try {
            return new Document(uri, id, name, tags);
        }
        catch (InvalidUriException exception) {
            System.out.println("Generated an invalid URI");
            System.exit(-1);
        }
        return null;
    }


    boolean isValidUri(String uri) {
        try {
            new URL(uri);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if given path is valid
     *
     * @param uri -> path
     * @return -> true if is valid path
     */
    boolean isValidPath(String uri) {
        try {
            Paths.get(uri);
        } catch (InvalidPathException | NullPointerException exception) {
            return false;
        }
        return true;
    }

    @JsonCreator
    Document(@JsonProperty("uri") String uri, @JsonProperty("id") int id,
             @JsonProperty("name") String name, @JsonProperty("tags") Map<String, String> tags) throws InvalidUriException {
        setUri(uri);
        setId(id);
        setName(name);
        setTags(tags);
    }

    Document(@JsonProperty("uri") String uri, @JsonProperty("id") int id,
             @JsonProperty("name") String name) throws InvalidUriException {
        setUri(uri);
        setId(id);
        setName(name);
        tags = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Document{" +
                "URI='" + uri + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }

    @JsonProperty
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) throws InvalidUriException {
        if (!isValidUri(uri) && !isValidPath(uri)) {
            throw new InvalidUriException("The path or url provided is invalid");
        }
        this.uri = uri;
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

    public Map<String, String> getMetadata() {
        Map<String, String> metadata = new HashMap<>();
        Metadata md = new Metadata();
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        FileInputStream inputStream;
        ParseContext context = new ParseContext();
        try {
            File file = new File(uri);
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            return null;
        }
        try {
            parser.parse(inputStream, handler, md, context);
        } catch (Exception e) {
            System.out.println("Error occurred while parsing");
            return null;
        }
        for (String name : md.names()) {
            metadata.put(name, md.get(name));
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Failed to close input stream");
        }
        return metadata;
    }
}
