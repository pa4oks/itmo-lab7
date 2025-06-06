package ru.se.ifmo.cli;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class DefaultCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(DefaultCommand.class);

    private CommandsScanner scanner;
    private Project project;

    protected DefaultCommand() {
    }

    @Inject
    public void setProject(Project project) {
        this.project = Objects.requireNonNull(project);
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public String getMask() {
        return project.getCommands().getNameByType(getClass());
    }

    public CommandsScanner getScanner() {
        if (scanner == null) {
            scanner = new CommandsScanner(System.in);
        }
        return scanner;
    }

    public <T> T requestInput(Supplier<T> supplier, String prompt, String errorMessage) {
        log.info(prompt);
        while (true) {
            try {
                return supplier.get();
            } catch (Exception ex) {
                log.warn("Input error: {}", ex.toString());
                log.info("{}\n{}", errorMessage, prompt);
            }
        }
    }

    public <T> T nullableInput(Function<String, T> parser, String prompt, String errorMessage) {
        log.info(prompt);
        while (true) {
            try {
                String input = getScanner().nextCommand().trim();
                if (input.equalsIgnoreCase("null") || input.isEmpty()) {
                    return null;
                }
                return parser.apply(input);
            } catch (Exception ex) {
                log.warn("Input error: {}", ex.toString());
                log.info("{}\n{}", errorMessage, prompt);
            }
        }
    }

    public Field promptForField(Class<?> targetClass) {
        List<Field> fields = Arrays.stream(targetClass.getDeclaredFields())
                .filter(f -> !Set.of("id", "creationDate").contains(f.getName()))
                .toList();

        while (true) {
            log.info("Available fields for {}:", targetClass.getSimpleName());
            fields.forEach(f -> log.info("- {}", f.getName()));
            log.info("Enter field name:");
            String input = getScanner().nextCommand();

            Optional<Field> selected = fields.stream()
                    .filter(f -> f.getName().equals(input))
                    .findFirst();

            if (selected.isPresent()) {
                return selected.get();
            }

            log.warn("Field '{}' not found. Try again.", input);
        }
    }

    public <T> T requestInputNullable(Supplier<T> supplier, String prompt, String error) {
        return requestInput(() -> {
            String raw = getScanner().nextCommand();
            if ("null".equalsIgnoreCase(raw)) return null;
            return supplier.get();
        }, prompt, error);
    }

    @Override
    public Class<?>[] getActionParams() {
        return findActionMethod()
                .map(Method::getParameterTypes)
                .orElse(new Class<?>[0]);
    }

    @Override
    public Action getAction(Object... parameters) {
        Optional<Method> methodOpt = findActionMethod();
        if (methodOpt.isEmpty()) {
            log.warn("No @CommandAction method found in {}", getClass().getSimpleName());
            return null;
        }

        Method method = methodOpt.get();
        if (!matchParameterTypes(method.getParameterTypes(), parameters)) {
            warnInvalidParameters(method);
            return null;
        }

        return createAction(method, parameters);
    }

    private Optional<Method> findActionMethod() {
        List<Method> actions = Arrays.stream(getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(CommandAction.class))
                .toList();

        if (actions.size() > 1) {
            throw new IllegalStateException(
                    "Command class may not contain more than one method annotated with @CommandAction");
        }

        return actions.stream().findFirst();
    }

    private boolean matchParameterTypes(Class<?>[] expected, Object[] actual) {
        if (expected.length != actual.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].isAssignableFrom(actual[i].getClass())) {
                return false;
            }
        }
        return true;
    }

    private void warnInvalidParameters(Method method) {
        String command = project.getCommands().getNameByType(getClass());
        log.warn("Invalid parameters passed to command '{}'", command);
        log.info("Expected method signature: {}({})", method.getName(), Arrays.toString(method.getParameterTypes()));
        log.info("Usage: {}", project.getCommands().get(command).getMask());
    }

    private Action createAction(Method method, Object... args) {
        method.setAccessible(true);
        return new Action() {
            @Override
            public void execute() {
                try {
                    method.invoke(DefaultCommand.this, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Failed to invoke action method", e);
                    throw new RuntimeException("Command execution failed", e);
                }
            }

            @Override
            public Command getCommand() {
                return DefaultCommand.this;
            }

            @Override
            public LocalDateTime getCreationDate() {
                return LocalDateTime.now();
            }
        };
    }

    @Override
    public String toString() {
        return getProject().getCommands().getNameByType(getClass()) + " command";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DefaultCommand other)) return false;
        return Objects.equals(
                getProject().getCommands().getNameByType(getClass()),
                getProject().getCommands().getNameByType(other.getClass())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProject().getCommands().getNameByType(getClass()));
    }
}
