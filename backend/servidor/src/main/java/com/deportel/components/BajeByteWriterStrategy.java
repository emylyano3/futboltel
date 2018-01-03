package com.deportel.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.modelo.Propiedad;

public class BajeByteWriterStrategy extends BjaeDataWriter
{
	@Override
	public void write (ByteArrayOutputStream bjae, Propiedad property) throws IOException
	{
		bjae.write(this.data);
	}

	@Override
	protected void validateData (Propiedad property)
	{
		if (Utils.isNullOrEmpty(property.getDRegularData()))
		{
			this.data = new byte[0];
		}
		else
		{
			this.data = new byte[] {Byte.parseByte(property.getDRegularData())};
		}
	}
}
