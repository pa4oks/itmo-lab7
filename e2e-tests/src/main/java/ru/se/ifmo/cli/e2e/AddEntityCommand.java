package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.cli.Command;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.cli.CommandAction;

public class AddEntityCommand extends DefaultCommand {
    @CommandAction
    public void run(String name) {
        // fetch the in-memory controller from the project
        var controller = getProject().getCollectionController();
        controller.add(new DemoEntity(name));
        System.out.println("added: " + name);
    }

    @Override public String getCaption() { return "Add an entity"; }
}
