package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class SimpleClient {

    public static void main(String[] args) {
        new SimpleClient().startClient();
    }

    public void startClient() {
        final String serverAddress = "127.0.0.1";
        final int PORT = 8100;
        printBasicCommands();

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
            });

            // Thread for reading server responses
            Thread readThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });

            writeThread.start();
            readThread.start();

            writeThread.join();
            readThread.join();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void printBasicCommands() {
        System.out.println("Commands:");
        System.out.println("Help");
        System.out.println("Exit");
        System.out.println("Stop");
        System.out.println("Create game          -> creates a new game, the server responds with the game id");
        System.out.println("Join game <id>       -> joins the game with the specified id");
        System.out.println("Submit move <x> <y>  -> submits move to current game");
    }
}