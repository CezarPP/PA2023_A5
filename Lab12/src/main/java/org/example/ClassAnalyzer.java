package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import java.lang.annotation.Annotation;

public class ClassAnalyzer {
    public static void analyze(String classFilePath) {

        Class<?> loadedClass;
        try {
            loadedClass = loadClass(classFilePath);

            String packageName = loadedClass.getPackage().getName();
            System.out.println("Package: " + packageName);

            extractClassMethods(loadedClass);
            invokeTestMethods(loadedClass);
        } catch (Exception exception) {
            System.out.println("Failed to load class" + exception.getMessage());
            System.exit(-1);
        }
    }

    private static Class<?> loadClass(String classFilePath) {
        File file = new File(classFilePath);
        String rootPath = classFilePath.substring(0, classFilePath.indexOf("\\target\\classes") + "\\target\\classes".length());

        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (IOException exception) {
            System.out.println("Error getting url from class file path");
            System.exit(-1);
        }
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{url})) {
            String className = file.getPath()
                    .replace(".class", "")
                    .replace("\\", ".")
                    .substring(rootPath.length() + 1);

            return classLoader.loadClass(className);
        } catch (ClassNotFoundException exception) {
            System.out.println("Class not found: " + exception.getMessage());
            System.exit(-1);
        } catch (IOException exception) {
            System.out.println("Error loading class: " + exception.getMessage());
            System.exit(-1);
        }
        return null;
    }

    private static void extractClassMethods(Class<?> loadedClass) {

        String className = loadedClass.getSimpleName();
        System.out.println("Class: " + className);

        Method[] methods = loadedClass.getDeclaredMethods();
        System.out.println("Methods:");
        for (Method method : methods) {
            System.out.println("- " + method.getName());
        }
    }

    private static void invokeTestMethods(Class<?> loadedClass) throws Exception {
        Method[] methods = loadedClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation testAnnotation = method.getAnnotation(Test.class);
            if (testAnnotation != null && method.getParameterCount() == 0) {
                method.invoke(null);
            }
        }
    }
}


