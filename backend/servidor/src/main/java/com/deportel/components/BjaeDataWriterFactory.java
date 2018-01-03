package com.deportel.components;

import com.deportel.componentes.modelo.TipoPropiedad;

public class BjaeDataWriterFactory
{
	public BjaeDataWriter getWriter (TipoPropiedad tp)
	{
		if (tp.getDTipoDato().equals(BjaeBuilder.STRING_DATA))
		{
			return new BajeStringWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.INT_DATA))
		{
			return new BajeIntWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.HEXA_INT_DATA))
		{
			return new BajeHIntWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.SHORT_DATA))
		{
			return new BajeShortWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.HEXA_SHORT_DATA))
		{
			return new BajeHShortWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.LONG_DATA))
		{
			return new BajeLongWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.HEXA_LONG_DATA))
		{
			return new BajeHLongWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.BYTE_DATA))
		{
			return new BajeByteWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.HEXA_BYTE_DATA))
		{
			return new BajeHByteWriterStrategy();
		}
		else if (tp.getDTipoDato().equals(BjaeBuilder.IMAGE_DATA))
		{
			return new BajeImageWriterStrategy();
		}
		return null;
	}
}
