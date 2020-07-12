package io.pusteblume.nexthink;

import java.util.*;

public class Q4HashCodeContract {
    static class Animal {
        private final int id;
        private final String name;

        public Animal(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Animal)) return false;
            Animal otherAnimal = (Animal) other;
            return id == otherAnimal.id;
        }

        @Override
        public int hashCode() {
            return this.id;
        }
    }

    public static void main(String[] args) {
        Animal lion1 = new Animal(1, "lion");
        Animal lion2 = new Animal(1, "lion");
        Set<Animal> animals = new HashSet<>();
        animals.add(lion1);
        animals.add(lion2);
        System.out.println("Number of animals: " + animals.size());
    }
}