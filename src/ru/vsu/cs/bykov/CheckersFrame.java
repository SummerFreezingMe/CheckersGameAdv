package ru.vsu.cs.bykov;

import ru.vsu.cs.bykov.utils.TextPrompt;

import javax.swing.*;
import java.awt.*;

public class CheckersFrame extends JFrame {


    public void createFrame(String text, String player2Text) {
        Board board = new Board(text, player2Text);
        board.createBoard();

        final DrawingPanel panel = new DrawingPanel(text, player2Text);

        panel.setModel(board.getBoard());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Шашки");
        setSize(850, 640);


        JLabel tf1 = new JLabel();
        tf1.setLocation(800, 150);
        Dimension labelSize = new Dimension(80, 80);
        tf1.setPreferredSize(labelSize);
        tf1.setBackground(Color.BLACK);
        tf1.setText("Я хочу пиццы");
        panel.add(tf1);

        add(panel);

        setLayout(new BorderLayout());
        add(tf1);
        addMouseListener(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void createPad() {
        JTextField player1 = new JTextField(15);
        JTextField player2 = new JTextField(15);
        TextPrompt tp1 = new TextPrompt("First Player Name...", player1);
        TextPrompt tp2 = new TextPrompt("Second Player Name...", player2);
        player1.setOpaque(false);
        JButton button = new JButton("Start Game");


        JPanel panel = new JPanel(new FlowLayout());
        panel.add(player1);
        panel.add(player2);
        panel.add(button);
        JFrame frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setSize(200, 155);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        button.addActionListener(ae -> {
            createFrame(player1.getText(), player2.getText());
            frame.dispose();
        });
    }
}