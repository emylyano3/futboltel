package com.deportel.futboltel.torneo.modelo;

// Generated 30/01/2011 15:36:14 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Usuario generated by hbm2java
 */
@Entity
@Table(name = "arbitro", catalog = "torneo")
public class Arbitro implements java.io.Serializable
{
	private static final long	serialVersionUID	= 1L;
	
	private Integer				CId;
	private String				DNombre;
	private String				DApellido;
	private String				MEstado;

	public Arbitro()
	{
	}

	public Arbitro(String DNombre, String DApellido, String MEstado)
	{
		this.DNombre = DNombre;
		this.DApellido = DApellido;
		this.MEstado = MEstado;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	public Integer getCId ()
	{
		return this.CId;
	}

	@Column(name = "d_apellido", length = 45)
	public String getDApellido ()
	{
		return this.DApellido;
	}

	@Column(name = "d_nombre", length = 45)
	public String getDNombre ()
	{
		return this.DNombre;
	}

	@Column(name = "m_estado", length = 1)
	public String getMEstado ()
	{
		return this.MEstado;
	}

	public void setCId (Integer CId)
	{
		this.CId = CId;
	}

	public void setDApellido (String DApellido)
	{
		this.DApellido = DApellido;
	}

	public void setDNombre (String DNombre)
	{
		this.DNombre = DNombre;
	}

	public void setMEstado (String MEstado)
	{
		this.MEstado = MEstado;
	}
}
