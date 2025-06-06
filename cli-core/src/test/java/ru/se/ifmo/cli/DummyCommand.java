package ru.se.ifmo.cli;

public class DummyCommand implements Command {
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
        return "";
    }

    public String getMask() {
        return "";
    }
}
