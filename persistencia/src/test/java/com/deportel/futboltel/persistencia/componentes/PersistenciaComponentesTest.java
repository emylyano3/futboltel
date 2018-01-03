package com.deportel.futboltel.persistencia.componentes;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.management.InstanceNotFoundException;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.common.Constants;
import com.deportel.componentes.controller.AccionComponenteController;
import com.deportel.componentes.controller.AlineacionComponenteController;
import com.deportel.componentes.controller.ClienteController;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.ConsultaDinamicaController;
import com.deportel.componentes.controller.FuenteContenidoController;
import com.deportel.componentes.controller.PropiedadController;
import com.deportel.componentes.controller.SuscripcionController;
import com.deportel.componentes.controller.TemaController;
import com.deportel.componentes.controller.TipoComponenteController;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.controller.TransaccionController;
import com.deportel.componentes.modelo.AccionComponente;
import com.deportel.componentes.modelo.AlineacionComponente;
import com.deportel.componentes.modelo.Cliente;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.ParametroConsulta;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.Suscripcion;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.modelo.TipoComponente;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.componentes.modelo.Transaccion;
import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;

import static org.junit.Assert.*;

public class PersistenciaComponentesTest
{
	@BeforeClass
	public static void initClass ()
	{
		ComponentesSessionFactoryUtil.getInstance().getSessionFactory();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(PersistenciaComponentesTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(PersistenciaComponentesTest.class);
	}

	private final static Logger					log							= LoggerFactory.getLogger(PersistenciaComponentesTest.class);

	private final ConsultaDinamicaController	consultaDinamicaController	= ConsultaDinamicaController.getInstance(true);

	private final TemaController				temaController				= TemaController.getInstance(true);

	private final SuscripcionController			suscripcionController 		= SuscripcionController.getInstance(true);

	private final TransaccionController				transaccionController			= TransaccionController.getInstance(true);

	private final ClienteController					clienteController				= ClienteController.getInstance(true);

	private final ComponenteController				componenteController			= ComponenteController.getInstance(true);

	private final TipoPropiedadController			tipoPropiedadController			= TipoPropiedadController.getInstance(true);

	private final FuenteContenidoController			fuenteContenidoController		= FuenteContenidoController.getInstance(true);

	private final PropiedadController				propiedadController				= PropiedadController.getInstance(true);

	private final AccionComponenteController		accionAplicacionController		= AccionComponenteController.getInstance(true);

	private final AlineacionComponenteController	alineacionComponenteController	= AlineacionComponenteController.getInstance(true);

	@Test
	public void transaccionControllerCreate()
	{
		Transaccion transaccion = new Transaccion(null, new Date(), 1000L);
		transaccion = this.transaccionController.create(transaccion);
		assertNotNull(transaccion.getCId());
	}

	@Test
	public void clienteControllerCreate()
	{
		Cliente cliente = new Cliente("PIBE");
		cliente = this.clienteController.create(cliente);
		assertNotNull(cliente.getId());
	}

	@Test(expected=InstanceNotFoundException.class)
	public void findTipoComponente () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findTipoComponente]");
		final TipoComponenteController controller = TipoComponenteController.getInstance(true);
		controller.findById(999);
		log.debug("Finaliza el test [findTipoComponente]");
	}

	@Ignore
	@Test
	public void findTipoComponente2 () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findTipoComponente2]");
		final TipoComponenteController controller = TipoComponenteController.getInstance(true);
		final TipoComponente tc = controller.findById(tipoComponenteId);
		log.debug("Componente Creado: " + tc);
		log.debug("Finaliza el test [findTipoComponente2]");
	}

	@Ignore
	@Test
	public void removeTipoComponente () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [removeTipoComponente]");
		final TipoComponenteController controller = TipoComponenteController.getInstance(true);
		controller.remove(tipoComponenteId);
		log.debug("Finaliza el test [removeTipoComponente]");
	}

	@Test
	public void getComponente () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [getComponente]");
		final ComponenteController controller = ComponenteController.getInstance(true);
		final Componente c = controller.findById(250);
		log.debug("Se encontro el componente:" + c);
		log.debug("Finaliza el test [getComponente]");
	}

	@Test(expected=InstanceNotFoundException.class)
	public void findComponente () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findComponente]");
		final ComponenteController controller = ComponenteController.getInstance(true);
		controller.findById(67108866);
		log.debug("Finaliza el test [findComponente]");
	}

	@Test
	public void findComponente2 () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findComponente2]");
		final ComponenteController controller = ComponenteController.getInstance(true);
		final Integer id = 250;
		final Componente component = controller.findById(id);
		log.debug
		(
				"Id: " + component.getCId() + " - " +
				"Estado: " + component.getMEstado() + " - " +
				"Tipo de Componente: " + component.getTipoComponente().getDNombre() + " - " +
				"Indice de Componente: " + component.getTipoComponente().getCTipoComponente()
		);
		log.debug("Finaliza el test [findComponente2]");
	}

	@Test
	public void findPropiedad () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findPropiedad]");
		final PropiedadController controller = PropiedadController.getInstance(true);
		final Propiedad propiedad = controller.findById(6300);
		log.debug("Se encontro la propiedad: " + propiedad);
		log.debug("Finaliza el test [findPropiedad]");
	}

	/*
	 * Test de Tipo de Componente
	 */

	@Test
	public void findAllTipoComponente() throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findAllTipoComponente]");
		final TipoComponenteController controller = TipoComponenteController.getInstance(true);
		final List<TipoComponente> tiposComponente = controller.findAll();
		TipoComponente tipoComponente;
		for (final Iterator<TipoComponente> it = tiposComponente.iterator(); it.hasNext();)
		{
			tipoComponente = it.next();
			log.debug
			(
					"Nombre: [" + tipoComponente.getDNombre() + "] - " +
					"Descripcion: [" + tipoComponente.getDDescripcion() + "] - " +
					"Indice: [" + tipoComponente.getCTipoComponente() + "] - " +
					"Tag XML: [" + tipoComponente.getDTagXml() + "]"
			);
		}
		log.debug("Finaliza el test [findAllTipoComponente]");
	}

	/*
	 * Test accion componente
	 */
	@Test
	public void findAllAccionAplicacion ()
	{
		final List<AccionComponente> result = this.accionAplicacionController.findAll();
		log.debug(result.toString());
	}

	/*
	 * Test alineacion componente
	 */
	@Test
	public void findAllAlineacionAplicacion ()
	{
		final List<AlineacionComponente> result = this.alineacionComponenteController.findAll();
		log.debug(result.toString());
	}

	/*
	 * Test de Tema
	 */

	@Test
	public void findTema () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [findTema]");
		final Tema tema = this.temaController.findByName("Futboltel");
		Componente componente;
		Propiedad property;
		for (final Iterator<Componente> itComponent = tema.getComponentes().iterator(); itComponent.hasNext();)
		{
			componente = itComponent.next();
			for (final Iterator<Propiedad> itProperty = componente.getPropiedades().iterator(); itProperty.hasNext();)
			{
				property = itProperty.next();
				log.debug
				(
						"Id Componente: " + componente.getCId() + " - " +
						"Tipo Propiedad: " + property.getTipoPropiedad().getDNombre() + " - " +
						"Valor Propiedad: " + property.getDRegularData()
				);
			}
		}
		log.debug("Finaliza el test [findTema]");
	}

	@Test
	public void suscripcion () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [suscripcion]");
		//Creo un cliente
		Cliente cliente = new Cliente("Emy");
		cliente = this.clienteController.create(cliente);
		//Busco un tema
		final Tema tema = this.temaController.findByName("Futboltel");
		//Creo una suscripcion
		Suscripcion suscripcion = new Suscripcion(cliente, tema, "Unterhaching", "sarasa@algomail.com", "1500112233");
		suscripcion = this.suscripcionController.create(suscripcion);
		//Elimino las entidades creadas
		this.suscripcionController.remove(suscripcion.getId());
		this.clienteController.remove(cliente.getId());
		log.debug("Finaliza el test [suscripcion]");
	}

	@Ignore
	@Test
	public void createDynamicQuery () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [createDynamicQuery]");
		ConsultaDinamica c = new ConsultaDinamica();
		c.setConsulta("select * from Componente");
		c.setDDescripcion("Query creada con modivos de testing");
		c.setDNombre("Query para testing");
		c.setComponenteRespuesta(this.componenteController.findById(7));
		c.setComponenteSolicitante(this.componenteController.findById(2));
		c.setTema(this.temaController.findById(1));
		c = this.consultaDinamicaController.create(c);
		dynamicQueryId = c.getCId();
		log.debug("Se creo el componente: " + c);
		log.debug("Finaliza el test [createDynamicQuery]");
	}

	@Ignore
	@Test
	public void updateDynamicQuery () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [updateDynamicQuery]");
		final ConsultaDinamica c = this.consultaDinamicaController.findById(dynamicQueryId);
		c.setDNombre("Test_Query");
		c.setComponenteSolicitante(this.componenteController.findById(3));
		this.consultaDinamicaController.update(c);
		log.debug("Finaliza el test [updateDynamicQuery]");
	}

	public void createQueryParams () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [createQueryParams]");
		final ParametroConsulta pc = new ParametroConsulta();
		pc.setConsultaDinamica(this.consultaDinamicaController.findById(dynamicQueryId));
		pc.setMTipoSalida(Constants.DESHABILITADO);
		pc.setNombre("Equipo");
		pc.setTipoDato("Texto");
		log.debug("Finaliza el test [createQueryParams]");
	}

	@Ignore
	@Test
	public void removeDynamicQuery () throws InstanceNotFoundException
	{
		log.debug("Inicia el test [removeDynamicQuery]");
		this.consultaDinamicaController.remove(dynamicQueryId);
		log.debug("Finaliza el test [removeDynamicQuery]");
	}

	@Ignore
	@Test
	public void update ()
	{
		Componente componente;
		Propiedad propiedad;
		final List<Componente> componentes = this.componenteController.findAll();
		for (final Iterator<Componente> it = componentes.iterator(); it.hasNext();)
		{
			componente = it.next();
			for (final Iterator<Propiedad> it2 = componente.getPropiedades().iterator(); it2.hasNext();)
			{
				propiedad = it2.next();
				if (propiedad.getTipoPropiedad().getCId().equals(16))
				{
					componente.setCComponente(propiedad.getDRegularData());
					this.componenteController.update(componente);
				}
			}
		}
	}

	@Ignore
	@Test
	public void setFathers ()
	{
		final List<Componente> componentes = this.componenteController.findAll();
		for (final Componente componente : componentes)
		{
			final Propiedad propiedad = new Propiedad();
			final TipoPropiedad tipoPropiedad = this.tipoPropiedadController.findByName("Father");
			final FuenteContenido fuenteContenido = this.fuenteContenidoController.findByName("RMS");
			propiedad.setBinaryData(null);
			propiedad.setComponente(componente);
			final Componente padre = componente.getPadre();
			if (padre != null)
			{
				propiedad.setDRegularData(padre.getCComponente());
			}
			else
			{
				propiedad.setDRegularData("");
			}
			propiedad.setFuenteContenido(fuenteContenido);
			propiedad.setNAltoPantalla(0);
			propiedad.setNAnchoPantalla(0);
			propiedad.setTipoPropiedad(tipoPropiedad);
			this.propiedadController.create(propiedad);
		}
	}

	@Ignore
	@Test
	public void elimnarPadresEspurios ()
	{
		final Collection<Componente> componentes = this.componenteController.findAll();
		Componente componente;
		Propiedad propiedad;
		boolean isSafeToDelete = false;
		for (final Iterator<Componente> it = componentes.iterator(); it.hasNext();)
		{
			componente = it.next();
			for (final Iterator<Propiedad> it2 = componente.getPropiedades().iterator(); it2.hasNext();)
			{
				propiedad = it2.next();
				if (isSafeToDelete && propiedad.getTipoPropiedad().getDNombre().equalsIgnoreCase("Father"))
				{
					try
					{
						this.propiedadController.remove(propiedad.getCId());
						log.debug("Se elimino la propiedad: " + propiedad.getCId());
					}
					catch (final InstanceNotFoundException e)
					{
						log.error("Error al querer eliminar la propiedad: " + propiedad.getCId());
					}
				}
				else
				{
					isSafeToDelete = isSafeToDelete || propiedad.getTipoPropiedad().getDNombre().equalsIgnoreCase("Father");
				}
			}
			isSafeToDelete = false;
		}
	}

	private static Integer dynamicQueryId;
	private static Integer tipoComponenteId;
}
