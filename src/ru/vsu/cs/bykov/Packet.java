package ru.vsu.cs.bykov;

public class Packet {
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    String command;

    public boolean contains() {
        return command != null;
    }

    public void flush() {
        command=null;
    }
}
