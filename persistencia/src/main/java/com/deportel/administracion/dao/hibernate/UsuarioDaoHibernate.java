package com.deportel.administracion.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.UsuarioDao;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.common.Constants;
import com.deportel.common.utils.Encoder;

public class UsuarioDaoHibernate extends AdministracionGenericDaoHibernate<Usuario, Integer> implements UsuarioDao
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized UsuarioDaoHibernate getInstance ()
	{
		if (instance == null)
		{
			instance = new UsuarioDaoHibernate();
		}
		return instance;
	}

	private UsuarioDaoHibernate()
	{

	}

	private static UsuarioDaoHibernate	instance;

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger			log	= Logger.getLogger(UsuarioDaoHibernate.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca si existe el nombre de usuario pasado por parametro
	 * 
	 * @param usuario
	 * @return Los datos del usuario
	 * @author Emy
	 * @since 01/02/2011
	 */
	@Override
	public Usuario findByName (String userName)
	{
		log.debug("Inicia findByName");
		userName = Encoder.encode(userName);
		Usuario user = (Usuario) this.getSession()
		.createCriteria(Usuario.class)
		.add(Restrictions.like(Usuario.FIELD_NAME, userName))
		.add(Restrictions.eq(Usuario.FIELD_STATE, Constants.HABILITADO))
		.uniqueResult();
		return user;
	}

	/**
	 * Busca si existe el nombre de usuario por alias pasado por parametro
	 * 
	 * @param alias
	 * @return Los datos del usuario
	 * @author Emy
	 * @since 01/02/2011
	 */
	@Override
	public Usuario findByAlias (String alias)
	{
		log.debug("Inicia findByAlias");
		alias = Encoder.encode(alias);
		Usuario user = (Usuario) this.getSession()
		.createCriteria(Usuario.class)
		.add(Restrictions.like(Usuario.FIELD_ALIAS, alias))
		.uniqueResult();
		return user;
	}

	/**
	 * Busca un usuario segun su contraseña.
	 * 
	 * @param password
	 *            La contraseña de usuario
	 * @return Los datos del usuario
	 * @since 01/02/2011
	 */
	public Usuario findByPassword (String password)
	{
		log.debug("Inicia findByAlias");
		password = Encoder.encode(password);
		Usuario user = (Usuario) this.getSession()
		.createCriteria(Usuario.class)
		.add(Restrictions.eq(Usuario.FIELD_PASSWORD, password))
		.uniqueResult();
		return user;
	}

	/**
	 * Obtiene todos los usuarios dados de alta
	 * 
	 * @return {@link Usuario} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> findAll ()
	{
		log.debug("Inicia findAll");
		List<Usuario> resultados = this.getSession()
		.createCriteria(Usuario.class)
		.list();
		return resultados;
	}

	/**
	 * Obtiene todos los usuarios dados de alta habilitados
	 * 
	 * @return {@link Usuario} List
	 * @throws EntityNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> findAllEnabled ()
	{
		log.debug("Inicia findAllEnabled");
		List<Usuario> resultados = this.getSession()
		.createCriteria(Usuario.class)
		.add(Restrictions.eq(Usuario.FIELD_STATE, Constants.HABILITADO))
		.list();
		return resultados;
	}

	@Override
	protected void onPreCreated (Usuario usuario)
	{
//		encodeSecretFields(usuario);
	}

	@Override
	protected void onCreated (Usuario usuario)
	{
	}

	@Override
	protected void onUpdate (Usuario usuario)
	{
//		encodeSecretFields(usuario);
	}

	@Override
	protected void onPreRemove (Usuario usuario)
	{
	}

//	private void encodeSecretFields (Usuario usuario)
//	{
//		usuario.setDAlias(Encoder.encode(usuario.getDAlias()));
//		usuario.setDNombre(Encoder.encode(usuario.getDNombre()));
//		usuario.setDApellido(Encoder.encode(usuario.getDApellido()));
//		usuario.setDPassword(Encoder.encode(usuario.getDPassword()));
//		usuario.setDEmail(Encoder.encode(usuario.getDEmail()));
//	}
}