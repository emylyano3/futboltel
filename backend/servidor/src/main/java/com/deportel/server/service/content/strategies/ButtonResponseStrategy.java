package com.deportel.server.service.content.strategies;

import static com.deportel.server.web.Resources.Constants.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;

/**
 * @author Emy
 */
public class ButtonResponseStrategy extends ComponentResponseStrategy
{
	private static final Logger	log	= Logger.getLogger(ButtonResponseStrategy.class);

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.response.strategies.ResponseStrategy#buildResponse(java.util.List, com.deportel.futboltel.modelo.componente.ConsultaDinamica)
	 */
	@Override
	public List<Componente> buildComponents (HttpServletRequest request, List<?> result, ConsultaDinamica consultaDinamica)
	{
		log.debug("Armando los componentes de respuesta: Buttons");
		Componente clone;
		String codigoTarget = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_TARGET));
		Componente target = this.componenteController.findByCodigo(codigoTarget);
		Componente componenteResuesta = consultaDinamica.getComponenteRespuesta();
		List<Componente> components = new ArrayList<Componente>(result.size());

		// Tomo la ubicacion del padre como referencia
		short x = obtenerUbicacionX(target);
		short y = obtenerUbicacionY(target);
		short h = Short.parseShort(componenteResuesta.getPropiedadByTagXml(TipoPropiedad.TAG_HEIGHT).getDRegularData());
		String componentText;
		Propiedad propiedad;
		Object row;
		for (int i = 0; i < result.size(); i++)
		{
			row = result.get(i);
			clone = this.componenteController.clone(componenteResuesta);

			// Actualizo el nombre del componente
			setComponentName(componenteResuesta, clone, i);

			// Modifico el texto, que es el dato que realimente quiero mostrar
			propiedad = clone.getPropiedadByTagXml(TipoPropiedad.TAG_TEXT);
			componentText = row.toString();
			propiedad.setDRegularData(componentText);

			// Actualizo la ubicacion del componente
			propiedad = clone.getPropiedadByTagXml(TipoPropiedad.TAG_X_POS);
			propiedad.setDRegularData(Short.toString(x));

			propiedad = clone.getPropiedadByTagXml(TipoPropiedad.TAG_Y_POS);
			propiedad.setDRegularData(Short.toString(y));

			propiedad = clone.getPropiedadByTagXml(TipoPropiedad.TAG_FATHER);
			propiedad.setDRegularData(codigoTarget);

			y += h;

			// TODO Agregar el icono como dato
			components.add(clone);
		}
		return components;
	}
}
