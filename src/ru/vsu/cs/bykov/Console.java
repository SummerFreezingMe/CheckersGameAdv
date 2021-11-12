package ru.vsu.cs.bykov;

public class Console {
    public void startGame() {
        Board board = new Board();
        board.createBoard();
        int moves = 0;
        System.out.println("Белые ходят первыми!");
        boolean endgame=false;

            while (!endgame) {
                board.drawBoard();
                endgame=board.moveInitialization(moves);
                moves++;
            }
            board.messenger(5, moves);
        }
}
