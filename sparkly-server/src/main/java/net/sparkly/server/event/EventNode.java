package net.sparkly.server.event;

import net.sparkly.api.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventNode<T extends Event> {
    
    private final Map<Class<T>, List<Consumer<T>>> listeners = new HashMap<>();

    public void addListener(Class<T> eventClass, Consumer<T> consumer) {
        listeners.computeIfAbsent(eventClass, k -> new ArrayList<>())
            .add(consumer);
    }
    
    public void call(T event) {
        for (Consumer<T> consumer : listeners.get(event.getClass())) {
            consumer.accept(event);
        }
    }
}
