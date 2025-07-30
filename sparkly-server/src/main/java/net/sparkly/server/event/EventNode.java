package net.sparkly.server.event;

import java.util.*;
import java.util.function.Consumer;

public class EventNode<T> {

    private final Map<Class<? extends T>, List<Consumer<T>>> listeners = new HashMap<>();

    public <E extends T> void addListener(Class<E> eventClass, Consumer<E> consumer) {
        List<Consumer<T>> list = listeners.computeIfAbsent(eventClass, k -> new ArrayList<>());
        list.add(event -> {
            E casted = eventClass.cast(event);
            consumer.accept(casted);
        });
    }

    @SuppressWarnings("unchecked")
    public void call(T event) {
        Class<? extends T> clazz = (Class<? extends T>) event.getClass();
        List<Consumer<T>> list = listeners.get(clazz);

        if (list == null) return;

        for (Consumer<T> consumer : list) {
            consumer.accept(event);
        }
    }
}
