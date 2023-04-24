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
* [ ] Concurrent MST
* [ ] Fast collaborative graph exploration