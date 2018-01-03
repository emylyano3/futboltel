package com.deportel.common;

import java.io.PrintStream;
import java.util.Properties;

import com.deportel.common.utils.Utils;

/**
 * {@link Config} Sirve para manejar la configuracion de una aplicacion. Levanta
 * la configuracion desde un archivo de properties especificado y la mantiene en
 * memoria. <br/>
 * Adicionalmente brinda diferentes metodos para acceder a la configuracion para
 * que sea posible obtener el valor de una misma key en diferentes tipos de
 * dato, como por ejembplo <tt>Boolean</tt> o <tt>String</tt>.
 *
 * @author Emy
 */
public class Config
{
	private final Properties	config;

	/**
	 * Constructor de la clase. Realiza la carga de la configuracion desde el
	 * archivo especificado. La ruta recibida debe ser relativa a la clase de
	 * referencia recibida.
	 *
	 * @param filePath
	 *            El path relativo del archivo que contiene los textos.
	 * @param clazz
	 *            La clase de referecia a partir de la cual se busca el archivo
	 *            especificado por el path.
	 * @throws RuntimeException
	 */
	public Config(String filePath, Class<?> clazz) throws RuntimeException
	{
		this.config = Utils.loadProperties(filePath, clazz);
	}

	/**
	 * Imprime las properties de la configuracion en el output stream
	 * especificado
	 *
	 * @param s
	 *            Un output stream
	 */
	public void list(PrintStream s)
	{
		this.config.list(s);
	}

	/**
	 * Devuelve el valor asociado a la key.
	 *
	 * @param key
	 *            La key del texto.
	 * @return El texto asociado a la key recibida
	 * @since 28/02/2011
	 */
	public String get(String key)
	{
		return this.config.getProperty(key);
	}

	/**
	 * Devuelve el valor asociado a la key. Si el valor cargado es vacio o nulo,
	 * se devuelve el valor por defecto.
	 *
	 * @param key
	 *            La key del texto.
	 * @param defValue
	 *            El valor por defecto a devolver en caso de que el valor
	 *            cargado sea vacio o nulo
	 * @return El texto asociado a la key recibida
	 */
	public String get(String key, String defValue)
	{
		final String loaded = this.config.getProperty(key);
		return Utils.isNullOrEmpty(loaded) ? defValue : loaded;
	}

	/**
	 * Devuelve el valor entero asociado a la key. Si el valor cargado es vacio
	 * o nulo, se devuelve el valor por defecto.
	 *
	 * @param key
	 *            La key del texto.
	 * @param defValue
	 *            El valor por defecto a devolver en caso de que el valor
	 *            cargado sea vacio o nulo
	 * @return El entero asociado a la key recibida
	 */
	public int getInt(String key, int defValue)
	{
		final String loaded = this.config.getProperty(key);
		try
		{
			return Utils.isNullOrEmpty(loaded) ? defValue : Integer.parseInt(loaded);
		}
		catch (final NumberFormatException e)
		{
			return defValue;
		}
	}

	/**
	 * Determina si una key esta activada o no. Si el valor de la property es
	 * vacio o nulo, se devuelve el valor por defecto.
	 *
	 * @param key
	 *            La key (flag) de la que se quiere saber su estado
	 * @param defValue
	 *            El valor por defecto a devolver en caso de que el valor de la
	 *            property sea nulo o vacio
	 * @return <tt>true</tt> si el flag esta up <tt>false</tt> si esta down
	 */
	public boolean isEnabled(String key, boolean defValue)
	{
		final String loaded = this.config.getProperty(key);
		return Utils.isNullOrEmpty(loaded) ? defValue : new Boolean(loaded);
	}

	/**
	 * Inserta un nuevo texto asociado a la key.
	 *
	 * @param key
	 *            La key del texto
	 * @param value
	 *            El valor del texto
	 * @since 28/02/2011
	 */
	public void put(String key, String value)
	{
		this.config.put(key, value);
	}
}