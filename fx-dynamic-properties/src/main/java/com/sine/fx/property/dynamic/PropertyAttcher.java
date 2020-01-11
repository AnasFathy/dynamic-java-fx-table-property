package com.sine.fx.property.dynamic;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.sf.cglib.proxy.Enhancer;

/**
 * this class attach javafx property for you POJOS to be used in data-binding in
 * tables and lists views
 * 
 * @author moslim
 *
 */
public class PropertyAttcher
{

	/**
	 * create an instance of the passed class with the following steps
	 * <p>
	 * <ul>
	 * <li>passed class must have no-arg constructor
	 * <li>fields must declared with setters and getters(in future versions it will
	 * not be a must)
	 * <li>this method will create a subclass of the passed class and attach java-fx
	 * property for each field(occur one time for each pojo)
	 * <li>after sub class created a proxy for it will be created to make getters
	 * and setters use javaf-fx properties
	 * <li>proxy is instantiated and return
	 * </ul>
	 * </p>
	 * 
	 * @param <T>type of pojo
	 * @param klass
	 * @return enhanced pojo
	 */
	public static <T> T attach(Class<T> klass)
	{
		try {

			BuildResult<? extends T> result = createExtention(klass);
			Class<? extends T> type = result.klass;

			@SuppressWarnings("unchecked")
			T instance = (T) Enhancer.create(type, new Handler());
			for (int i = 0; i < result.names.size(); i++) {
				Field field = result.klass.getDeclaredField(result.names.get(i));
				field.setAccessible(true);
				field.set(instance, result.props.get(i));
			}

			return instance;

		} catch (Exception e) {
			throw new RuntimeException("An error occured while attching properties to your class", e);
		}
	}

	static class BuildResult<T>
	{
		private Class<? extends T> klass;
		private List<SimpleObjectProperty<?>> props;
		private List<String> names;
	}

	@SuppressWarnings("unchecked")
	private static <T> BuildResult<? extends T> createExtention(Class<T> klass)
	{
		SimpleObjectProperty<?> property;

		Builder<T> builder = new ByteBuddy().subclass(klass).modifiers(Visibility.PUBLIC);
		List<String> names = new LinkedList<>();
		List<SimpleObjectProperty<?>> props = new LinkedList<>();
		for (Field field : Student.class.getDeclaredFields()) {
			String propertyName = field.getName() + "Property";
			property = new SimpleObjectProperty<>();
			names.add(propertyName);
			props.add(property);
			builder = builder.defineField(propertyName, SimpleObjectProperty.class, Visibility.PUBLIC)
					.defineMethod(propertyName, SimpleObjectProperty.class, Visibility.PUBLIC)
					.intercept(FixedValue.value(property));
		}

		Class<? extends T> type = builder.make()
				.load(Student.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();

		BuildResult<T> result = new BuildResult<>();
		result.klass = type;
		result.names = names;
		result.props = props;

		return result;

	}

	private PropertyAttcher()
	{
	}
}
