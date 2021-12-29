package ru.vsu.cs.bykov.server;

import ru.vsu.cs.bykov.Board;
import ru.vsu.cs.bykov.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AppClient extends JFrame {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final DrawingPanel panel;


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        DrawingPanel drawPanel = new DrawingPanel(sc.nextLine(), "bot");
        drawPanel.setBoard(new Board(sc.nextLine(), "bot"));
        AppClient client = new AppClient("localhost", 9989, drawPanel);
        client.start();
    }

    private void draw(DrawingPanel panel) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Шашки");
        setSize(850, 640);
        add(panel);
        setLayout(new BorderLayout());
        addMouseListener(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public AppClient(String host, int port, DrawingPanel panel) {
        this.host = host;
        this.port = port;
        this.panel = panel;

    }

    public void start() throws IOException {
        draw(panel);
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        if (panel.getBoard().getMoves() == 0) {
            while (!socket.isClosed()) {
                if (panel.getPeak() != null && panel.getHit() != null) {
                    out.println(panel.getPeak() + " " + panel.getHit());
                    panel.setPeak(null);
                    panel.setHit(null);
                    break;
                }
            }
            String command = in.readLine();
            while (!socket.isClosed()) {
                if ((command == null)) {
                    command = in.readLine();
                }
                String[] parsed = command.split(" ");
                System.out.println("From server:" + command);
                if (parsed[0].equals("END")) {
                    socket.close();
                }
                panel.getBoard().move(parsed[0], parsed[1], Color.BLACK);
                panel.repaint();
                while (!socket.isClosed()) {
                    if (panel.getPeak() != null && panel.getHit() != null) {
                        out.println(panel.getPeak() + " " + panel.getHit());
                        panel.setPeak(null);
                        panel.setHit(null);
                        command = null;
                        break;
                    }
                }
            }
        }
    }
}