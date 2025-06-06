package ru.se.ifmo.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.se.ifmo.cli.include.CommandContainerImpl;
import ru.se.ifmo.cli.include.ProjectImpl;
import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.db.DatabaseManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectImplTest {
    private CommandContainer container;
    private Parser parser;
    private DatabaseManager<?> databaseManager;
    private CollectionManager<?> collectionManager;

    @BeforeEach
    void setup() {
        container = mock(CommandContainer.class);
        parser = mock(Parser.class);
        databaseManager = mock(DatabaseManager.class);
        collectionManager = mock(CollectionManager.class);
    }

    @Test
    void constructor_assignsAllFields() {
        ProjectImpl project = new ProjectImpl(container, parser, databaseManager, collectionManager);

        assertSame(container, project.getCommands());
        assertSame(parser, project.getParser());
        assertSame(databaseManager, project.getDatabaseController());
        assertSame(collectionManager, project.getCollectionController());
    }

    @Test
    void constructor_setsProjectInContainerImpl() {
        CommandContainerImpl impl = mock(CommandContainerImpl.class);
        ProjectImpl project = new ProjectImpl(impl, parser, databaseManager, collectionManager);

        verify(impl).setProject(project);
    }

    @Test
    void constructor_nullCommandContainer_throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
            new ProjectImpl(null, parser, databaseManager, collectionManager));
        assertEquals("commandContainer must not be null", ex.getMessage());
    }

    @Test
    void constructor_nullParser_throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
            new ProjectImpl(container, null, databaseManager, collectionManager));
        assertEquals("parser must not be null", ex.getMessage());
    }

    @Test
    void constructor_nullDatabaseController_throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
            new ProjectImpl(container, parser, null, collectionManager));
        assertEquals("databaseController must not be null", ex.getMessage());
    }

    @Test
    void constructor_nullCollectionController_throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
            new ProjectImpl(container, parser, databaseManager, null));
        assertEquals("collectionController must not be null", ex.getMessage());
    }

    @Test
    void equals_sameInstance_returnsTrue() {
        ProjectImpl project = new ProjectImpl(container, parser, databaseManager, collectionManager);
        assertEquals(project, project);
    }

    @Test
    void equals_null_returnsFalse() {
        ProjectImpl project = new ProjectImpl(container, parser, databaseManager, collectionManager);
        assertNotEquals(null, project);
    }

    @Test
    void equals_differentType_returnsFalse() {
        ProjectImpl project = new ProjectImpl(container, parser, databaseManager, collectionManager);
        assertNotEquals("some string", project);
    }

    @Test
    void equals_equalFields_returnsTrue() {
        ProjectImpl p1 = new ProjectImpl(container, parser, databaseManager, collectionManager);
        ProjectImpl p2 = new ProjectImpl(container, parser, databaseManager, collectionManager);
        assertEquals(p1, p2);
    }

    @Test
    void equals_differentCommandContainer_returnsFalse() {
        ProjectImpl p1 = new ProjectImpl(mock(CommandContainer.class), parser, databaseManager, collectionManager);
        ProjectImpl p2 = new ProjectImpl(mock(CommandContainer.class), parser, databaseManager, collectionManager);
        assertNotEquals(p1, p2);
    }

    @Test
    void equals_differentParser_returnsFalse() {
        ProjectImpl p1 = new ProjectImpl(container, mock(Parser.class), databaseManager, collectionManager);
        ProjectImpl p2 = new ProjectImpl(container, mock(Parser.class), databaseManager, collectionManager);
        assertNotEquals(p1, p2);
    }

    @Test
    void equals_differentDbController_returnsFalse() {
        ProjectImpl p1 = new ProjectImpl(container, parser, mock(DatabaseManager.class), collectionManager);
        ProjectImpl p2 = new ProjectImpl(container, parser, mock(DatabaseManager.class), collectionManager);
        assertNotEquals(p1, p2);
    }

    @Test
    void equals_differentCollectionController_returnsFalse() {
        ProjectImpl p1 = new ProjectImpl(container, parser, databaseManager, mock(CollectionManager.class));
        ProjectImpl p2 = new ProjectImpl(container, parser, databaseManager, mock(CollectionManager.class));
        assertNotEquals(p1, p2);
    }

    @Test
    void hashCode_consistentWithEquals() {
        ProjectImpl p1 = new ProjectImpl(container, parser, databaseManager, collectionManager);
        ProjectImpl p2 = new ProjectImpl(container, parser, databaseManager, collectionManager);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void toString_containsAllFields() {
        ProjectImpl project = new ProjectImpl(container, parser, databaseManager, collectionManager);
        String str = project.toString();
        assertTrue(str.contains("commandContainer"));
        assertTrue(str.contains("parser"));
        assertTrue(str.contains("databaseController"));
        assertTrue(str.contains("collectionController"));
    }
}
