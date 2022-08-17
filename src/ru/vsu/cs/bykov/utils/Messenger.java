package ru.vsu.cs.bykov.utils;

public class Messenger {
    private final String firstPlayerName;
    private final String secondPlayerName;

    public Messenger(String firstName, String secondName) {
        this.firstPlayerName = firstName;
        this.secondPlayerName = secondName;
    }


    public void messenger(final GameStatus n, int moves) {
        String name = (moves % 2 == 0) ? firstPlayerName : secondPlayerName;
        int msg = n.ordinal();
        switch (msg) {
            case (0) -> System.out.println(name + ", choose a square: ");
            case (1) -> System.out.println(name + ", choose a move: ");
            case (2) -> System.out.println(name + ", this move is not possible ");
            case (3) -> System.out.println(name + ", this cell is not available ");
            case (4) -> System.out.println(name + "The game is over for " + moves + " moves, the winner - " + name);
            default -> throw new IllegalStateException("Unexpected value: " + n);
        }

    }
}