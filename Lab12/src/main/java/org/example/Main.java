package org.example;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.runTests(new File("C:\\Users\\Cezar\\Desktop\\Java\\Lab12\\target\\classes\\org\\example"));
        testRunner.printStatistics();
        BytecodeModifier
                .modifyClass("org.example.ExampleClass", "exampleMethod");
        BytecodeModifier
                .createClass("org.example.NewClass");
        ClassAnalyzer
                .analyze("org.example.NewClass");
        ClassAnalyzer
                .analyze("C:\\Users\\Cezar\\Desktop\\Java\\Lab12\\target\\classes\\org\\example\\ExampleClass.class");
    }
}
