package ru.vsu.cs.bykov;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.Objects;

public class DrawingPanel extends JPanel implements MouseListener {
    private final int MAX_WIDTH = 600;
    private final int MAX_HEIGHT = 600;
    private final int SQUARE_SIZE = 60;
    boolean isChosen = false;
    Board b1 = new Board();
    Square[][] model = b1.getBoard();
    int moves = 0;
    int storedCol = -1;
    int storedRow = -1;

    public DrawingPanel() {
        setVisible(true);
        setSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
        b1.createBoard();
    }

    @Override
    public void paint(Graphics g) {
        //todo визуал
        Graphics2D g2d = (Graphics2D) g;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        g2d.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        String imagePath = "img/abc.png";
        try {
            BufferedImage myPicture = ImageIO.read(new File(imagePath));
            ImageObserver obs = new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int info, int x, int y, int width, int height) {
                    return false;
                }
            };
            g2d.drawImage(myPicture, SQUARE_SIZE, MAX_HEIGHT - SQUARE_SIZE, obs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {

                paintSquare(g2d, model[i][j], model[i][j].getClr());
                if (model[i][j].getStatus() != null) {
                    paintPawn(g2d, model[i][j]);
                }
            }
        }
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
        g2d.drawRect(SQUARE_SIZE + (sqr.getYStart() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        g2d.fillRect(SQUARE_SIZE + (sqr.getYStart() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    //todo отрисовка дамки
    public void paintPawn(Graphics g, Square sqr) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.drawOval(SQUARE_SIZE + (sqr.getYStart() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        if (sqr.status.getTeam() == Color.WHITE) {
            g2d.setColor(Color.WHITE);
        } else if (sqr.status.getTeam() == Color.BLACK) {
            g2d.setColor(Color.RED);
        }
        g2d.fillOval(SQUARE_SIZE + (sqr.getYStart() - 65) * SQUARE_SIZE, SQUARE_SIZE + (model.length - sqr.getX()) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = (e.getX() - 65) / 60;
        int row = (570 - e.getY()) / 60;

        if (!isChosen) {
            isChosen = true;
            displayAvailableMoves(row, col, model[row][col].status.team);
            storedCol = col;
            storedRow = row;
        } else {
            isChosen = false;
            if (col == storedCol && storedRow == row) {
                restoreBoard();
                repaint();
                return;

            }
            b1.moveInitialization(moves, col, row, storedCol, storedRow);
            model = b1.getBoard();
            restoreBoard();
            repaint();
            moves++;
        }
        System.out.println(col + " " + row);

    }

    private void displayAvailableMoves(int row, int col, Color team) {
        int direction = (team == Color.BLACK) ? -1 : 1;
        Color rival = new Color(Math.abs(team.getRed() - 255), Math.abs(team.getRed() - 255), Math.abs(team.getRed() - 255));
        if (col == 0) {
            if (model[row + direction][col + 1].status == null) {
                model[row + direction][col + 1].setClr(Color.GREEN);
            }


        } else if (col == 7) {
            if (model[row + direction][col - 1].status == null) {
                model[row + direction][col - 1].setClr(Color.GREEN);
            }
        } else {
            if (model[row + direction][col + 1].status == null) {
                model[row + direction][col + 1].setClr(Color.GREEN);
            }
            if (model[row + direction][col - 1].status == null) {
                model[row + direction][col - 1].setClr(Color.GREEN);
            }


        }
        if (row < 6 && col < 6) {
            if (model[row + 1][col + 1].status != null) {
                if (Objects.equals(model[row + 1][col + 1].status.team, rival) && model[row + 2][col + 2].status == null) {
                    model[row + 2][col + 2].setClr(Color.PINK);
                }
            }
        }
        if (row < 6 && col > 1) {
            if (model[row + 1][col - 1].status != null) {
                if (Objects.equals(model[row + 1][col - 1].status.team, rival) && model[row + 2][col - 2].status == null) {
                    model[row + 2][col + 2].setClr(Color.PINK);
                }
            }
        }
        if (row > 1 && col < 6) {
            if (model[row - 1][col + 1].status != null) {
                if (Objects.equals(model[row - 1][col + 1].status.team, rival) && model[row - 2][col + 2].status == null) {
                    model[row - 2][col + 2].setClr(Color.PINK);
                }
            }
        }
        if (row > 1 && col > 1) {
            if (model[row - 1][col - 1].status != null) {
                if (Objects.equals(model[row - 1][col - 1].status.team, rival) && model[row - 2][col - 2].status == null) {
                    model[row - 2][col - 2].setClr(Color.PINK);
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

    Graphics2D g2d = new Graphics2D() {
        @Override
        public void draw(Shape s) {

        }

        @Override
        public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
            return false;
        }

        @Override
        public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {

        }

        @Override
        public void drawRenderedImage(RenderedImage img, AffineTransform xform) {

        }

        @Override
        public void drawRenderableImage(RenderableImage img, AffineTransform xform) {

        }

        @Override
        public void drawString(String str, int x, int y) {

        }

        @Override
        public void drawString(String str, float x, float y) {

        }

        @Override
        public void drawString(AttributedCharacterIterator iterator, int x, int y) {

        }

        @Override
        public void drawString(AttributedCharacterIterator iterator, float x, float y) {

        }

        @Override
        public void drawGlyphVector(GlyphVector g, float x, float y) {

        }

        @Override
        public void fill(Shape s) {

        }

        @Override
        public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
            return false;
        }

        @Override
        public GraphicsConfiguration getDeviceConfiguration() {
            return null;
        }

        @Override
        public void setComposite(Composite comp) {

        }

        @Override
        public void setPaint(Paint paint) {

        }

        @Override
        public void setStroke(Stroke s) {

        }

        @Override
        public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {

        }

        @Override
        public Object getRenderingHint(RenderingHints.Key hintKey) {
            return null;
        }

        @Override
        public void setRenderingHints(Map<?, ?> hints) {

        }

        @Override
        public void addRenderingHints(Map<?, ?> hints) {

        }

        @Override
        public RenderingHints getRenderingHints() {
            return null;
        }

        @Override
        public void translate(int x, int y) {

        }

        @Override
        public void translate(double tx, double ty) {

        }

        @Override
        public void rotate(double theta) {

        }

        @Override
        public void rotate(double theta, double x, double y) {

        }

        @Override
        public void scale(double sx, double sy) {

        }

        @Override
        public void shear(double shx, double shy) {

        }

        @Override
        public void transform(AffineTransform Tx) {

        }

        @Override
        public void setTransform(AffineTransform Tx) {

        }

        @Override
        public AffineTransform getTransform() {
            return null;
        }

        @Override
        public Paint getPaint() {
            return null;
        }

        @Override
        public Composite getComposite() {
            return null;
        }

        @Override
        public void setBackground(Color color) {

        }

        @Override
        public Color getBackground() {
            return null;
        }

        @Override
        public Stroke getStroke() {
            return null;
        }

        @Override
        public void clip(Shape s) {

        }

        @Override
        public FontRenderContext getFontRenderContext() {
            return null;
        }

        @Override
        public Graphics create() {
            return null;
        }

        @Override
        public Color getColor() {
            return null;
        }

        @Override
        public void setColor(Color c) {

        }

        @Override
        public void setPaintMode() {

        }

        @Override
        public void setXORMode(Color c1) {

        }

        @Override
        public Font getFont() {
            return null;
        }

        @Override
        public void setFont(Font font) {

        }

        @Override
        public FontMetrics getFontMetrics(Font f) {
            return null;
        }

        @Override
        public Rectangle getClipBounds() {
            return null;
        }

        @Override
        public void clipRect(int x, int y, int width, int height) {

        }

        @Override
        public void setClip(int x, int y, int width, int height) {

        }

        @Override
        public Shape getClip() {
            return null;
        }

        @Override
        public void setClip(Shape clip) {

        }

        @Override
        public void copyArea(int x, int y, int width, int height, int dx, int dy) {

        }

        @Override
        public void drawLine(int x1, int y1, int x2, int y2) {

        }

        @Override
        public void fillRect(int x, int y, int width, int height) {

        }

        @Override
        public void clearRect(int x, int y, int width, int height) {

        }

        @Override
        public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

        }

        @Override
        public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

        }

        @Override
        public void drawOval(int x, int y, int width, int height) {

        }

        @Override
        public void fillOval(int x, int y, int width, int height) {

        }

        @Override
        public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

        }

        @Override
        public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

        }

        @Override
        public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

        }

        @Override
        public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        }

        @Override
        public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        }

        @Override
        public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
            return false;
        }

        @Override
        public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
            return false;
        }

        @Override
        public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
            return false;
        }

        @Override
        public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
            return false;
        }

        @Override
        public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
            return false;
        }

        @Override
        public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
            return false;
        }

        @Override
        public void dispose() {

        }
    };
}
