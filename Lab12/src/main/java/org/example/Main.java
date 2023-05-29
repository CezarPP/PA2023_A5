package org.example;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // ClassAnalyzer.analyze("C:\\Users\\Cezar\\Desktop\\Java\\Lab12\\target\\classes\\org\\example\\Cat.class");
        TestRunner testRunner = new TestRunner();
        testRunner.runTests(new File("C:\\Users\\Cezar\\Desktop\\Java\\Lab12\\target\\classes\\org\\example"));
        testRunner.printStatistics();
    }
}
