# Lab10

* [x] Compulsory
* [x] Homework
  * [x] Implemented Game, Board, Player classes
    (Below are the method signatures and local variables of these classes)
```java
public class Board {
    final int BOARD_SIZE = 15;
    private final int[][] board;
    
    public Board();
    
    public void submitMove(int x, int y, int player);
    
    public int[][] getBoard();
    
    boolean isInBoard(int x, int y);
    
    boolean isValidMove(int x, int y);
}

public class Player {
    private final int playerNumber;
    public Player(int number);
    public int getPlayerNumber();
}

public class Game implements TimerObserver {
    static int gameId = 0;
    static final Random random = new Random();
    private final Board board;
    private final Player[] players;
    private final ClientThread[] clientThreads;
    private int currentPlayer;
    private final GameTimer[] timers;

    public static synchronized int getNewId();

    public Game(Player p1, Player p2, int timerSeconds, ClientThread cl);

    public void join(ClientThread clientThread);

    public void startGame();

    public boolean submitMove(int x, int y, ClientThread clientThread);

    public boolean checkWinner(int lastMoveX, int lastMoveY);

    private boolean checkLine(int playerNumber, int startX, int startY, int dx, int dy);

    int getCountInDirection(int playerNumber, int startX, int startY, int dx, int dy, int sign);

    public void leaveGame(ClientThread clientThread);

    public ClientThread getOtherThread(ClientThread clientThread);

    private void stopTimers();
    
    @Override
    public void timeUp(Player player);
}
```
* [x] Client will send server commands such as create game, join game, submit move:
```java
public class SimpleClient {
    // ...
    public void startClient() {
        // ...
        try (Socket socket = new Socket(serverAddress, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Thread for reading user input and sending requests
            Thread writeThread = new Thread(() -> {
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String request = inputReader.readLine().toLowerCase();
                        out.println(request);
                        out.flush();
                        if (Objects.equals(request, "exit") || Objects.equals(request, "stop")) {
                            System.exit(0);
                        }
                        if (Objects.equals(request, "help")) {
                            printBasicCommands();
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                // ...
            });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
```
* [x] The server is responsible for game management and mediating the players
```java
public class ClientThread extends Thread {
    private final Socket _socket;
    private final GameList gameList;
    Game game;
    int gameId;
    final int GAME_LENGTH_SECONDS = 300;

    public ClientThread(Socket socket, GameList gameList);

    public void run() {
        try (Socket socket = _socket; BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()))) {
            boolean stopReached = false;
            while (!stopReached) {
                String request = in.readLine();
                if (request == null) {
                    this.interrupt();
                }
                assert request != null;
                request = request.toLowerCase();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                String response;
                if (Objects.equals(request, "stop") || Objects.equals(request, "exit")) {
                    response = "Server stopped";
                    if (game != null) {
                        game.leaveGame(this);
                        gameList.removeGame(gameId);
                    }
                    stopReached = true;
                } else if (request.startsWith("create game")) {
                    response = handleCreateGameRequest();
                } else if (request.startsWith("join game")) {
                    response = handleJoinGameRequest(request);
                } else if (request.startsWith("submit move")) {
                    response = handleSubmitMoveRequest(request);
                } else {
                    response = "Invalid request " + request;
                }
                out.println(response);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void notifyGameStart(boolean isFirst);

    public void notifyMoveMade(int x, int y);
    
    public void notifyWinner(boolean youAreWinner, boolean wonByTimeout);

    public void notifyOpponentLeft();

    void exitGame();

    private String handleSubmitMoveRequest(String request);
    private String handleCreateGameRequest();
    private String handleJoinGameRequest(String request);
}
```
* [x] Time control (blitz)
```java
public class GameTimer {
    private final TimerObserver observer;
    private Timer timer;
    private int seconds;
    private final Player player;

    public GameTimer(int initialSeconds, Player player, TimerObserver observer);

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                seconds--;
                if (seconds <= 0) {
                    timer.cancel();
                    observer.timeUp(player);
                }
            }
        }, 0, 1000); // 1 second
    }

    public void stop() {
        timer.cancel();
    }
}

// game implements the TimerObserver interface
public interface TimerObserver {
    void timeUp(Player player);
}
```
* [x] Bonus
* [x] Tournaments
```java
public class Solver {
    public static boolean[][][] solveILP(int n, int d, int p) {
        /*
        i, j <= n
        k    <= d
        D(i, j, k) -> whether i plays with j on day k

        forAll i,j, i != j, sum(k = 1 to d) D(i, j, k) = 1
        forAll i, k => sum(j = 1 to n, i != j) D(i, j, k) <= p
         */
        Loader.loadNativeLibraries();
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            return null;
        }
        double infinity = java.lang.Double.POSITIVE_INFINITY;
        // x and y are integer non-negative variables.
        MPVariable[][][] D = new MPVariable[n][n][d];
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                for (int k = 0; k < d; k++)
                    D[i][j][k] = solver.makeBoolVar(getName(i, j, k));
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                MPConstraint ct = solver.makeConstraint(1.0, 1.0, "i" + i + "j" + j);
                for (int k = 0; k < d; k++) {
                    ct.setCoefficient(D[i][j][k], 1.0);
                }
            }
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < d; k++) {
                MPConstraint ct = solver.makeConstraint(0.0, p, "i" + i + "k" + k);
                for (int j = i + 1; j < n; j++) {
                    ct.setCoefficient(D[i][j][k], 1.0);
                }
            }
        }
        /// ... print and get ans
        return ans;
    }
}
```
* [x] Try to find hamiltonian path in complete oriented graph
```java
public class Solver {
    static final Random rand = new Random();

    public static List<Integer> solveForSchedule(int N) {
        List<List<Integer>> v = new ArrayList<>();
        List<List<Integer>> inv = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            v.add(new ArrayList<Integer>());
            inv.add(new ArrayList<Integer>());
        }
        // get random complete graph
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++)
                if (rand.nextBoolean()) {
                    v.get(i).add(j);
                    inv.get(j).add(i);
                } else {
                    v.get(j).add(i);
                    inv.get(i).add(j);
                }
        }
        // standard dp approach to find a hamiltonian cycle
    }
}
```
Main:
```java
public class Main {
    static final int N = 10;
    static final int D = 5;
    static final int P = 5;

    public static void main(String[] args) {
        // SimpleServer.startSimpleServer();
        boolean[][][] d = Solver.solveILP(N, D, P);
        List<Integer> path = Solver.solveForSchedule(10);
        if (path == null) {
            System.out.println("No hamiltonian cycle");
        } else {
            for (Integer i : path)
                System.out.print(i + " ");
            System.out.println();
        }
    }
}
```

Output:
```
Number of variables = 225
Solver found solution
During day 0 the matches are: 
0 plays against 1
0 plays against 2
0 plays against 3
0 plays against 8
0 plays against 9
2 plays against 8
2 plays against 9
3 plays against 9
4 plays against 5
4 plays against 6
4 plays against 7
4 plays against 8
4 plays against 9
5 plays against 6
5 plays against 7
5 plays against 8
5 plays against 9
6 plays against 7
6 plays against 8
6 plays against 9
7 plays against 8
7 plays against 9
8 plays against 9
During day 1 the matches are: 
0 plays against 4
0 plays against 5
0 plays against 6
0 plays against 7
During day 2 the matches are: 
2 plays against 7
During day 3 the matches are: 
1 plays against 4
1 plays against 5
1 plays against 9
3 plays against 8
During day 4 the matches are: 
1 plays against 2
1 plays against 3
1 plays against 6
1 plays against 7
1 plays against 8
2 plays against 3
2 plays against 4
2 plays against 5
2 plays against 6
3 plays against 4
3 plays against 5
3 plays against 6
3 plays against 7
1 8 7 9 2 3 5 4 6 0 
```