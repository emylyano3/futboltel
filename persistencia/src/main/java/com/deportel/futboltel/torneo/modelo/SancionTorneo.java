package com.deportel.futboltel.torneo.modelo;

// Generated 30/01/2011 15:37:39 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SancionTorneo generated by hbm2java
 */
@Entity
@Table(name = "sancion_torneo", catalog = "torneo")
public class SancionTorneo implements java.io.Serializable
{
	private static final long	serialVersionUID	= 1L;

	private Integer				CId;
	private Sancion				sancion;
	private Torneo				torneo;
	private String				MEstado;

	public SancionTorneo()
	{
	}

	public SancionTorneo(Sancion sancion, Torneo torneo)
	{
		this.sancion = sancion;
		this.torneo = torneo;
	}

	public SancionTorneo(Sancion sancion, Torneo torneo, String MEstado)
	{
		this.sancion = sancion;
		this.torneo = torneo;
		this.MEstado = MEstado;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	public Integer getCId ()
	{
		return this.CId;
	}

	@Column(name = "m_estado", length = 1)
	public String getMEstado ()
	{
		return this.MEstado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sancion_c_id", nullable = false)
	public Sancion getSancion ()
	{
		return this.sancion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "torneo_c_id", nullable = false)
	public Torneo getTorneo ()
	{
		return this.torneo;
	}

	public void setCId (Integer CId)
	{
		this.CId = CId;
	}

	public void setMEstado (String MEstado)
	{
		this.MEstado = MEstado;
	}

	public void setSancion (Sancion sancion)
	{
		this.sancion = sancion;
	}

	public void setTorneo (Torneo torneo)
	{
		this.torneo = torneo;
	}

}
