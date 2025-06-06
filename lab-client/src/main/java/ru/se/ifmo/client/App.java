package ru.se.ifmo.client;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.se.ifmo.cli.Application;
import ru.se.ifmo.cli.CommandsScanner;
import ru.se.ifmo.cli.Project;
import ru.se.ifmo.cli.include.ProjectModule;
import ru.se.ifmo.client.command.AddCommand;
import ru.se.ifmo.client.command.ClearCommand;
import ru.se.ifmo.client.command.CountGreaterThanMinimalPointCommand;
import ru.se.ifmo.client.command.ExecuteScriptCommand;
import ru.se.ifmo.client.command.HeadCommand;
import ru.se.ifmo.client.command.InfoCommand;
import ru.se.ifmo.client.command.RemoveAnyByAuthorCommand;
import ru.se.ifmo.client.command.RemoveByIdCommand;
import ru.se.ifmo.client.command.RemoveFirstCommand;
import ru.se.ifmo.client.command.RemoveLowerCommand;
import ru.se.ifmo.client.command.ShowCommand;
import ru.se.ifmo.client.command.HelpCommand;
import ru.se.ifmo.client.command.UpdateIdCommand;
import ru.se.ifmo.client.db.LabWorkCollectionManager;
import ru.se.ifmo.client.db.LabWorkDatabaseManager;

public class App extends Application {
    private static final NioEndpointClient client = new NioEndpointClient("localhost", 8080);

    private static final LabWorkCollectionManager collectionManager = new LabWorkCollectionManager(client);;
    private static final LabWorkDatabaseManager databaseManager = new LabWorkDatabaseManager(client);;

    @Override
    public void configure(Project project) {
        project.getCommands().register("help", HelpCommand.class);
        project.getCommands().register("clear", ClearCommand.class);
        project.getCommands().register("show", ShowCommand.class);
        project.getCommands().register("add", AddCommand.class);
        project.getCommands().register("info", InfoCommand.class);
        project.getCommands().register("head", HeadCommand.class);
        project.getCommands().register("update", UpdateIdCommand.class);
        project.getCommands().register("count_greater_than_minimal_point", CountGreaterThanMinimalPointCommand.class);
        project.getCommands().register("remove_any_by_author", RemoveAnyByAuthorCommand.class);
        project.getCommands().register("remove_by_id", RemoveByIdCommand.class);
        project.getCommands().register("remove_first", RemoveFirstCommand.class);
        project.getCommands().register("remove_lower", RemoveLowerCommand.class);
        project.getCommands().register("execute_script", ExecuteScriptCommand.class);
    }

    public static void main(String[] args) {
        App app = launch(new ProjectModule(collectionManager, databaseManager) {
            @Provides
            @Singleton
            public NioEndpointClient provideConnection() {
                return client;
            }
        });
        databaseManager.load(collectionManager);
        CommandsScanner scanner = new CommandsScanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
            String[] command = scanner.nextCommand().split(" ");
            execute(command);
        }
    }

    @Override
    public LabWorkDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public LabWorkCollectionManager getCollectionManager() {
        return collectionManager;
    }
}
