package com.deportel.componentes.modelo;

// Generated 30/09/2011 18:55:03 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ConsultaDinamica generated by hbm2java
 */
@Entity
@Table(name = "consulta_dinamica", catalog = "componentes")
public class ConsultaDinamica /*extends FutboltelEntity*/ implements java.io.Serializable
{
	private static final long	serialVersionUID	= -4217595819251006373L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "c_id", unique = true, nullable = false)
	private Integer					CId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tema_c_id", nullable = false)
	private Tema					tema;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "respuesta_c_id", nullable = false)
	private Componente				componenteRespuesta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "solicitante_c_id", nullable = false)
	private Componente				componenteSolicitante;

	@Column(name = "d_consulta", nullable = false, length = 16777215)
	private String					DConsulta;

	@Column(name = "d_nombre", nullable = false, length = 45)
	private String					DNombre;

	@Column(name = "d_descripcion", length = 200)
	private String					DDescripcion;

	@Column(name = "n_row_limit")
	private Integer					NRowLimit;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "consultaDinamica")
	private Set<ParametroConsulta>	parametrosConsulta	= new HashSet<ParametroConsulta>(0);

	public ConsultaDinamica()
	{
	}

	public ConsultaDinamica(Tema tema, Componente componenteRespuesta, Componente componenteSolicitante, String DConsulta, String DNombre)
	{
		this.setTema(tema);
		this.componenteRespuesta = componenteRespuesta;
		this.componenteSolicitante = componenteSolicitante;
		this.DConsulta = DConsulta;
		this.setDNombre(DNombre);
	}

	public ConsultaDinamica(Tema tema, Componente componenteRespuesta, Componente componenteSolicitante, String DConsulta, String DNombre, String DDescripcion, Set<ParametroConsulta> parametrosConsulta)
	{
		this.setTema(tema);
		this.componenteRespuesta = componenteRespuesta;
		this.componenteSolicitante = componenteSolicitante;
		this.DConsulta = DConsulta;
		this.setDNombre(DNombre);
		this.setDDescripcion(DDescripcion);
		this.parametrosConsulta = parametrosConsulta;
	}

	public Integer getCId ()
	{
		return this.CId;
	}

	public void setCId (Integer CId)
	{
		this.CId = CId;
	}

	public Integer getNRowLimit ()
	{
		return this.NRowLimit;
	}

	public void setNRowLimit (Integer NRowLimit)
	{
		this.NRowLimit = NRowLimit;
	}

	public Componente getComponenteRespuesta ()
	{
		return this.componenteRespuesta;
	}

	public void setComponenteRespuesta (Componente componenteRespuesta)
	{
		this.componenteRespuesta = componenteRespuesta;
	}

	public Componente getComponenteSolicitante ()
	{
		return this.componenteSolicitante;
	}

	public void setComponenteSolicitante (Componente componenteSolicitante)
	{
		this.componenteSolicitante = componenteSolicitante;
	}

	public String getConsulta ()
	{
		return this.DConsulta;
	}

	public void setConsulta (String consulta)
	{
		this.DConsulta = consulta;
	}

	public Set<ParametroConsulta> getParametrosConsulta ()
	{
		return this.parametrosConsulta;
	}

	public void setParametrosConsultas (Set<ParametroConsulta> parametrosConsulta)
	{
		this.parametrosConsulta = parametrosConsulta;
	}

	public void setTema (Tema tema)
	{
		this.tema = tema;
	}

	public Tema getTema ()
	{
		return this.tema;
	}

	public void setDNombre (String dNombre)
	{
		this.DNombre = dNombre;
	}

	public String getDNombre ()
	{
		return this.DNombre;
	}

	public void setDDescripcion (String dDescripcion)
	{
		this.DDescripcion = dDescripcion;
	}

	public String getDDescripcion ()
	{
		return this.DDescripcion;
	}

	public static final String FIELD_NAME = "DNombre";
}
