package net.sparkly.api.entity.metadata;

public interface Metadata<T> {
    int type();

    int id();

    T value();

    void setValue(T value);
}
