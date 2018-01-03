package com.deportel.administracion.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.administracion.dao.hibernate.ModuloDaoHibernate;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.Constants;

public class ModuloController
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public synchronized static ModuloController getInstance ()
	{
		if (instance == null)
		{
			instance = new ModuloController();
		}
		return instance;
	}

	private static ModuloController	instance;

	private ModuloController()
	{

	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static Logger				log					= Logger.getLogger(ModuloController.class);

	private final ModuloDaoHibernate	moduloDaoHibernate	= ModuloDaoHibernate.getInstance();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Modulo crear (Modulo modulo)
	{
		log.info("Comienza el m�todo crear");
		Transaction tx = this.moduloDaoHibernate.getSession().beginTransaction();
		try
		{
			modulo = this.moduloDaoHibernate.create(modulo);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return modulo;
	}

	public Modulo modificar (Modulo modulo)
	{
		log.info("Comienza el m�todo modificar");
		Transaction tx = this.moduloDaoHibernate.getSession().beginTransaction();
		try
		{
			modulo = this.moduloDaoHibernate.update(modulo);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return modulo;
	}

	public void deshabilitar (int idModulo)
	{
		log.info("Comienza el m�todo deshabilitar");
		Modulo modulo = null;
		Transaction tx = this.moduloDaoHibernate.getSession().beginTransaction();
		try
		{
			modulo = this.moduloDaoHibernate.findById(idModulo);
			modulo.setMEstado(Constants.DESHABILITADO);
			this.moduloDaoHibernate.update(modulo);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
	}

	public void eliminar (int idModulo)
	{
		log.info("Comienza el m�todo eliminar");
		Transaction tx = this.moduloDaoHibernate.getSession().beginTransaction();
		try
		{
			this.moduloDaoHibernate.remove(idModulo);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
	}

	/**
	 * Busca el nombre pasado como par�metro
	 * 
	 * @param nombre
	 * @return {@link Modulo}datos del modulo
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public Modulo findByName (String name)
	{
		Transaction tx = this.moduloDaoHibernate.getSession().beginTransaction();
		Modulo modulo = null;
		try
		{
			modulo = this.moduloDaoHibernate.findByName(name);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return modulo;
	}

	/**
	 * Busca el modulo segun el id recibido
	 * 
	 * @param id
	 *            El id del modulo
	 * @return {@link Modulo}datos del modulo
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public Modulo findById (Integer id)
	{
		//TODO Verificar si es necesario englobar el findById dentro de una TX
		Modulo modulo = this.moduloDaoHibernate.findById(id);
		if (modulo == null)
		{
			log.error("M�dulo no encontrado");
			throw new EntityNotFoundException("M�dulo no encontrado");
		}
		else
		{
			return modulo;
		}
	}

	/**
	 * Obtiene todos los modulos dados de alta
	 * 
	 * @return {@link Modulo} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public List<Modulo> findAll ()
	{
		Transaction tx = this.moduloDaoHibernate.getSession().beginTransaction();
		List<Modulo> resultados = null;
		try
		{
			resultados = this.moduloDaoHibernate.findAll();
			if (resultados == null)
			{
				throw new EntityNotFoundException("Modullos no encontrados");
			}
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return resultados;
	}

}
