Lab7

* [x] Compulsory
  * [x] Object-oriented model of the problem -> Classes: ```Robot, Supervisor, Map, Cell, SharedMemory```
  * [x] Each robot has a name, can visit unvisited cells and place tokens in them
  * [x] Synchronized function for visiting
```java
public class Main {
    static final int SIZE_OF_MATRIX = 10;

    public static void main(String[] args) {

        Supervisor supervisor = new Supervisor();
        Map map = new Map(SIZE_OF_MATRIX);
        SharedMemory sharedMemory = new SharedMemory(SIZE_OF_MATRIX);
        Random random = new Random();
        for (int i = 0; i < 10; i++)
            supervisor.addRobot(new Robot("", random.nextInt(SIZE_OF_MATRIX), random.nextInt(SIZE_OF_MATRIX), map, sharedMemory));
        supervisor.startAll();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        supervisor.pauseAll();
        for (int i = 0; i < SIZE_OF_MATRIX; i++) {
            for (int j = 0; j < SIZE_OF_MATRIX; j++)
                System.out.print(map.getCell(i, j).isVisited() + " ");
            System.out.println();
        }
    }
}
```
* [x] Homework

Model interaction
```
Enter command (start, pause, start_specific, pause_specific, quit): 
pause_specific
Enter robot index: 
1
Enter command (start, pause, start_specific, pause_specific, quit): 
start_specific
Enter robot index: 
1
Enter command (start, pause, start_specific, pause_specific, quit): 
pause
Pausing all
Enter command (start, pause, start_specific, pause_specific, quit): 
start
Starting all
Enter command (start, pause, start_specific, pause_specific, quit): 
Time limit exceeded. Stopping exploration.
Robot 0 placed 3500 tokens.
Robot 1 placed 5000 tokens.
Robot 2 placed 19000 tokens.
Robot 3 placed 8400 tokens.
Robot 4 placed 1300 tokens.
Robot 5 placed 23200 tokens.
Robot 6 placed 8600 tokens.
Robot 7 placed 5600 tokens.
Robot 8 placed 5500 tokens.
Robot 9 placed 21800 tokens.
```

* Robots are exploring the map with a dfs traversal
* TimeKeeper prints its output to a file to not interfere with console commands
```java
class TimeKeeper extends Thread {
    private final long timeLimit;
    private final ExplorationStatus explorationStatus;

    public TimeKeeper(long timeLimit, ExplorationStatus explorationStatus) {
        this.timeLimit = timeLimit;
        this.explorationStatus = explorationStatus;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        try (PrintWriter writer = new PrintWriter(new FileWriter("time_keeper_output.txt"))) {
            while (System.currentTimeMillis() - startTime < timeLimit) {
                long currentTime = System.currentTimeMillis() - startTime;
                writer.printf("Elapsed time: %d ms%n", currentTime);
                writer.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("Time limit exceeded. Stopping exploration.");
            explorationStatus.setExplorationComplete(true);
        } catch (IOException e) {
            System.out.println("Error writing to time_keeper_output.txt");
            e.printStackTrace();
        }
    }
}
```

* Supervisor determines how many tokens each robot has placed
```java
class Supervisor {
    // ...
    public void displayNumberOfPlacedTokens(Map map) {
        int[] tokenCounts = new int[getRobots().size()];
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Cell cell = map.getCell(i, j);
                if (cell.isVisited()) {
                    for (Robot robot : getRobots()) {
                        if (robot.getName().equals(cell.getWasVisitedBy())) {
                            int index = getRobots().indexOf(robot);
                            tokenCounts[index] += cell.getTokens().size();
                        }
                    }
                }
            }
        }

        for (int i = 0; i < tokenCounts.length; i++) {
            System.out.println(getRobots().get(i).getName() + " placed " + tokenCounts[i] + " tokens.");
        }
    }
}
```

