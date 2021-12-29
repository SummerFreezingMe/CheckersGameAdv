package ru.vsu.cs.bykov.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AppServer {
    private final ServerSocket serverSocket1;


    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            AppServer server = new AppServer(port);
            server.start();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start the server", e);
        }
    }
    public AppServer(int port) throws IOException {
        serverSocket1 = new ServerSocket(port,2);
    }

    public void start() throws IOException {
        System.out.println("Game server started");
        while (true) {
            Socket windowSocket = serverSocket1.accept();
            System.out.println("Client connected from: "+ windowSocket.getInetAddress());
            Session gameSession = new Session(windowSocket);
            Thread t = new Thread(gameSession);
            t.start();
        }
    }

}
