package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassAnalyzer {

    public void run(String classPath, String className) {
        try {
            File file = new File(classPath);

            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader loader = new URLClassLoader(urls);

            // Load in the class
            Class<?> cls = loader.loadClass(className);

            Package pkg = cls.getPackage();
            if (pkg != null) {
                String packageName = pkg.getName();
                System.out.println("Package: " + packageName);
            } else {
                System.out.println("No package for the class " + cls.getName());
            }

            System.out.println("The methods are:");
            for (Method method : cls.getDeclaredMethods()) {
                System.out.println(method);
            }
            System.out.println("The fields are:");
            for (Field field : cls.getDeclaredFields()) {
                System.out.println(field);
            }

            // Find and invoke methods with @Test annotation
            for (Method method : cls.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    method.setAccessible(true);

                    if (java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                        method.invoke(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

