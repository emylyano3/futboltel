package com.deportel.common.utils;

public class Properties extends java.util.Properties
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Devuelve el valor entero de una property. Si la key recibida no existe o se encuentra un error en el
	 * parseo del value correspondiente a la key se devuelve el valor default recibido.
	 * 
	 * @param key
	 *            La key de la property de la que se quiere el valor
	 * @param defaultValue
	 *            El valor default a devolver en caso de error
	 * @return El valor entero de una property. Si la key recibida no existe o se encuentra un error en el
	 *         parseo del value correspondiente a la key se devuelve el valor default recibido
	 */
	public int getPropertyInt (String key, int defaultValue)
	{
		String value = getProperty(key);
		if (key != null)
		{
			try
			{
				return Integer.parseInt(value);
			}
			catch (NumberFormatException e)
			{
				return defaultValue;
			}
		}
		return defaultValue;
	}

}
