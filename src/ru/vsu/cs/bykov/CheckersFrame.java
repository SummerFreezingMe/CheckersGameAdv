package ru.vsu.cs.bykov;

import javax.swing.*;

public class CheckersFrame extends JFrame {
    final DrawingPanel panel = new DrawingPanel();

    public void createFrame() {
        Board board = new Board();
        board.createBoard();
        panel.model = board.getBoard();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Шашки");
        setSize(600,600);
        add(panel);
        addMouseListener(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}