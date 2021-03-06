package com.deportel.componentes.modelo;

// Generated 15/09/2011 22:54:04 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Propiedad generated by hbm2java
 */
@Entity
@Table(name = "propiedad", catalog = "componentes")
public class Propiedad implements Serializable, Cloneable, Comparable<Propiedad>
{
	private static final long	serialVersionUID	= 3470380991328942489L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	private Integer				CId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuente_contenido_c_id", nullable = false)
	private FuenteContenido		fuenteContenido;

	@Column(name = "n_alto_pantalla", nullable = false)
	private int					NAltoPantalla;

	@Column(name = "n_ancho_pantalla", nullable = false)
	private int					NAnchoPantalla;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_propiedad_c_id", nullable = false)
	private TipoPropiedad		tipoPropiedad;

	@Column(name = "d_regular_data", length = 200)
	private String				DRegularData;

	@Column(name = "b_binary_data", nullable = true)
	private byte[]				BBinaryData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "componente_c_id")
	private Componente			componente;

	public Propiedad()
	{
	}

	public Integer getCId ()
	{
		return this.CId;
	}

	public void setCId (Integer CId)
	{
		this.CId = CId;
	}

	public byte[] getBinaryData ()
	{
		return this.BBinaryData;
	}

	public void setBinaryData (byte[] binaryData)
	{
		this.BBinaryData = binaryData;
	}

	public TipoPropiedad getTipoPropiedad ()
	{
		return this.tipoPropiedad;
	}

	public void setTipoPropiedad (TipoPropiedad tipoPropiedad)
	{
		this.tipoPropiedad = tipoPropiedad;
	}

	public FuenteContenido getFuenteContenido ()
	{
		return this.fuenteContenido;
	}

	public void setFuenteContenido (FuenteContenido fuenteContenido)
	{
		this.fuenteContenido = fuenteContenido;
	}

	public int getNAltoPantalla ()
	{
		return this.NAltoPantalla;
	}

	public void setNAltoPantalla (int NAltoPantalla)
	{
		this.NAltoPantalla = NAltoPantalla;
	}

	public int getNAnchoPantalla ()
	{
		return this.NAnchoPantalla;
	}

	public void setNAnchoPantalla (int NAnchoPantalla)
	{
		this.NAnchoPantalla = NAnchoPantalla;
	}

	public String getDRegularData ()
	{
		return this.DRegularData;
	}

	public void setDRegularData (String DRegularData)
	{
		this.DRegularData = DRegularData;
	}

	public void setComponente (Componente componente)
	{
		this.componente = componente;
	}

	public Componente getComponente ()
	{
		return this.componente;
	}

	/**
	 * Retorna una copia de la propiedad en una nueva instancia. Se
	 * copian todos los atributos que NO definen a la propiedad como
	 * unica o que tengan referencias a otra tablas, es decir que se
	 * copian todos aquellos que son utiles para el usuario pero NO
	 * aquellos atributos utilizados por el sistema para
	 * identificacion o adminstracion de la entidad.
	 */
	@Override
	public Propiedad clone () throws CloneNotSupportedException
	{
		Propiedad clon = new Propiedad();
		clon.setBinaryData(this.getBinaryData());
		clon.setDRegularData(this.getDRegularData());
		clon.setFuenteContenido(this.getFuenteContenido());
		clon.setNAltoPantalla(this.getNAltoPantalla());
		clon.setNAnchoPantalla(this.getNAnchoPantalla());
		clon.setTipoPropiedad(this.getTipoPropiedad());
		//		No seteo ni el ID ni el componente al que hace referencia.
		//		clon.setComponente(componente);
		//		clon.setCId(CId);
		return clon;
	}



	@Override
	public int compareTo(Propiedad o)
	{
		if (o == null)
		{
			return -1;
		}
		return getTipoPropiedad().getDNombre().compareTo(o.getTipoPropiedad().getDNombre());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getBinaryData());
		result = prime * result + ((getDRegularData() == null) ? 0 : getDRegularData().hashCode());
		result = prime * result + getNAltoPantalla();
		result = prime * result + getNAnchoPantalla();
		result = prime * result + ((getComponente() == null) ? 0 : getComponente().hashCode());
		result = prime * result + ((getFuenteContenido() == null) ? 0 : getFuenteContenido().hashCode());
		result = prime * result + ((getTipoPropiedad() == null) ? 0 : getTipoPropiedad().hashCode());
		return result;
	}

	/**
	 * Si ambos componentes poseen ID realiza la comparacion a trav�s de ese atributo porque es un
	 * identificador unico. En caso contrario se realiza la comparacion utilizando todos los atributos del
	 * componente
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Propiedad))
		{
			return false;
		}
		Propiedad other = (Propiedad) obj;

		if (this.getCId() == null || other.getCId() == null)
		{
			if (!Arrays.equals(getBinaryData(), other.getBinaryData()))
			{
				return false;
			}
			if (getDRegularData() == null)
			{
				if (other.getDRegularData() != null)
				{
					return false;
				}
			}
			else if (!getDRegularData().equals(other.getDRegularData()))
			{
				return false;
			}
			if (getNAltoPantalla() != other.getNAltoPantalla())
			{
				return false;
			}
			if (getNAnchoPantalla() != other.getNAnchoPantalla())
			{
				return false;
			}
			if (this.getComponente() == null)
			{
				if (other.getComponente() != null)
				{
					return false;
				}
			}
			else if (!this.getComponente().equals(other.getComponente()))
			{
				return false;
			}
			if (getFuenteContenido() == null)
			{
				if (other.getFuenteContenido() != null)
				{
					return false;
				}
			}
			else if (!getFuenteContenido().equals(other.getFuenteContenido()))
			{
				return false;
			}
			if (getTipoPropiedad() == null)
			{
				if (other.getTipoPropiedad() != null)
				{
					return false;
				}
			}
			else if (!getTipoPropiedad().equals(other.getTipoPropiedad()))
			{
				return false;
			}
			return true;
		}
		else
		{
			return getCId().equals(other.getCId());
		}
	}
	
	public static final String FIELD_REGULAR_DATA = "DRegularData";
}
