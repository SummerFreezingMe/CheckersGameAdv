package ru.vsu.cs.bykov;

import java.awt.*;

public class Square {
    private Color clr;
    Pawn status;
    private char YStart;
    private int X;


    public Square(char YAxis,int XAxis) {
        this.YStart = YAxis;
        this.X = XAxis;
        setClr(chooseColor(YAxis,XAxis));

    }


    public int getX() {
        return X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public char getYStart() {
        return YStart;
    }

    public void setYStart(char YStart) {
        this.YStart = YStart;
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
    public Color chooseColor(char y, int x){
        if((y+x)%2==0){
            return Color.BLACK;
        }else return Color.WHITE;
    }
}
