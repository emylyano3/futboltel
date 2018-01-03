package com.deportel.editor.contenido.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;

import com.deportel.common.Constants;
import com.deportel.common.Text;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.common.utils.Converter;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.ConsultaDinamicaController;
import com.deportel.componentes.controller.TipoComponenteController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.ParametroConsulta;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.modelo.TipoComponente;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.editor.common.core.Editor.Commands;
import com.deportel.editor.common.core.ServerNotifier;
import com.deportel.editor.contenido.exception.ContenidoEditorException;
import com.deportel.editor.contenido.main.ContenidoEditor;
import com.deportel.editor.contenido.model.ProjectTexts;
import com.deportel.editor.contenido.model.ProjectValues;
import com.deportel.editor.contenido.view.CEWindow;
import com.deportel.editor.contenido.view.R;
import com.deportel.guiBuilder.gui.component.AutoBindedComboBox;
import com.deportel.guiBuilder.gui.component.ComponentObserver;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.persistencia.utils.QueryParam;

/**
 * @author Emy
 */
public class CEGuiController implements CallBackListener, ComponentObserver
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger					log								= Logger.getLogger(CEGuiController.class);
	private final GuiManager					manager;
	private Map<String, QueryParam>				queryInParams					= new HashMap<String, QueryParam>();
	private Map<String, QueryParam>				queryOutParams					= new HashMap<String, QueryParam>();
	private ConsultaDinamica					dynaQuery;
	private Componente							componenteRespuesta;
	private Componente							componenteSolicitante;
	private final ServerNotifier				serverNotifier					= new ServerNotifier();
	private final Text							texts							= ContenidoEditor.getTexts();
	private Integer								selectedQuery;
	private Set<ParametroConsulta> 				parametrosConsulta;
	private boolean								isNew;
	private boolean								changesToSave					= false;

	private int									currentAction;

	private final ConsultaDinamicaController	consultaDinamicaController		= ConsultaDinamicaController.getInstance(true);
	private final ComponenteController			componenteController			= ComponenteController.getInstance(true);
	private final TipoComponenteController		tipoComponenteController		= TipoComponenteController.getInstance(true);
	private final QTController					qtController;
	private final CEWindow						window;
	private final Properties					properties						= ContenidoEditor.getProperties();

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static CEGuiController	instance;

	private CEGuiController(GuiManager manager)
	{
		this.manager = manager;
		this.qtController = QTController.getInstance(this.manager, this);
		this.window = CEWindow.getInstance(null);
	}

	public static synchronized CEGuiController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new CEGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 *
	 */
	public void updateInterface (String claimer)
	{
		log.debug("Actualizando la interfaz por pedido del componente: " + claimer);
		if (R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE.equals(claimer))
		{
			requestComponentTypeSelected(claimer);
			setChangesToSave(true);
		}
		if (R.BINDED_COMBOS.TIPO_COMPONENTE_RESP.equals(claimer))
		{
			responseComponentTypeSelected(claimer);
			setChangesToSave(true);
		}
		else if (R.BINDED_COMBOS.COMPONENTE_SOLICITANTE.equals(claimer))
		{
			setChangesToSave(true);
		}
		else if (R.BINDED_COMBOS.COMPONENTE_RESP.equals(claimer))
		{
			setChangesToSave(true);
		}
		else if (R.BINDED_COMBOS.TEMA_DE_APLICACION.equals(claimer))
		{
			themeSelectionChanged();
			boolean enable = this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedIndex() > 0;
			enableComponents(enable);
			setChangesToSave(true);
		}
		else if (R.CHECK_BOXES.LIMIT_ROWS.equals(claimer))
		{
			this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).setEnabled
			(
					isLimitRows()
			);
			setChangesToSave(true);
		}
		else if (R.BINDED_COMBOS.TIPO_COMPONENTE_RESP.equals(claimer))
		{
			updateResponseComponentSelectionPanel(claimer);
			setChangesToSave(true);
		}
		else if (R.TEXT_AREAS.SQL_CONSULTA.equals(claimer))
		{
			boolean enabled = !Utils.isNullOrEmpty(this.manager.getTextArea(claimer).getText());
			this.manager.getButton(R.BUTTONS.TEST_QUERY).setEnabled(enabled);
		}
	}

	/**
	 * Se encarga de invocar al test del query tester
	 */
	public void openQueryTesterWindow ()
	{
		try
		{
			updateParams();
			this.qtController.openWindow();
		}
		catch (Exception e)
		{
			Popup.getInstance(this.texts.get(ProjectTexts.ERROR_GENERAL), Popup.POPUP_ERROR).showGui();
			e.printStackTrace();
		}
	}

	/**
	 * Se encarga de invocar al test del query tester
	 */
	public void openQuerySelectionWindow (String source)
	{
		try
		{
			QSController.getInstance(this.manager).openWindow(source);
		}
		catch (ContenidoEditorException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
	}

	/**
	 *
	 */
	private void updateParams ()
	{
		String query = this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText();
		parseInParams(query);
		parseOutParams(query);
		setChangesToSave(true);
	}

	/**
	 *
	 */
	public void enableQueryCreation ()
	{
		log.debug("Se persiste la configuracion de la consulta");
		if (!isChangesToSave())
		{
			initializeQueryCreation();
		}
		else
		{
			this.currentAction = Commands.CREATE;
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_UNSAVED_CHANGES),
					Popup.POPUP_CONFIRM,
					CEWindow.getInstance().getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
		}
	}

	/**
	 *
	 */
	public void enableQueryEdition (int queryId)
	{
		log.debug("Se inicializa la pantalla de edicion de consultas");
		this.selectedQuery = queryId;
		if (!isChangesToSave())
		{
			initializeQueryEdition();
		}
		else
		{
			this.currentAction = Commands.EDIT;
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_UNSAVED_CHANGES),
					Popup.POPUP_CONFIRM,
					CEWindow.getInstance().getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
		}
	}

	private void validateQuery () throws ContenidoEditorException
	{
		validateQueryParams();
		String text = this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText();
		text = text.trim();
		if (Utils.isNullOrEmpty(text)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_NULL_QUERY);
		try
		{
			this.qtController.testQuery();
		}
		catch (Exception e)
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_INVALID_QUERY);
		}
	}

	private void validateQueryParams () throws ContenidoEditorException
	{
		for (QueryParam param : this.queryInParams.values())
		{
			if (Utils.isNullOrEmpty(param.getName())) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_NULL_QUERY_PARAM);
			if (!Utils.validateStringSize(param.getName(), 45)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_TOO_LONG_QUERY_PARAM);
		}
		for (QueryParam param : this.queryOutParams.values())
		{
			if (Utils.isNullOrEmpty(param.getName())) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_NULL_QUERY_PARAM);
			if (!Utils.validateStringSize(param.getName(), 45)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_TOO_LONG_QUERY_PARAM);
		}
	}

	/**
	 *
	 */
	public void saveQueryConfiguration (String source)
	{
		try
		{
			updateParams();
			validateFields();
			validateQuery();
			if (this.isNew || R.BUTTONS.SAVE_AS_NEW.equals(source))
			{
				saveNewQueryConfiguration();
			}
			else if (isChangesToSave())
			{
				updateQueryConfiguration();
			}
			updateNewState(false);
			setChangesToSave(false);
			updateServerCache(this.window.getContainer().getServerURL());
		}
		catch (ContenidoEditorException e)
		{
			Popup.getInstance
			(
					e.getMessage(),
					Popup.POPUP_ERROR,
					CEWindow.getInstance().getWorkbench().getWorkPanel()
			)
			.showGui();
		}
		catch (Exception e)
		{
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.ERROR_GENERAL),
					Popup.POPUP_ERROR,
					CEWindow.getInstance().getWorkbench().getWorkPanel()
			)
			.showGui();
			e.printStackTrace();
		}
	}

	private void updateServerCache (String url)
	{
		this.serverNotifier.updateServerCache(url);
	}

	/**
	 *
	 */
	public List<TipoComponente> getTiposComponenteSolicitante ()
	{
		List<TipoComponente> tiposComponente = new ArrayList<TipoComponente>();
		tiposComponente.add(this.tipoComponenteController.findByNombre(this.properties.getProperty(ProjectValues.COMP_TYPE_IMAGE)));
		tiposComponente.add(this.tipoComponenteController.findByNombre(this.properties.getProperty(ProjectValues.COMP_TYPE_SCREEN)));
		tiposComponente.add(this.tipoComponenteController.findByNombre(this.properties.getProperty(ProjectValues.COMP_TYPE_BUTTON)));
		return tiposComponente;
	}

	/**
	 *
	 */
	public List<TipoComponente> getTiposComponenteRespuesta ()
	{
		List<TipoComponente> tiposComponente = new ArrayList<TipoComponente>();
		tiposComponente.add(this.tipoComponenteController.findByNombre(this.properties.getProperty(ProjectValues.COMP_TYPE_TABLE)));
		tiposComponente.add(this.tipoComponenteController.findByNombre(this.properties.getProperty(ProjectValues.COMP_TYPE_LABEL)));
		tiposComponente.add(this.tipoComponenteController.findByNombre(this.properties.getProperty(ProjectValues.COMP_TYPE_BUTTON)));
		return tiposComponente;
	}

	// *********************************************************************************************************************
	// Metodos Privados
	// *********************************************************************************************************************

	/**
	 *
	 * @param claimer
	 */
	private void requestComponentTypeSelected (String claimer)
	{
		if (R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE.equals(claimer))
		{
			boolean enable = this.manager.getBindedCombo(claimer).hasValidSelection();
			if (enable)
			{
				List<Componente> componentes = this.componenteController.findByIdTemaAndIdTipoComponente
				(
						(Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem(),
						(TipoComponente) this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE).getSelectedItem()
				);
				this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).bindWithList
				(
						this.componenteController.getComponentsPropertyFiltered
						(
								componentes,
								TipoPropiedad.TAG_NAME
						),
						Propiedad.FIELD_REGULAR_DATA
				);
			}
			else
			{
				this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).setSelectedIndex(AutoBindedComboBox.OUT_OF_RANGE);
			}
			this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).setEnabled(enable);
			this.manager.getLabel(R.LABELS.COMPONENTE_SOLICITANTE).setEnabled(enable);
		}
	}

	/**
	 *
	 * @param claimer
	 */
	private void responseComponentTypeSelected (String claimer)
	{
		if (R.BINDED_COMBOS.TIPO_COMPONENTE_RESP.equals(claimer))
		{
			boolean enable = this.manager.getBindedCombo(claimer).hasValidSelection();
			if (enable)
			{
				List<Componente> componentes = this.componenteController.findByIdTemaAndIdTipoComponente
				(
						(Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem(),
						(TipoComponente) this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).getSelectedItem(),
						true
				);
				this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).bindWithList
				(
						this.componenteController.getComponentsPropertyFiltered
						(
								componentes,
								TipoPropiedad.TAG_NAME
						),
						Propiedad.FIELD_REGULAR_DATA
				);
			}
			else
			{
				this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).setSelectedIndex(AutoBindedComboBox.OUT_OF_RANGE);
			}
			this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).setEnabled(enable);
			this.manager.getLabel(R.LABELS.COMPONENTE_RESP).setEnabled(enable);
		}
	}

	/**
	 * Actualiza en la base la consiguracion de la consulta modificada
	 *
	 * @throws ContenidoEditorException
	 * @throws Exception
	 */
	private void updateQueryConfiguration () throws ContenidoEditorException, Exception
	{
		log.debug("Se actualiza la configuracion de la Consulta Dinamica");
		this.dynaQuery.setConsulta(this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText());
		this.dynaQuery.setDDescripcion(this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).getText());
		this.dynaQuery.setDNombre(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).getText());
		this.dynaQuery.setTema((Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem());
		this.dynaQuery.setNRowLimit(getRowLimit());
		this.parametrosConsulta = createQueryParams(this.dynaQuery);
		this.componenteRespuesta = getResponseComponent();
		this.componenteSolicitante = getComponenteSolicitante();
		this.dynaQuery.setParametrosConsultas(this.parametrosConsulta);
		this.dynaQuery.setComponenteRespuesta(this.componenteRespuesta);
		this.dynaQuery.setComponenteSolicitante(this.componenteSolicitante);
		this.dynaQuery = this.consultaDinamicaController.updateSafe(this.dynaQuery);
	}

	/**
	 * Guarda en la base la configuracion de la nueva consulta
	 *
	 * @throws ContenidoEditorException
	 * @throws Exception
	 */
	private void saveNewQueryConfiguration () throws ContenidoEditorException, Exception
	{
		log.debug("Se persiste la configuracion de la consulta");
		validateQueryCreation();
		this.dynaQuery = new ConsultaDinamica();
		this.dynaQuery.setConsulta(this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText());
		this.dynaQuery.setDDescripcion(this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).getText());
		this.dynaQuery.setDNombre(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).getText());
		this.dynaQuery.setTema((Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem());
		this.dynaQuery.setNRowLimit(getRowLimit());
		this.parametrosConsulta = createQueryParams(this.dynaQuery);
		this.componenteRespuesta = getResponseComponent();
		this.componenteSolicitante = getComponenteSolicitante();
		this.dynaQuery.setParametrosConsultas(this.parametrosConsulta);
		this.dynaQuery.setComponenteRespuesta(this.componenteRespuesta);
		this.dynaQuery.setComponenteSolicitante(this.componenteSolicitante);
		this.dynaQuery = this.consultaDinamicaController.createSafe(this.dynaQuery);
	}

	private int getRowLimit ()
	{
		if (isLimitRows())
		{
			return Converter.stringToInt(this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).getText());
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Crea una entidad {@link ParametroConsulta} por cada entrada que haya en {@link #queryInParams} y
	 * {@link #queryOutParams}
	 *
	 * @param dynaQuery
	 *            {@link ConsultaDinamica}
	 */
	private Set<ParametroConsulta> createQueryParams (ConsultaDinamica dynaQuery)
	{
		log.debug("Se crean los parámetros de la consulta");
		Set<ParametroConsulta> queryParams = new HashSet<ParametroConsulta>();
		for (QueryParam param : this.queryInParams.values())
		{
			ParametroConsulta dynaQueryParam = new ParametroConsulta();
			dynaQueryParam.setConsultaDinamica(dynaQuery);
			dynaQueryParam.setMTipoSalida(Constants.DESHABILITADO);
			dynaQueryParam.setNombre(param.getName());
			dynaQueryParam.setTipoDato(param.getType());
			dynaQueryParam.setIndice(param.getIndex());
			queryParams.add(dynaQueryParam);
		}
		for (QueryParam param : this.queryOutParams.values())
		{
			ParametroConsulta dynaQueryParam = new ParametroConsulta();
			dynaQueryParam.setConsultaDinamica(dynaQuery);
			dynaQueryParam.setMTipoSalida(Constants.HABILITADO);
			dynaQueryParam.setNombre(param.getName());
			dynaQueryParam.setTipoDato(param.getType());
			dynaQueryParam.setIndice(param.getIndex());
			queryParams.add(dynaQueryParam);
		}
		return queryParams;
	}

	/**
	 * Devuelve el componente solicitante seleccionado
	 *
	 * @return Una nueva instancia de {@link Componente}
	 */
	private Componente getComponenteSolicitante ()
	{
		return ((Propiedad) this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).getSelectedItem()).getComponente();
	}

	/**
	 * Devuelve el componente de respuesta seleccionado
	 *
	 * @return Una nueva instancia de {@link Componente}
	 */
	private Componente getResponseComponent ()
	{
		return ((Propiedad) this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).getSelectedItem()).getComponente();
	}

	/**
	 * Toma la consulta y parsea los parametros de salida de la misma. Los parametros encontrados son
	 * insertados en {@link #queryOutParams}.
	 *
	 * @param query
	 *            La consulta en lenguaje HQL tomada desde el panel del editor
	 */
	private void parseOutParams (String query)
	{
		int start = query.indexOf("select") + "select".length();
		int end = query.indexOf("from");
		String paramsString;
		String[] params;
		QueryParam param;
		this.queryOutParams.clear();
		int cursor;
		int paramIndex = 0;
		if (start <= end)
		{
			paramsString = query.substring(start, end);
			params = paramsString.split(",");
			for (int i = 0; i < params.length; i++)
			{
				if (!Utils.isNullOrEmpty(params[i]))
				{
					params[i].trim();
					if ((cursor = params[i].indexOf(" as ")) != -1)
					{
						String paramName = "";
						cursor += " as ".length();
						while (cursor < params[i].length() && validParametterChar(params[i].charAt(cursor)))
						{
							paramName += params[i].charAt(cursor++);
						}
						param = new QueryParam();
						param.setIndex(paramIndex++);
						param.setType("Texto");
						param.setName(paramName.trim());
						if (!this.queryOutParams.containsKey(param.getName()))
						{
							this.queryOutParams.put(param.getName(), param);
						}
					}
					else
					{
						param = new QueryParam();
						param.setIndex(paramIndex++);
						param.setType("Texto");
						param.setName(params[i].trim());
						if (!this.queryOutParams.containsKey(param.getName()))
						{
							this.queryOutParams.put(param.getName(), param);
						}
					}
				}
			}
		}
	}

	/**
	 * Toma la query existente en el componentes de ingreso buscando los parametros de entrada de la misma.
	 * Los parametros encontrados son insertados en {@link #queryInParams}.
	 *
	 * @param query
	 *            La consulta en lenguaje HQL tomada desde el panel del editor
	 */
	private void parseInParams (String query)
	{
		int start = 0;
		String paramName;
		QueryParam param;
		this.queryInParams.clear();
		int paramIndex = 0;
		while ((start = query.indexOf(":", start)) != -1)
		{
			paramName = "";
			while (start + 1 < query.length() && validParametterChar(query.charAt(start + 1)))
			{
				paramName += query.charAt(++start);
			}
			if (!this.queryInParams.containsKey(paramName))
			{
				param = new QueryParam();
				param.setName(paramName);
				param.setType("Texto");
				param.setIndex(paramIndex++);
				this.queryInParams.put(paramName, param);
			}
		}
	}

	/**
	 * Valida si el caracter utilizado para nombre el parametro de la query es valido.
	 * Se aceptan solo letras y numeros
	 * @param c El caracter a analizar
	 * @return
	 */
	private boolean validParametterChar (Character c)
	{
		if (Character.isDigit(c) || Character.isLetter(c))
		{
			return true;
		}
		return false;
	}

	/**
	 * Valida que se cumplan las condiciones para la creacion de la nueva consulta.
	 *
	 * @throws ContenidoEditorException
	 *             Si alguna de las condiciones no se cumple.
	 */
	private void validateQueryCreation () throws ContenidoEditorException
	{
		String queryName;
		queryName = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).getText();
		if (this.consultaDinamicaController.findByName(queryName) != null)
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_QUERY_NAME_REPEATED);
		}
	}

	/**
	 *
	 * @throws ContenidoEditorException
	 */
	private void validateFields () throws ContenidoEditorException
	{
		String desc = this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).getText();
		String name = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).getText();
		String script = this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText();
		if (Utils.isNullOrEmpty(script)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_NULL_QUERY);
		if (Utils.isNullOrEmpty(name)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_NULL_NAME);
		if (!Utils.validateAlphaNumeric(desc, 200)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_INVALID_DESC);
		if (!Utils.validateAlphaNumeric(name, 45)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_INVALID_NAME);
		if (!this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).hasValidSelection())
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_RESPONSE_COMPONENT_TYPE_NULL);
		}
		if (!this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).hasValidSelection())
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_REQUEST_COMPONENT_NULL);
		}
		if (!this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).hasValidSelection())
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_RESPONSE_COMPONENT_NULL);
		}
		validateRowLimit();
	}

	private void validateRowLimit ()
	{
		if (isLimitRows())
		{
			String rowLimit = this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).getText();
			if (Utils.isNullOrEmpty(rowLimit)) throw new ContenidoEditorException(ProjectTexts.ERROR_VAL_NULL_ROW_LIMIT);
		}
	}

	private void clearComponents ()
	{
		this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).setSelectedIndex(0);
		this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).setText("");
		this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).setText("");
		this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).setText("");
		this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS).setSelected(false);
	}

	private void themeSelectionChanged ()
	{
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).setSelectedIndex(0);
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE).setSelectedIndex(0);
		this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS).setSelected(false);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).setText("");
		this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).setText("");
		this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).setText("");
	}

	/**
	 * Habilita los componentes dependiendo de quien hace el request.
	 *
	 * @param claimer
	 *            El nombre del componente que realiza el request
	 */
	private void enableComponents (boolean enable)
	{
		this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS).setEnabled(enable);
		this.manager.getLabel(R.LABELS.TIPO_COMPONENTE_RESP).setEnabled(enable);
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).setEnabled(enable);
		this.manager.getLabel(R.LABELS.TIPO_COMPONENTE_SOLICITANTE).setEnabled(enable);
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE).setEnabled(enable);
		this.manager.getLabel(R.LABELS.NOMBRE_CONSULTA).setEnabled(enable);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).setEnabled(enable);
		this.manager.getLabel(R.LABELS.SQL_CONSULTA).setEnabled(enable);
		this.manager.getLabel(R.LABELS.DESCRIPCION_CONSULTA).setEnabled(enable);
		this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).setEnabled(enable);
		this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).setEnabled(enable);
		this.manager.getButton(R.BUTTONS.SAVE_AS_NEW).setEnabled(enable);
		this.manager.getButton(R.BUTTONS.TEST_QUERY).setEnabled(enable);
		this.manager.getButton(R.BUTTONS.SAVE).setEnabled(enable);
	}

	/**
	 *
	 * @param claimer
	 */
	private void updateResponseComponentSelectionPanel (String claimer)
	{
		boolean enable = this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).hasValidSelection();
		if (enable)
		{
			this.componenteController.findByIdTemaAndIdTipoComponente
			(
					(Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem(),
					(TipoComponente) this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).getSelectedItem(),
					true
			);
		}
	}

	/**
	 * Inicializa el editor para que pueda crearse una nueva configuracion de consulta. Cambia los estados de
	 * save y reinicia el estado de algunos componentes de la interfaz
	 */
	private void initializeQueryCreation ()
	{
		updateNewState(true);
		setChangesToSave(false);
		clearComponents();
		this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).setSelectedIndex(0);
		this.manager.getPanel(R.PANELS.CE_GLOBAL).setVisible(true);
		CEWindow.getInstance().getWorkbench().updateUI();
	}

	/**
	 * Inicializa el editor con los datos de la consulta que corresponde con el id recibido
	 *
	 * @param queryId
	 *            El id de la query que se desea cargar
	 */
	private void initializeQueryEdition ()
	{
		if (this.selectedQuery != null)
		{
			try
			{
				ConsultaDinamica query = this.consultaDinamicaController.findById(this.selectedQuery);
				clearComponents();
				setQuery(query);
				this.manager.getPanel(R.PANELS.CE_GLOBAL).setVisible(true);
				CEWindow.getInstance().getWorkbench().updateUI();
				this.selectedQuery = null;
				updateNewState(false);
				setChangesToSave(false);
			}
			catch (InstanceNotFoundException e)
			{
				Popup.getInstance
				(
						this.texts.get(ProjectTexts.ERROR_QUERY_LOAD) + this.selectedQuery,
						Popup.POPUP_ERROR
				)
				.showGui();
			}
		}
	}

	/**
	 * Vuelca los datos de la query recibida en los componentes de la interfaz.
	 *
	 * @param query
	 *            La query que se va a cargar
	 */
	private void setQuery (ConsultaDinamica query)
	{
		this.dynaQuery = query;
		setTheme(query);
		setSimpleFields(query);
		setRequestComponent(query);
		setResponseComponent(query);
		setRowLimit(query);
		setQueryParams(query);
	}

	private void setTheme (ConsultaDinamica query)
	{
		setComboSelection(this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION), query.getTema(), "CId");
	}

	private void setRequestComponent(ConsultaDinamica query)
	{
		Componente solicitante = query.getComponenteSolicitante();
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE).setSelectedItem(solicitante.getTipoComponente());
		List<Componente> componentes = this.componenteController.findByIdTemaAndIdTipoComponente
		(
				(Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem(),
				solicitante.getTipoComponente()
		);
		List<Propiedad> nombresComponentes = this.componenteController.getComponentsPropertyFiltered
		(
				componentes,
				TipoPropiedad.TAG_NAME
		);
		this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).bindWithList
		(
				nombresComponentes,
				Propiedad.FIELD_REGULAR_DATA
		);
		Propiedad nombreComponenteSeleccionado = solicitante.getPropiedadByTagXml(TipoPropiedad.TAG_NAME);
		this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE).setSelectedItem(nombreComponenteSeleccionado);
	}

	private void setResponseComponent(ConsultaDinamica query)
	{
		Componente respuesta = query.getComponenteRespuesta();
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).setSelectedItem(respuesta.getTipoComponente());
		List<Componente> componentes = this.componenteController.findByIdTemaAndIdTipoComponente
		(
				(Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).getSelectedItem(),
				respuesta.getTipoComponente(),
				true
		);
		this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).bindWithList
		(
				this.componenteController.getComponentsPropertyFiltered
				(
						componentes,
						TipoPropiedad.TAG_NAME
				),
				Propiedad.FIELD_REGULAR_DATA
		);
		Propiedad nombreComponenteSeleccionado = respuesta.getPropiedadByTagXml(TipoPropiedad.TAG_NAME);
		this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP).setSelectedItem(nombreComponenteSeleccionado);
	}

	private void setSimpleFields (ConsultaDinamica query)
	{
		this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).setText(query.getConsulta());
		this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).setText(query.getDDescripcion());
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).setText(query.getDNombre());
		this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).setText(Converter.toString(query.getNRowLimit()));
	}

	private void setRowLimit (ConsultaDinamica query)
	{
		Integer rowLimit = query.getNRowLimit();
		rowLimit = rowLimit == null ? 0 : rowLimit;
		boolean limit = rowLimit != 0;
		this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).setText(rowLimit.toString());
		this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).setEnabled(limit);
		this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS).setSelected(limit);
	}

	private void setQueryParams (ConsultaDinamica query)
	{
		Set<ParametroConsulta> parametros = query.getParametrosConsulta();
		QueryParam param;
		this.queryInParams.clear();
		this.queryOutParams.clear();
		for (ParametroConsulta parametroConsulta : parametros)
		{
			param = new QueryParam();
			param.setName(parametroConsulta.getNombre().trim());
			param.setType(parametroConsulta.getTipoDato().trim());
			param.setIndex(parametroConsulta.getIndice());
			if (parametroConsulta.getMTipoSalida().equals(Constants.HABILITADO))
			{
				this.queryOutParams.put(parametroConsulta.getNombre().trim(), param);
			}
			else
			{
				this.queryInParams.put(parametroConsulta.getNombre().trim(), param);
			}
		}
	}

	private void setComboSelection (AutoBindedComboBox combo, Object selection, String fieldName)
	{
		int prevSelection = combo.getSelectedIndex();
		combo.setSelectedItem(selection);
		int currSelection = combo.getSelectedIndex();
		if (prevSelection == currSelection)
		{
			//No hay cambio de seleccion, probablemente no se encontro el objecto a seleccionar en el combo
			Object component;
			Object result = null;
			Object result2 = null;
			for (int i = 0; i < combo.getComponentCount(); i++)
			{
				try
				{
					component = combo.getComponent(i);
					if (component.getClass().equals(selection.getClass()))
					{
						result = Utils.getFieldValue(selection, fieldName);
						result2 = Utils.getFieldValue(component, fieldName);
						if (result2 != null && result != null)
						{
							if (result.equals(result2))
							{
								combo.setSelectedIndex(i);
							}
						}
					}
				}
				catch (Exception e)
				{
					log.error("Se tomo del combo " + combo.getName() + " un item que no era del tipo " + selection.getClass().getName());
				}
			}
		}
	}

	private void updateNewState (boolean isNew)
	{
		this.isNew = isNew;
	}

	public Map<String, QueryParam> getQueryInParams ()
	{
		return this.queryInParams;
	}

	public void setQueryInParams (Map<String, QueryParam> queryInParams)
	{
		this.queryInParams = queryInParams;
	}

	public Map<String, QueryParam> getQueryOutParams ()
	{
		return this.queryOutParams;
	}

	public void setQueryOutParams (Map<String, QueryParam> queryOutParams)
	{
		this.queryOutParams = queryOutParams;
	}

	public boolean isInitialized ()
	{
		return CEWindow.getInstance().isInitialized();
	}

	public boolean isChangesToSave ()
	{
		return this.changesToSave;
	}

	public boolean isLimitRows ()
	{
		return this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS).isSelected();
	}

	private void setChangesToSave (boolean changesToSave)
	{
		this.manager.getButton(R.BUTTONS.SAVE).setEnabled(changesToSave);
		this.changesToSave = changesToSave;
	}

	@Override
	public Object getData ()
	{
		return null;
	}

	@Override
	public void receiveCallBack (int command)
	{
		switch (command)
		{
			case Popup.ACTION_CONFIRM:
				switch (this.currentAction)
				{
					case Commands.EDIT:
						initializeQueryEdition();
						break;
					case Commands.CREATE:
						initializeQueryCreation();
						break;
				}
				break;
		}
	}

	@Override
	public void receiveCallBack (String command, CallBackLauncher laucher)
	{
	}

	@Override
	public void receiveCallBack (String command, Object data)
	{
	}

	@Override
	public void notifyComponentUpdate ()
	{
		setChangesToSave(true);
	}

	/**
	 * Realiza las acciones necesarias por el cierre del editor.
	 */
	public void editorIsClosing ()
	{
		clearComponents();
		this.manager.getPanel(R.PANELS.CE_GLOBAL).setVisible(false);
		updateNewState(false);
		setChangesToSave(false);
	}
}
