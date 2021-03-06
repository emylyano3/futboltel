package com.deportel.administracion.modelo;

// Generated 05/04/2012 23:09:35 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Modulo generated by hbm2java
 */
@Entity
@Table(name = "modulo", catalog = "administracion")
public class Modulo implements java.io.Serializable
{
	private static final long	serialVersionUID	= 1L;

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

	public Modulo()
	{
	}

	public Modulo(String DNombre, String DDescripcion, String MEstado)
	{
		this.DNombre = DNombre;
		this.DDescripcion = DDescripcion;
		this.MEstado = MEstado;
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
