package com.deportel.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.deportel.componentes.modelo.Propiedad;

public abstract class BjaeDataWriter
{
	protected static Logger	log	= Logger.getLogger(BjaeDataWriter.class);
	protected byte[]		data;

	public abstract void write (ByteArrayOutputStream bjae, Propiedad property) throws IOException;

	protected abstract void validateData (Propiedad property);

	public void writeData (ByteArrayOutputStream bjae, Propiedad property) throws IOException
	{
		validateData(property);
		write(bjae, property);
	}
}
