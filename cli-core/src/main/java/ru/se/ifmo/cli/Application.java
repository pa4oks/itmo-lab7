package ru.se.ifmo.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.include.ProjectModule;
import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.db.entity.Entity;

import java.util.Arrays;
import java.util.Objects;

/**
 * Base class for a command-line app.
 *
 * <p>Invoke {@link #launch()} from your {@code main(...)} method to
 * locate the concrete subclass, set up Guice injection, and run
 * hooks in this order:
 * <ol>
 *   <li>{@link #beforeConfigure()}</li>
 *   <li>{@link #configure(Project)}</li>
 *   <li>{@link #afterConfigure(Project)}</li>
 * </ol>
 * Use {@link #execute(String[])} to parse user input and execute commands.</p>
 */
public abstract class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static Project project;
    private static Injector injector;

    /**
     * @param <T> collection controller type
     * @return database controller for the app
     */
    public abstract <T extends CollectionManager<?>> DatabaseManager<T> getDatabaseManager();

    /**
     * @param <T> entity type
     * @return collection controller for the app
     */
    public abstract <T extends Entity> CollectionManager<T> getCollectionManager();

    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        Application.injector = injector;
    }

    public static <T extends Application> T launch() {
        return launch(null);
    }

    /**
     * Initializes and configures the app. Call from your main().
     */
    public static <T extends Application> T launch(Module module) {
        logger.info("Starting application launch");
        T app = getInstance();
        app.beforeConfigure();

        logger.debug("Creating Guice injector");
        Injector injector;
        injector = Guice.createInjector(Objects.requireNonNullElseGet(module,
                () -> new ProjectModule(app.getCollectionManager(),
                app.getDatabaseManager())));

        Application.injector = injector;
        project = injector.getInstance(Project.class);

        logger.info("Configuring application");
        app.configure(project);
        app.afterConfigure(project);
        logger.info("Application launch complete");
        return app;
    }

    /**
     * Parses command-line tool args and executes the resulting actions.
     *
     * @param args user input arguments
     */
    public static void execute(String[] args) {
        Action[] actions;
        try {
            actions = project.getParser().parse(args);
        } catch (UnknownCommandException e) {
            logger.warn("Unknown command '{}'", e.getUnknownCommand());
            return;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid command format; use 'help' for usage");
            return;
        }

        for (Action action : actions) {
            try {
                action.execute();
            } catch (Exception e) {
                String name = project.getCommands()
                        .getNameByType(action.getCommand().getClass());
                logger.error("Error executing command '{}'", name);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Application> T getInstance() {
        String className = determineApplicationClass();
        try {
            Class<?> cls = Class.forName(className, false,
                    Thread.currentThread().getContextClassLoader());
            if (!Application.class.isAssignableFrom(cls)) {
                throw new RuntimeException(cls + " is not a subclass of Application");
            }
            Class<? extends Application> appClass = (Class<? extends Application>) cls;
            return (T) appClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            logger.error("Failed to instantiate Application subclass", ex);
            throw new RuntimeException(ex);
        }
    }

    private static String determineApplicationClass() {
        boolean foundLaunch = false;
        for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
            if (foundLaunch) {
                return se.getClassName();
            }
            if (Application.class.getName().equals(se.getClassName())
                    && "launch".equals(se.getMethodName())) {
                foundLaunch = true;
            }
        }
        throw new RuntimeException("Unable to determine Application subclass");
    }

    /**
     * Hook before Guice configuration runs.
     */
    public void beforeConfigure() {
    }

    /**
     * Application configuration hook.
     */
    public void configure(Project project) {
    }

    /**
     * Hook after app is fully configured.
     */
    public void afterConfigure(Project project) {
    }
}
