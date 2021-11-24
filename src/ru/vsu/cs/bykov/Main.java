package ru.vsu.cs.bykov;

public class Main {

    public static void main(String[] args) {
  CheckersFrame checkersFrame = new CheckersFrame();
      checkersFrame.createPad();
        Console consoleGame = new Console();
        consoleGame.startGame();


    }
}
