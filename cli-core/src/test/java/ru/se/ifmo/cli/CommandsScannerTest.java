package ru.se.ifmo.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CommandsScannerTest {

    private CommandsScanner scanner(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        return new CommandsScanner(stream);
    }

    @Test
    void nextCommand_readsNormalLine() {
        CommandsScanner scanner = scanner("hello\n");
        assertEquals("hello", scanner.nextCommand());
    }

    @Test
    void nextInt_parsesValidInteger() {
        CommandsScanner scanner = scanner("42\n");
        assertEquals(42, scanner.nextInt());
    }

    @Test
    void nextInt_invalidInput_throws() {
        CommandsScanner scanner = scanner("abc\n");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, scanner::nextInt);
        assertTrue(ex.getMessage().contains("Failed to parse input"));
    }

    @Test
    void nextLong_parsesValidLong() {
        CommandsScanner scanner = scanner("9999999999\n");
        assertEquals(9999999999L, scanner.nextLong());
    }

    @Test
    void nextLong_invalidInput_throws() {
        CommandsScanner scanner = scanner("longval!\n");
        assertThrows(IllegalArgumentException.class, scanner::nextLong);
    }

    @Test
    void nextDouble_parsesValidDouble() {
        CommandsScanner scanner = scanner("3.14\n");
        assertEquals(3.14, scanner.nextDouble());
    }

    @Test
    void nextDouble_invalidInput_throws() {
        CommandsScanner scanner = scanner("pi?\n");
        assertThrows(IllegalArgumentException.class, scanner::nextDouble);
    }

    @Test
    void nextFloat_parsesValidFloat() {
        CommandsScanner scanner = scanner("2.71\n");
        assertEquals(2.71f, scanner.nextFloat(), 0.0001f);
    }

    @Test
    void nextFloat_invalidInput_throws() {
        CommandsScanner scanner = scanner("floatfail\n");
        assertThrows(IllegalArgumentException.class, scanner::nextFloat);
    }


    @Test
    void constructor_withNullInput_throws() {
        assertThrows(NullPointerException.class, () -> new CommandsScanner(null));
    }

    @Test
    void constructor_withNullExitString_throws() {
        InputStream stream = new ByteArrayInputStream("abc".getBytes());
        assertThrows(NullPointerException.class, () -> new CommandsScanner(stream, null));
    }

    @Test
    void parse_allNumbers_correctTypes() {
        CommandsScanner scanner = new CommandsScanner(new ByteArrayInputStream("""
            42
            3.14
            2.71
            10000000000
            """.getBytes()));

        assertEquals(42, scanner.nextInt());
        assertEquals(3.14, scanner.nextDouble());
        assertEquals(2.71f, scanner.nextFloat(), 0.0001f);
        assertEquals(10000000000L, scanner.nextLong());
    }
}
