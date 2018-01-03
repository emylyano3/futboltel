package com.deportel.futboltel.torneo.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.deportel.futboltel.torneo.dao.hibernate.NoticiaDaoHibernate;
import com.deportel.futboltel.torneo.modelo.Categoria;
import com.deportel.futboltel.torneo.modelo.Equipo;
import com.deportel.futboltel.torneo.modelo.Noticia;

/**
 * @author Emy
 */
public class NoticiaController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public NoticiaController()
	{
		this.dao = new NoticiaDaoHibernate();
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger			log	= Logger.getLogger(NoticiaController.class);

	private final NoticiaDaoHibernate	dao;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Noticia findById (Integer id)
	{
		log.debug("Obteniendo el Noticia segun el id");
		return this.dao.find(id);
	}

	public List<Noticia> findAll ()
	{
		log.debug("Obteniendo todas las Noticias");
		return this.dao.findAll();
	}

	public List<Noticia> findByCategoria (Categoria categoria)
	{
		log.debug("Obteniendo todas las Noticias para una categoria");
		return this.dao.findByCategoria(categoria);
	}

	public List<Noticia> findByEquipo (Equipo equipo)
	{
		log.debug("Obteniendo todas las Noticias para una equipo");
		return this.dao.findByEquipo(equipo);
	}

	public Noticia create (Noticia Noticia)
	{
		log.debug("Creando la entidad de Noticia");
		Noticia = this.dao.create(Noticia);
		return Noticia;
	}

	public void remove (Integer id)
	{
		log.debug("Creando la entidad de Suscripcion");
		try
		{
			this.dao.remove(id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
