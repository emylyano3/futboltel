package com.deportel.editor.contenido.view;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

import com.deportel.administracion.modelo.Modulo;
import com.deportel.componentes.controller.TemaController;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.modelo.TipoComponente;
import com.deportel.editor.common.core.EditorContainer;
import com.deportel.editor.common.core.EditorImpl;
import com.deportel.editor.contenido.controller.CEGuiController;
import com.deportel.editor.contenido.main.ContenidoEditor;
import com.deportel.editor.contenido.model.ProjectTexts;
import com.deportel.editor.contenido.model.ProjectValues;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.ComponentDocumentListener;
import com.deportel.guiBuilder.gui.component.ToolBar;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.guiBuilder.presentation.GuiBuilderPresentation;

/**
 * Clase singleton que representa la ventana principal del editor de contenido. Posee toda la logica de
 * acomodamiento de componentes en pantalla y parte de la implementacion de la interfaz editor necesaria para
 * interactuar con el contenedor de editores.
 *
 * @author Emy
 * @since 24/09/2011
 */
public class CEWindow extends EditorImpl
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized CEWindow getInstance ()
	{
		return getInstance(null);
	}

	public static synchronized CEWindow getInstance (EditorContainer container)
	{
		if (instance == null)
		{
			instance = new CEWindow(container);
		}
		return instance;
	}

	private CEWindow(EditorContainer container)
	{
		this.container = container;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long	serialVersionUID	= 1L;

	private static Logger		log					= Logger.getLogger(CEWindow.class);

	private final Properties	properties			= ContenidoEditor.getProperties();

	private final String		XML_FILE_PATH		= this.properties.getProperty(ProjectValues.GUI_DEFINITION_FILE_PATH);

	private final GuiManager	manager				= GuiBuilderPresentation.getManager(this.XML_FILE_PATH, CEWindow.class);

	private Map<String, String> tpm;

	private static CEWindow		instance;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void arrangeComponents ()
	{
		arrangeWorkPanel();
	}

	/**
	 * Acomoda los componentes sobre el panel de trabajo tomado del workbench
	 */
	private void arrangeWorkPanel ()
	{
		GroupLayout gl = new GroupLayout(this.workbench.getWorkPanel());
		this.workbench.getWorkPanel().setLayout(gl);
		gl.setVerticalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.CE_GLOBAL)));
		gl.setHorizontalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.CE_GLOBAL)));
		arrangeGlobalPanel();
	}

	private void arrangeGlobalPanel ()
	{
		GroupLayout gl = new GroupLayout(this.manager.getPanel(R.PANELS.CE_GLOBAL));
		this.manager.getPanel(R.PANELS.CE_GLOBAL).setLayout(gl);
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);

		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.CONFIGURACION_PRINCIPAL))
				.add(this.manager.getPanel(R.PANELS.CONSULTA))
		);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.CONFIGURACION_PRINCIPAL))
				.add(this.manager.getPanel(R.PANELS.CONSULTA))
		);
		arrangeQueryPrincipalPanel();
		arrangeResponseComponentSelection();
	}

	/**
	 *
	 */
	private void arrangeQueryPrincipalPanel ()
	{
		GroupLayout gl = new GroupLayout(this.manager.getPanel(R.PANELS.CONFIGURACION_PRINCIPAL));
		this.manager.getPanel(R.PANELS.CONFIGURACION_PRINCIPAL).setLayout(gl);
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.TEMA_DE_APLICACION))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION))
				.add(this.manager.getLabel(R.LABELS.TIPO_COMPONENTE_SOLICITANTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE))
				.add(this.manager.getLabel(R.LABELS.COMPONENTE_SOLICITANTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE))
				.add(this.manager.getLabel(R.LABELS.TIPO_COMPONENTE_RESP))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP))
				.add(this.manager.getLabel(R.LABELS.COMPONENTE_RESP))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP))
				.add(this.manager.getLabel(R.LABELS.NOMBRE_CONSULTA))
				.add(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA))
				.add(this.manager.getLabel(R.LABELS.DESCRIPCION_CONSULTA))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.DESCRIPCION_CONSULTA))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getLabel(R.LABELS.TEMA_DE_APLICACION))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION))
				.add(this.manager.getLabel(R.LABELS.TIPO_COMPONENTE_SOLICITANTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE))
				.add(this.manager.getLabel(R.LABELS.COMPONENTE_SOLICITANTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE))
				.add(this.manager.getLabel(R.LABELS.TIPO_COMPONENTE_RESP))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP))
				.add(this.manager.getLabel(R.LABELS.COMPONENTE_RESP))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP))
				.add(this.manager.getLabel(R.LABELS.NOMBRE_CONSULTA))
				.add(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA))
				.add(this.manager.getLabel(R.LABELS.DESCRIPCION_CONSULTA))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.DESCRIPCION_CONSULTA))
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION),
						this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA),
						this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE),
						this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_SOLICITANTE),
						this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP),
						this.manager.getBindedCombo(R.BINDED_COMBOS.COMPONENTE_RESP)
				},
				GroupLayout.VERTICAL
		);
	}

	/**
	 *
	 */
	private void arrangeResponseComponentSelection ()
	{
		GroupLayout gl = new GroupLayout(this.manager.getPanel(R.PANELS.CONSULTA));
		this.manager.getPanel(R.PANELS.CONSULTA).setLayout(gl);
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.SQL_CONSULTA))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.SQL_CONSULTA))
				.add(this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS))
				.add(this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup(GroupLayout.LEADING)
				.add(this.manager.getLabel(R.LABELS.SQL_CONSULTA))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.SQL_CONSULTA))
				.add(this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS))
				.add(this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT))
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getCheckBox(R.CHECK_BOXES.LIMIT_ROWS),
						this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT)
				},
				GroupLayout.VERTICAL
		);
	}

	@Override
	protected String getTrayIconName (String type)
	{
		return this.properties.getProperty(type);
	}

	@Override
	protected String getTrayIconDescription (String type)
	{
		return this.properties.getProperty(ProjectValues.TRAY_ICON_DESCRIPTION);
	}

	@Override
	public void doSettings ()
	{
		log.debug("Se efectua el doSettings de la pantalla del Editor Contenido");
		createPanelsBorder();
		setListeners();
		setScrollPaneViewPorts();
		bindCombos();
	}

	/**
	 *
	 */
	private void setListeners ()
	{
		ComponentDocumentListener listener = new ComponentDocumentListener(CEGuiController.getInstance(this.manager));
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE_CONSULTA).getDocument().addDocumentListener(listener);
		this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).getDocument().addDocumentListener(listener);
		this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA).getDocument().addDocumentListener(listener);
		this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getDocument().addDocumentListener(listener);
	}

	/**
	 *
	 */
	private void setScrollPaneViewPorts ()
	{
		this.manager.getScrollPane(R.SCROLL_PANES.SQL_CONSULTA).setViewportView
		(
				this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA)
		);
		this.manager.getScrollPane(R.SCROLL_PANES.DESCRIPCION_CONSULTA).setViewportView
		(
				this.manager.getTextArea(R.TEXT_AREAS.DESCRIPCION_CONSULTA)
		);
	}

	/**
	 *
	 */
	private void bindCombos ()
	{
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE).bindWithList
		(
				CEGuiController.getInstance(this.manager).getTiposComponenteSolicitante(), TipoComponente.FIELD_NAME
		);
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_COMPONENTE_RESP).bindWithList
		(
				CEGuiController.getInstance(this.manager).getTiposComponenteRespuesta(), TipoComponente.FIELD_NAME
		);
		this.manager.getBindedCombo(R.BINDED_COMBOS.TEMA_DE_APLICACION).bindWithList
		(
				TemaController.getInstance(true).findAll(), Tema.FIELD_NAME
		);
	}

	/**
	 *
	 */
	private void createPanelsBorder ()
	{
		GuiUtils.addTitledBorder
		(
				this.manager.getPanel(R.PANELS.CE_GLOBAL), "Configuración de la consulta de obtención de datos"
		);
	}

	@Override
	public JMenuBar getMenuBar ()
	{
		return null;
	}

	@Override
	public String getName ()
	{
		return ContenidoEditor.getName();
	}

	@Override
	public ToolBar getToolBar ()
	{
		return this.manager.getToolBar(R.TOOL_BARS.TOOL_BAR_EDITOR_CONTENIDO);
	}

	@Override
	public Modulo getModule ()
	{
		return ContenidoEditor.getModule(getModuleName());
	}

	@Override
	protected boolean canClose ()
	{
		boolean needConfirmation = CEGuiController.getInstance(this.manager).isChangesToSave();
		if (needConfirmation)
		{
			int option = JOptionPane.showConfirmDialog
			(
					this.getWorkbench().getWorkPanel(),
					ContenidoEditor.getTexts().get(ProjectTexts.WARN_UNSAVED_CHANGES),
					ContenidoEditor.getTexts().get(ProjectTexts.MSG_TITLE_ATENTION),
					JOptionPane.YES_NO_OPTION
			);
			return option == JOptionPane.YES_OPTION;
		}
		else
		{
			return true;
		}
	}

	@Override
	protected void actionsOnClose ()
	{
		CEGuiController.getInstance(this.manager).editorIsClosing();
	}

	@Override
	protected Map<String, String> getToolsPermissionMap ()
	{
		if (this.tpm == null)
		{
			this.tpm = new HashMap<String, String>();
			this.tpm.put(R.BUTTONS.NEW, this.properties.getProperty(ProjectValues.PERMISSION_CREATE));
			this.tpm.put(R.BUTTONS.SAVE, this.properties.getProperty(ProjectValues.PERMISSION_CREATE));
			this.tpm.put(R.BUTTONS.EDIT, this.properties.getProperty(ProjectValues.PERMISSION_EDIT));
			this.tpm.put(R.BUTTONS.DELETE, this.properties.getProperty(ProjectValues.PERMISSION_DELETE));
			this.tpm.put(R.BUTTONS.SAVE_AS_NEW, this.properties.getProperty(ProjectValues.PERMISSION_CREATE));
			this.tpm.put(R.BUTTONS.TEST_QUERY, this.properties.getProperty(ProjectValues.PERMISSION_CREATE));
		}
		return this.tpm;
	}

	@Override
	protected String getMenuIconName ()
	{
		return this.properties.getProperty(ProjectValues.MENU_ICON);
	}

}
