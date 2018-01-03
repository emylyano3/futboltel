package com.deportel.componentes.modelo;

// Generated 15/09/2011 22:54:04 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * FuenteContenido generated by hbm2java
 */
@Entity
@Table(name = "fuente_contenido", catalog = "componentes")
public class FuenteContenido /*extends FutboltelEntity*/ implements java.io.Serializable
{
	private static final long	serialVersionUID	= -8430663102758426513L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	private Integer				CId;

	@Column(name = "d_nombre", length = 15)
	private String				DNombre;

	@Column(name = "d_descripcion", length = 300)
	private String				DDescripcion;

	@Column(name = "d_caracter_xml", length = 1)
	private String				DCaracterXml;

	@Column(name = "c_fuente_contenido", nullable = false)
	private int					CFuenteContenido;

	public FuenteContenido()
	{
	}

	public FuenteContenido(String DNombre, String DDescripcion, String DCaracterXml)
	{
		this.DNombre = DNombre;
		this.DDescripcion = DDescripcion;
		this.DCaracterXml = DCaracterXml;
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

	public String getDCaracterXml ()
	{
		return this.DCaracterXml;
	}

	public void setDCaracterXml (String DCaracterXml)
	{
		this.DCaracterXml = DCaracterXml;
	}

	public void setCTipoContenido (int cFuenteContenido)
	{
		this.CFuenteContenido = cFuenteContenido;
	}

	public int getCFuenteContenido ()
	{
		return this.CFuenteContenido;
	}

	public static final String TAG_JAR = "J";
	public static final String TAG_RMS = "R";
	public static final String TAG_WEB = "I";

	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getCId() == null) ? 0 : this.getCId().hashCode());
		return result;
	}

	@Override
	public boolean equals (Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		FuenteContenido other = (FuenteContenido) obj;
		if (this.getCId() == null)
		{
			if (other.getCId() != null)
				return false;
		}
		else if (!this.getCId().equals(other.getCId()))
			return false;
		return true;
	}

	public static final String	FIELD_NAME		= "DNombre";
	public static final String	FIELD_XML_CHAR	= "DCaracterXml";
	public static final String	FIELD_SOURCE	= "CFuenteContenido";
}
