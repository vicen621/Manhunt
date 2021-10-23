package io.github.vicen621.manhunt.Utils.Annotations;

import io.github.vicen621.manhunt.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RegisterExecutor {

    public void register() {
        Set<Class<?>> classes = findAllClassesUsingReflectionsLibrary();

        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(Register.class)) {
                Register register = cls.getAnnotation(Register.class);
                System.out.println(cls.getSimpleName());

                register(cls, register.name());
            }
        }
    }

    private Set<Class<?>> findAllClassesUsingReflectionsLibrary() {
        Reflections reflections = new Reflections("io.github.vicen621.manhunt", new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Record.class));
    }

    private void register(Class<?> cls, String[] cmdName) {
        Object obj = null;
        try {
            obj = cls.getDeclaredConstructor(Main.class).newInstance(Main.getInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assert obj != null;

        if (obj instanceof Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) obj, Main.getInstance());
        }

        if (obj instanceof CommandExecutor) {
            Object finalObj = obj;
            Arrays.stream(cmdName).forEach(name -> {
                try {
                    Main.getInstance().getCommand(name).setExecutor((CommandExecutor) finalObj);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println(cls.getName());
                }
            });
        }
    }
}
