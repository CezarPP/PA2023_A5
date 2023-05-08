package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

class ClientThread extends Thread {
    private final Socket _socket;

    public ClientThread(Socket socket) {
        this._socket = socket;
    }

    public void run() {
        try (Socket socket = _socket;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(_socket.getInputStream()))) {
            boolean stoppedReached = false;
            while (!stoppedReached) {
                String request = in.readLine();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                String response;
                if (Objects.equals(request, "Stop") || Objects.equals(request, "stop")) {
                    response = "Server stopped";
                    stoppedReached = true;
                } else response = "Server received the request " + request;
                out.println(response);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}