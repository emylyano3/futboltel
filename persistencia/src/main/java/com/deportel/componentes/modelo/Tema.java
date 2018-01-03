package com.deportel.componentes.modelo;

// Generated 15/09/2011 22:54:04 by Hibernate Tools 3.3.0.GA

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
 * Tema generated by hbm2java
 */
@Entity
@Table(name = "tema", catalog = "componentes")
public class Tema /*extends FutboltelEntity*/ implements java.io.Serializable
{
	private static final long	serialVersionUID	= -6828629177906806451L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	private Integer				CId;

	@Column(name = "d_nombre", length = 45)
	private String				DNombre;

	@Column(name = "d_descripcion", length = 300)
	private String				DDescripcion;

	@Column(name = "m_estado", length = 1)
	private String				MEstado;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema")
	private Set<Componente>		componentes			= new HashSet<Componente>(0);

	public Tema()
	{
	}

	public Tema(String DNombre, String DDescripcion, String MEstado, Set<Componente> componentes)
	{
		this.DNombre = DNombre;
		this.DDescripcion = DDescripcion;
		this.MEstado = MEstado;
		this.componentes = componentes;
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

	public Set<Componente> getComponentes ()
	{
		return this.componentes;
	}

	public void setComponentes (Set<Componente> componentes)
	{
		this.componentes = componentes;
	}

	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.CId == null) ? 0 : this.CId.hashCode());
		return result;
	}

	@Override
	public boolean equals (Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Tema other = (Tema) obj;
		if (getCId() == null)
		{
			if (other.getCId() != null)
				return false;
		}
		else if (!getCId().equals(other.getCId()))
			return false;
		return true;
	}

	@Override
	public String toString ()
	{
		return this.DNombre;
	}

	public static final String	FIELD_NAME	= "DNombre";
	public static final String	FIELD_STATE	= "MEstado";
}
