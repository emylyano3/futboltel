package com.deportel.futboltel.editor.loader;

import java.io.File;
import java.util.Properties;

public class EditorJar
{
	private final File			jar;
	private final Properties	jad;

	public EditorJar(File jar, Properties jad)
	{
		this.jar = jar;
		this.jad = jad;
	}

	/**
	 * @return the descriptor
	 */
	public Properties getJad ()
	{
		return this.jad;
	}

	/**
	 * @return the jar
	 */
	public File getJar ()
	{
		return this.jar;
	}
}
