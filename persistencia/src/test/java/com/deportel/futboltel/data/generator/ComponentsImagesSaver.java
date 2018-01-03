package com.deportel.futboltel.data.generator;

import java.io.InputStream;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.PropiedadController;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;

public class ComponentsImagesSaver
{
	@BeforeClass
	public static void initClass ()
	{
		TorneoSessionFactoryUtil.getInstance();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(ComponentsImagesSaver.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(ComponentsImagesSaver.class);
	}

	private final PropiedadController	propiedadController	= PropiedadController.getInstance(true);

	@Test
	public void saveImage ()
	{
		try
		{
			final Componente componente = ComponenteController.getInstance(true).findByCodigo("6000000");
			final TipoPropiedad tipoPropiedadImagen = TipoPropiedadController.getInstance(true).findByXmlTag("img");
			for (final Propiedad propiedad : componente.getPropiedades())
			{
				if (propiedad.getTipoPropiedad().equals(tipoPropiedadImagen))
				{
					final InputStream is = this.getClass().getResourceAsStream("/banner.png");
					final byte[] imageData = inputStreamToByteArray(is);

					propiedad.setBinaryData(imageData);
					propiedad.setNAltoPantalla(320);
					propiedad.setNAnchoPantalla(240);

					this.propiedadController.update(propiedad);
					break;
				}
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Transforma un input stram a un byte array.
	 *
	 * @param is
	 *            {@link InputStream}
	 * @return byte[] Array de bytes
	 */
	private byte[] inputStreamToByteArray (final InputStream is)
	{
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
		}
		return array;
	}
}

