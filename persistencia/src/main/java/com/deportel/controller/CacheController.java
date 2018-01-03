package com.deportel.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.ParametroConsulta;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoComponente;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;

public class CacheController
{
	private static final Logger	log				= Logger.getLogger(CacheController.class);

	public void reloadCache() throws IOException
	{
		log.info("Haciendo un reload de la cache del esquema componentes");
		evictEntyties();
		clearSessions();
	}

	@SuppressWarnings("deprecation")
	private void clearSessions()
	{
		Session session = ComponentesSessionFactoryUtil.getInstance().getCurrentSession();
		if (session == null)
		{
			session = ComponentesSessionFactoryUtil.getInstance().openSession();
		}
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.clear();
			session.reconnect();
			tx.commit();
		}
		catch (final Exception e)
		{
			if (tx != null) tx.rollback();
			throw new RuntimeException(e);
		}
	}

	private void evictEntyties()
	{
		Session session = ComponentesSessionFactoryUtil.getInstance().getCurrentSession();
		if (session == null)
		{
			session = ComponentesSessionFactoryUtil.getInstance().openSession();
		}
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			ComponentesSessionFactoryUtil.getInstance().getSessionFactory().evict(Componente.class);
			ComponentesSessionFactoryUtil.getInstance().getSessionFactory().evict(Propiedad.class);
			ComponentesSessionFactoryUtil.getInstance().getSessionFactory().evict(TipoPropiedad.class);
			ComponentesSessionFactoryUtil.getInstance().getSessionFactory().evict(ConsultaDinamica.class);
			ComponentesSessionFactoryUtil.getInstance().getSessionFactory().evict(ParametroConsulta.class);
			ComponentesSessionFactoryUtil.getInstance().getSessionFactory().evict(TipoComponente.class);
			tx.commit();
		}
		catch (final Exception e)
		{
			if (tx != null) tx.rollback();
			throw new RuntimeException(e);
		}
	}
}
