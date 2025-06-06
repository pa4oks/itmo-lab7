package ru.se.ifmo.cli;

public class TestCommand implements Command {
    public Project getProject() {
        return null;
    }

    public Class<?>[] getActionParams() {
        return new Class[0];
    }

    public Action getAction(Object... params) {
        return null;
    }

    public String getCaption() {
        return "test";
    }

    public String getMask() {
        return "test";
    }
}
