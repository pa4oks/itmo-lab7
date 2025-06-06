package ru.se.ifmo.cli.include;

import com.google.inject.Injector;
import jakarta.inject.Inject;
import ru.se.ifmo.cli.Command;
import ru.se.ifmo.cli.CommandContainer;
import ru.se.ifmo.cli.Project;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CommandContainerImpl implements CommandContainer {
    private final Map<String, Command> container = new HashMap<>();
    private final Injector injector;
    private Project project;

    @Inject
    public CommandContainerImpl(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void register(String command, Class<? extends Command> commandClass) {
        if (command == null || command.isEmpty()) {
            throw new UnsupportedOperationException("Cannot add empty command");
        }
        if (container.containsKey(command)) {
            throw new UnsupportedOperationException("Command \"%s\" is already registered.".formatted(command));
        }

        try {
            Command commandInstance = injector.getInstance(commandClass);
            container.put(command, commandInstance);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create command instance of class %s".formatted(commandClass.getSimpleName()), e);
        }
    }

    @Override
    public Command get(String command) {
        if (!container.containsKey(command)) {
            throw new IllegalArgumentException("Command \"%s\" is not registered.".formatted(command));
        }
        return container.get(command);
    }

    @Override
    public String getNameByType(Class<?> commandType) {
        return container.entrySet().stream()
            .filter(entry -> commandType.isInstance(entry.getValue()))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Cannot find command of type: " + commandType));
    }

    @Override
    public boolean contains(String command) {
        return container.containsKey(command);
    }

    @Override
    public void forEach(Consumer<? super Command> action) {
        container.values().forEach(action);
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Nullable
    @Override
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommandContainerImpl other)) return false;
        return Objects.equals(container, other.container);
    }

    @Override
    public int hashCode() {
        return Objects.hash(container);
    }

    @Override
    public String toString() {
        return "CommandContainerImpl{" +
            "container=" + container +
            '}';
    }
}
