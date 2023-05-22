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
        System.out.println("Test: this is a cat");
        assertTrue(true);
    }

    private void method() {
        System.out.println("This is a method");
    }
}
