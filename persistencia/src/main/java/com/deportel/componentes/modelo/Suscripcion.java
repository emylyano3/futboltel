package com.deportel.componentes.modelo;

// Generated 15/09/2011 22:54:04 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Suscripcion generated by hbm2java
 */
@Entity
@Table(name = "suscripcion", catalog = "componentes")
public class Suscripcion /*extends FutboltelEntity*/ implements java.io.Serializable
{
	private static final long	serialVersionUID	= -7081792121090777940L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	private Integer				id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_c_id", nullable = false)
	private Cliente				cliente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tema_c_id", nullable = false)
	private Tema				tema;

	@Column(name = "d_customizacion", length = 50, nullable = false)
	private String				customizacion;

	@Column(name = "d_email", length = 45)
	private String				email;

	@Column(name = "d_num_movil", length = 15)
	private String				celular;

	@Temporal(TemporalType.DATE)
	@Column(name = "f_fecha_suscripcion", length = 10)
	private Date				fechaSuscripcion;

	@Temporal(TemporalType.DATE)
	@Column(name = "f_fecha_baja_suscripcion", length = 10)
	private Date				fechaBajaSuscripcion;

	@Column(name = "m_estado", length = 1)
	private String				estado;

	public Suscripcion()
	{
	}

	public Suscripcion(Cliente cliente, Tema tema, String customizacion, String email, String celular)
	{
		this.cliente = cliente;
		this.tema = tema;
		this.customizacion = customizacion;
		this.email = email;
		this.celular = celular;
	}

	public Integer getId ()
	{
		return this.id;
	}

	public void setId (Integer id)
	{
		this.id = id;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente ()
	{
		return this.cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente (Cliente cliente)
	{
		this.cliente = cliente;
	}

	/**
	 * @return the tema
	 */
	public Tema getTema ()
	{
		return this.tema;
	}

	/**
	 * @param tema the tema to set
	 */
	public void setTema (Tema tema)
	{
		this.tema = tema;
	}

	/**
	 * @return the customizacion
	 */
	public String getCustomizacion ()
	{
		return this.customizacion;
	}

	/**
	 * @param customizacion the customizacion to set
	 */
	public void setCustomizacion (String customizacion)
	{
		this.customizacion = customizacion;
	}

	/**
	 * @return the celular
	 */
	public String getCelular ()
	{
		return this.celular;
	}

	/**
	 * @param celular the celular to set
	 */
	public void setCelular (String celular)
	{
		this.celular = celular;
	}

	public String getEmail ()
	{
		return this.email;
	}

	public void setEmail (String DEmail)
	{
		this.email = DEmail;
	}

	public Date getFechaSuscripcion ()
	{
		return this.fechaSuscripcion;
	}

	public void setFechaSuscripcion (Date fechaSuscripcion)
	{
		this.fechaSuscripcion = fechaSuscripcion;
	}

	public Date getFechaBajaSuscripcion ()
	{
		return this.fechaBajaSuscripcion;
	}

	public void setFechaBajaSuscripcion (Date FFechaBajaSuscripcion)
	{
		this.fechaBajaSuscripcion = FFechaBajaSuscripcion;
	}

	public String getEstado ()
	{
		return this.estado;
	}

	public void setEstado (String MEstado)
	{
		this.estado = MEstado;
	}

}