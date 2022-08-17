package ru.vsu.cs.bykov.ui;

import ru.vsu.cs.bykov.game.Board;
import ru.vsu.cs.bykov.server.Client;
import ru.vsu.cs.bykov.utils.TextPrompt;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CheckersFrame extends JFrame {
    private DrawingPanel drawPanel;

    public static void main(String[] args) {
        CheckersFrame checkersFrame = new CheckersFrame();
        checkersFrame.createPad();
    }

    public void createFrame(String playerOne, String playerTwo, boolean b) {
        Board board = new Board(playerOne, playerTwo);
        drawPanel = new DrawingPanel(playerOne, playerTwo);
        drawPanel.setModel(board.getBoard());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Шашки");
        setSize(850, 640);

        add(drawPanel);
        setLayout(new BorderLayout());
        addMouseListener(drawPanel);
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
        JButton mp = new JButton("Multiplayer");
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(player1);
        panel.add(player2);
        panel.add(button);
        panel.add(mp);
        JFrame frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setSize(200, 155);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        button.addActionListener(ae -> {
            createFrame(player1.getText(), player2.getText(), false);
            frame.dispose();
        });
        mp.addActionListener(ae -> {
            serverLauncher();
            frame.dispose();
        });
    }

    public void serverLauncher() {
        JTextField player = new JTextField(15);
        TextPrompt tp1 = new TextPrompt("Player Name...", player);
        JButton button = new JButton("Connect");
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(player);
        panel.add(button);
        JFrame frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setSize(200, 155);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        button.addActionListener(ae -> {
            createFrame(player.getText(), "Bot", true);
            Client client = new Client("localhost", 9989, drawPanel);
            try {
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            frame.dispose();
        });
    }
}