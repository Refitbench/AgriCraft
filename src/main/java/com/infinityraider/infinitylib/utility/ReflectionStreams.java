/*
 */
package com.infinityraider.infinitylib.utility;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

/**
 *
 * @author Ryan
 */
public final class ReflectionStreams {

	/**
	 * Creates a stream of all the constructors for a given class regardless of
	 * accessibility. That is, this method ignores if a constructor is private
	 * or otherwise protected.
	 *
	 * @param from the object or class representing the class to stream
	 * constructors from.
	 * @return a stream containing all the fields for the class or object.
	 */
    public static Stream<Constructor> streamConstructors(@Nonnull Object from) {
        Objects.requireNonNull(from);
		return Arrays.stream(classify(from).getConstructors());
	}

	/**
	 * Creates a stream of all the fields present in a given object or class
	 * regardless of accessibility. I.e. if only a class is passed then the
	 * stream will still include instance fields.
	 *
	 * @param from the object or class to stream the fields from.
	 * @return a stream containing all the fields for the class or object.
	 */
	public static Stream<Field> streamFields(@Nonnull Object from) {
        Objects.requireNonNull(from);
		return Arrays.stream(classify(from).getDeclaredFields());
	}

	/**
	 * Creates a stream of all the fields present in a given object or class
	 * that are accessible in that context. I.e. if an object is passed then the
	 * stream will include instance fields, but if a class is passed then
	 * instance fields will not be included.
	 *
	 * @param from the object or class to stream the methods from.
	 * @return a stream containing all the accessible fields for the class or
	 * object.
	 */
	public static Stream<Field> streamAccessibleFields(@Nonnull Object from) {
        Objects.requireNonNull(from);
		Stream<Field> stream = streamFields(from);
		if (from instanceof Class) {
			stream = stream.filter(ReflectionStreams::isStatic);
		}
		return stream;
	}

	/**
	 * Creates a stream of all the methods present in a given object or class
	 * regardless of accessibility. I.e. if only a class is passed then the
	 * stream will still include instance methods.
	 *
	 * @param from the object or class to stream the methods from.
	 * @return a stream containing all the methods for the class or object.
	 */
	public static Stream<Method> streamMethods(@Nonnull Object from) {
        Objects.requireNonNull(from);
		return Arrays.stream(classify(from).getDeclaredMethods());
	}

	/**
	 * Creates a stream of all the methods present in a given object or class
	 * that are accessible in that context. I.e. if an object is passed then the
	 * stream will include instance methods, but if a class is passed then
	 * instance methods will not be included.
	 *
	 * @param from the object or class to stream the methods from.
	 * @return a stream containing all the accessible methods for the class or
	 * object.
	 */
	public static Stream<Method> streamAccessibleMethods(@Nonnull Object from) {
        Objects.requireNonNull(from);
		Stream<Method> stream = streamMethods(from);
		if (from instanceof Class) {
			stream = stream.filter(ReflectionStreams::isStatic);
		}
		return stream;
	}

	/**
	 * Creates a stream of all the members (field or method) present in a given
	 * object or class that are accessible in that context. I.e. if only a class
	 * is passed then the stream will still include instance members.
	 *
	 * @param <T> the type of the members (not important).
	 * @param from the object or class to stream the members from.
	 * @return a stream containing all the accessible members for the class or
	 * object.
	 */
	public static <T extends AccessibleObject & Member> Stream<T> streamMembers(@Nonnull Object from) {
        Objects.requireNonNull(from);
		return Stream.concat((Stream<T>) streamFields(from), (Stream<T>) streamMethods(from));
	}

	/**
	 * Creates a stream of all the members (field or method) present in a given
	 * object or class that are accessible in that context. I.e. if an object is
	 * passed then the stream will include instance members, but if a class is
	 * passed then instance members will not be included.
	 *
	 * @param <T> the type of the members (not important).
	 * @param from the object or class to stream the members from.
	 * @return a stream containing all the accessible members for the class or
	 * object.
	 */
	public static <T extends AccessibleObject & Member> Stream<T> streamAccessibleMembers(@Nonnull Object from) {
        Objects.requireNonNull(from);
		Stream<T> stream = streamMembers(from);
		if (from instanceof Class) {
			stream = stream.filter(ReflectionStreams::isStatic);
		}
		return stream;
	}

	/**
	 * Determines the class value of the given object, be it from casting to a
	 * class or by calling object.getClass().
	 *
	 * @param obj the object to determine the class value of.
	 * @return the class value of the object.
	 */
	public static Class<?> classify(@Nonnull Object obj) {
        Objects.requireNonNull(obj);
		return (obj instanceof Class) ? (Class) obj : obj.getClass();
	}

	/**
	 * Determines if a given member is static. A simplification of the
	 * {@link Modifier#isStatic} method.
	 *
	 * @param m the member to test if static.
	 * @return if the given member is static.
	 */
	public static boolean isStatic(@Nonnull Member m) {
        Objects.requireNonNull(m);
		return Modifier.isStatic(m.getModifiers());
	}

}
