package org.example;

import javassist.*;

import java.net.URL;
import java.net.URLClassLoader;

public class BytecodeModifier {
    public static void modifyClass(String className, String methodName) {
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get(className);

            CtMethod method = cc.getDeclaredMethod(methodName);

            method.insertBefore("{ System.out.println(\"This line was injected by Javassist at the beginning of the method.\"); }");
            method.insertAfter("{ System.out.println(\"This line was injected by Javassist at the end of the method.\"); }");

            URLClassLoader classLoader = new URLClassLoader(new URL[0], BytecodeModifier.class.getClassLoader());

            Class<?> clazz = cc.toClass(classLoader, null);
            ClassAnalyzer.invokeMethods(clazz);
            cc.detach();
        } catch (NotFoundException | CannotCompileException exception) {
            System.out.println("Error modifying bytecode " + exception.getMessage());
            System.out.println("Cause: " + exception.getCause());
            exception.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createClass(String className) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.makeClass(className);

            CtMethod method = CtNewMethod.make("public void hello() { System.out.println(\"Hello, world!\"); }", cc);

            cc.addMethod(method);

            cc.toClass();
            cc.detach();
        } catch (CannotCompileException exception) {
            System.out.println("Exception: " + exception.getMessage());
            exception.getStackTrace();
            System.exit(-1);
        }
    }
}
