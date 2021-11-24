package ru.vsu.cs.bykov;

import java.awt.*;
import java.util.Scanner;

import static ru.vsu.cs.bykov.GameStatus.END_GAME;

public class Console {

    Scanner sc = new Scanner(System.in);

    public void startGame() {
        System.out.println("Enter first player name: ");
        String firstPlayerName=sc.nextLine();
        System.out.println("Enter second player name: ");
        String secondPlayerName=sc.nextLine();
        Board board = new Board(firstPlayerName,secondPlayerName);
        board.createBoard();
        System.out.println("White goes first!");
        boolean endgame=false;
            while (!endgame) {
                drawBoard(board.getBoard());
                endgame=board.moveInitialization();
            }
            board.messenger(END_GAME);
        }
    public void drawBoard(Square[][] board) {
        System.out.println("--------------------------------");
        char ch = 'A';
        for (int i = 8; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                if (i == 8) {
                    System.out.print(" " + (ch++) + " ");
                    continue;
                }
                if (board[i][j].getStatus() == null) {
                    System.out.print(" - ");
                } else {
                    if (!board[i][j].getStatus().isQueen()) {
                        System.out.print(" " + (board[i][j].getStatus().getTeam() == Color.BLACK ? "b" : "w") + " ");
                    } else {
                        System.out.print(" " + (board[i][j].getStatus().getTeam() == Color.BLACK ? "B" : "W") + " ");
                    }
                }
                if (j == 7) {
                    System.out.print(" " + (i + 1));
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }

}
