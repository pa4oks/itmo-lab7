package ru.se.ifmo.cli.include;

import ru.se.ifmo.cli.Project;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class Utils {
    private static final Map<Class<?>, Optional<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Optional<Method>> METHOD_CACHE = new ConcurrentHashMap<>();

    private Utils() {
    }

    public static void injectProject(Project project, Object target) {
        Objects.requireNonNull(project, "project must not be null");
        Objects.requireNonNull(target, "target must not be null");

        Class<?> cls = target.getClass();

        Method setter = METHOD_CACHE
            .computeIfAbsent(cls, Utils::findSetter)
            .orElse(null);
        if (setter != null) {
            try {
                setter.setAccessible(true);
                setter.invoke(target, project);
                return;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(
                    String.format("Failed to inject project via setter into %s", cls.getName()), e);
            }
        }

        Field field = FIELD_CACHE
            .computeIfAbsent(cls, Utils::findField)
            .orElse(null);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(target, project);
                return;
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                    String.format("Failed to inject project via field into %s", cls.getName()), e);
            }
        }

        throw new IllegalStateException(
            String.format("No setter or field found for project injection in %s", cls.getName()));
    }

    private static Optional<Method> findSetter(Class<?> cls) {
        for (Method m : cls.getDeclaredMethods()) {
            if (m.getName().equals("setProject")
                && m.getParameterCount() == 1
                && m.getParameterTypes()[0].equals(Project.class)) {
                return Optional.of(m);
            }
        }
        Class<?> sup = cls.getSuperclass();
        return sup != null ? findSetter(sup) : Optional.empty();
    }

    private static Optional<Field> findField(Class<?> cls) {
        for (Field f : cls.getDeclaredFields()) {
            if (f.getType().equals(Project.class) && f.getName().equals("project")) {
                return Optional.of(f);
            }
        }
        Class<?> sup = cls.getSuperclass();
        return sup != null ? findField(sup) : Optional.empty();
    }
}
