package com.deportel.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.deportel.common.Constants;

public final class JarClassLoader extends ClassLoader
{
	private final JarFile			file;

	public static final int			BUFFER_SIZE	= 1024;

	private static JarClassLoader	instance;

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static JarClassLoader getInstance (String fileName) throws IOException
	{
		if (instance == null)
		{
			instance = new JarClassLoader(fileName);
		}
		return instance;
	}

	/**
	 * 
	 * @param filename
	 * @throws IOException
	 */
	private JarClassLoader(String filename) throws IOException
	{
		this.file = new JarFile(filename);
	}

	/**
	 * 
	 */
	@Override
	protected Class <?> findClass(String name) throws ClassNotFoundException
	{
		ZipEntry entry = this.file.getEntry(name.replace(Constants.PACKAGE_SEPARATOR, Constants.PATH_SEPARATOR) + Constants.DOT + Constants.CLASS_EXTENSION);
		if (entry == null)
		{
			throw new ClassNotFoundException(name);
		}
		try
		{
			byte[] array = new byte[BUFFER_SIZE];
			InputStream in = this.file.getInputStream(entry);
			ByteArrayOutputStream out = new ByteArrayOutputStream(array.length);
			int length = in.read(array);
			while (length > 0)
			{
				out.write(array, 0, length);
				length = in.read(array);
			}
			return defineClass(name, out.toByteArray(), 0, out.size());
		}
		catch (IOException exception)
		{
			throw new ClassNotFoundException(name, exception);
		}
	}
}
