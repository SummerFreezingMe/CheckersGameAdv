package ru.vsu.cs.bykov;

import java.awt.*;

public class Pawn {
    public Color getTeam() {
        return team;
    }

    public void setTeam(Color team) {
        this.team = team;
    }

    public Color team;
    public boolean isQueen = false;
    public char XStart;
    public int YStart;

    public int getYStart() {
        return YStart;
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
