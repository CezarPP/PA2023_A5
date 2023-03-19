Lab 4

* [x] Compulsory
* [x] Homework
    * [x] Class for problem
    * [x] Query for students using streams
    * [x] Use third-party library for random names
    * [x] Greedy algo for matching

* [x] Bonus
    * [x] Implement matching
    * [x] Generate big random instances
    * [x] Compare to greedy
    * [x] Min vertex cover
    * [x] Max independent set

Sample output for homework and bonus:

```java
package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Problem problem = Problem.getRandomProblem(15, 15, 20);
        problem.doMatching();
        int matchingResult = problem.getMatchingResult();
        int greedyMatchResult = problem.getGreedyMatching();
        System.out.println("The greedy matching yields a result of " + greedyMatchResult);
        System.out.println("The actual matching is " + matchingResult);

        ArrayList<Object> vertexCover = problem.getMinVertexCover();
        ArrayList<Object> independentSet = problem.getMaxIndependentSet();
        System.out.println("The minimum vertex cover is: ");
        for (Object o : vertexCover)
            System.out.println(o);
        System.out.println();
        System.out.println("The maximum independent set is: ");
        for (Object o : independentSet)
            System.out.println(o);
    }
}
```

```
The greedy matching yields a result of 9
The actual matching is 10
The minimum vertex cover is: 
Project{name='South Carolina ogres'}
Project{name='Iowa ants'}
Student{name='Michiko Gaylord Jr.'}
Student{name='Erasmo Emmerich IV'}
Student{name='Solomon Treutel'}
Student{name='Mirta Kshlerin'}
Student{name='Jami Wolff'}
Student{name='Zula Koss'}
Student{name='Dr. Christel Brown'}
Student{name='Josiah Tremblay'}

The maximum independent set is: 
Project{name='Alaska enchanters'}
Project{name='Rhode Island werewolves'}
Project{name='Arkansas geese'}
Project{name='New Hampshire worshipers'}
Project{name='Louisiana pigs'}
Project{name='Maryland cats'}
Project{name='Colorado oxen'}
Project{name='North Dakota chickens'}
Project{name='Texas conspirators'}
Project{name='Montana ants'}
Project{name='Vermont elves'}
Project{name='Maryland chickens'}
Project{name='Idaho birds'}
Student{name='Virgen Wiegand'}
Student{name='Kyung Spencer'}
Student{name='Stan Kuhn'}
Student{name='Miss Clemente Strosin'}
Student{name='Mrs. Guillermo Schowalter'}
Student{name='Opal Pfannerstill PhD'}
Student{name='Mrs. Ruthann Reilly'}
```

Time duration in ms:

```java
package org.example;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Problem problem = Problem.getRandomProblem(10000, 10000, 30000);
        problem.doMatching();
        int matchingResult = problem.getMatchingResult();
        int greedyMatchResult = problem.getGreedyMatching();
        long endTime = System.currentTimeMillis();
        System.out.println("The greedy matching yields a result of " + greedyMatchResult);
        System.out.println("The actual matching is " + matchingResult);
        long totalTime = endTime - startTime;
        System.out.println("For an instance of 20.000 nodes and 30.000 edges, the algorithm took " + totalTime + " ms");

    }
}
```

```
The greedy matching yields a result of 7771
The actual matching is 9233
For an instance of 20.000 nodes and 30.000 edges, the algorithm took 1085 ms
```