package com.deportel.futboltel.data.generator;

import java.util.List;
import java.util.Random;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.futboltel.torneo.dao.hibernate.EquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.JugadorDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresEquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresJugadorDaoHibernate;
import com.deportel.futboltel.torneo.modelo.Equipo;
import com.deportel.futboltel.torneo.modelo.Jugador;
import com.deportel.futboltel.torneo.modelo.ValoresEquipo;
import com.deportel.futboltel.torneo.modelo.ValoresJugador;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;

public class GeneradorDatosJugadores
{
	@BeforeClass
	public static void initClass ()
	{
		TorneoSessionFactoryUtil.getInstance();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(GeneradorDatosJugadores.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(GeneradorDatosJugadores.class);
	}

	private final ValoresJugadorDaoHibernate	valoresJugadorDaoHibernate	= new ValoresJugadorDaoHibernate();

	private final ValoresEquipoDaoHibernate		valoresEquipoDaoHibernate	= new ValoresEquipoDaoHibernate();

	private final JugadorDaoHibernate			jugadorDaoHibernate			= new JugadorDaoHibernate();

	private final EquipoDaoHibernate			equipoDaoHibernate			= new EquipoDaoHibernate();

	@Test
	public void insertValoresJugador ()
	{
		Integer golesEquipo = 0;
		Integer golesJugador = 0;
		Integer amarillas;
		Integer fechasSuspencion;
		ValoresEquipo valoresEquipo;
		ValoresJugador valoresJugador;
		Random random = new Random();
		List<Equipo> equipos = this.equipoDaoHibernate.findAll();
		for (Equipo equipo : equipos)
		{
			List<Jugador> jugadores = this.jugadorDaoHibernate.findByIdEquipo(equipo.getCId());
			valoresEquipo = this.valoresEquipoDaoHibernate.findByIdEquipo(equipo.getCId());
			golesEquipo = valoresEquipo.getNGf();
			for (Jugador jugador : jugadores)
			{
				//Calculo los goles
				if (golesEquipo > 0)
				{
					golesJugador = random.nextInt(golesEquipo);
					golesEquipo = golesEquipo - golesJugador >= 0 ? golesEquipo - golesJugador : golesEquipo;
				}
				//Calculo las tarjetas
				fechasSuspencion = random.nextInt(1) * random.nextInt(1);
				amarillas = random.nextInt(3) + random.nextInt(1);
				//Creo el registro
				valoresJugador = new ValoresJugador();
				valoresJugador.setCategoria(equipo.getCategoria());
				valoresJugador.setEquipo(equipo);
				valoresJugador.setJugador(jugador);
				valoresJugador.setNAmarillas(amarillas);
				valoresJugador.setNFechasSusp(fechasSuspencion);
				valoresJugador.setNGoles(golesJugador);

				this.valoresJugadorDaoHibernate.create(valoresJugador);
			}
		}
	}
}

