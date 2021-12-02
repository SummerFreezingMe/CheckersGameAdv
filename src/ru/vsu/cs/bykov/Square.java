package ru.vsu.cs.bykov;

import java.awt.*;

public class Square {
    private Color clr;
    private Pawn status;
    private final char yAxis;
    private final int xAxis;


    public Square(char YAxis, int XAxis) {
        this.yAxis = YAxis;
        this.xAxis = XAxis;
        setClr(chooseColor(YAxis, XAxis));

    }


    public int getX() {
        return xAxis;
    }

    public char getYAxis() {
        return yAxis;
    }

    public Pawn getStatus() {
        return status;
    }

    public void setStatus(Pawn status) {
        this.status = status;
    }

    public Color getClr() {
        return clr;
    }

    public void setClr(Color clr) {
        this.clr = clr;
    }

    public Color chooseColor(char y, int x) {
        if ((y + x) % 2 == 0) {
            return Color.BLACK;
        } else return Color.WHITE;
    }
}
