package com.deportel.common.utils;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectUtil
{
	/**
	 * @deprecated Devuelve null
	 * @param clazz
	 * @return
	 */
	@Deprecated
	public static List<Method> getSetters(Class<?> clazz)
	{
		return null;
	}

	/**
	 * @deprecated Devuelve null
	 * @param clazz
	 * @return
	 */
	@Deprecated
	public static List<Method> getGetters(Class<?> clazz)
	{
		return null;
	}

	/**
	 * Retorna el valor del atributo del objeto entity informado como properyName. Para realizar
	 * esta función se utiliza reflection.
	 *
	 * @param entity
	 *            el objeto del que se requiere el valor de uno de sus atributos.
	 * @param properyName
	 *            el nombre del atributo del que se requiere su valor.
	 * @return el valor del atributo informado.
	 */
	public static Object getPropertyValue(Object entity, String properyName)
	{
		if (entity == null || properyName == null || !(properyName.length() > 0)) { return null; }
		Class<?> clazz = entity.getClass();
		String getterName = "get" + properyName.substring(0, 1).toUpperCase() + properyName.substring(1, properyName.length());
		try
		{
			return clazz.getMethod(getterName, (Class<?>[]) null).invoke(entity, (Object[]) null);
		}
		catch (NoSuchMethodException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}

}
