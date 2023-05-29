package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Cat {
    private final String name;
    private int age;

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Cat() {
        this.name = "George";
        this.age = 2;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Test
    public static void thisIsACat() {
        System.out.println("Static test: this is a cat");
        assertTrue(true);
    }

    @Test
    public void nonStaticTestMethod() {
        System.out.println("Non static test: This is really a cat");
        assertTrue(true);
    }

    private void method() {
        System.out.println("This is a method");
    }
}
