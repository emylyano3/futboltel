package com.deportel.server.service.content.strategies;

import static com.deportel.server.web.Resources.Texts.*;
import static com.deportel.server.web.Resources.Constants.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.ParametroConsulta;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.server.web.ResourceManager;

/**
 * @author Emy
 */
public class TableResponseStrategy extends ComponentResponseStrategy
{
	private final static Logger		log	= Logger.getLogger(TableResponseStrategy.class);
	private final ResourceManager	rm	= ResourceManager.getInstance();

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.response.strategies.ResponseStrategy#buildResponse(java.util.List, com.deportel.futboltel.modelo.componente.ConsultaDinamica)
	 */
	@Override
	public List<Componente> buildComponents (HttpServletRequest request, List<?> result, ConsultaDinamica consultaDinamica)
	{
		log.debug("Armando el componente tabla de respuesta");
		List<Componente> components = new ArrayList<Componente>(result.size());
		String codigoTarget = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_TARGET));
		Componente target = this.componenteController.findByCodigo(codigoTarget);
		Componente componenteResuesta = consultaDinamica.getComponenteRespuesta();
		//Tomo la ubicacion del target como referencia
		short x = obtenerUbicacionX(target);
		short y = obtenerUbicacionY(target);
		Propiedad propiedad;
		propiedad = componenteResuesta.getPropiedadByTagXml(TipoPropiedad.TAG_X_POS);
		propiedad.setDRegularData(Short.toString(x));
		propiedad = componenteResuesta.getPropiedadByTagXml(TipoPropiedad.TAG_Y_POS);
		propiedad.setDRegularData(Short.toString(y));
		propiedad = componenteResuesta.getPropiedadByTagXml(TipoPropiedad.TAG_DATA);
		propiedad.setDRegularData(buildTableData(result, consultaDinamica.getNRowLimit()));
		propiedad = componenteResuesta.getPropiedadByTagXml(TipoPropiedad.TAG_DATA_DESC);
		propiedad.setDRegularData(buildTableDescription(result, consultaDinamica));
		propiedad = componenteResuesta.getPropiedadByTagXml(TipoPropiedad.TAG_FATHER);
		propiedad.setDRegularData(target.getPropiedadByTagXml(TipoPropiedad.TAG_ID).getDRegularData());
		components.add(componenteResuesta);
		return components;
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private String buildTableData (List<?> result, int rowLimit)
	{
		Object[] row;
		StringBuilder sb = new StringBuilder();
		boolean limitRow = rowLimit > 0;
		int rowCount = 0;
		if (!Utils.isNullOrEmpty(result))
		{
			for (Object rawRow : result)
			{
				row = (Object[]) rawRow;
				for (Object element : row)
				{
					sb.append(element);
					sb.append(this.rm.getConst(SPLITTER_TABLE_DATA));
				}
				if (limitRow && ++rowCount >= rowLimit)
				{
					break;
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param consultaDinamica
	 * @return
	 */
	private String buildTableDescription (List<?> result, ConsultaDinamica consultaDinamica)
	{
		StringBuilder sb = new StringBuilder();
		if (!Utils.isNullOrEmpty(result))
		{
			Set<ParametroConsulta> queryParams = consultaDinamica.getParametrosConsulta();
			SortedSet<ParametroConsulta> sorted = orderAndFilterQueryParams(queryParams);
			for (ParametroConsulta queryParam : sorted)
			{
				if (!Utils.isNullOrEmpty(queryParam.getNombre()))
				{
					sb.append(queryParam.getNombre().trim());
					sb.append(this.rm.getConst(SPLITTER_TABLE_DATA));
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		else
		{
			sb.append(this.rm.getText(MOBILE_REQUEST_NO_DATA));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param queryParams
	 * @return
	 */
	private SortedSet<ParametroConsulta> orderAndFilterQueryParams (Set<ParametroConsulta> queryParams)
	{
		SortedSet<ParametroConsulta> sorted = new TreeSet<ParametroConsulta>();
		for (ParametroConsulta parametroConsulta : queryParams)
		{
			if (Constants.HABILITADO.equals(parametroConsulta.getMTipoSalida()))
			{
				sorted.add(parametroConsulta);
			}
		}
		return sorted;
	}

}
