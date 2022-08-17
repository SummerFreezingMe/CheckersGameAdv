package ru.vsu.cs.bykov.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


    public class AvailableMoves {


        private final List<Square> moves= new ArrayList<>();
        private final List<Square> kills= new ArrayList<>();

        public void displayAvailableMoves(int row, int col, Color team, Square[][] model) {
            int direction = (team == Color.BLACK) ? -1 : 1;
            Color rival = new Color(Math.abs(team.getRed() - 255), Math.abs(team.getRed() - 255), Math.abs(team.getRed() - 255));
            if (model[row][col].getStatus().isQueen()) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (Math.abs(i - row) == Math.abs(j - col)) {
                            if (model[i][j].getStatus() == null) {
                                moves.add(model[i][j]);
                            }
                        }
                    }
                }
            } else {
                if (col == 0) {
                    if (model[row + direction][col + 1].getStatus() == null) {
                        moves.add(model[row + direction][col + 1]);
                    }


                } else if (col == 7) {
                    if (model[row + direction][col - 1].getStatus() == null) {
                        moves.add(model[row + direction][col - 1]);
                    }
                } else {
                    if (model[row + direction][col + 1].getStatus() == null) {
                        moves.add(model[row + direction][col + 1]);
                    }
                    if (model[row + direction][col - 1].getStatus() == null) {
                        moves.add(model[row + direction][col - 1]);
                    }


                }
                if (row < 6 && col < 6) {
                    if (model[row + 1][col + 1].getStatus() != null) {
                        if (Objects.equals(model[row + 1][col + 1].getStatus().getTeam(), rival) &&
                                model[row + 2][col + 2].getStatus() == null) {
                            kills.add(model[row + 2][col + 2]);
                        }
                    }
                }
                if (row < 6 && col > 1) {
                    if (model[row + 1][col - 1].getStatus() != null) {
                        if (Objects.equals(model[row + 1][col - 1].getStatus().getTeam(), rival) &&
                                model[row + 2][col - 2].getStatus() == null) {
                            kills.add(model[row + 2][col - 2]);
                        }
                    }
                }
                if (row > 1 && col < 6) {
                    if (model[row - 1][col + 1].getStatus() != null) {
                        if (Objects.equals(model[row - 1][col + 1].getStatus().getTeam(), rival) &&
                                model[row - 2][col + 2].getStatus() == null) {
                            kills.add(model[row - 2][col + 2]);
                        }
                    }
                }
                if (row > 1 && col > 1) {
                    if (model[row - 1][col - 1].getStatus() != null) {
                        if (Objects.equals(model[row - 1][col - 1].getStatus().getTeam(), rival) && model[row - 2][col - 2].getStatus() == null) {
                            kills.add(model[row - 2][col - 2]);
                        }
                    }
                }
            }
        }
        public List<Square> getKills() {
            return kills;
        }
        public List<Square> getMoves() {
            return moves;
        }
        public void flush(){
            kills.subList(0, kills.size()).clear();
            moves.subList(0, moves.size()).clear();
        }
    }
