package com.deportel.componentes.dao.hibernate;

import com.deportel.componentes.dao.ClienteDao;
import com.deportel.componentes.modelo.Cliente;

/**
 * @author Emy
 */
public class ClienteDaoHibernate extends ComponentesGenericDaoHibernate<Cliente, Integer> implements ClienteDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public ClienteDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}
}
