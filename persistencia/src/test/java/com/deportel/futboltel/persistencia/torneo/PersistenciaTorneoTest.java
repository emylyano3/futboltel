package com.deportel.futboltel.persistencia.torneo;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.common.Constants;
import com.deportel.futboltel.torneo.dao.TorneoGenericDao;
import com.deportel.futboltel.torneo.dao.hibernate.ArbitroDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.CanchaDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.CategoriaDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.EquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.HorarioDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.JugadorDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.SancionDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.TipoEventoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.TipoSancionDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.TorneoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresEquipoDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.ValoresJugadorDaoHibernate;
import com.deportel.futboltel.torneo.modelo.Arbitro;
import com.deportel.futboltel.torneo.modelo.Cancha;
import com.deportel.futboltel.torneo.modelo.Categoria;
import com.deportel.futboltel.torneo.modelo.Equipo;
import com.deportel.futboltel.torneo.modelo.Horario;
import com.deportel.futboltel.torneo.modelo.Jugador;
import com.deportel.futboltel.torneo.modelo.Sancion;
import com.deportel.futboltel.torneo.modelo.TipoEvento;
import com.deportel.futboltel.torneo.modelo.TipoSancion;
import com.deportel.futboltel.torneo.modelo.Torneo;
import com.deportel.futboltel.torneo.modelo.ValoresEquipo;
import com.deportel.futboltel.torneo.modelo.ValoresJugador;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;

