package com.deportel.futboltel.data.generator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.futboltel.torneo.dao.hibernate.CategoriaDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.EquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresPartidoDaoHibernate;
import com.deportel.futboltel.torneo.modelo.Categoria;
import com.deportel.futboltel.torneo.modelo.Equipo;
import com.deportel.futboltel.torneo.modelo.ValoresPartido;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;

public class GeneradorDataPartidos
{
	@BeforeClass
	public static void initClass ()
	{
		TorneoSessionFactoryUtil.getInstance();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(GeneradorDataPartidos.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(GeneradorDataPartidos.class);
	}

	private final Logger								log							= Logger.getLogger(this.getClass());

	private final CategoriaDaoHibernate					categoriaDaoHibernate		= new CategoriaDaoHibernate();

	private final ValoresPartidoDaoHibernate			valoresPartidoDaoHibernate	= new ValoresPartidoDaoHibernate();

	private final EquipoDaoHibernate					equipoDaoHibernate			= new EquipoDaoHibernate();

	public static Random								random						= new Random();

	private static int									resultsCount;

	private static HashMap<Integer, ArrayList<Integer>>	results						= new HashMap<Integer, ArrayList<Integer>>();

	private static Map<Integer, Double> probabilidades = new HashMap<Integer, Double>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(0, 0.22D);
			put(1, 0.28D);
			put(2, 0.20D);
			put(3, 0.16D);
			put(4, 0.10D);
			put(5, 0.020D);//98
			put(6, 0.008D);
			put(7, 0.0065D);
			put(8, 0.0045D);//99.9
			put(9, 0.0007D);
			put(10, 0.0003D);
		}
	};

	@Test
	public void createPartidos ()
	{
		List<Categoria> categorias = this.categoriaDaoHibernate.findAll();
		for (Categoria categoria : categorias)
		{
			List<Equipo> equipos = this.equipoDaoHibernate.findByIdCategoria(categoria.getCId());
			Map<GregorianCalendar, List<Equipo>> fechas = initFechas(equipos.size());
			int start = 0, inc;
			Equipo local, visita;
			GregorianCalendar fecha;
			for (int i = start; i < equipos.size(); i++)
			{
				inc = 1;
				while (start + inc < equipos.size())
				{
					local = equipos.get(i);
					visita = equipos.get(i + inc++);
					ValoresPartido vp = new ValoresPartido();
					vp.setCategoria(categoria);
					vp.setLocal(local);
					vp.setVisita(visita);
					vp.setNGolVisita(getNextResult());
					vp.setNGolLocal(getNextResult());
					fecha = findFecha(local, visita, fechas);
					vp.setFFecha(new Timestamp(fecha.getTimeInMillis()));
					addEquipo(local, fecha, fechas);
					addEquipo(visita, fecha, fechas);
					this.log.debug(vp);
					vp = this.valoresPartidoDaoHibernate.create(vp);
				}
				++start;
			}
		}
	}

	private Integer getNextResult ()
	{
		int result;
		result = random.nextInt(10);
		while (!validResult(result))
		{
			result = random.nextInt(10);
		}
		return result;
	}

	private boolean validResult (int result)
	{
		boolean valid = false;
		ArrayList<Integer> resultsList = results.get(result);
		if (resultsList == null)
		{
			resultsList = new ArrayList<Integer>();
			results.put(result, resultsList);
		}
		if (resultsList.isEmpty())
		{
			valid = true;
			resultsCount++;
			resultsList.add(result);
		}
		else
		{
			double proba = probabilidades.get(result);
			double ratio = ((double)resultsList.size()) / ((double)resultsCount);
			if (ratio < proba)
			{
				valid = true;
				resultsCount++;
				resultsList.add(result);
			}
		}
		return valid;
	}

	private void addEquipo (Equipo equipo, GregorianCalendar fecha, Map<GregorianCalendar, List<Equipo>> fechas)
	{
		List<Equipo> equipoEnFecha = fechas.get(fecha);
		equipoEnFecha.add(equipo);
	}

	private GregorianCalendar findFecha (Equipo local, Equipo visita, Map<GregorianCalendar, List<Equipo>> fechas)
	{
		List<Equipo> equipos;
		for (GregorianCalendar fecha : fechas.keySet())
		{
			equipos = fechas.get(fecha);
			if (!(equipos.contains(local) || equipos.contains(visita)))
			{
				return fecha;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private Map<GregorianCalendar, List<Equipo>> initFechas (int cantEquipos)
	{
		GregorianCalendar cal;
		Map<GregorianCalendar, List<Equipo>> fechas = new HashMap<GregorianCalendar, List<Equipo>>(cantEquipos);
		int dom = 3;
		for (int i = 0; i < cantEquipos + 5; i++) // TODO Ver por que mierda tuve que harcodear
		{
			cal = new GregorianCalendar(2012, Calendar.MARCH, dom, 11, 15, 00);
			cal = updateFecha(cal);
			fechas.put(cal, new ArrayList<Equipo>());
			dom += 7;
			this.log.debug(cal.getTime());
		}
		return fechas;
	}

	private GregorianCalendar updateFecha (GregorianCalendar cal)
	{
		switch (cal.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.FRIDAY:
				cal.add(Calendar.DAY_OF_MONTH, 1);
				break;

			case Calendar.THURSDAY:
				cal.add(Calendar.DAY_OF_MONTH, 2);
				break;

			case Calendar.WEDNESDAY:
				cal.add(Calendar.DAY_OF_MONTH, 3);
				break;

			case Calendar.TUESDAY:
				cal.add(Calendar.DAY_OF_MONTH, 4);
				break;

			case Calendar.MONDAY:
				cal.add(Calendar.DAY_OF_MONTH, 5);
				break;

			case Calendar.SUNDAY:
				cal.add(Calendar.DAY_OF_MONTH, 6);
				break;

		}
		return cal;
	}

	@Test
	public void actualizarFechaPartidos ()
	{
		List<ValoresPartido> caloresPartidos = this.valoresPartidoDaoHibernate.findAll();
		for (ValoresPartido valorPartido : caloresPartidos)
		{
			Calendar c = new GregorianCalendar();
			Date fecha;
			this.log.debug("Fecha: " + (fecha = valorPartido.getFFecha()));
			c.setTime(fecha);
			c.add(Calendar.MONTH, 5);
			this.log.debug("Fecha actualizada: " + c.getTime());
			valorPartido.setFFecha(c.getTime());
			this.valoresPartidoDaoHibernate.update(valorPartido);
		}
	}
}

