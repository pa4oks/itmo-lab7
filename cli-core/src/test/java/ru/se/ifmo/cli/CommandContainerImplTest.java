package ru.se.ifmo.cli;

import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.se.ifmo.cli.include.CommandContainerImpl;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandContainerImplTest {

    private Injector injector;
    private CommandContainerImpl container;

    @BeforeEach
    void setup() {
        injector = mock(Injector.class);
        container = new CommandContainerImpl(injector);
    }

    @Test
    void registerAndGetCommand_success() {
        TestCommand cmdInstance = new TestCommand();
        when(injector.getInstance(TestCommand.class)).thenReturn(cmdInstance);

        container.register("test", TestCommand.class);
        Command retrieved = container.get("test");

        assertSame(cmdInstance, retrieved);
    }

    @Test
    void register_duplicate_throwsException() {
        when(injector.getInstance(DummyCommand.class)).thenReturn(new DummyCommand());

        container.register("cmd", DummyCommand.class);
        assertThrows(UnsupportedOperationException.class, () -> container.register("cmd", DummyCommand.class));
    }

    @Test
    void get_unknownCommand_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> container.get("missing"));
    }

    @Test
    void contains_returnsCorrectResult() {
        DummyCommand dummy = new DummyCommand();
        when(injector.getInstance(DummyCommand.class)).thenReturn(dummy);

        assertFalse(container.contains("mycmd"));
        container.register("mycmd", DummyCommand.class);
        assertTrue(container.contains("mycmd"));
    }

    @Test
    void getNameByType_findsCorrectName() {
        DummyCommand dummy = new DummyCommand();
        when(injector.getInstance(DummyCommand.class)).thenReturn(dummy);

        container.register("mycmd", DummyCommand.class);
        assertEquals("mycmd", container.getNameByType(DummyCommand.class));
    }

    @Test
    void getNameByType_throwsIfTypeMissing() {
        assertThrows(IllegalArgumentException.class, () -> container.getNameByType(DummyCommand.class));
    }

    @Test
    void forEach_appliesActionToAll() {
        DummyCommand dummy = new DummyCommand();
        when(injector.getInstance(DummyCommand.class)).thenReturn(dummy);

        container.register("dummy", DummyCommand.class);

        AtomicBoolean called = new AtomicBoolean(false);
        container.forEach(cmd -> {
            assertSame(dummy, cmd);
            called.set(true);
        });
        assertTrue(called.get());
    }

    @Test
    void isEmpty_reflectsCommandState() {
        assertTrue(container.isEmpty());
        when(injector.getInstance(DummyCommand.class)).thenReturn(new DummyCommand());
        container.register("some", DummyCommand.class);
        assertFalse(container.isEmpty());
    }

    @Test
    void equals_and_hashCode_workProperly() {
        Injector anotherInjector = mock(Injector.class);
        CommandContainerImpl otherContainer = new CommandContainerImpl(anotherInjector);

        DummyCommand dummy = new DummyCommand();
        when(injector.getInstance(DummyCommand.class)).thenReturn(dummy);
        when(anotherInjector.getInstance(DummyCommand.class)).thenReturn(dummy);

        container.register("x", DummyCommand.class);
        otherContainer.register("x", DummyCommand.class);

        assertEquals(container, otherContainer);
        assertEquals(container.hashCode(), otherContainer.hashCode());
    }

    @Test
    void toString_containsCommandName() {
        DummyCommand dummy = new DummyCommand();
        when(injector.getInstance(DummyCommand.class)).thenReturn(dummy);
        container.register("x", DummyCommand.class);

        String str = container.toString();
        assertTrue(str.contains("x"));
    }

    @Test
    void register_nullCommandName_throwsException() {
        assertThrows(UnsupportedOperationException.class, () ->
            container.register(null, DummyCommand.class));
    }

    @Test
    void register_emptyCommandName_throwsException() {
        assertThrows(UnsupportedOperationException.class, () ->
            container.register("", DummyCommand.class));
    }

    @Test
    void register_injectorThrows_exceptionPropagatedAsIllegalArgument() {
        when(injector.getInstance(DummyCommand.class))
            .thenThrow(new RuntimeException("failed"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            container.register("bad", DummyCommand.class));

        assertTrue(ex.getMessage().contains("Failed to create command instance"));
    }

    @Test
    void setAndGetProject_worksCorrectly() {
        Project mockProject = mock(Project.class);
        container.setProject(mockProject);
        assertSame(mockProject, container.getProject());
    }

    @Test
    void equals_returnsFalseForDifferentContainers() {
        Injector anotherInjector = mock(Injector.class);
        CommandContainerImpl other = new CommandContainerImpl(anotherInjector);

        when(injector.getInstance(DummyCommand.class)).thenReturn(new DummyCommand());
        when(anotherInjector.getInstance(DummyCommand.class)).thenReturn(new DummyCommand());

        container.register("a", DummyCommand.class);
        other.register("b", DummyCommand.class);

        assertNotEquals(container, other);
    }

    @Test
    void hashCode_differsForDifferentContents() {
        Injector anotherInjector = mock(Injector.class);
        CommandContainerImpl other = new CommandContainerImpl(anotherInjector);

        when(injector.getInstance(DummyCommand.class)).thenReturn(new DummyCommand());
        when(anotherInjector.getInstance(DummyCommand.class)).thenReturn(new DummyCommand());

        container.register("a", DummyCommand.class);
        other.register("b", DummyCommand.class);

        assertNotEquals(container.hashCode(), other.hashCode());
    }
}

