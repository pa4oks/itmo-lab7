package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.cli.CommandAction;

public class ListEntitiesCommand extends DefaultCommand {
    @CommandAction
    public void run() {
        var controller = getProject().getCollectionController();
        controller.getCollection().forEach(e ->
            System.out.println("[ENTITY] " + ((DemoEntity) e).getName())
        );
    }

    @Override public String getCaption() { return "List all entities"; }
}

