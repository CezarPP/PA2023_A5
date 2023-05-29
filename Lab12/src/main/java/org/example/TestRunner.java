package org.example;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class TestRunner {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private int testsRun = 0;
    private int testsPassed = 0;

    public void runTests(File file) throws IOException {
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                runTests(child);
            }
        } else if (file.getName().endsWith(".class")) {
            URL url = file.toURI().toURL();
            String className = file.getPath()
                    .replace(".class", "")
                    .replace("/", ".")
                    .replace("\\", ".");
            className = className.substring(className.indexOf("org"));
            runTestClass(className, url);
        } else if (file.getName().endsWith(".jar")) {
            runTestJar(file);
        } else if (file.getName().endsWith(".java")) {
            compileJavaFile(file);
            String classFileName = file.getName().replace(".java", ".class");
            File classFile = new File(file.getParentFile(), classFileName);
            if (classFile.exists()) {
                String rootPath = classFile.getPath().substring(0, classFile.getPath().indexOf("\\target\\classes") + "\\target\\classes".length());
                URL classUrl = new File(rootPath).toURI().toURL();
                String className = classFile.getPath()
                        .replace(".class", "")
                        .replace("/", ".")
                        .replace("\\", ".");
                className = className.substring(className.indexOf("org"));
                runTestClass(className, classUrl);
            }
        }
    }

    private void compileJavaFile(File file) {
        String filePath = file.getPath();
        compiler.run(null, null, null, filePath);
    }
    private void runTestClass(String className, URL classUrl) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{classUrl})) {
            Class<?> loadedClass = classLoader.loadClass(className);
            // if (loadedClass.isAnnotationPresent(Test.class))
            for (Method method : loadedClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    runTestMethod(loadedClass, method);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading class " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void runTestMethod(Class<?> loadedClass, Method method) {
        testsRun++;
        try {
            Object instance = null;
            // if (!method.isAnnotationPresent(Test.class))
            instance = loadedClass.getConstructor().newInstance();
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Class<?> type = parameters[i].getType();
                if (type.equals(int.class) || type.equals(Integer.class)) {
                    args[i] = 0;
                } else if (type.equals(String.class)) {
                    args[i] = "";
                } else {
                    args[i] = null;
                }
            }
            method.invoke(instance, args);
            testsPassed++;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void runTestJar(File file) throws IOException {
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace(".class", "")
                            .replace("/", ".")
                            .replace("\\", ".");
                    URL url = new URL("jar:" + file.toURI().toURL() + "!/");
                    runTestClass(className, url);
                }
            }
        }
    }

    public void printStatistics() {
        System.out.println("Tests run: " + testsRun);
        System.out.println("Tests passed: " + testsPassed);
    }
}