package com.deportel.futboltel.persistencia.componentes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;
import com.deportel.controller.GlobalConsultasController;
import com.deportel.futboltel.torneo.modelo.Categoria;
import com.deportel.persistencia.utils.ConsultaUtils;
import com.deportel.persistencia.utils.QueryParam;

public class CustomQueryTest
{
	@BeforeClass
	public static void initClass ()
	{
		ComponentesSessionFactoryUtil.getInstance().getSessionFactory();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(CustomQueryTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(CustomQueryTest.class);
	}

	private final static Logger				log					= LoggerFactory.getLogger(CustomQueryTest.class);

	private final GlobalConsultasController	consultasController	= new GlobalConsultasController(true);

	@Test
	public void consultaDescripcionCategoria ()
	{
		log.debug("Comienza el test de consultaDescripcionCategoria");
		final String query = "select cat.DDescripcion from Categoria cat where DNombre = :name";
		final List<QueryParam> params = new ArrayList<QueryParam>();
		final QueryParam param = new QueryParam();
		param.setName("name");
		param.setType(ConsultaUtils.VARCHAR);
		param.setValue("Premier");
		params.add(param);
		final List<?> result = this.consultasController.executeCustomQuery(query, params);
		Object data;
		for (final Iterator<?> it = result.iterator(); it.hasNext();)
		{
			data = it.next();
			if (data != null)
			{
				log.debug("" + data);
			}
		}
		log.debug("Finaliza el test de consultaDescripcionCategoria");
	}

	@Test
	public void consultaCategoria ()
	{
		log.debug("Comienza el test de consultaCategoria");
		final String query = "from Categoria cat where DNombre = :name";
		final List<QueryParam> params = new ArrayList<QueryParam>();
		final QueryParam param = new QueryParam();
		param.setName("name");
		param.setType(ConsultaUtils.VARCHAR);
		param.setValue("Premier");
		params.add(param);
		final List<?> result = this.consultasController.executeCustomQuery(query, params);
		Categoria data;
		for (final Object name : result)
		{
			data = (Categoria) name;
			if (data != null)
			{
				log.debug("Nombre: " + data.getDNombre() + " | Descripcion: " + data.getDDescripcion() + " | Estado: " + data.getMEstado() + " | Id: " + data.getCId());
			}
		}
		log.debug("Finaliza el test de consultaCategoria");
	}

	@Test
	public void consultaNombreEquiposPorCategoria ()
	{
		log.debug("Comienza el test de consultaNombreEquiposPorCategoria");
		final String query = "select eq.DNombre from Equipo eq inner join eq.categoria as cat where cat.DNombre = :categoria";
		final List<QueryParam> params = new ArrayList<QueryParam>();
		final QueryParam param = new QueryParam();
		param.setName("categoria");
		param.setType(ConsultaUtils.VARCHAR);
		param.setValue("Premier");
		params.add(param);
		final List<?> result = this.consultasController.executeCustomQuery(query, params);
		Object data;
		for (final Iterator<?> it = result.iterator(); it.hasNext();)
		{
			data = it.next();
			if (data != null)
			{
				log.debug(data.toString());
			}
		}
		log.debug("Fin del test de consultaNombreEquiposPorCategoria");
	}

	@Test
	public void consultaSoporte ()
	{
		final String query = "select ss.DConsulta as Consulta from ServicioSoporte ss where ss.DNombre is not null";
		final List<QueryParam> params = new ArrayList<QueryParam>();
		final List<?> result = this.consultasController.executeCustomQuery(query, params);
		for (final Object data : result)
		{
			if (data != null)
			{
				log.debug(data.toString());
			}
		}
	}



}
