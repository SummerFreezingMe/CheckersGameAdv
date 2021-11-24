package ru.vsu.cs.bykov;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DrawingPanel extends JPanel implements MouseListener {
    private final int MAX_WIDTH = 825;
    private final int MAX_HEIGHT = 600;
    private final int SQUARE_SIZE = 60;
    private boolean isChosen = false;
    private final Board b1;
    private Square[][] model;
    // todo унификация moves
    private int storedCol = -1;
    private int storedRow = -1;

    public DrawingPanel(String playerOne, String playerTwo) {
        b1 = new Board(playerOne, playerTwo);
        model = b1.getBoard();
        setVisible(true);
        setSize(new Dimension(MAX_WIDTH * 2, MAX_HEIGHT * 2));
        b1.createBoard();

    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        g2d.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(640, 80, 170, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(640, 80, 170, 25);
        g2d.drawRect(SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE * 8, SQUARE_SIZE * 8);
        currentPlayer(g2d);

        String imagePathA = "img/abc.png";
        String imagePathN = "img/123.png";
        try {
            BufferedImage lettersPic = ImageIO.read(new File(imagePathA));
            BufferedImage numbersPic = ImageIO.read(new File(imagePathN));
            ImageObserver obs = (img, info, x, y, width, height) -> false;
            g2d.drawImage(lettersPic, SQUARE_SIZE, MAX_HEIGHT - SQUARE_SIZE, obs);
            g2d.drawImage(numbersPic, MAX_HEIGHT - SQUARE_SIZE, SQUARE_SIZE, obs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2d.drawRect(SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE * 8, SQUARE_SIZE * 8);
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {

                paintSquare(g2d, model[i][j], model[i][j].getClr());
                if (model[i][j].getStatus() != null) {
                    paintPawn(g2d, model[i][j]);
                }
            }
        }
        if (b1.getTeamWhite().isEmpty()) {
            gameOver(g2d, b1.getSecondPlayerName());
        } else if (b1.getTeamBlack().isEmpty()) {
            gameOver(g2d, b1.getFirstPlayerName());
        }
    }


    public void currentPlayer(Graphics2D g2d) {
        Font font = new Font("Arial", Font.PLAIN, 18);
        g2d.setFont(font);
        String currPlayer = (b1.getMoves() % 2 == 0) ? b1.getFirstPlayerName() : b1.getSecondPlayerName();
        g2d.drawString("Player's move: " + currPlayer, 640, 100);
    }

    public void restoreBoard() {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if ((i + j) % 2 == 0)
                    model[i][j].setClr(Color.BLACK);
            }
        }
    }

    public void paintSquare(Graphics g, Square sqr, Color clr) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(clr);
        g2d.drawRect(SQUARE_SIZE + (sqr.getYAxis() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        g2d.fillRect(SQUARE_SIZE + (sqr.getYAxis() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    public void paintPawn(Graphics g, Square sqr) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.drawOval(SQUARE_SIZE + (sqr.getYAxis() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        if (sqr.getStatus().getTeam() == Color.WHITE) {
            g2d.setColor(Color.WHITE);
        } else if (sqr.getStatus().getTeam() == Color.BLACK) {
            g2d.setColor(Color.RED);
        }
        g2d.fillOval(SQUARE_SIZE + (sqr.getYAxis() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        if (sqr.getStatus().isQueen()) {
            g2d.setColor(Color.BLACK);
            g2d.fillOval(SQUARE_SIZE + (sqr.getYAxis() - 65) * SQUARE_SIZE + SQUARE_SIZE / 3, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE + SQUARE_SIZE / 3, SQUARE_SIZE / 3, SQUARE_SIZE / 3);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = (e.getX() - 65) / 60;
        int row = (570 - e.getY()) / 60;

        if (!isChosen) {
            isChosen = true;
            displayAvailableMoves(row, col, model[row][col].getStatus().getTeam());
            storedCol = col;
            storedRow = row;
        } else {
            isChosen = false;
            if (col == storedCol && storedRow == row) {
                restoreBoard();
                repaint();
                return;
            }
            b1.moveInitialization(col, row, storedCol, storedRow);
            model = b1.getBoard();
            restoreBoard();
            repaint();
        }
    }

    private void gameOver(Graphics2D g2d, String winner) {
        Font font = new Font("Arial", Font.PLAIN, 18);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(640, 80, 150, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Победил игрок " + winner, 640, 100);
    }

    private void displayAvailableMoves(int row, int col, Color team) {
        int direction = (team == Color.BLACK) ? -1 : 1;
        Color rival = new Color(Math.abs(team.getRed() - 255), Math.abs(team.getRed() - 255), Math.abs(team.getRed() - 255));
        if (model[row][col].getStatus().isQueen()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (Math.abs(i - row) == Math.abs(j - col)) {
                        if (model[i][j].getStatus() == null) {
                            model[i][j].setClr(Color.GREEN);
                        }
                    }
                }
            }
        } else {
            if (col == 0) {
                if (model[row + direction][col + 1].getStatus() == null) {
                    model[row + direction][col + 1].setClr(Color.GREEN);
                }


            } else if (col == 7) {
                if (model[row + direction][col - 1].getStatus() == null) {
                    model[row + direction][col - 1].setClr(Color.GREEN);
                }
            } else {
                if (model[row + direction][col + 1].getStatus() == null) {
                    model[row + direction][col + 1].setClr(Color.GREEN);
                }
                if (model[row + direction][col - 1].getStatus() == null) {
                    model[row + direction][col - 1].setClr(Color.GREEN);
                }


            }
            if (row < 6 && col < 6) {
                if (model[row + 1][col + 1].getStatus() != null) {
                    if (Objects.equals(model[row + 1][col + 1].getStatus().getTeam(), rival) &&
                            model[row + 2][col + 2].getStatus() == null) {
                        model[row + 2][col + 2].setClr(Color.PINK);
                    }
                }
            }
            if (row < 6 && col > 1) {
                if (model[row + 1][col - 1].getStatus() != null) {
                    if (Objects.equals(model[row + 1][col - 1].getStatus().getTeam(), rival) &&
                            model[row + 2][col - 2].getStatus() == null) {
                        model[row + 2][col - 2].setClr(Color.PINK);
                    }
                }
            }
            if (row > 1 && col < 6) {
                if (model[row - 1][col + 1].getStatus() != null) {
                    if (Objects.equals(model[row - 1][col + 1].getStatus().getTeam(), rival) &&
                            model[row - 2][col + 2].getStatus() == null) {
                        model[row - 2][col + 2].setClr(Color.PINK);
                    }
                }
            }
            if (row > 1 && col > 1) {
                if (model[row - 1][col - 1].getStatus() != null) {
                    if (Objects.equals(model[row - 1][col - 1].getStatus().getTeam(), rival) && model[row - 2][col - 2].getStatus() == null) {
                        model[row - 2][col - 2].setClr(Color.PINK);
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setModel(Square[][] model) {
        this.model = model;
    }
}