public class PersistenciaTorneoTest
{
	@BeforeClass
	public static void initClass ()
	{
		TorneoSessionFactoryUtil.getInstance();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(PersistenciaTorneoTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(PersistenciaTorneoTest.class);
	}

	private final Logger				log							= Logger.getLogger(PersistenciaTorneoTest.class);

	private ValoresJugadorDaoHibernate	valoresJugadorDaoHibernate	= new ValoresJugadorDaoHibernate();

	private ValoresEquipoDaoHibernate	valoresEquipoDaoHibernate	= new ValoresEquipoDaoHibernate();

	private CategoriaDaoHibernate		categoriaDaoHibernate		= new CategoriaDaoHibernate();

	private JugadorDaoHibernate			jugadorDaoHibernate			= new JugadorDaoHibernate();

	private EquipoDaoHibernate			equipoDaoHibernate			= new EquipoDaoHibernate();

	//@Ignore
	@Test
	public void createCancha ()
	{
		this.log.debug("Creando la cancha");
		final Cancha cancha = new Cancha();
		cancha.setDDescripcion("cancha de prueba 1");
		cancha.setMEstado(Constants.HABILITADO);
		final TorneoGenericDao<Cancha, Integer> dao = new CanchaDaoHibernate();
		dao.create(cancha);
	}

	@Ignore
	@Test
	public void createHorario()
	{
		this.log.debug("Creando el horario");
		final Horario horario = new Horario();
		horario.setNHora(18);
		horario.setNMinuto(30);
		final TorneoGenericDao<Horario, Integer> dao = new HorarioDaoHibernate();
		dao.create(horario);
	}

	@Ignore
	@Test
	public void createCategoria()
	{
		this.log.debug("Creando la categoria");
		final Categoria categoria = new Categoria();
		categoria.setDDescripcion("Categoria de prueba 1");
		categoria.setDNombre("Categoria prueba 1");
		categoria.setMEstado(Constants.HABILITADO);
		final TorneoGenericDao<Categoria, Integer> dao = new CategoriaDaoHibernate();
		dao.create(categoria);
	}

	@Ignore
	@Test
	public void createEquipo()
	{
		this.log.debug("Creando el equipo");
		final Equipo equipo = new Equipo();
		equipo.setDDescripcion("Equipo de prueba 1");
		equipo.setDNombre("equipoPrueba1");
		equipo.setMEstado(Constants.HABILITADO);
		equipo.setCategoria(new CategoriaDaoHibernate().find(new Integer(1)));
		final TorneoGenericDao<Equipo, Integer> dao = new EquipoDaoHibernate();
		dao.create(equipo);
	}

	@Ignore
	@Test
	public void createTorneo()
	{
		this.log.debug("Creando el torneo");
		final Torneo torneo = new Torneo();
		torneo.setDNombre("torneoPrueba1");
		torneo.setDDescripcion("Torneo de prueba 1");
		torneo.setCategoria(new CategoriaDaoHibernate().find(new Integer(4)));
		//Agregar tipo torneo
		torneo.setFFechaInicio("03/03/2011");
		torneo.setFFechaFin("15/06/2011");
		torneo.setMEstado(Constants.HABILITADO);
		final TorneoGenericDao<Torneo, Integer> dao = new TorneoDaoHibernate();
		dao.create(torneo);
	}

	@Ignore
	@Test
	public void createJugador()
	{
		this.log.debug("Creando el jugador");
		final Jugador jugador = new Jugador();
		jugador.setDApellido("Apellido JugadorPrueba1");
		jugador.setDNombre("Nombre jugadorPrueba1");
		jugador.setDDireccion("Direccion del jugador prueba1");
		jugador.setDDocumento("DNI del jugador prueba1");
		jugador.setDTelefono("TE de jugador prueba1");
		jugador.setEquipo(new EquipoDaoHibernate().find(new Integer(1)));
		jugador.setMDelegado('0');
		jugador.setMEstado(Constants.HABILITADO);
		final TorneoGenericDao<Jugador, Integer> dao = new JugadorDaoHibernate();
		dao.create(jugador);
	}

	@Ignore
	@Test
	public void createArbiitro()
	{
		this.log.debug("Creando el arbitro");
		final Arbitro arbitro = new Arbitro();
		arbitro.setDApellido("Apellido ArbitroPrueba1");
		arbitro.setDNombre("Nombre ArbitoPrueba1");
		arbitro.setMEstado(Constants.HABILITADO);
		final TorneoGenericDao<Arbitro, Integer> dao = new ArbitroDaoHibernate();
		dao.create(arbitro);
	}

	@Ignore
	@Test
	public void createTipoEvento()
	{
		this.log.debug("Creando el tipo de evento");
		final TipoEvento tipoevento = new TipoEvento();
		tipoevento.setDDescripcion("Descripcion tipoEventoPrueba1");
		tipoevento.setDNombre("Nombre tipoEventoPrueba1");
		final TorneoGenericDao<TipoEvento, Integer> dao = new TipoEventoDaoHibernate();
		dao.create(tipoevento);
	}

	@Ignore
	@Test
	public void createTipoSancion()
	{
		this.log.debug("Creando el tipo de sancion");
		final TipoSancion tiposancion = new TipoSancion();
		tiposancion.setDDescripcion("Descripcion tipoSancionPrueba1");
		tiposancion.setDNombre("Nombre tipoSancionPrueba1");
		tiposancion.setMAgravada('S');
		final TorneoGenericDao<TipoSancion, Integer> dao = new TipoSancionDaoHibernate();
		dao.create(tiposancion);
	}

	@Ignore
	@Test
	public void createSancion()
	{
		this.log.debug("Creando la sancion");
		final Sancion sancion = new Sancion();
		sancion.setEquipo(new EquipoDaoHibernate().find(new Integer(1)));
		sancion.setJugador(new JugadorDaoHibernate().find(new Integer(1)));
		sancion.setTipoSancion(new TipoSancionDaoHibernate().find(new Integer(1)));
		sancion.setMEstado(Constants.HABILITADO);
		final TorneoGenericDao<Sancion, Integer> dao = new SancionDaoHibernate();
		dao.create(sancion);
	}

	@Test
	public void testByteReadWrite ()
	{
		short s = (short) 3;
		this.log.debug("Tengo el short: " + s);
		this.log.debug("Lo convierto a array de bytes");
		byte[] sb = shortToByteArray(s);
		this.log.debug("Lo paso a short");
		short s2 = readShort(sb, new int[] {0});
		this.log.debug("Tengo el short nuevo: " + s2);
	}

	/**
	 * 
	 */
	public static short readShort (byte[] file, int[] curOffset)
	{
		int off = curOffset[0];
		curOffset[0] += 2;
		short theInt = 0;
		int i = 0;
		try
		{
			for (i = curOffset[0] - 1; i >= off; i--)
			{
				theInt = (short) ((theInt <<  8) | (file[i] &  0xFF));
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		}
		return theInt;
	}

	@Test
	public void valoresJugador ()
	{
		ValoresJugador vj = new ValoresJugador();
		Categoria cat = this.categoriaDaoHibernate.find(1);
		Equipo eq = this.equipoDaoHibernate.find(1);
		Jugador jug = this.jugadorDaoHibernate.find(1);
		vj.setCategoria(cat);
		vj.setEquipo(eq);
		vj.setJugador(jug);
		vj.setNAmarillas(0);
		vj.setNFechasSusp(0);
		vj.setNGoles(1);
		vj = this.valoresJugadorDaoHibernate.create(vj);
		this.valoresJugadorDaoHibernate.remove(vj.getCId());
		this.log.debug(vj);
	}

	@Test
	public void valoresEquipo ()
	{
		ValoresEquipo ve = new ValoresEquipo();
		Categoria cat = this.categoriaDaoHibernate.find(1);
		Equipo eq = this.equipoDaoHibernate.find(1);
		ve.setCategoria(cat);
		ve.setEquipo(eq);
		ve.setNGc(12);
		ve.setNGf(15);
		ve.setNPg(4);
		ve.setNPp(2);
		ve.setNPe(2);
		ve.setNPts(2);
		ve = this.valoresEquipoDaoHibernate.create(ve);
		this.log.debug(ve);
		this.valoresEquipoDaoHibernate.remove(ve.getCId());
	}

	/**
	 * Transforma un short a array de bytes utlizando el sistema
	 * big-endian.
	 * <p/>
	 * @param i
	 *            El entero <tt>short</tt> a convertir
	 * @return El array de bytes que representa el entero
	 */
	public static byte[] shortToByteArray (short i)
	{
		byte[] bytes = new byte[3];
		bytes[2] = (byte) ((i >> 16) & 0xFF);
		bytes[1] = (byte) ((i >> 8)  & 0xFF);
		bytes[0] = (byte) ( i		 & 0xFF);
		return bytes;
	}
	
	@Test
	public void executeSQL ()
	{
		Session session = categoriaDaoHibernate.getSessionAndBeginTransaction();
		List<?> result = session.createSQLQuery("select n.d_nombre from noticia n join equipo e on e.c_id = n.equipo_c_id where e.d_nombre = 'ATAUN'").list();
		log.debug(result);
	}
}

