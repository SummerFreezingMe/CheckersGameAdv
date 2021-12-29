package ru.vsu.cs.bykov.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
    public static List<String> getStrategy(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<String> strategies = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] positions = line.split("(--)|(:)");
            strategies.add(positions[0].toUpperCase()+" " +positions[1].toUpperCase());
        }
        scanner.close();
        return strategies;
    }
}