package com.deportel.futboltel.data.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.futboltel.torneo.dao.hibernate.EquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresEquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresPartidoDaoHibernate;
import com.deportel.futboltel.torneo.modelo.Categoria;
import com.deportel.futboltel.torneo.modelo.Equipo;
import com.deportel.futboltel.torneo.modelo.ValoresEquipo;
import com.deportel.futboltel.torneo.modelo.ValoresPartido;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;

public class GeneradorDatosEquipos
{
	@BeforeClass
	public static void initClass ()
	{
		TorneoSessionFactoryUtil.getInstance();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(GeneradorDatosEquipos.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(GeneradorDatosEquipos.class);
	}

	private final ValoresEquipoDaoHibernate		valoresEquipoDaoHibernate	= new ValoresEquipoDaoHibernate();

	private final ValoresPartidoDaoHibernate	valoresPartidoDaoHibernate	= new ValoresPartidoDaoHibernate();

	private final EquipoDaoHibernate			equipoDaoHibernate			= new EquipoDaoHibernate();

	@Test
	public void createValoresEquipo ()
	{
		List<ValoresPartido> valoresPartido = this.valoresPartidoDaoHibernate.findAll();
		List<Equipo> equipos = this.equipoDaoHibernate.findAll();
		Map<Categoria, List<ValoresEquipo>> mapValoresEquipos = new HashMap<Categoria, List<ValoresEquipo>>();
		createValoresEquipoInMemory(valoresPartido, equipos, mapValoresEquipos);
		establacerPosiciones(mapValoresEquipos);
		crearEntidadesValoresEquipo(mapValoresEquipos);
	}

	private void establacerPosiciones (Map<Categoria, List<ValoresEquipo>> mapValoresEquipos)
	{
		for (Categoria categoria : mapValoresEquipos.keySet())
		{
			ValoresEquipo ve1;
			ValoresEquipo ve2;
			int difGol1, difGol2;
			Object[] ves = mapValoresEquipos.get(categoria).toArray();
			boolean ordenado = false;
			while (!ordenado)
			{
				ordenado = true;
				for (int i = 0; i < ves.length - 1; i++)
				{
					ve1 = (ValoresEquipo) ves[i];
					ve2 = (ValoresEquipo) ves[i+1];
					difGol1 = ve1.getNGf() - ve1.getNGc();
					difGol2 = ve2.getNGf() - ve2.getNGc();
					if (ve1.getNPts() < ve2.getNPts() || ((ve1.getNPts() == ve2.getNPts()) && difGol1 < difGol2))
					{
						ves[i] = ve2;
						ves[i+1] = ve1;
						ordenado = false;
					}
				}
			}
			for (int i = 0; i < ves.length; i++)
			{
				((ValoresEquipo) ves[i]).setNPos(i+1);
			}
		}
	}

	private void crearEntidadesValoresEquipo (Map<Categoria, List<ValoresEquipo>> mapValoresEquipos)
	{
		for (Categoria categoria : mapValoresEquipos.keySet())
		{
			for (ValoresEquipo ve : mapValoresEquipos.get(categoria))
			{
				this.valoresEquipoDaoHibernate.create(ve);
			}
		}
	}

	private void createValoresEquipoInMemory (List<ValoresPartido> valoresPartido, List<Equipo> equipos, Map<Categoria, List<ValoresEquipo>> mapValoresEquipos)
	{
		ValoresEquipo ve;
		for (Equipo equipo : equipos)
		{
			ve = new ValoresEquipo();
			ve.setCategoria(equipo.getCategoria());
			ve.setEquipo(equipo);
			for (ValoresPartido partido : valoresPartido)
			{
				if (partido.getLocal().equals(equipo)) //El equipo es local
				{
					ve.setNGf(ve.getNGf() + partido.getNGolLocal());
					ve.setNGc(ve.getNGc() + partido.getNGolVisita());
					ve.setNPj(ve.getNPj() + 1);
					if (partido.getNGolLocal().intValue() > partido.getNGolVisita().intValue())
					{
						//suma 3
						ve.setNPg(ve.getNPg() + 1);
						ve.setNPts(ve.getNPts() + 3);
					}
					else if (partido.getNGolLocal().intValue() == partido.getNGolVisita().intValue())
					{
						//suma 1
						ve.setNPe(ve.getNPe() + 1);
						ve.setNPts(ve.getNPts() + 1);
					}
					else
					{
						//no suma
						ve.setNPp(ve.getNPp() + 1);
					}
				}
				else if (partido.getVisita().equals(equipo)) //El equipo es visita
				{
					ve.setNGf(ve.getNGf() + partido.getNGolVisita());
					ve.setNGc(ve.getNGc() + partido.getNGolLocal());
					ve.setNPj(ve.getNPj() + 1);
					if (partido.getNGolLocal() > partido.getNGolVisita())
					{
						//no suma
						ve.setNPp(ve.getNPp() + 1);
					}
					else if (partido.getNGolLocal() == partido.getNGolVisita())
					{
						//suma 1
						ve.setNPe(ve.getNPe() + 1);
						ve.setNPts(ve.getNPts() + 1);
					}
					else
					{
						//suma 3
						ve.setNPg(ve.getNPg() + 1);
						ve.setNPts(ve.getNPts() + 3);
					}
				}
			}
			List<ValoresEquipo> aux = mapValoresEquipos.get(ve.getCategoria());
			if (aux == null)
			{
				aux = new ArrayList<ValoresEquipo>();
				mapValoresEquipos.put(ve.getCategoria(), aux);
			}
			aux.add(ve);
		}
	}

}

