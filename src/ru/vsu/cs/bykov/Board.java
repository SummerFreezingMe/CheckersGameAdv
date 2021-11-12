package ru.vsu.cs.bykov;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Board {
    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    private Square[][] board;
    static final int BOARD_SIZE = 8;
    List<Pawn> teamWhite = new ArrayList<>();
    List<Pawn> teamBlack = new ArrayList<>();

    public Board() {
        board = new Square[BOARD_SIZE][BOARD_SIZE];
    }

    public void createBoard() {
        for (int i = 1; i < BOARD_SIZE + 1; i++) {
            for (char j = 'A'; j < 'I'; j++) {
                board[i - 1][j - 65] = new Square(j, i);
            }
        }
        serveBoard();
    }

    public void serveBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getClr() == Color.BLACK) {
                    Pawn pawn = new Pawn();
                    pawn.setYStart(i + 1);
                    pawn.setXStart((char) (j + 65));
                    if (i < 3) {
                        pawn.setTeam(Color.WHITE);
                        board[i][j].setStatus(pawn);
                        teamWhite.add(pawn);
                    } else if (i > 4) {
                        pawn.setTeam(Color.BLACK);
                        board[i][j].setStatus(pawn);
                        teamBlack.add(pawn);
                    }


                }
            }

        }
    }

    protected boolean moveInitialization(int moves) {
        messenger(1, moves);
        Scanner sc = new Scanner(System.in);
        String peak = sc.nextLine();
        peak = peak.toUpperCase();
        messenger(2, moves);
        String hit = sc.nextLine();
        hit = hit.toUpperCase();
        return (moves % 2 == 0) ? move(moves, peak, hit, Color.WHITE) : move(moves, peak, hit, Color.BLACK);

    }

    void moveInitialization(int moves, int col, int row, int storedCol,
                                    int storedRow) {
        String peak = String.valueOf(Character.toChars(storedCol + 65)).concat(String.valueOf((storedRow + 1)));
        String hit = String.valueOf(Character.toChars(col + 65)).concat(String.valueOf((row + 1)));
        boolean endgame = (moves % 2 == 0) ? move(moves, peak, hit, Color.WHITE) : move(moves, peak, hit, Color.BLACK);

    }


    private void streakCheck(String peak, int moves, Color clr) throws ArrayIndexOutOfBoundsException {
        Color enemy = (clr.getBlue() == 255) ? Color.BLACK : Color.WHITE;
        char xAxis = (char) (peak.charAt(0) - 65);
        int yAxis = Character.getNumericValue(peak.charAt(1) - 1);
        if (yAxis < BOARD_SIZE - 2 && xAxis < BOARD_SIZE - 2) {
            if (board[yAxis + 1][xAxis + 1].status != null) {
                if (board[yAxis + 1][xAxis + 1].status.team == enemy &&
                        board[yAxis + 2][xAxis + 2].status == null
                ) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 67)).concat(String.valueOf((yAxis + 3)));
                    move(moves, peak, newHit, clr);
                    return;
                }
            }
        }
        if (yAxis < BOARD_SIZE - 2 && xAxis > 1) {
            if (board[yAxis + 1][xAxis - 1].status != null) {

                if (board[yAxis + 1][(xAxis - 1)].status.team == enemy &&
                        board[yAxis + 2][xAxis - 2].status == null) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 63)).concat(String.valueOf((yAxis + 3)));
                    move(moves, peak, newHit, clr);
                    return;
                }
            }
        }
        if (yAxis > 1 && xAxis < BOARD_SIZE - 2) {
            if (board[yAxis - 1][xAxis + 1].status != null) {
                if (board[yAxis - 1][xAxis + 1].status.team == enemy &&
                        board[yAxis - 2][xAxis + 2].status == null) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 65 + 2)).concat(String.valueOf((yAxis - 1)));
                    move(moves, peak, newHit, clr);
                    return;
                }
            }
        }
        if (yAxis > 1 && xAxis > 1) {
            if (board[yAxis - 1][xAxis - 1].status != null) {
                if (board[(peak.charAt(1)) - 49 - 1][(peak.charAt(0) - 65 - 1)].status.team == enemy &&
                        board[yAxis - 2][xAxis - 2].status == null) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 65 - 2)).concat(String.valueOf((yAxis - 1)));
                    move(moves, peak, newHit, clr);
                }
            }
        }
    }

    private void queenMove(Color color, String peak, String hit, int moveLength, int moves) {
        int xPeak = peak.charAt(0) - 65;
        int yPeak = Character.getNumericValue(peak.charAt(1) - 1);
        int xHit = hit.charAt(0) - 65;
        int yHit = Character.getNumericValue(hit.charAt(1) - 1);
        Pawn curr = board[yPeak][xPeak].status;
        Color enemy = (color.getBlue() == 255) ? Color.BLACK : Color.WHITE;
        if (Math.abs(xPeak - xHit) == Math.abs(moveLength) &&
                board[yHit][xHit].status == null) {
            int xDirection = (xHit > xPeak) ? 1 : -1;
            int yDirection = (yHit > yPeak) ? 1 : -1;
            for (int i = 1; i <= Math.abs(moveLength); i++) {
                if (board[yPeak + i * xDirection][(xPeak + i * yDirection)].status != null) {
                    if (board[yPeak + i * xDirection][(xPeak + i * yDirection)].status.team == color) {
                        messenger(3, moves);
                        moveInitialization(moves);
                        break;
                    } else if (board[yPeak + i * xDirection][(xPeak + i * yDirection)].status.team == enemy) {
                        Pawn fall = board[yPeak + i * xDirection][xPeak + i * yDirection].status;
                        if ((enemy == Color.BLACK)) {
                            teamBlack.remove(fall);
                        } else {
                            teamWhite.remove(fall);
                        }
                        fall.deletePawn();
                        board[yPeak + i * xDirection][xPeak + i * yDirection].status = null;
                    }
                }
            }
            board[yHit][xHit].status = curr;
            board[yPeak][xPeak].status = null;
            streakCheck(hit, moves, color);
        } else {
            messenger(4, moves);
            moveInitialization(moves);
        }
    }

    public void drawBoard() {
        System.out.println("--------------------------------");
        char ch = 'A';
        for (int i = 8; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                if (i == 8) {
                    System.out.print(" " + (ch++) + " ");
                    continue;
                }
                if (board[i][j].status == null) {
                    System.out.print(" - ");
                } else {
                    if (!board[i][j].status.isQueen()) {
                        System.out.print(" " + (board[i][j].status.team == Color.BLACK ? "b" : "w") + " ");
                    } else {
                        System.out.print(" " + (board[i][j].status.team == Color.BLACK ? "B" : "W") + " ");
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
    //todo раздельная переинициализация ходов
    private boolean move(int moves, String peak, String hit, Color clr) {
        int xPeak = peak.charAt(0) - 65;
        int yPeak = Character.getNumericValue(peak.charAt(1) - 1);
        int xHit = hit.charAt(0) - 65;
        int yHit = Character.getNumericValue(hit.charAt(1) - 1);
        int colorPick = moves % 2;
        if (board[yPeak][xPeak].status.team == clr) {
            Pawn curr = board[yPeak][xPeak].status;
            int moveLength = peak.charAt(1) - hit.charAt(1);
            if (curr.isQueen) {
                queenMove(clr, peak, hit, moveLength, moves);
            } else {
                if (Math.abs(peak.charAt(0) - hit.charAt(0)) == 2 && Math.abs(moveLength) == 2) {//case:
                    if (Objects.equals((board[(yHit + yPeak) / 2][(xHit + xPeak) / 2]).status.team,
                            new Color(colorPick * 255, colorPick * 255, colorPick * 255))) {
                        Pawn fall = board[(yHit + yPeak) / 2][(xHit + xPeak) / 2].getStatus();
                        if ((clr == Color.BLACK)) {
                            teamWhite.remove(fall);
                        } else {
                            teamBlack.remove(fall);
                        }
                        fall.deletePawn();
                        board[(yHit + yPeak) / 2][(xHit + xPeak) / 2].status = null;
                        board[yHit][xHit].status = curr;
                        board[yPeak][xPeak].status = null;
                        streakCheck(hit, moves, clr);

                    } else {
                        messenger(3, moves);
                        moveInitialization(moves);
                    }

                } else if (yHit - yPeak == 1 - 2 * colorPick && Math.abs(moveLength) == 1) {
                    if (board[yHit][xHit].status == null) {
                        board[yHit][xHit].status = curr;
                        board[yPeak][xPeak].status = null;
                    } else {
                        messenger(3, moves);
                        moveInitialization(moves);
                    }
                }
                if (yHit == 7 - colorPick * 7) {
                    board[yHit][xHit].status.setQueen(true);
                }
            }
        } else {
            messenger(4, moves);
            moveInitialization(moves);
        }
        return teamWhite.isEmpty();
    }

    protected void messenger(int n, int moves) {
        String name = (moves % 2 == 0) ? "Player 1" : "Player 2";
        switch (n) {
            case (1) -> System.out.println(name + ", выберите клетку: ");
            case (2) -> System.out.println(name + ", выберите ход: ");
            case (3) -> System.out.println(name + ", данный ход невозможен ");
            case (4) -> System.out.println(name + ", данная клетка недоступна ");
            case (5) -> System.out.println(name + "Игра закончена за " + moves + " ходов, победитель - " + name);
        }
    }
}
