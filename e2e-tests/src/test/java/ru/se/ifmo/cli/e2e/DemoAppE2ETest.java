package ru.se.ifmo.cli.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoAppE2ETest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void captureOutput() {
        originalOut = System.out;
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @BeforeEach
    void resetApp() {
        DemoApp.launch();
    }

    private String[] extractEntities() {
        return out.toString(StandardCharsets.UTF_8)
            .lines()
            .filter(line -> line.contains("[ENTITY]"))
            .map(line -> line.replace("[ENTITY]", "").trim())
            .toArray(String[]::new);
    }

    @Test
    void hello_command_printsGreeting() {
        DemoApp.execute(new String[]{"hello"});
        String s = out.toString(StandardCharsets.UTF_8);
        assertTrue(s.contains("Hello, world!"));
    }

    @Test
    void add_and_list_roundtrip() {
        DemoApp.execute(new String[]{"add", "foo"});
        DemoApp.execute(new String[]{"add", "bar"});
        out.reset();
        DemoApp.execute(new String[]{"list"});
        assertArrayEquals(new String[]{"foo", "bar"}, extractEntities());
    }

    @Test
    void add_single_entity_is_listed() {
        DemoApp.execute(new String[]{"add", "baz"});
        out.reset();
        DemoApp.execute(new String[]{"list"});
        assertArrayEquals(new String[]{"baz"}, extractEntities());
    }

    @Test
    void list_without_add_returnsEmpty() {
        DemoApp.execute(new String[]{"list"});
        String[] entities = extractEntities();
        assertEquals(0, entities.length);
    }

    @Test
    void multiple_hello_commands_repeatOutput() {
        DemoApp.execute(new String[]{"hello"});
        DemoApp.execute(new String[]{"hello"});
        String s = out.toString(StandardCharsets.UTF_8);
        assertEquals(2, s.lines().filter(l -> l.contains("Hello, world!")).count());
    }

    @Test
    void add_duplicate_names_are_stored() {
        DemoApp.execute(new String[]{"add", "same"});
        DemoApp.execute(new String[]{"add", "same"});
        out.reset();
        DemoApp.execute(new String[]{"list"});
        assertArrayEquals(new String[]{"same", "same"}, extractEntities());
    }

    @Test
    void add_empty_name_ignoredOrStored() {
        DemoApp.execute(new String[]{"add", ""});
        out.reset();
        DemoApp.execute(new String[]{"list"});
        String[] result = extractEntities();
        assertTrue(result.length <= 1);
    }

    @Test
    void unknown_command_printsWarningOrIgnores() {
        out.reset();
        DemoApp.execute(new String[]{"unknownCommand"});
        String s = out.toString(StandardCharsets.UTF_8);
        assertTrue(s.toLowerCase().contains("unknown") || s.toLowerCase().contains("not recognized"));
    }

    @Test
    void add_without_argument_failsGracefully() {
        out.reset();
        DemoApp.execute(new String[]{"add"});
        String s = out.toString(StandardCharsets.UTF_8);
        assertTrue(s.toLowerCase().contains("error") || s.toLowerCase().contains("missing") || s.toLowerCase().contains("usage"));
    }

    @Test
    void list_with_extra_argument_failsOrIgnores() {
        out.reset();
        DemoApp.execute(new String[]{"list", "extra"});
        String s = out.toString(StandardCharsets.UTF_8);
        assertTrue(s.contains("Unknown command 'extra'"));
    }

    @Test
    void hello_with_extra_argument_failsOrPrintsHelloAnyway() {
        out.reset();
        DemoApp.execute(new String[]{"hello", "extra"});
        String s = out.toString(StandardCharsets.UTF_8);
        assertTrue(s.contains("Unknown command 'extra'"));
    }
}
