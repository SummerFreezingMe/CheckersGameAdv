package ru.vsu.cs.bykov.ui;

import ru.vsu.cs.bykov.AvailableMoves;
import ru.vsu.cs.bykov.Board;
import ru.vsu.cs.bykov.Square;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class DrawingPanel extends JPanel implements MouseListener {
    private final int MAX_WIDTH = 830;
    private final int MAX_HEIGHT = 600;
    private final int SQUARE_SIZE = 60;
    private String peak;
    private String hit;
    private Board b1;
    private Square[][] model;
    private int storedCol = -1;
    private int storedRow = -1;
    private boolean endgame = false;
    private boolean isChosen = false;
    private final AvailableMoves am = new AvailableMoves();

    public Board getBoard() {
        return b1;
    }

    public String getHit() {
        return hit;
    }

    public String getPeak() {
        return peak;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public void setBoard(Board b1) {
        this.b1 = b1;
    }


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
        g2d.fillRect(635, 80, 190, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(635, 80, 190, 25);
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
                if (am.getMoves().contains(model[i][j])) {
                    paintSquare(g2d, model[i][j], Color.GREEN);
                }else  if (am.getKills().contains(model[i][j])) {
                    paintSquare(g2d, model[i][j], Color.PINK);
                }
                else {
                    paintSquare(g2d, model[i][j], model[i][j].getClr());
                }
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
        String currPlayer = (currColor == Color.WHITE) ? b1.getFirstPlayerName() : b1.getSecondPlayerName();
        g2d.drawString("Player's move: " + currPlayer, 640, 100);
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

    Color currColor;

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = (e.getX() - 65) / 60;
        int row = (570 - e.getY()) / 60;

        if (!isChosen) {
            isChosen = true;
            currColor = model[row][col].getStatus().getTeam();
            am.displayAvailableMoves(row, col, currColor, model);
            storedCol = col;
            storedRow = row;
        } else {
            isChosen = false;
            if (col == storedCol && storedRow == row) {
                am.flush();
                repaint();
                return;
            }

            moveInitializationWindow(col, row, storedCol, storedRow, currColor);
            model = b1.getBoard();
            am.flush();


        }
        repaint();
    }

    void moveInitializationWindow(int col, int row, int storedCol,
                                  int storedRow, Color currColor) {
        peak = String.valueOf(Character.toChars(storedCol + 65)).concat(String.valueOf((storedRow + 1)));
        hit = String.valueOf(Character.toChars(col + 65)).concat(String.valueOf((row + 1)));
        b1.move(peak, hit, currColor);
    }

    private void gameOver(Graphics2D g2d, String winner) {
        Font font = new Font("Arial", Font.PLAIN, 18);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(640, 80, 150, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Победил игрок " + winner, 640, 100);
        endgame = false;
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

    public boolean isEndgame() {
        return endgame;
    }

    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }

}