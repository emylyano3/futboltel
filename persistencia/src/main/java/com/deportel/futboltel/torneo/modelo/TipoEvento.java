package com.deportel.futboltel.torneo.modelo;

// Generated 30/01/2011 15:37:39 by Hibernate Tools 3.3.0.GA

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
 * TipoEvento generated by hbm2java
 */
@Entity
@Table(name = "tipo_evento", catalog = "torneo")
public class TipoEvento implements java.io.Serializable
{
	private static final long	serialVersionUID	= 1L;

	private Integer				CId;
	private String				DNombre;
	private String				DDescripcion;
	private Set<Evento>			eventos				= new HashSet<Evento>(0);

	public TipoEvento()
	{
	}

	public TipoEvento(String DNombre)
	{
		this.DNombre = DNombre;
	}

	public TipoEvento(String DNombre, String DDescripcion, Set<Evento> eventos)
	{
		this.DNombre = DNombre;
		this.DDescripcion = DDescripcion;
		this.eventos = eventos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	public Integer getCId ()
	{
		return this.CId;
	}

	@Column(name = "d_descripcion", length = 300)
	public String getDDescripcion ()
	{
		return this.DDescripcion;
	}

	@Column(name = "d_nombre", nullable = false, length = 45)
	public String getDNombre ()
	{
		return this.DNombre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoEvento")
	public Set<Evento> getEventos ()
	{
		return this.eventos;
	}

	public void setCId (Integer CId)
	{
		this.CId = CId;
	}

	public void setDDescripcion (String DDescripcion)
	{
		this.DDescripcion = DDescripcion;
	}

	public void setDNombre (String DNombre)
	{
		this.DNombre = DNombre;
	}

	public void setEventos (Set<Evento> eventos)
	{
		this.eventos = eventos;
	}

}
