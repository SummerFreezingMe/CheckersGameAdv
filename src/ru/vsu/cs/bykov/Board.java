package ru.vsu.cs.bykov;


import ru.vsu.cs.bykov.utils.GameStatus;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Board {
    private final Square[][] board;
    static final int BOARD_SIZE = 8;

    private boolean isWindow = true;
    private final GameLog log = new GameLog();
    private final String firstPlayerName;
    private final String secondPlayerName;
    private int moves = 0;
    private final List<Pawn> teamWhite = new ArrayList<>();
    private final List<Pawn> teamBlack = new ArrayList<>();

    public List<Pawn> getTeamWhite() {
        return teamWhite;
    }
    public List<Pawn> getTeamBlack() {
        return teamBlack;
    }
    public String getFirstPlayerName() {
        return firstPlayerName;
    }
    public String getSecondPlayerName() {
        return secondPlayerName;
    }
    public int getMoves() {
        return moves;
    }
    public Square[][] getBoard() {
        return board;
    }

    public Board(String firstPlayerName, String secondPlayerName) {
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
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

    protected boolean moveInitializationConsole() {
        if (moves == 0) {
            isWindow = false;
        }
        messenger(GameStatus.CHOOSE_SQUARE);
        Scanner sc = new Scanner(System.in);
        String peak = sc.nextLine();
        peak = peak.toUpperCase();
        messenger(GameStatus.CHOOSE_MOVE);
        String hit = sc.nextLine();
        hit = hit.toUpperCase();
        return (moves % 2 == 0) ? move(peak, hit, Color.WHITE) : move(peak, hit, Color.BLACK);

    }

    void moveInitializationWindow(int col, int row, int storedCol,
                                  int storedRow) {
        String peak = String.valueOf(Character.toChars(storedCol + 65)).concat(String.valueOf((storedRow + 1)));
        String hit = String.valueOf(Character.toChars(col + 65)).concat(String.valueOf((row + 1)));
        if ((moves % 2 == 0)) {
            move(peak, hit, Color.WHITE);
        } else {
            move( peak, hit, Color.BLACK);
        }
    }


    private void streakCheck(String peak, Color clr) throws ArrayIndexOutOfBoundsException {
        Color enemy = (clr.getBlue() == 255) ? Color.BLACK : Color.WHITE;
        char xAxis = (char) (peak.charAt(0) - 65);
        int yAxis = Character.getNumericValue(peak.charAt(1) - 1);
        if (yAxis < BOARD_SIZE - 2 && xAxis < BOARD_SIZE - 2) {
            if (board[yAxis + 1][xAxis + 1].getStatus() != null) {
                if (board[yAxis + 1][xAxis + 1].getStatus().getTeam() == enemy &&
                        board[yAxis + 2][xAxis + 2].getStatus() == null
                ) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 67)).concat(String.valueOf((yAxis + 3)));
                    move(peak, newHit, clr);
                    return;
                }
            }
        }
        if (yAxis < BOARD_SIZE - 2 && xAxis > 1) {
            if (board[yAxis + 1][xAxis - 1].getStatus() != null) {
                if (board[yAxis + 1][(xAxis - 1)].getStatus().getTeam() == enemy &&
                        board[yAxis + 2][xAxis - 2].getStatus() == null) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 63)).concat(String.valueOf((yAxis + 3)));
                    move(peak, newHit, clr);
                    return;
                }
            }
        }
        if (yAxis > 1 && xAxis < BOARD_SIZE - 2) {
            if (board[yAxis - 1][xAxis + 1].getStatus() != null) {
                if (board[yAxis - 1][xAxis + 1].getStatus().getTeam() == enemy &&
                        board[yAxis - 2][xAxis + 2].getStatus() == null) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 65 + 2)).concat(String.valueOf((yAxis - 1)));
                    move( peak, newHit, clr);
                    return;
                }
            }
        }
        if (yAxis > 1 && xAxis > 1) {
            if (board[yAxis - 1][xAxis - 1].getStatus() != null) {
                if (board[(peak.charAt(1)) - 49 - 1][(peak.charAt(0) - 65 - 1)].getStatus().getTeam() == enemy &&
                        board[yAxis - 2][xAxis - 2].getStatus() == null) {
                    String newHit = String.valueOf(Character.toChars(xAxis + 65 - 2)).concat(String.valueOf((yAxis - 1)));
                    move( peak, newHit, clr);
                }
            }
        }
    }

    private void queenMove(Color color, String peak, String hit, int moveLength) {
        int xPeak = peak.charAt(0) - 65;
        int yPeak = Character.getNumericValue(peak.charAt(1) - 1);
        int xHit = hit.charAt(0) - 65;
        int yHit = Character.getNumericValue(hit.charAt(1) - 1);
        Pawn curr = board[yPeak][xPeak].getStatus();
        Color enemy = (color.getBlue() == 255) ? Color.BLACK : Color.WHITE;
        if (Math.abs(xPeak - xHit) == Math.abs(moveLength) &&
                board[yHit][xHit].getStatus() == null) {
            int xDirection = (xHit > xPeak) ? 1 : -1;
            int yDirection = (yHit > yPeak) ? 1 : -1;
            for (int i = 1; i <= Math.abs(moveLength); i++) {
                if (board[yPeak + i * yDirection][(xPeak + i * xDirection)].getStatus() != null) {
                    if (board[yPeak + i * yDirection][(xPeak + i * xDirection)].getStatus().getTeam() == color) {
                        messenger(GameStatus.MOVE_UNAVAILABLE);
                        moveInitializationConsole();
                        break;
                    } else if (board[yPeak + i * yDirection][(xPeak + i * xDirection)].getStatus().getTeam() == enemy) {
                        Pawn fall = board[yPeak + i * yDirection][xPeak + i * xDirection].getStatus();
                        if ((enemy == Color.BLACK)) {
                            teamBlack.remove(fall);
                        } else {
                            teamWhite.remove(fall);
                        }
                        fall.deletePawn();
                        board[yPeak + i * yDirection][xPeak + i * xDirection].setStatus(null);
                    }
                }
            }
            board[yHit][xHit].setStatus(curr);
            board[yPeak][xPeak].setStatus(null);
            streakCheck(hit, color);
        } else {
            messenger(GameStatus.SQUARE_UNAVAILABLE);
            moveInitializationConsole();
        }
    }

    private boolean move( String peak, String hit, Color clr) {
        int xPeak = peak.charAt(0) - 65;
        int yPeak = Character.getNumericValue(peak.charAt(1) - 1);
        int xHit = hit.charAt(0) - 65;
        int yHit = Character.getNumericValue(hit.charAt(1) - 1);
        int colorPick = moves % 2;
        if (board[yPeak][xPeak].getStatus().getTeam() == clr) {
            Pawn curr = board[yPeak][xPeak].getStatus();
            int moveLength = peak.charAt(0) - hit.charAt(0);
            if (curr.isQueen()) {
                queenMove(clr, peak, hit, moveLength);
            } else {
                if (Math.abs(yHit - yPeak) == 2 && Math.abs(moveLength) == 2) {//case:
                    if (Objects.equals((board[(yHit + yPeak) / 2][(xHit + xPeak) / 2]).getStatus().getTeam(),
                            new Color(colorPick * 255, colorPick * 255, colorPick * 255))) {
                        Pawn fall = board[(yHit + yPeak) / 2][(xHit + xPeak) / 2].getStatus();
                        if ((clr == Color.BLACK)) {
                            teamWhite.remove(fall);
                        } else {
                            teamBlack.remove(fall);
                        }
                        fall.deletePawn();
                        board[(yHit + yPeak) / 2][(xHit + xPeak) / 2].setStatus(null);
                        board[yHit][xHit].setStatus(curr);
                        board[yPeak][xPeak].setStatus(null);
                        streakCheck(hit, clr);
                    } else {
                        messenger(GameStatus.MOVE_UNAVAILABLE);
                        if (!isWindow) {
                            moveInitializationConsole();
                        } else {
                            return false;
                        }
                    }
                } else if (yHit - yPeak == 1 - 2 * colorPick && Math.abs(moveLength) == 1) {
                    if (board[yHit][xHit].getStatus() == null) {
                        board[yHit][xHit].setStatus(curr);
                        board[yPeak][xPeak].setStatus(null);
                    } else {
                        messenger(GameStatus.MOVE_UNAVAILABLE);
                        if (!isWindow) {
                            moveInitializationConsole();
                        } else {
                            return false;
                        }
                    }
                } else {
                    messenger(GameStatus.MOVE_UNAVAILABLE);
                    if (!isWindow) {
                        moveInitializationConsole();
                    } else {
                        return false;
                    }
                }
                if (yHit == 7 - colorPick * 7) {
                    board[yHit][xHit].getStatus().setQueen(true);
                }
                log.writeOutput();
            }
        } else {
            messenger(GameStatus.SQUARE_UNAVAILABLE);
            if (!isWindow) {
                moveInitializationConsole();
            } else {
                return false;
            }
        }
        if ((moves % 2 == 0)) {
            log.writeMove(moves, hit, peak, firstPlayerName);
        } else {
            log.writeMove(moves, hit, peak, secondPlayerName);
        }
        this.moves++;
        return teamWhite.isEmpty();
    }

    protected void messenger(final GameStatus n) {
        String name = (moves % 2 == 0) ? firstPlayerName : secondPlayerName;
        int msg = n.ordinal();
        switch (msg) {
            case (0) -> System.out.println(name + ", choose a square: ");
            case (1) -> System.out.println(name + ", choose a move: ");
            case (2) -> System.out.println(name + ", this move is not possible ");
            case (3) -> System.out.println(name + ", this cell is not available ");
            case (4) -> System.out.println(name + "The game is over for " + moves + " moves, the winner - " + name);
            default -> throw new IllegalStateException("Unexpected value: " + n);
        }

    }
}
