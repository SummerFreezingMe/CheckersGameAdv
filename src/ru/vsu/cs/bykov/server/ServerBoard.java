package ru.vsu.cs.bykov.server;

import ru.vsu.cs.bykov.game.Board;
import ru.vsu.cs.bykov.utils.Reader;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerBoard extends Board {

    public ServerBoard(String firstPlayerName, String secondPlayerName) {
        super(firstPlayerName, secondPlayerName);
    }

    public void serverMove(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            if (getMoves() / 2 > botMoves.size()-1) {
                out.println("END");

            } else {
                String currMove = botMoves.get(getMoves() / 2);

                String input = in.readLine();
                String[] parsed = input.split(" ");
                String[] curr = currMove.split(" ");
                move(parsed[0], parsed[1], Color.WHITE);
                move(curr[0], curr[1], Color.BLACK);
                System.out.println("From user: " + input);
                System.out.println("To user: " + currMove);
                if (getTeamBlack().size() == 0 || getTeamWhite().size() == 0) {
                    out.println("END");
                }
                out.println(currMove);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot connect to client", ex);
        }


    }

    private List<String> botMoves;

    {
        try {
            botMoves = Reader.getStrategy(new File("bot.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
