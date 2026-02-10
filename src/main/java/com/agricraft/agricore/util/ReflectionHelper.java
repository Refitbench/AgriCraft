/*
 */
package com.agricraft.agricore.util;

import com.agricraft.agricore.core.AgriCore;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A class to aid in the execution of reflection magic.
 *
 * @author RlonRyan
 */
public class ReflectionHelper {

    // Dummy Constructor.
    private ReflectionHelper() {
    }

    /**
     * Iterates over all the accessible values of type T in the class, and applies the consumer to them.
     * 
     * This method is designed to allow for reflection into any class without having to worry about exceptions.
     * 
     * @param <T> the desired type of value to extract from the class.
     * @param from the class or object to extract values from.
     * @param type the class of the desired type of values to extract. Required for casting purposes.
     * @param consumer a function accepting the extracted values.
     */
    public static <T> void forEachValueIn(Object from, Class<T> type, Consumer<T> consumer) {
        final Object target = from instanceof Class ? null : from;
        forEachFieldIn(from, (field) -> {
            try {
                field.setAccessible(true);
                Object obj = field.get(target);
                if (obj != null && type.isAssignableFrom(obj.getClass())) {
                    consumer.accept(type.cast(obj));
                }
            } catch (IllegalAccessException e) {
                // Oh well...
                AgriCore.getLogger("AgriCraft").warn(
                        "ReflectionHelper.forEachIn() Skipping Field: \"{0}\" in Class: \"{1}\"!",
                        field.getName(),
                        from.getClass().getCanonicalName()
                );
            }
        });
    }

    /**
     * Iterates over all the accessible values of type T with annotation of type A in the class, and applies the consumer to them.
     * 
     * This method is designed to allow for reflection into any class without having to worry about exceptions.
     * 
     * @param <T> the desired type of value to extract from the class.
     * @param <A> the type of annotation desired to be present.
     * @param from the class or object to extract values from.
     * @param type the class of the desired type of values to extract. Required for casting purposes.
     * @param annotation the type of annotation the extracted values should be annotated with.
     * @param consumer a function accepting the extracted values.
     */
    public static <T, A extends Annotation> void forEachValueIn(Object from, Class<T> type, Class<A> annotation, BiConsumer<T, A> consumer) {
        final Object target = from instanceof Class ? null : from;
        ReflectionHelper.forEachFieldIn(from, annotation, (field, anno) -> {
            try {
                field.setAccessible(true);
                Object obj = field.get(target);
                if (obj != null && type.isAssignableFrom(obj.getClass())) {
                    consumer.accept(type.cast(obj), anno);
                }
            } catch (IllegalAccessException e) {
                // Oh well...
                AgriCore.getLogger("AgriCraft").warn(
                        "ReflectionHelper.forEachIn() Skipping Field: \"{0}\" in Class: \"{1}\"!",
                        field.getName(),
                        from.getClass().getCanonicalName()
                );
            }
        });
    }

    /**
     * Iterates over all the accessible fields of type T with annotation A in the class, and applies the consumer to them.
     * 
     * This method is designed to allow for reflection into any class without having to worry about exceptions.
     * 
     * @param <A> the desired type of annotation to be present on the extracted fields.
     * @param from the class or object to extract values from.
     * @param annotation the class of the desired type of annotation to be present on the extracted fields. Required for casting purposes.
     * @param consumer a function accepting the extracted fields, with respective annotations.
     */
    public static <A extends Annotation> void forEachFieldIn(Object from, Class<A> annotation, BiConsumer<Field, A> consumer) {
        forEachFieldIn(from, (field) -> {
            if (field.isAnnotationPresent(annotation)) {
                consumer.accept(field, field.getAnnotation(annotation));
            }
        });
    }

    /**
     * Iterates over all the accessible fields of type T in the class, and applies the consumer to them.
     * 
     * This method is designed to allow for reflection into any class without having to worry about exceptions.
     * 
     * @param from the class or object to extract values from.
     * @param consumer a function accepting the extracted fields, with respective annotations.
     */
    public static void forEachFieldIn(Object from, Consumer<Field> consumer) {
        final boolean isInstance = !(from instanceof Class);
        final Class clazz = isInstance ? from.getClass() : (Class) from;
        for (Field f : clazz.getDeclaredFields()) {
            if (isInstance || Modifier.isStatic(f.getModifiers())) {
                consumer.accept(f);
            }
        }
    }

}
