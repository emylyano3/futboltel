package com.deportel.componentes.dao.hibernate;

import com.deportel.componentes.dao.SuscripcionDao;
import com.deportel.componentes.modelo.Suscripcion;

/**
 * @author Emy
 */
public class SuscripcionDaoHibernate extends ComponentesGenericDaoHibernate<Suscripcion, Integer> implements SuscripcionDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public SuscripcionDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}
}
