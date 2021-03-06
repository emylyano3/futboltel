package com.deportel.administracion.modelo;

// Generated 05/04/2012 23:09:35 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Perfil generated by hbm2java
 */
@Entity
@Table(name = "perfil", catalog = "administracion")
public class Perfil implements java.io.Serializable
{
	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	private Integer				CId;

	@Column(name = "d_nombre", length = 45, nullable = false, unique=true)
	private String				DNombre;

	@Column(name = "d_descripcion", length = 300)
	private String				DDescripcion;

	@Column(name = "m_estado", length = 1)
	private String				MEstado;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfil")
	private Set<ModuloPerfil>	modulosPerfil	= new HashSet<ModuloPerfil>(0);

	public Perfil()
	{
	}

	public Perfil(String DNombre, String DDescripcion, String MEstado, Set<ModuloPerfil> modulosPerfil)
	{
		this.DNombre = DNombre;
		this.DDescripcion = DDescripcion;
		this.MEstado = MEstado;
		this.modulosPerfil = modulosPerfil;
	}

	public Integer getCId ()
	{
		return this.CId;
	}

	public void setCId (Integer CId)
	{
		this.CId = CId;
	}

	public String getDNombre ()
	{
		return this.DNombre;
	}

	public void setDNombre (String DNombre)
	{
		this.DNombre = DNombre;
	}

	public String getDDescripcion ()
	{
		return this.DDescripcion;
	}

	public void setDDescripcion (String DDescripcion)
	{
		this.DDescripcion = DDescripcion;
	}

	public String getMEstado ()
	{
		return this.MEstado;
	}

	public void setMEstado (String MEstado)
	{
		this.MEstado = MEstado;
	}

	public Set<ModuloPerfil> getModulosPerfil ()
	{
		return this.modulosPerfil;
	}

	public void setModulosPerfil (Set<ModuloPerfil> modulosPerfil)
	{
		this.modulosPerfil = modulosPerfil;
	}

	@Override
	public String toString()
	{
		StringBuilder st = new StringBuilder();
		st.append(this.DNombre);
		return st.toString();
	}

	public static final String FIELD_NAME = "DNombre";
	public static final String FIELD_STATE = "MEstado";
}
