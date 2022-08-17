package ru.vsu.cs.bykov.game;


import ru.vsu.cs.bykov.utils.GameStatus;
import ru.vsu.cs.bykov.utils.Messenger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    private final Square[][] board;
    public static final int BOARD_SIZE = 8;
    private final GameLog log = new GameLog();
    private final String firstPlayerName;
    private final String secondPlayerName;
    private int moves = 0;
    private final List<Pawn> teamWhite = new ArrayList<>();
    private final List<Pawn> teamBlack = new ArrayList<>();
    private final AvailableMoves am = new AvailableMoves();
    private final Messenger msg;

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
        msg = new Messenger(firstPlayerName,secondPlayerName);
        createBoard();
    }

    public void createBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (char j = 'A'; j < 'I'; j++) {
                board[i][j - 65] = new Square(j, i);
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


    private void streakCheck(int xAxis, int yAxis, Color clr) throws ArrayIndexOutOfBoundsException {
        Color enemy = (clr.getBlue() == 255) ? Color.BLACK : Color.WHITE;
        if (yAxis < BOARD_SIZE - 2 && xAxis < BOARD_SIZE - 2) {
            if (board[yAxis + 1][xAxis + 1].getStatus() != null) {
                if (board[yAxis + 1][xAxis + 1].getStatus().getTeam() == enemy &&
                        board[yAxis + 2][xAxis + 2].getStatus() == null
                ) {
                    moves--;
                    return;
                }
            }
        }
        if (yAxis < BOARD_SIZE - 2 && xAxis > 1) {
            if (board[yAxis + 1][xAxis - 1].getStatus() != null) {
                if (board[yAxis + 1][(xAxis - 1)].getStatus().getTeam() == enemy &&
                        board[yAxis + 2][xAxis - 2].getStatus() == null) {
                    moves--;
                    return;
                }
            }
        }
        if (yAxis > 1 && xAxis < BOARD_SIZE - 2) {
            if (board[yAxis - 1][xAxis + 1].getStatus() != null) {
                if (board[yAxis - 1][xAxis + 1].getStatus().getTeam() == enemy &&
                        board[yAxis - 2][xAxis + 2].getStatus() == null) {
                    moves--;
                    return;
                }
            }
        }
        if (yAxis > 1 && xAxis > 1) {
            if (board[yAxis - 1][xAxis - 1].getStatus() != null) {
                if (board[yAxis - 1][xAxis - 1].getStatus().getTeam() == enemy &&
                        board[yAxis - 2][xAxis - 2].getStatus() == null) {
                    moves--;

                }
            }
        }
    }

    private boolean queenMove(Color color, String peak, String hit, int moveLength) {
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
                        msg.messenger(GameStatus.MOVE_UNAVAILABLE,moves);
                        return false;
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
            streakCheck(xHit, yHit, color);
        } else {
            msg.messenger(GameStatus.SQUARE_UNAVAILABLE,moves);
            return false;
        }
        return false;
    }

    public boolean move(String peak, String hit, Color clr) {
        int xPeak = peak.charAt(0) - 65;
        int yPeak = Character.getNumericValue(peak.charAt(1) - 1);
        int xHit = hit.charAt(0) - 65;
        int yHit = Character.getNumericValue(hit.charAt(1) - 1);
        int colorPick = moves % 2;

        if (moveValidation(clr, xPeak, yPeak)) {
            Pawn curr = board[yPeak][xPeak].getStatus();
            am.displayAvailableMoves(yPeak, xPeak, clr, board);
            int moveLength = peak.charAt(0) - hit.charAt(0);
            if (curr.isQueen()) {
                queenMove(clr, peak, hit, moveLength);
            } else {
                if (am.getKills().contains(board[yHit][xHit])) {//case:
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
                    streakCheck(xHit, yHit, clr);

                } else if (am.getMoves().contains(board[yHit][xHit])) {
                    if (board[yHit][xHit].getStatus() == null) {
                        board[yHit][xHit].setStatus(curr);
                        board[yPeak][xPeak].setStatus(null);
                    } else {
                        msg.messenger(GameStatus.MOVE_UNAVAILABLE,moves);
                        return false;
                    }
                } else {
                    msg.messenger(GameStatus.MOVE_UNAVAILABLE,moves);
                    return false;
                }
                if (yHit == 7 - colorPick * 7) {
                    board[yHit][xHit].getStatus().setQueen(true);
                }
                log.writeOutput();
            }
        } else {
            msg.messenger(GameStatus.SQUARE_UNAVAILABLE,moves);
            return false;

        }
        if ((clr == Color.WHITE)) {
            log.writeMove(moves, hit, peak, firstPlayerName);
        } else {
            log.writeMove(moves, hit, peak, secondPlayerName);
        }
        this.moves++;
        return endgame();
    }

    private boolean endgame() {
        if (getTeamBlack().isEmpty()||getTeamWhite().isEmpty()){
            msg.messenger(GameStatus.END_GAME,moves);
            return true;
        }
        return false;
    }

    private boolean moveValidation(Color clr, int xPeak, int yPeak) {
        if (moves % 2 == 0 && clr == Color.WHITE || moves % 2 == 1 && clr == Color.BLACK) {
            return Objects.equals(board[yPeak][xPeak].getStatus().getTeam(), clr);
        }

        return false;
    }
    }
