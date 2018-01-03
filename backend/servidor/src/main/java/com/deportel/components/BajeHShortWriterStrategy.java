package com.deportel.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.modelo.Propiedad;

public class BajeHShortWriterStrategy extends BjaeDataWriter
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
			this.data = Utils.shortToByteArray((short)0);
		}
		else
		{
			this.data = Utils.shortToByteArray(Short.parseShort(property.getDRegularData(), Constants.HEXA_DECIMAL));
		}
	}
}
