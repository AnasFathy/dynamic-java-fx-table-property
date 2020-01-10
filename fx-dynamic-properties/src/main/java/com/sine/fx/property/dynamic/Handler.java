package com.sine.fx.property.dynamic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javafx.beans.value.WritableObjectValue;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Handler implements MethodInterceptor
{

	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
	{

		if (!method.getName().startsWith("set") && !method.getName().startsWith("get")) {
			return methodProxy.invokeSuper(o, args);
		}

		String setterMethod = method.getName();
		String prop = setterMethod.substring(3).toLowerCase() + "Property";
		Field field = Reflector.getField(o.getClass(), prop)
				.orElseThrow(() -> new RuntimeException(String.format("field %s not found", prop)));
		if (args.length > 0) {
			Method declaredMethod = WritableObjectValue.class.getDeclaredMethod("set", Object.class);

			declaredMethod.invoke(field.get(o), args[0]);
		} else {
			Method declaredMethod = WritableObjectValue.class.getDeclaredMethod("get");
			return declaredMethod.invoke(field.get(o));
		}

		return null;
	}
}
