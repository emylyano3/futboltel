package com.deportel.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.modelo.Propiedad;

public class BajeIntWriterStrategy extends BjaeDataWriter
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
			this.data = Utils.intToByteArray(0);
		}
		else
		{
			this.data = Utils.intToByteArray(Integer.parseInt(property.getDRegularData()));
		}
	}
}
