package com.deportel.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.modelo.Propiedad;

public class BajeImageWriterStrategy extends BjaeDataWriter
{

	@Override
	public void write (ByteArrayOutputStream bjae, Propiedad property) throws IOException
	{
		if (Utils.isNullOrEmpty(property.getDRegularData()))
		{
			bjae.write(BjaeBuilder.EMBEDDED_DATA); //Indica data embebida true
			this.data = property.getBinaryData();
			if (this.data != null)
			{
				bjae.write(Utils.intToByteArray(this.data.length));
				bjae.write(this.data);
			}
			else
			{
				log.info("La binary data de la imagen es nula. Verificar el dato en la base.");
				bjae.write(Utils.intToByteArray(0));
			}
		}
		else
		{
			this.data = property.getDRegularData().getBytes();
			bjae.write(BjaeBuilder.NOT_EMBEDDED_DATA); //Indica data embebida false
			bjae.write(Utils.shortToByteArray((short) this.data.length));
			bjae.write(this.data);
		}
	}

	@Override
	protected void validateData (Propiedad property)
	{
	}
}
