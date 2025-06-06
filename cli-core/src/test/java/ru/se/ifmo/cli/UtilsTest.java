package ru.se.ifmo.cli;

import org.junit.jupiter.api.Test;
import ru.se.ifmo.cli.include.Utils;
import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.db.SQLConnectionController;
import ru.se.ifmo.db.entity.Entity;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    static class SetterInjection {
        private Project injected;

        public void setProject(Project project) {
            this.injected = project;
        }
    }

    static class FieldInjection {
        private Project project;
    }

    static class NoInjectionPoint {
        private String name;
    }

    @Test
    void injectProject_viaSetter_success() {
        SetterInjection target = new SetterInjection();
        Project project = new MockProject();

        Utils.injectProject(project, target);

        assertSame(project, target.injected);
    }

    @Test
    void injectProject_viaField_success() throws Exception {
        FieldInjection target = new FieldInjection();
        Project project = new MockProject();

        Utils.injectProject(project, target);

        Field field = FieldInjection.class.getDeclaredField("project");
        field.setAccessible(true);
        Object value = field.get(target);

        assertSame(project, value);
    }

    @Test
    void injectProject_noSetterOrField_throws() {
        NoInjectionPoint target = new NoInjectionPoint();
        Project project = new MockProject();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
            Utils.injectProject(project, target)
        );

        assertTrue(ex.getMessage().contains("No setter or field found"));
    }

    @Test
    void injectProject_nullTarget_throws() {
        Project project = new MockProject();
        assertThrows(NullPointerException.class, () -> Utils.injectProject(project, null));
    }

    @Test
    void injectProject_nullProject_throws() {
        SetterInjection target = new SetterInjection();
        assertThrows(NullPointerException.class, () -> Utils.injectProject(null, target));
    }

    static class MockProject implements Project {
        @Override public ru.se.ifmo.cli.CommandContainer getCommands() { return null; }
        @Override public ru.se.ifmo.cli.Parser getParser() { return null; }
        @Override public <T extends CollectionManager<?>> DatabaseManager<T> getDatabaseController() { return null; }
        @Override public <T extends Entity> CollectionManager<T> getCollectionController() { return null; }
    }
}