* [ ] Bonus
* [x] Robots exploring a graph in a DFS fashion and have commands just like when exploring the matrix
* [x] Concurrent MST -> parallel Boruvka's Algorithm
```java
public class ConcurrentBoruvka {
    private static final int NUM_THREADS = Math.min(4, Runtime.getRuntime().availableProcessors() - 1);

    public static List<Edge> findMST(Graph graph) {
        System.out.println("Running concurrent on " + NUM_THREADS + " threads");
        int N = graph.vertices().length;

        int[] components = new int[N];
        for (int i = 0; i < N; i++) {
            components[i] = i;
        }

        List<List<Integer>> componentLists = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            List<Integer> initialCompForNode = new ArrayList<>();
            initialCompForNode.add(i);
            componentLists.add(initialCompForNode);
        }


        List<Edge> mstEdges = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        while (true) {
            List<Future<Edge>> futuresList = new ArrayList<>();

            int cntNotEmpty = 0;
            for (int i = 0; i < componentLists.size(); i++)
                if (!componentLists.get(i).isEmpty()) {
                    cntNotEmpty++;
                    List<Integer> componentNodes = new ArrayList<>(componentLists.get(i));
                    futuresList.add(executor.submit(() -> findMinEdge(graph, componentNodes, components)));
                }
            if (cntNotEmpty == 1)
                break;

            for (Future<Edge> future : futuresList) {
                try {
                    Edge minEdge = future.get();
                    if (mergeComponents(componentLists, components, minEdge)) {
                        mstEdges.add(minEdge);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.out.println("Exception while getting future of edge: " + e.getMessage());
                }
            }
        }

        executor.shutdown();

        return mstEdges;
    }

    private static boolean mergeComponents(List<List<Integer>> componentLists, int[] components, Edge edge);
    private static Edge findMinEdge(Graph graph, List<Integer> componentNodes, int[] components);
}
```

Output:
```
Running concurrent on 4 threads
The cost is 19999.0 and my cost is 19999.0
Execution time for library: 1432 ms
Execution time for my concurrent implementation: 650 ms
```
* [x] Fast collaborative graph exploration
 * Agents use locks and share their sets of visited nodes when meeting at a vertex
Output:
```
Running collaborative exploration on 4 threads
Executor finished
The algorithm ran in 5 ms
```

```java
package org.example;

import org.graph4j.Graph;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Agent {
    private final Graph graph;
    private final Set<Integer> visited;
    private final LinkedList<Integer> path;
    private final ReentrantLock lock;
    
    public void explore() {
        lock.lock();
        try {
            if (isFinished())
                return;
            assert (!path.isEmpty());
            int currentNode = path.peek();
            Integer nextNode = getNextUnvisitedNode(currentNode);

            if (nextNode != null) {
                path.push(nextNode);
                visited.add(nextNode);
            } else {
                path.pop();
            }
        } finally {
            lock.unlock();
        }
    }

    public void meet(Agent other) {
        acquireTwoLocks(this.lock, other.lock);
        try {
            for (Integer node : other.visited) {
                if (!visited.contains(node)) {
                    visited.add(node);
                    path.push(node);
                }
            }
        } finally {
            this.lock.unlock();
            other.lock.unlock();
        }
    }
}
```

```java
package org.example;

import org.graph4j.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

public class FastCollaborativeGraphExploration {
    private final Graph graph;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    FastCollaborativeGraphExploration(Graph graph) {
        this.graph = graph;
    }

    public void exploreGraph(int cntAgents) {
        int N = graph.vertices().length;
        List<Agent> agents = new ArrayList<>();
        Random random = new Random();
        ConcurrentHashMap<Integer, Agent> agentPositions = new ConcurrentHashMap<>();

        for (int i = 0; i < cntAgents; i++) {
            Agent agent = new Agent(graph, random.nextInt(N));
            agents.add(agent);
            agentPositions.put(i, agent);
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < cntAgents; i++) {
            final int agentId = i;
            executor.execute(() -> {
                Agent currentAgent = agents.get(agentId);
                while (!currentAgent.isFinished()) {
                    currentAgent.explore();
                    agentPositions.replace(agentId, currentAgent);

                    handleAgentMeetings(agentPositions, currentAgent);
                }
            });
        }

        executor.shutdown();
        try {
            boolean done = executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
            if (done)
                System.out.println("Executor finished");
            else
                System.out.println("Executor not finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error awaiting termination of executor" + e.getMessage());
        }
    }
}
```