package ru.vsu.cs.bykov.game;

import java.awt.*;

public class Pawn {


    private Color team;
    private boolean isQueen = false;
    private char XStart;
    private int YStart;

    public int getYStart() {
        return YStart;
    }

    public Color getTeam() {
        return team;
    }

    public void setTeam(Color team) {
        if (team == Color.BLACK || team == Color.WHITE)
            this.team = team;
    }

    public void setYStart(int YStart) {
        this.YStart = YStart;
    }


    public char getXStart() {
        return XStart;
    }

    public void setXStart(char XStart) {
        this.XStart = XStart;
    }


    public boolean isQueen() {
        return isQueen;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public void deletePawn() {
        this.team = null;
    }

    public Pawn() {
    }
}
