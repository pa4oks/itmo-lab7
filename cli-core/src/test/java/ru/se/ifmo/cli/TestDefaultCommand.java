package ru.se.ifmo.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestDefaultCommand {

    private Project project;
    private CommandContainer container;

    @BeforeEach
    void setup() {
        project = mock(Project.class);
        container = mock(CommandContainer.class);
        when(project.getCommands()).thenReturn(container);
    }

    @Test
    void getActionParams_singleAction_returnsParams() {
        SampleCommand cmd = new SampleCommand();
        cmd.setProject(project);

        Class<?>[] params = cmd.getActionParams();
        assertArrayEquals(new Class[]{String.class}, params);
    }

    @Test
    void getAction_validParam_returnsAction() {
        SampleCommand cmd = new SampleCommand();
        cmd.setProject(project);

        Action action = cmd.getAction("text");
        assertNotNull(action);
        assertDoesNotThrow(action::execute);
    }

    @Test
    void getAction_invalidParamType_returnsNull() {
        SampleCommand cmd = new SampleCommand();
        cmd.setProject(project);

        when(container.getNameByType(SampleCommand.class)).thenReturn("sample");
        when(container.get("sample")).thenReturn(cmd); // <--- это важно

        Action action = cmd.getAction(123); // Not String
        assertNull(action);
    }

    @Test
    void getAction_noCommandAction_returnsNull() {
        NoActionCommand cmd = new NoActionCommand();
        cmd.setProject(project);

        assertNull(cmd.getAction());
    }

    @Test
    void getAction_multipleCommandActions_throws() {
        MultiActionCommand cmd = new MultiActionCommand();
        cmd.setProject(project);

        assertThrows(IllegalStateException.class, cmd::getActionParams);
    }

    @Test
    void equalsAndHashCode_byCommandName_works() {
        when(container.getNameByType(SampleCommand.class)).thenReturn("sample");
        when(container.getNameByType(DuplicateCommand.class)).thenReturn("sample");

        SampleCommand c1 = new SampleCommand();
        DuplicateCommand c2 = new DuplicateCommand();
        c1.setProject(project);
        c2.setProject(project);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void toString_containsCommandName() {
        when(container.getNameByType(SampleCommand.class)).thenReturn("sample");

        SampleCommand c = new SampleCommand();
        c.setProject(project);

        assertEquals("sample command", c.toString());
    }

    @Test
    void getCaption_returnsCorrectCaption() {
        SampleCommand cmd = new SampleCommand();
        cmd.setProject(project);

        assertEquals("Sample caption", cmd.getCaption());
    }


    // --- Supporting test commands ---

    public static class SampleCommand extends DefaultCommand {
        @CommandAction
        public void action(String s) {
            System.out.println("Called: " + s);
        }

        @Override
        public String getCaption() {
            return "Sample caption";
        }
    }

    public static class DuplicateCommand extends DefaultCommand {
        @CommandAction
        public void action(String s) {}

        @Override
        public String getCaption() {
            return "";
        }
    }

    public static class NoActionCommand extends DefaultCommand {
        public void dummy() {}

        @Override
        public String getCaption() {
            return "";
        }
    }

    public static class MultiActionCommand extends DefaultCommand {
        @CommandAction
        public void a() {}

        @CommandAction
        public void b() {}

        @Override
        public String getCaption() {
            return "";
        }
    }
}
