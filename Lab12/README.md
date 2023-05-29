# Lab12

* [x] Compulsory
* [x] Homework
* [x] Bonus
  * [x] Compile the source code
```java
class TestRunner {
    //...
    private void compileJavaFile(File file) {
        String filePath = file.getPath();
        compiler.run(null, null, null, filePath);
    }
}
```
  * [x] Use javaAssist to inject code into method

```java
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
        } catch (Exception ignored) {//...
        }
    }
}
```

  * [x] Use javaAssist to create a new class

```java
class BytecodeModifier {
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
```

Main:
```java
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
```

Sample output:
```
Static test: this is a cat
Non static test: This is really a cat
Static test: this is a dog
Non static test: This is really a dog
Static test: this is a dog
Non static test: This is really a dog
Tests run: 6
Tests passed: 6
This line was injected by Javassist at the beginning of the method.
This is an example method.
This line was injected by Javassist at the end of the method.
Package: org.example
Class: NewClass
Methods:
- hello
Package: org.example
Class: ExampleClass
Methods:
- exampleMethod
```