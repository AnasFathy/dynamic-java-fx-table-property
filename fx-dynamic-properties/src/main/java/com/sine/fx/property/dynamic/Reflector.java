package com.sine.fx.property.dynamic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class Reflector
{

	public static Optional<Field> getField(Class<?> klass, String fieldName)
	{
		if (klass == Object.class) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(klass.getDeclaredField(fieldName));

		} catch (Exception e) {
			return getField(klass.getSuperclass(), fieldName);
		}
	}

	public static Optional<Method> getMethod(Class<?> klass, String fieldName)
	{
		if (klass == Object.class) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(klass.getDeclaredMethod(fieldName));

		} catch (Exception e) {
			return getMethod(klass.getSuperclass(), fieldName);
		}
	}

	private Reflector()
	{
	}
}
