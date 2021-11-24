package ru.vsu.cs.bykov;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameLog {
    private final List<String> log = new ArrayList<>();

    public void writeMove(int moves, String hit, String peak, String playerName) {
        if(moves+1<log.size()){
            log.remove(log.size()-1);
        }
        log.add("Move №" + (moves+1) + " by " + playerName + ": " + peak + " to " + hit);
    }

    protected static File createFile(String name) {
        return new File(name + ".txt");
    }

    protected void writeOutput() {
        File object = createFile("test");
        try {
            PrintWriter print = new PrintWriter(object);
            for (String strings : log) {
                print.print(strings + "\n");
            }
            print.close();
        } catch (
                IOException e) {
            System.out.println("Couldn't write result");
        }

    }
}
