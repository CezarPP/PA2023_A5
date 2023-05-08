package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class SimpleClient {
    public void startClient() {
        final String serverAddress = "127.0.0.1";
        final int PORT = 8100;
        BufferedReader inputReader = new BufferedReader(
                new InputStreamReader(System.in));
        boolean shouldStop = false;
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!shouldStop) {
                String request = inputReader.readLine();
                out.println(request);
                if (Objects.equals(request, "exit") || Objects.equals(request, "Exit")) {
                    return;
                }
                if (Objects.equals(request, "stop") || Objects.equals(request, "Stop")) {
                    shouldStop = true;
                }
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}