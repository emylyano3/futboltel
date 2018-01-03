package com.deportel.common;

import java.util.Properties;

import com.deportel.common.utils.Utils;

/**
 * {@link Text} es utilizada para cargar todos los textos de la aplicacion. La
 * estructura que utiliza es identica a la estructura que manejan los archivos
 * de properties. De hecho, esta clase es solo un wrapper de la clase
 * {@link Properties}, donde se exponen solo los metodos necesarios para obtener
 * y setear el texto deseado.</br> La carga de los textos se realiza en el
 * constructor.
 *
 * @author Emy
 * @since 28/02/2011
 */
public class Text
{
	private final Properties	texts;

	private String				placeHolder;

	/**
	 * Constructor de la clase. Realiza la carga de los textos desde el archivo
	 * especificado. La ruta enviada debe ser relativa a la clase de referencia
	 * enviada.
	 *
	 * @param filePath
	 *            El path relativo del archivo que contiene los textos.
	 * @param clazz
	 *            La clase de referecia a partir de la cual se busca el archivo
	 *            especificado por el path.
	 * @throws RuntimeException
	 * @since 28/02/2011
	 */
	public Text(String filePath, Class<?> clazz) throws RuntimeException
	{
		this.texts = Utils.loadProperties(filePath, clazz);
	}

	public Text()
	{
		this.texts = new Properties();
	}

	public Text(Properties p)
	{
		this.texts = p;
	}

	public void setPlaceholder(String ph)
	{
		this.placeHolder = ph;
	}

	/**
	 * Devuelve el texto asociado a la key.
	 *
	 * @param key
	 *            La key del texto.
	 * @return El texto asociado a la key enviada
	 * @since 28/02/2011
	 */
	public String get(String key)
	{
		return this.texts.getProperty(key);
	}

	/**
	 *
	 * @param key
	 * @param placeHolder
	 * @param tokens
	 * @return
	 */
	public String get(String key, String...tokens)
	{
		int start = 0;
		final int phLength = this.placeHolder.length();
		String text = this.texts.getProperty(key);
		if (Utils.isNullOrEmpty(text)) return text;
		for (final String token : tokens)
		{
			final int pos = text.indexOf(this.placeHolder, start);
			if (pos == -1 || pos + phLength >= text.length()) break;
			start = pos;
			text = text.substring(0, pos) + token + text.substring(pos + phLength, text.length());
		}
		return text;
	}

	/**
	 * Devuelve el texto asociado a la key. Si el texto cargado es vacio o nulo,
	 * se devuelve el valor por defecto.
	 *
	 * @param key
	 *            La key del texto.
	 * @param defValue
	 *            El valor por defecto a devolver en caso de que el valor
	 *            cargado sea vacio o nulo
	 * @return El texto asociado a la key enviada
	 */
	public String get(String key, String defValue)
	{
		final String loaded = this.texts.getProperty(key);
		return Utils.isNullOrEmpty(loaded) ? defValue : loaded;
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
		this.texts.put(key, value);
	}
}