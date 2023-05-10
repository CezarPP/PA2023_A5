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
* [ ] Bonus