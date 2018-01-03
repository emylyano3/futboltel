package com.deportel.componentes.dao.hibernate;

import com.deportel.componentes.dao.TransaccionDao;
import com.deportel.componentes.modelo.Transaccion;

/**
 * @author Emy
 */
public class TransaccionDaoHibernate extends ComponentesGenericDaoHibernate<Transaccion, Integer> implements TransaccionDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public TransaccionDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}
}
