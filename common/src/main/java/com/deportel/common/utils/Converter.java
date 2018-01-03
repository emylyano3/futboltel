package com.deportel.common.utils;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.deportel.common.Constants;

public class Converter
{
	public static final String	DEFAULT_DATE_PATTERN	= "yyyyMMddHHmmss";

	private static Logger		log						= Logger.getLogger(Converter.class);

	public static BigDecimal intToBigDecimal(Integer value)
	{
		if (value != null)
		{
			return new BigDecimal(value.intValue());
		}
		else
		{
			return null;
		}
	}

	public static Integer bigDecimalToInt(BigDecimal value)
	{
		if (value != null)
		{
			return new Integer(value.intValue());
		}
		else
		{
			return new Integer(0);
		}
	}

	/**
	 * Convierte un string a entero. Si el string ingresado es vacio, nulo o no es un numero entero
	 * valido, se devuelve 0.
	 *
	 * @param n
	 *            El numero representado en String
	 * @return el numero entero representado por n. Si el string ingresado es vacio, nulo o no es un
	 *         numero entero valido, se devuelve 0.
	 */
	public static Integer stringToInt (String n)
	{
		if (!Utils.isNullOrEmpty(n))
		{
			try
			{
				return Integer.valueOf(n.trim());
			}
			catch (NumberFormatException e)
			{
				log.warn("La cadena de texto con valor " + n + " no representa un entero");
				return 0;
			}
		}
		else
		{
			log.warn("Se intento convertir null o vacio a entero");
			return 0;
		}
	}

	/**
	 * Identico a {@link #stringToInt(String)} solo que ademas de se le debe especificar la base
	 * numerica (decimal, hexadecimal, etc.)
	 *
	 * @param n
	 *            El numero representado en String
	 * @param radix
	 *            La base numerica
	 * @return el numero entero representado por n. Si el string ingresado es vacio, nulo o no es un
	 *         numero entero valido, se devuelve 0.
	 */
	public static Integer stringToInt (String n, int radix)
	{
		if (!Utils.isNullOrEmpty(n))
		{
			try
			{
				return Integer.parseInt(n.trim(), radix);
			}
			catch (NumberFormatException e)
			{
				log.warn("La cadena de texto con valor " + n + " no representa un entero");
				return 0;
			}
		}
		else
		{
			log.warn("Se intento convertir null a entero");
			return 0;
		}
	}

	/**
	 *
	 * @param n
	 * @return
	 */
	public static String intToString (int i)
	{
		return intToString(i, Constants.DECIMAL);
	}

	/**
	 *
	 * @param n
	 * @return
	 */
	public static String intToString (int i, int radix)
	{
		try
		{
			return Integer.toString(i, radix);
		}
		catch (NumberFormatException e)
		{
			return "0";
		}
	}

	public static String toString (Integer n)
	{
		try
		{
			return n.toString();
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static String toString (Object o)
	{
		try
		{
			return o.toString();
		}
		catch (Exception e)
		{
			return "";
		}
	}
}
