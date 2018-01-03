package com.deportel.componentes.dao;

import java.io.Serializable;

import javax.management.InstanceNotFoundException;

/**
 * E, es la clase persistente para la que se implementará el DAO PK,
 * define el tipo del identificador de la clase persistente. El
 * identificador debe ser Serializable
 */
public interface ComponentesGenericDao<E, PK extends Serializable>
{
	public E create (E entity);

	public boolean exists (PK id);

	public E find (PK id) throws InstanceNotFoundException;

	public void remove (PK id) throws InstanceNotFoundException;

	public E update (E entity);
}
