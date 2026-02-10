/*
 */
package com.agricraft.agricore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author RlonRyan
 */
@SuppressWarnings("unchecked")
public class TypeConsumerMap {

    private final Map<Class, Consumer> consumers;

    public TypeConsumerMap() {
        this.consumers = new HashMap<>();
    }

    public <T> Consumer<T> put(Class<T> clazz, Consumer<T> consumer) {
        return this.consumers.put(clazz, consumer);
    }

    public <T> Consumer<T> putIfAbsent(Class<T> clazz, Consumer<T> consumer) {
        return this.consumers.putIfAbsent(clazz, consumer);
    }

    public <T> Consumer<T> get(Class<T> clazz) {
        return this.consumers.get(clazz);
    }

    public <T> Consumer<T> getOrDefault(Class<T> clazz, Consumer<T> defaultConsumer) {
        return this.consumers.getOrDefault(clazz, defaultConsumer);
    }

    public <T> Consumer<T> replace(Class<T> clazz, Consumer<T> consumer) {
        return this.consumers.replace(clazz, consumer);
    }

    public <T> Consumer<T> remove(Class<T> clazz) {
        return this.consumers.remove(clazz);
    }

    public <T> boolean containsKey(Class<T> clazz) {
        return this.consumers.containsKey(clazz);
    }

    public <T> boolean containsValue(Consumer<T> consumer) {
        return this.consumers.containsValue(consumer);
    }

}
