package ru.vsu.cs.bykov;

import ru.vsu.cs.bykov.utils.GameStatus;

import java.awt.*;
import java.util.Scanner;

import static ru.vsu.cs.bykov.utils.GameStatus.END_GAME;

public class Console {

    Scanner sc = new Scanner(System.in);
    Board board;
    public void startGame() {
        System.out.println("Enter first player name: ");
        String firstPlayerName=sc.nextLine();
        System.out.println("Enter second player name: ");
        String secondPlayerName=sc.nextLine();
        board = new Board(firstPlayerName,secondPlayerName);
        board.createBoard();
        System.out.println("White goes first!");
        boolean endgame=false;
            while (!endgame) {
                drawBoard(board.getBoard());
                endgame=moveInitializationConsole();
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

    protected boolean moveInitializationConsole() {
        if (board.getMoves() == 0) {
            board.setWindow(false);
        }
        board.messenger(GameStatus.CHOOSE_SQUARE);
        Scanner sc = new Scanner(System.in);
        String peak = sc.nextLine();
        peak = peak.toUpperCase();
        board.messenger(GameStatus.CHOOSE_MOVE);
        String hit = sc.nextLine();
        hit = hit.toUpperCase();
        return (board.getMoves() % 2 == 0) ? board.move(peak, hit, Color.WHITE) : board.move(peak, hit, Color.BLACK);

    }
}
