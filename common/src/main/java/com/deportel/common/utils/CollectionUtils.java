package com.deportel.common.utils;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtils
{
	/**
	 * Devuelve una coleccion armada con el atributo indicado de cada uno de los items de la coleccion original.<br/>
	 * <b>Ejemplo:</b><br/>
	 * Si se invoca con los siguientes parametros:<br/>
	 * <b>Items:</b> Coleccion de A. A tiene dos atributos, nombre y apellido.<br/>
	 * <b>Nombre atributo:</b> nombre <br/>
	 * <b>Tipo atributo:</b> String<br/>
	 * Se devuelve una coleccion de String con todos los atributos nombre de la coleccion original.
	 *
	 * @param items
	 *            {@link Collection} coleccion de items
	 * @param nombreAtributo
	 *            Nombre del atributo que se desea obtener
	 * @param tipoAtributo
	 *            Tipo de atributo que se desea obtener
	 * @return {@link Collection} del atributo indicado existente en cada uno de los objetos de la coleccion original.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object, U extends Object> Collection<U> transformarCollection (Collection<T> items, String nombreAtributo, Class<U> tipoAtributo)
	{
		Collection<U> result = new ArrayList<U>(0);
		for (T t : items)
		{
			Object value = ReflectUtil.getPropertyValue(t, nombreAtributo);
			try
			{
				result.add((U)value);
			}
			catch (ClassCastException e)
			{
				System.err.println("El atributo [" + nombreAtributo + "] del objeto [" + t + "] NO es del tipo " + tipoAtributo.getClass());
				return result;
			}
		}
		return result;
	}
}
