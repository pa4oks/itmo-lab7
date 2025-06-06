package ru.se.ifmo.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class ApplicationTest {
    private Parser mockParser;

    @BeforeEach
    void setUp() throws Exception {
        Project mockProject = mock(Project.class);
        mockParser = mock(Parser.class);
        CommandContainer mockCommands = mock(CommandContainer.class);

        when(mockParser.parse(any(String[].class)))
            .thenReturn(new Action[0]);

        when(mockProject.getParser()).thenReturn(mockParser);
        when(mockProject.getCommands()).thenReturn(mockCommands);

        Field projectField = Application.class.getDeclaredField("project");
        projectField.setAccessible(true);
        projectField.set(null, mockProject);
    }

    @Test
    void execute_noArgs_doesNothing() {
        assertDoesNotThrow(() -> Application.execute(new String[]{}));
        verify(mockParser).parse(new String[]{});
        verifyNoMoreInteractions(mockParser);
    }

    @Test
    void execute_unknownCommand_exceptionIsCaught() {
        when(mockParser.parse(any()))
            .thenThrow(new UnknownCommandException("foo", "no foo"));

        assertDoesNotThrow(() -> Application.execute(new String[]{"foo"}));
        verify(mockParser).parse(new String[]{"foo"});
    }

    @Test
    void execute_badArgs_exceptionIsCaught() {
        // Делаем так, чтобы parse(...) бросал IllegalArgumentException
        when(mockParser.parse(any()))
            .thenThrow(new IllegalArgumentException("bad format"));

        assertDoesNotThrow(() -> Application.execute(new String[]{"bad"}));
        verify(mockParser).parse(new String[]{"bad"});
    }

    @Test
    void execute_withActions_executesAllAndContinuesOnError() {
        Command fakeCmd = mock(Command.class);

        Action good1 = mock(Action.class);
        Action bad = mock(Action.class);
        Action good2 = mock(Action.class);

        when(good1.getCommand()).thenReturn(fakeCmd);
        when(bad.getCommand()).thenReturn(fakeCmd);
        when(good2.getCommand()).thenReturn(fakeCmd);

        doThrow(new RuntimeException("boom")).when(bad).execute();

        when(mockParser.parse(any()))
            .thenReturn(new Action[]{good1, bad, good2});

        assertDoesNotThrow(() -> Application.execute(new String[]{"cmd"}));

        verify(good1).execute();
        verify(bad).execute();
        verify(good2).execute();
    }
}
