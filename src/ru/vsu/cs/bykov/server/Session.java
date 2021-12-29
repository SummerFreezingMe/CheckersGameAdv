package ru.vsu.cs.bykov.server;


import java.net.Socket;

public class Session implements Runnable {


    private  ServerBoard board;
    private Socket socket;

    public Session(Socket socket) {
        String p1 = "socket";
        String p2 = "bot";
        board = new ServerBoard(p1, p2);
        this.socket = socket;

    }

    public void run() {
        while (board.getTeamBlack().size()>0&&board.getTeamWhite().size()>0) {
            board.serverMove(socket);
        }
        System.out.println("Game Over!");
    }

}