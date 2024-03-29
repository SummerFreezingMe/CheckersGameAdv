package ru.vsu.cs.bykov.game;

import ru.vsu.cs.bykov.utils.GameStatus;
import ru.vsu.cs.bykov.utils.Messenger;

import java.awt.*;
import java.util.Scanner;

public class Console {
    public  static void main(String[] args) {
          Console consoleGame = new Console();
         consoleGame.startGame();
        }

    Scanner sc = new Scanner(System.in);
    Board board;

    private Messenger msg;

    public void startGame() {
        System.out.println("Enter first player name: ");
        String firstPlayerName=sc.nextLine();
        System.out.println("Enter second player name: ");
        String secondPlayerName=sc.nextLine();
        msg = new Messenger(firstPlayerName,secondPlayerName);
        board = new Board(firstPlayerName,secondPlayerName);
        board.createBoard();
        System.out.println("White goes first!");
        boolean endgame=false;
            while (!endgame) {
                drawBoard(board.getBoard());
                endgame=moveInitializationConsole();
            }
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
        msg.messenger(GameStatus.CHOOSE_SQUARE,-1);
        Scanner sc = new Scanner(System.in);
        String peak = sc.nextLine();
        peak = peak.toUpperCase();
        msg.messenger(GameStatus.CHOOSE_MOVE,-1);
        String hit = sc.nextLine();
        hit = hit.toUpperCase();
        Color currColor=board.getBoard()[Character.getNumericValue(peak.charAt(1) - 1)]
                [peak.charAt(0) - 65].getStatus().getTeam();
        return board.move(peak, hit, currColor);

    }
}
