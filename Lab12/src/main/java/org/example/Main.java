package org.example;

public class Main {
    public static void main(String[] args) {
        ClassAnalyzer analyzerTester = new ClassAnalyzer();
        analyzerTester.run(
                "../../../target/classes/Cat.class",
                "org.example.Cat");
    }
}
