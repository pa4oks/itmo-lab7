package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.cli.Command;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;

public class HelloCommand extends DefaultCommand implements Command {
    @CommandAction
    public void run() {
        System.out.println("Hello, world!");
    }

    @Override
    public String getCaption() {
        return "Say hello";
    }
}
