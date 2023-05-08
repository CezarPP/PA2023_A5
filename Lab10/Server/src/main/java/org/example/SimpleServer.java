package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static final int PORT = 8100;

    public SimpleServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                System.out.println("Starting client thread ...");
                new ClientThread(socket).start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}