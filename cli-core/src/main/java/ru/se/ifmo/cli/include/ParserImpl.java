package ru.se.ifmo.cli.include;

import com.google.inject.Inject;
import ru.se.ifmo.cli.Action;
import ru.se.ifmo.cli.Command;
import ru.se.ifmo.cli.CommandContainer;
import ru.se.ifmo.cli.Parser;
import ru.se.ifmo.cli.Project;
import ru.se.ifmo.cli.UnknownCommandException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ParserImpl implements Parser {
    private final Project project;
    private final List<Action> history = new ArrayList<>();
    private final Map<Class<?>, Function<String, ?>> typeParsers = new HashMap<>();

    @Inject
    public ParserImpl(Project project) {
        this.project = Objects.requireNonNull(project);
        initTypeParsers();
    }

    private void initTypeParsers() {
        typeParsers.put(String.class, s -> s);
        typeParsers.put(int.class, Integer::parseInt);
        typeParsers.put(Integer.class, Integer::parseInt);
        typeParsers.put(long.class, Long::parseLong);
        typeParsers.put(Long.class, Long::parseLong);
        typeParsers.put(double.class, Double::parseDouble);
        typeParsers.put(Double.class, Double::parseDouble);
        typeParsers.put(float.class, Float::parseFloat);
        typeParsers.put(Float.class, Float::parseFloat);
        typeParsers.put(boolean.class, Boolean::parseBoolean);
        typeParsers.put(Boolean.class, Boolean::parseBoolean);
        typeParsers.put(byte.class, Byte::parseByte);
        typeParsers.put(Byte.class, Byte::parseByte);
        typeParsers.put(short.class, Short::parseShort);
        typeParsers.put(Short.class, Short::parseShort);
        Function<String, Character> charParser = s -> {
            if (s.length() != 1) throw new IllegalArgumentException("Cannot parse '" + s + "' as char");
            return s.charAt(0);
        };
        typeParsers.put(char.class, charParser);
        typeParsers.put(Character.class, charParser);
        typeParsers.put(BigInteger.class, BigInteger::new);
        typeParsers.put(BigDecimal.class, BigDecimal::new);
    }

    @Override
    public List<Action> getHistory() {
        return Collections.unmodifiableList(history);
    }


    @Override
    public Action[] parse(String[] args) {
        CommandContainer commands = project.getCommands();
        List<Action> actions = new ArrayList<>();
        int idx = 0;

        while (idx < args.length) {
            ParseResult pr = parseNextAction(args, idx, commands);
            actions.add(pr.action());
            idx = pr.nextIndex();
        }

        history.addAll(actions);
        return actions.toArray(new Action[0]);
    }

    // remove 3
    // RemoveByIdCommand.remove()
    // ["rem]
    private ParseResult parseNextAction(String[] args, int startIdx, CommandContainer commands) {
        String name = args[startIdx++];
        if (!commands.contains(name)) {
            throw new UnknownCommandException(name, "Cannot find command " + name);
        }

        Command cmd = commands.get(name);
        Class<?>[] defs = cmd.getActionParams();
        int need = defs == null ? 0 : defs.length;
        if (startIdx + need > args.length) {
            throw new IllegalArgumentException(String.format("Command %s expects %d args, got %d", name, need, args.length - startIdx));
        }

        Object[] parsed = new Object[need];
        for (int i = 0; i < need; i++) {
            parsed[i] = parseParam(args[startIdx++], defs[i], name);
        }

        Action action = need == 0 ? cmd.getAction() : cmd.getAction(parsed);
        Objects.requireNonNull(action, "CommandAction returned null for " + name);
        return new ParseResult(action, startIdx);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object parseParam(String arg, Class<?> type, String cmdName) {
        Function<String, ?> parser = typeParsers.get(type);

        if (parser == null && type.isEnum()) {
            Class<? extends Enum> enumType = (Class) type;
            parser = s -> Enum.valueOf(enumType, s);
        }

        if (parser == null) {
            throw new UnsupportedOperationException("No parser for type " + type.getSimpleName() + " in command " + cmdName);
        }
        try {
            return parser.apply(arg);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to parse '" + arg + "' as " + type.getSimpleName(), ex);
        }
    }


    private record ParseResult(Action action, int nextIndex) {
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public String toString() {
        return "ParserImpl";
    }
}
