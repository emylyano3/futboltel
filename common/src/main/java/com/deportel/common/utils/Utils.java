package com.deportel.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JTable;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.DateFormatManager;

import com.deportel.common.Text;
import com.deportel.common.exception.PropertiesNotFoundException;

import static com.deportel.common.Constants.*;

/**
 * Posee varias funcionalidades basicas y de caracter general. Toda su firma es de caracter estatico.
 *
 * @since 10/10/2009
 * @author Emy
 */
public abstract class Utils
{
	private static Logger	log		= Logger.getLogger(Utils.class);

	private static int		BUFFER	= 1024;

	/**
	 * Copies a file from a location to a new destination.
	 *
	 * @param srFile
	 *            The source file.
	 * @param dtFile
	 *            The destination file.
	 * @throws IOException
	 *
	 * @since 10/10/2009
	 */
	public static void copyFile(String srFile, String dtFile) throws IOException
	{
		validateParameters(srFile, dtFile);
		log.debug("Copying file from " + srFile + " to " + dtFile);
		OutputStream out = null;
		InputStream in = null;
		try
		{
			final File f1 = new File(srFile);
			final File f2 = new File(dtFile);
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2);
			final byte[] buf = new byte[BUFFER];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
		}
		catch (final FileNotFoundException ex)
		{
			throw new FileNotFoundException();
		}
		catch (final IOException e)
		{
			throw new IOException();
		}
		finally
		{
			try
			{
				in.close();
				out.close();
			}
			catch (final IOException e)
			{
				log.error("IO exception realizando la copia del archivo: " + srFile + " a: " + dtFile);
				e.printStackTrace();
			}
		}
	}

	public static Boolean getBoolean(String bool)
	{
		return new Boolean(bool);
	}

	/**
	 * Devuelve la fecha actual utilizando como base la fecha del sistema.
	 *
	 * @return {@link Date} Con la fecha actual
	 *
	 * @since 01/03/2011
	 */
	public static Date getDate()
	{
		return new Date(System.currentTimeMillis());
	}

	public static Object getFieldValue(Object target, String field) throws NoSuchMethodException
	{
		try
		{
			field = Character.toUpperCase(field.charAt(0)) + field.substring(1, field.length());
			Method getter;
			getter = target.getClass().getDeclaredMethod("get" + field, new Class<?>[] {});
			Object result;
			result = getter.invoke(target, new Object[] {});
			return result;
		}
		catch (final IllegalArgumentException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (final IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (final InvocationTargetException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (final SecurityException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (final NoSuchMethodException e)
		{
			throw e;
		}
	}

	/**
	 * Obtiene los datos de un registro seleccionado de un JTable.
	 *
	 * @param table
	 *            {@link JTable}
	 * @param cantidadColumnas
	 *            La cantidad de columnas
	 * @return {@link Date} String[]
	 * @since 01/10/2011
	 */
	public static String[] getSelectedRow(JTable table, int cantidadColumnas)
	{
		log.debug("Comienza el método getSelectedRow. Cantidad de columnas: " + cantidadColumnas);
		// Get the min and max ranges of selected cells
		/*
		 * TODO: no se comporta bien el colIndexEnd, chequear el tipo de selección, mientras pasamos como parámetro la
		 * cantidad de columnas que tiene la tabla int colIndexEnd =
		 * table.getColumnModel().getSelectionModel().getMaxSelectionIndex(); int rowIndexEnd =
		 * table.getSelectionModel().getMaxSelectionIndex(); int colIndexStart = table.getSelectedColumn();
		 */
		final int rowIndexStart = table.getSelectedRow();
		final int colIndexStart = 0;
		final int colIndexEnd = cantidadColumnas;

		int r;
		if ((r = rowIndexStart) != -1)
		{
			final String datos[] = new String[colIndexEnd];
			// Check each cell in the range
			for (int c = colIndexStart; c < colIndexEnd; c++)
			{
				if (c != -1 && table.getValueAt(r, c) != null)
				{
					datos[c] = table.getValueAt(r, c).toString();
					log.debug(datos[c]);
				}
			}
			log.debug("Retorno los datos del row seleccionado ");
			return datos;
		}
		log.debug("No hay row seleccionada. Se retorna null");
		return null;
	}

	/**
	 * Transforma un int a array de bytes utlizando el sistema big-endian.
	 * <p/>
	 * <b>Ejemplo:</b><br/>
	 * Se recibe el entero 10 (0x0000000A en hexa y 1010 en binario)<br/>
	 * El resultado será un array con los siguientes valores: [00 00 00 10]<br/>
	 *
	 * @param i
	 *            El entero a convertir
	 * @return El array de bytes que representa el entero
	 */
	public static byte[] intToByteArray(int i)
	{
		final byte[] bytes = new byte[4];
		bytes[3] = (byte) (i >> 24);
		bytes[2] = (byte) (i >> 16 & 0xFF);
		bytes[1] = (byte) (i >> 8 & 0xFF);
		bytes[0] = (byte) (i & 0xFF);
		return bytes;
	}

	public static <T extends Object> List<T> invert(List<T> list)
	{
		final List<T> inverted = new ArrayList<T>(list.size());
		for (int i = list.size() - 1; i >= 0; i--)
		{
			inverted.add(list.get(i));
		}
		return inverted;
	}

	/**
	 * Determina si un string es nulo o vacio
	 *
	 * @param string
	 *            El String a analizar
	 * @return <tt>true</tt> si el string enviado es nulo o vacio <tt>false</tt> en caso contrario
	 */
	public static boolean isNullOrEmpty(Collection<?> col)
	{
		if (col == null || col.isEmpty())
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Determina si un array de objetos es nulo o vacio
	 *
	 * @param array
	 *            El array a analizar
	 * @return <tt>true</tt> si el array recibido es nulo o vacio <tt>false</tt> en caso contrario
	 */
	public static boolean isNullOrEmpty(Object[] array)
	{
		if (array == null || array.length == 0)
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Determina si un string es nulo o vacio
	 *
	 * @param string
	 *            El String a analizar
	 * @return <tt>true</tt> si el string enviado es nulo o vacio <tt>false</tt> en caso contrario
	 */
	public static boolean isNullOrEmpty(String string)
	{
		if (string == null || string.isEmpty())
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Transforma un input stream a un byte array.
	 *
	 * @param is
	 *            {@link InputStream}
	 * @return Array de bytes
	 */
	public static byte[] isToByteArray(InputStream is)
	{
		validateParameters(is);
		byte[] array = null;
		try
		{
			array = new byte[is.available()];
			int i = 0;
			while (is.available() != 0)
			{
				array[i] = (byte) is.read();
				i++;
			}
		}
		catch (final Exception e)
		{
			log.error("Error covirtiendo el Input Stream a Byte Array");
		}
		return array;
	}

	/**
	 * Busca la ubicacion del archivo de properties tomando como referencia el path de la clase enviada. Carga el
	 * archvio y devuelve las properties encontradas en el.
	 *
	 * @param propertyFilePath
	 *            El path relativo del archivo de properties
	 * @param clazz
	 *            La clase de referencia para encontrar el archivo de properties
	 * @return Las properties encontradas
	 *
	 * @since 18/02/2011
	 */
	public static Properties loadLogProperties(String propertyFilePath, Class<?> clazz)
	{
		final Properties p = new Properties();
		try
		{
			final InputStream is = clazz.getResourceAsStream(propertyFilePath);
			p.load(is);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * Carga las properties desde el archivo especificado. El path enviado debe ser absoluto.
	 *
	 * @param propertyFilePath
	 *            El path absoluto del archivo de properties
	 * @return Las properties encontradas
	 * @throws PropertiesNotFoundException
	 *
	 * @since 24/02/2011
	 */
	public static Properties loadProperties(String propertyFilePath) throws PropertiesNotFoundException
	{
		validateParameters(propertyFilePath);
		final Properties p = new Properties();
		final File file = new File(propertyFilePath);
		try
		{
			final Reader reader = new FileReader(file);
			p.load(reader);
			reader.close();
			return p;
		}
		catch (final IOException e)
		{
			throw new PropertiesNotFoundException("No se encontró el archivo de properties especificado en: " + propertyFilePath);
		}
	}

	/**
	 * Carga las properties desde el archivo especificado. El path enviado debe ser absoluto.
	 *
	 * @param propertyIS
	 *            {@link InputStream} del archivo de properties
	 * @return Las properties encontradas
	 * @throws PropertiesNotFoundException
	 *
	 * @since 24/02/2011
	 */
	public static Properties loadProperties(InputStream propertyIS) throws RuntimeException
	{
		validateParameters(propertyIS);
		final Properties p = new Properties();
		try
		{
			final Reader reader = new InputStreamReader(propertyIS);
			p.load(reader);
			reader.close();
			return p;
		}
		catch (final IOException e)
		{
			throw new RuntimeException("No se pudo leer el input stream de las properties.");
		}
	}

	/**
	 * Busca la ubicacion del archivo de properties tomando como referencia el path de la clase enviada. Carga el
	 * archvio y devuelve las properties encontradas en el.
	 *
	 * @param propertyFilePath
	 *            El path relativo del archivo de properties
	 * @param clazz
	 *            La clase de referencia para encontrar el archivo de properties
	 * @return Las properties encontradas
	 * @throws RuntimeException
	 * @since 18/02/2011
	 */
	public static Properties loadProperties(String propertyFilePath, Class<?> clazz) throws RuntimeException
	{
		validateParameters(propertyFilePath, clazz);
		final Properties p = new Properties();
		try
		{
			final InputStream is = clazz.getResourceAsStream(propertyFilePath);
			if (is == null)
				throw new RuntimeException("Error cargando las properties desde el archivo: " + propertyFilePath);
			p.load(is);
			return p;
		}
		catch (final IOException e)
		{
			throw new RuntimeException("Error cargando las properties desde el archivo: " + propertyFilePath, e);
		}
	}

	/**
	 * Carga los textos de la aplicacion segun el idioma seleccionado
	 *
	 * @param language
	 *            El lenguage a cargar
	 * @param textFilePath
	 *            El path relativo de los textos
	 * @param clazz
	 *            {@link Class} La clase desde donde se invoca
	 * @return {@link Text} Los textos de la aplicacion
	 * @throws RuntimeException
	 *             , IllegalArgumentException
	 */
	public static Text loadTexts(String language, String textFilePath, Class<?> clazz) throws RuntimeException, IllegalArgumentException
	{
		validateParameters(language, textFilePath, clazz);
		final int pos = textFilePath.lastIndexOf('.');
		final String aux = textFilePath.substring(0, pos) + UNDER_SCORE + language + textFilePath.substring(pos, textFilePath.length());
		return new Text(aux, clazz);
	}

	/**
	 *
	 * @param params
	 *
	 * @since 21/01/2011
	 */
	private static void logParamaeters(Object... params)
	{
		String aux;
		String logMessage = "";
		int number = 0;
		for (final Object param : params)
		{
			aux = param == null ? null : param.toString();
			logMessage += "Param " + number++ + ": " + aux + " | ";
		}
		log.debug(logMessage);
	}

	/**
	 * Converts an long to byte array.
	 *
	 * @param l
	 *            The long.
	 * @return The long as byte array.
	 *
	 * @since 10/10/2009
	 */
	public static byte[] longToByteArray(long l)
	{
		final byte[] b = new byte[8];
		b[7] = (byte) (l >> 56);
		b[6] = (byte) (l >> 48 & 0xFF);
		b[5] = (byte) (l >> 40 & 0xFF);
		b[4] = (byte) (l >> 32 & 0xFF);
		b[3] = (byte) (l >> 24 & 0xFF);
		b[2] = (byte) (l >> 16 & 0xFF);
		b[1] = (byte) (l >> 8 & 0xFF);
		b[0] = (byte) (l >> 0 & 0xFF);
		return b;
	}

	public static Date parseDate(String date) throws ParseException
	{
		return new DateFormatManager().parse(date);
	}

	/**
	 *
	 * @param str
	 * @param search
	 * @param replaceBy
	 * @return
	 */
	public static String replaceString(String str, String search, String replaceBy)
	{
		if (search.length() > str.length())
		{
			return str;
		}
		String newString = "";

		for (int i = 0; i < str.length() - search.length(); i++)
		{
			if (str.substring(i, i + search.length()).equals(search))
			{
				newString = newString + replaceBy;
				i += search.length() - 1;
			}
			else
			{
				newString = newString + str.charAt(i);
			}
		}
		newString = newString + str.substring(str.length() - search.length(), str.length());
		return newString;
	}

	/**
	 * Transforma un short a array de bytes utlizando el sistema big-endian.
	 * <p/>
	 *
	 * @param i
	 *            El entero <tt>short</tt> a convertir
	 * @return El array de bytes que representa el entero
	 */
	public static byte[] shortToByteArray(short i)
	{
		final byte[] bytes = new byte[2];
		bytes[1] = (byte) (i >> 8 & 0xFF);
		bytes[0] = (byte) (i & 0xFF);
		return bytes;
	}

	/**
	 * Duerme el thread de ejecucion por el tiempo determinado.
	 *
	 * @param time
	 *            El tiempo que se quiere pausar el thread.
	 *
	 *
	 * @since 18/02/2011
	 */
	public static void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static <T extends Object> List<T> sort(List<T> col, String atributo)
	{
		for (final Object object : col)
		{
			try
			{
				// TODO Implemenetar
				BeanUtils.getProperty(object, atributo);
			}
			catch (final IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (final InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (final NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
		return col;
	}

	/**
	 *
	 * @param str
	 * @param splitChar
	 * @return
	 */
	public static String[] split(String str, String splitChar)
	{
		int splits = 0;
		int currentIndex = 0;
		int lastSplitPos = 0;
		str = replaceString(str, "  ", " ");
		str = str + " ";

		for (int i = 0; i < str.length(); i++)
		{
			if (str.substring(i, i + splitChar.length()).equals(splitChar))
			{
				splits++;
				lastSplitPos = i;
			}
		}
		if (lastSplitPos + splitChar.length() < str.length())
		{
			splits++;
		}
		lastSplitPos = 0;

		final String[] buffer = new String[splits];

		int j = 0;

		while (currentIndex < splits)
		{
			while (j + splitChar.length() < str.length() && !str.substring(j, j + splitChar.length()).equals(splitChar))
			{
				j++;
			}
			buffer[currentIndex] = str.substring(lastSplitPos, j);
			j += splitChar.length();
			lastSplitPos = j;
			currentIndex++;
		}
		return buffer;
	}

	public static String toString(Collection<?> list, String separator)
	{
		final StringBuilder sb = new StringBuilder();
		for (final Iterator<?> it = list.iterator(); it.hasNext();)
		{
			sb.append(it.next()).append(it.hasNext() ? separator : "");
		}
		return sb.toString();
	}

	/**
	 * Descomprime un archivo zip en la direccion destino
	 *
	 * @param zipFile
	 *            El archivo a descomprimir
	 * @param dest
	 *            El path del directorio de destino
	 * @throws IOException
	 *             Ante cualquier error en la escritura durante la descompresion.
	 *
	 * @since 10/07/2011
	 */
	public static void unZip(File zipFile, String dest) throws IOException
	{
		final FileInputStream fis = new FileInputStream(zipFile);
		final ZipInputStream zis = new ZipInputStream(fis);
		ZipEntry entry;
		FileOutputStream fos;
		byte[] data;
		int read = 0;
		FileUtils.createFolder(dest);
		BufferedOutputStream bos;
		while ((entry = zis.getNextEntry()) != null)
		{
			fos = new FileOutputStream(dest + PATH_SEPARATOR + entry.getName());
			bos = new BufferedOutputStream(fos, BUFFER);
			data = new byte[BUFFER];
			while ((read = zis.read(data)) != -1)
			{
				bos.write(data, 0, read);
			}
			zis.closeEntry();
			bos.flush();
			bos.close();
		}
		fis.close();
		zis.close();
	}

	/**
	 * Valida una cadena de caracteres como entero de una cantidad maxima de digitos.
	 *
	 * @param text
	 *            La cadena a validar
	 * @param maxChars
	 *            La cantidad maxima de caracteres
	 * @return <tt>true</tt> si la cadena es un entero valido con una cantidad valida de digitos, <tt>false</tt> en caso
	 *         contrario.
	 */
	public static boolean validateAlphaNumeric(String text, int maxChars)
	{
		final RegexValidator rv = new RegexValidator("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ._-]*");
		if (text.length() > maxChars || rv.validate(text) == null)
		{
			return false;
		}
		return true;
	}

	public static boolean validateEmail(String email)
	{
		final RegexValidator rv = new RegexValidator(
				"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
		return rv.validate(email) != null;
	}

	/**
	 * Valida una cadena de caracteres como entero de una cantidad maxima de digitos.
	 *
	 * @param number
	 *            La cadena a validar
	 * @param maxDigits
	 *            La cantidad maxima de digitos
	 * @return <tt>true</tt> si la cadena es un entero valido con una cantidad valida de digitos, <tt>false</tt> en caso
	 *         contrario.
	 */
	public static boolean validateIntegerNumber(String number, int maxDigits)
	{
		final RegexValidator rv = new RegexValidator("[0-9]*");
		if (number.length() > maxDigits || rv.validate(number) == null)
		{
			return false;
		}
		return true;
	}

	/**
	 * Valida que los parámetros enviados no sean null
	 *
	 * @param params
	 *            Los parametros
	 * @throws IllegalArgumentException
	 *
	 * @since 10/01/2011
	 */
	public static void validateParameters(Object... params) throws IllegalArgumentException
	{
		logParamaeters(params);
		if (params == null)
		{
			throw new IllegalArgumentException();
		}
		for (final Object object : params)
		{
			if (object == null)
			{
				log.error("Parametros no validos");
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * Valida la longitud de una cadena de caracteres.
	 *
	 * @param text
	 *            La cadena a validar
	 * @param maxChars
	 *            La cantidad maxima de caracteres
	 * @return <tt>true</tt> si la cadena tiene una cantidad de caracteres menor o igual a la indicada.
	 */
	public static boolean validateStringSize(String text, int maxChars)
	{
		if (!isNullOrEmpty(text) && text.length() > maxChars)
		{
			return false;
		}
		return true;
	}
}