package com.deportel.server.service.content.strategies;

import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.ConsultaDinamicaController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.server.web.ResourceManager;

/**
 * @author Emy
 */
public abstract class ComponentResponseStrategy
{
	protected final ComponenteController		componenteController		= ComponenteController.getInstance(false);
	protected final ResourceManager				rm							= ResourceManager.getInstance();
	protected final ConsultaDinamicaController	consultaDinamicaController	= ConsultaDinamicaController.getInstance(false);

	public abstract List<Componente> buildComponents (HttpServletRequest request, List<?> result, ConsultaDinamica consultaDinamica) throws InstanceNotFoundException;

	/**
	 * @param componentePadre
	 * @return
	 */
	protected short obtenerUbicacionY (Componente componentePadre)
	{
		Propiedad propiedad;
		String value;
		short y = 0;
		if ((propiedad = componentePadre.getPropiedadByTagXml(TipoPropiedad.TAG_Y_POS)) != null)
		{
			if ((value = propiedad.getDRegularData()) != null)
			{
				y = Short.parseShort(value);
			}
		}
		return y;
	}

	/**
	 * @param componente
	 * @return
	 */
	protected short obtenerUbicacionX (Componente componente)
	{
		Propiedad propiedad;
		String value;
		short x = 0;
		if ((propiedad = componente.getPropiedadByTagXml(TipoPropiedad.TAG_X_POS)) != null)
		{
			if ((value = propiedad.getDRegularData()) != null)
			{
				x = Short.parseShort(value);
			}
		}
		return x;
	}

	/**
	 * Le asigna un nombre al componente. Primero se determina si el componente de respuesta tiene o no alguna
	 * consulta dinamica asociada. Si la tiene, se le setea como nombre el nombre de la consulta, y si no se
	 * le setea su antiguo nombre con un sufijo. Esto se hace debido a que al componente que se envia como
	 * respuesta se le modifica el id en la capa mobile, entonces cuando eventualmente desde el telefono se
	 * dispare la consulta a traves de él el server no podra buscar la consulta asociada ya que su id ya no es
	 * el mismo.
	 *
	 * @param componente
	 *            El componente al que se le quiere setear el nombre
	 * @param index
	 *            El indice utilizado como sufijo para no repetir el nombre en distintos componentes.
	 */
	protected void setComponentName (Componente componente, Componente clon, int index)
	{
		Propiedad name = clon.getPropiedadByTagXml(TipoPropiedad.TAG_NAME);
		Propiedad action = clon.getPropiedadByTagXml(TipoPropiedad.TAG_ACTION);
		if (action != null && !Utils.isNullOrEmpty(action.getDRegularData()))
		{
			try
			{
				ConsultaDinamica consultaDinamica = this.consultaDinamicaController.findByIdSolicitante(componente.getCId());
				name.setDRegularData(consultaDinamica.getDNombre());
			}
			catch (InstanceNotFoundException e)
			{
				name.setDRegularData(name.getDRegularData() + "_" + index);
			}
		}
		else
		{
			name.setDRegularData(name.getDRegularData() + "_" + index);
		}
	}
}
