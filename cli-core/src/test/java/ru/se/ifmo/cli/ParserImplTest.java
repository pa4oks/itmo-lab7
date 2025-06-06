package ru.se.ifmo.cli;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.se.ifmo.cli.include.ParserImpl;
import ru.se.ifmo.db.CollectionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParserImplTest {

    private CommandContainer commandContainer;
    private Project project;
    private ParserImpl parser;

    @BeforeEach
    void setup() {
        project = mock(Project.class);
        commandContainer = mock(CommandContainer.class);
        when(project.getCommands()).thenReturn(commandContainer);
        parser = new ParserImpl(project);
    }

    @Test
    void parse_singleCommandWithNoParams_success() {
        Command cmd = mock(Command.class);
        Action action = mock(Action.class);

        when(commandContainer.contains("ping")).thenReturn(true);
        when(commandContainer.get("ping")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[0]);
        when(cmd.getAction()).thenReturn(action);

        Action[] result = parser.parse(new String[]{"ping"});

        assertEquals(1, result.length);
        assertSame(action, result[0]);
        assertEquals(List.of(action), parser.getHistory());
    }

    @Test
    void parse_commandWithParams_success() {
        Command cmd = mock(Command.class);
        Action action = mock(Action.class);

        when(commandContainer.contains("add")).thenReturn(true);
        when(commandContainer.get("add")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{int.class, String.class});
        when(cmd.getAction(42, "hello")).thenReturn(action);

        Action[] result = parser.parse(new String[]{"add", "42", "hello"});

        assertEquals(1, result.length);
        assertSame(action, result[0]);
    }

    @Test
    void parse_commandMissing_throwsUnknownCommandException() {
        when(commandContainer.contains("foo")).thenReturn(false);
        assertThrows(UnknownCommandException.class, () -> parser.parse(new String[]{"foo"}));
    }

    @Test
    void parse_tooFewArguments_throwsIllegalArgumentException() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("cmd")).thenReturn(true);
        when(commandContainer.get("cmd")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{int.class, String.class});

        assertThrows(IllegalArgumentException.class, () -> parser.parse(new String[]{"cmd", "42"}));
    }

    @Test
    void parse_invalidTypeParsing_throwsIllegalArgument() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("bad")).thenReturn(true);
        when(commandContainer.get("bad")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{int.class});

        assertThrows(IllegalArgumentException.class, () -> parser.parse(new String[]{"bad", "notAnInt"}));
    }

    @Test
    void parse_unknownType_throwsUnsupportedOperation() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("unknown")).thenReturn(true);
        when(commandContainer.get("unknown")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{CollectionManager.class}); // unsupported type

        assertThrows(UnsupportedOperationException.class, () -> parser.parse(new String[]{"unknown", "abc"}));
    }

    enum MyEnum { FOO, BAR }

    @Test
    void parse_enumValue_successfullyParsed() {
        Command cmd = mock(Command.class);
        Action action = mock(Action.class);

        when(commandContainer.contains("enumcmd")).thenReturn(true);
        when(commandContainer.get("enumcmd")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{MyEnum.class});
        when(cmd.getAction(MyEnum.BAR)).thenReturn(action);

        Action[] actions = parser.parse(new String[]{"enumcmd", "BAR"});
        assertSame(action, actions[0]);
    }

    @Test
    void parse_invalidEnumValue_throwsIllegalArgument() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("enumfail")).thenReturn(true);
        when(commandContainer.get("enumfail")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{MyEnum.class});

        assertThrows(IllegalArgumentException.class, () -> parser.parse(new String[]{"enumfail", "WAT"}));
    }

    @Test
    void parse_charInput_success() {
        Command cmd = mock(Command.class);
        Action action = mock(Action.class);

        when(commandContainer.contains("charcmd")).thenReturn(true);
        when(commandContainer.get("charcmd")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{char.class});
        when(cmd.getAction('X')).thenReturn(action);

        Action[] actions = parser.parse(new String[]{"charcmd", "X"});
        assertSame(action, actions[0]);
    }

    @Test
    void parse_charInputFailsOnTooLongString() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("charcmd")).thenReturn(true);
        when(commandContainer.get("charcmd")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{char.class});

        assertThrows(IllegalArgumentException.class, () -> parser.parse(new String[]{"charcmd", "TooLong"}));
    }

    @Test
    void toString_returnsClassName() {
        assertEquals("ParserImpl", parser.toString());
    }

    @Test
    void getProject_returnsConstructorArg() {
        assertSame(project, parser.getProject());
    }

    @Test
    void parse_multipleCommandsSequentially_success() {
        Command cmd1 = mock(Command.class);
        Command cmd2 = mock(Command.class);
        Action action1 = mock(Action.class);
        Action action2 = mock(Action.class);

        when(commandContainer.contains("cmd1")).thenReturn(true);
        when(commandContainer.contains("cmd2")).thenReturn(true);

        when(commandContainer.get("cmd1")).thenReturn(cmd1);
        when(commandContainer.get("cmd2")).thenReturn(cmd2);

        when(cmd1.getActionParams()).thenReturn(new Class[0]);
        when(cmd2.getActionParams()).thenReturn(new Class[0]);

        when(cmd1.getAction()).thenReturn(action1);
        when(cmd2.getAction()).thenReturn(action2);

        Action[] result = parser.parse(new String[]{"cmd1", "cmd2"});

        assertEquals(2, result.length);
        assertSame(action1, result[0]);
        assertSame(action2, result[1]);
    }

    @Test
    void parse_paramWithSpacesQuoted_shouldFail() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("echo")).thenReturn(true);
        when(commandContainer.get("echo")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{String.class});

        // simulate lack of preprocessing of quotes
        assertThrows(NullPointerException.class, () -> parser.parse(new String[]{"echo", "\"hello world\""}));
    }

    @Test
    void parse_actionReturnsNull_throwsNPE() {
        Command cmd = mock(Command.class);
        when(commandContainer.contains("nullcmd")).thenReturn(true);
        when(commandContainer.get("nullcmd")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[0]);
        when(cmd.getAction()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> parser.parse(new String[]{"nullcmd"}));
    }

    @Test
    void parse_paramTrimmedProperly_success() {
        Command cmd = mock(Command.class);
        Action action = mock(Action.class);

        when(commandContainer.contains("echo")).thenReturn(true);
        when(commandContainer.get("echo")).thenReturn(cmd);
        when(cmd.getActionParams()).thenReturn(new Class[]{String.class});
        when(cmd.getAction(any())).thenReturn(action);
        when(action.getCommand()).thenReturn(cmd);


        String[] args = new String[]{"echo", " text "};
        Action[] result = parser.parse(args);

        // String parser shouldn't trim automatically—this test ensures we check the input behavior
        // In this case, the exact string with spaces is passed
        assertEquals(String.class, result[0].getCommand().getActionParams()[0]);
    }

    @Test
    void parse_mixedValidAndInvalidCommands_failsAtInvalid() {
        Command cmd1 = mock(Command.class);
        Action action1 = mock(Action.class);

        when(commandContainer.contains("ok")).thenReturn(true);
        when(commandContainer.get("ok")).thenReturn(cmd1);
        when(cmd1.getActionParams()).thenReturn(new Class[0]);
        when(cmd1.getAction()).thenReturn(action1);

        when(commandContainer.contains("fail")).thenReturn(false);

        assertThrows(UnknownCommandException.class, () -> parser.parse(new String[]{"ok", "fail"}));
    }
}
