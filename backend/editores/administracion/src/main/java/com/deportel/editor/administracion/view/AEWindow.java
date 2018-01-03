package com.deportel.editor.administracion.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JMenuBar;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.editor.administracion.controller.AEGuiControllerStrategy;
import com.deportel.editor.administracion.controller.AEGuiControllerStrategyFactory;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.model.ProjectValues;
import com.deportel.editor.common.core.EditorContainer;
import com.deportel.editor.common.core.EditorImpl;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.ToolBar;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.guiBuilder.presentation.GuiBuilderPresentation;

/**
 * Clase singleton que representa la ventana principal del editor.
 * 
 * @author Emy
 * @since 20/08/2011
 */
public class AEWindow extends EditorImpl implements CallBackListener
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static AEWindow getInstance (EditorContainer container)
	{
		if (instance == null)
		{
			instance = new AEWindow(container);
		}
		return instance;
	}

	private AEWindow(EditorContainer container)
	{
		this.container = container;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long			serialVersionUID	= 1L;

	private static Logger				log					= Logger.getLogger(AEWindow.class);

	private final Properties			properties			= AdministracionEditor.getProperties();

	private final String				XML_FILE_PATH		= this.properties.getProperty(ProjectValues.GUI_DEFINITION_FILE_PATH);

	private final GuiManager			manager				= GuiBuilderPresentation.getManager(this.XML_FILE_PATH, AEWindow.class);

	private static AEWindow				instance;

	/**
	 * Map de asociacion de tipo permiso - herramienta.
	 */
	private Map<String, String> 		tpm;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void arrangeComponents ()
	{
		arrangeWorkPanel();
		arrangeGeneralPanel();
		arrangeDataGridPanel();
		arrangeConfirmationPanel();
		arrangeFieldsInputPanel();
		arrangeTableInputPanel();
		arrangeInputPanel();
		arrangeCrossDataPanel();
	}

	/**
	 * 
	 */
	private void arrangeWorkPanel ()
	{
		GroupLayout gl = new GroupLayout(this.workbench.getWorkPanel());
		this.workbench.getWorkPanel().setLayout(gl);
		gl.setVerticalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.GENERAL)));
		gl.setHorizontalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.GENERAL)));
	}

	/**
	 * 
	 */
	private void arrangeGeneralPanel ()
	{
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.GENERAL)).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.LISTADO))
				.add(this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL))
				.add(this.manager.getPanel(R.PANELS.CONFIRMACION))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup(GroupLayout.CENTER)
				.add(this.manager.getPanel(R.PANELS.LISTADO))
				.add(this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL))
				.add(this.manager.getPanel(R.PANELS.CONFIRMACION))
		);
	}

	/**
	 * 
	 */
	private void arrangeDataGridPanel ()
	{
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.LISTADO)).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.LISTADO_PERFILES))
						.add(this.manager.getLabel(R.LABELS.LISTADO_USUARIOS))
						.add(this.manager.getLabel(R.LABELS.LISTADO_TIPO_PERMISO))
						.add(this.manager.getLabel(R.LABELS.LISTADO_MODULOS))
						.add(this.manager.getScrollPane(R.SCROLL_PANES.GRILLA_SCROLL))
				)
				.add(this.manager.getPanel(R.PANELS.CROSS_DATA))
		);
		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.LISTADO_PERFILES))
						.add(this.manager.getLabel(R.LABELS.LISTADO_USUARIOS))
						.add(this.manager.getLabel(R.LABELS.LISTADO_TIPO_PERMISO))
						.add(this.manager.getLabel(R.LABELS.LISTADO_MODULOS))
						.add(this.manager.getScrollPane(R.SCROLL_PANES.GRILLA_SCROLL))
				)
				.add(this.manager.getPanel(R.PANELS.CROSS_DATA))
		);
	}

	private void arrangeCrossDataPanel ()
	{
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.CROSS_DATA)).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getLabel(R.LABELS.MODULOS))
				.add(this.manager.getLabel(R.LABELS.PERFILES))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.CROSS_DATA))
		);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.MODULOS))
				.add(this.manager.getLabel(R.LABELS.PERFILES))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.CROSS_DATA))
		);
	}

	/**
	 * 
	 */
	private void arrangeConfirmationPanel ()
	{
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.CONFIRMACION)).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getButton(R.BUTTONS.OK))
				.add(this.manager.getButton(R.BUTTONS.CANCEL))
		);
		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getButton(R.BUTTONS.OK))
				.add(this.manager.getButton(R.BUTTONS.CANCEL))
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getButton(R.BUTTONS.OK),
						this.manager.getButton(R.BUTTONS.CANCEL)
				},
				GroupLayout.HORIZONTAL
		);
	}

	/**
	 * 
	 */
	private void arrangeTableInputPanel ()
	{
		log.debug("Comienza arrangeDetailInputPanel");
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA)).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getButton(R.BUTTONS.ADD_MODULE))
						.add(this.manager.getButton(R.BUTTONS.ADD_PERFIL))
						.add(this.manager.getButton(R.BUTTONS.DEL_MODULE))
						.add(this.manager.getButton(R.BUTTONS.DEL_PERFIL))
				)
				.add(this.manager.getScrollPane(R.SCROLL_PANES.INPUT_GRID_SCROLL))
		);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getButton(R.BUTTONS.ADD_MODULE))
						.add(this.manager.getButton(R.BUTTONS.ADD_PERFIL))
						.add(this.manager.getButton(R.BUTTONS.DEL_MODULE))
						.add(this.manager.getButton(R.BUTTONS.DEL_PERFIL))
				)
				.add(this.manager.getScrollPane(R.SCROLL_PANES.INPUT_GRID_SCROLL))
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getButton(R.BUTTONS.ADD_MODULE),
						this.manager.getButton(R.BUTTONS.ADD_PERFIL),
						this.manager.getButton(R.BUTTONS.DEL_MODULE),
						this.manager.getButton(R.BUTTONS.DEL_PERFIL)
				},
				GroupLayout.HORIZONTAL
		);
	}

	/**
	 * 
	 */
	private void arrangeInputPanel ()
	{
		log.info("Comienza arrangeInputPanel");
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL)).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS))
				.add(this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA))
		);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS))
				.add(this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA))
		);
	}

	/**
	 * 
	 */
	private void arrangeFieldsInputPanel ()
	{
		log.debug("Comienza arrangeDetailInputPanel");
		GroupLayout gl = (GroupLayout) (this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS)).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.ALIAS))
						.add(this.manager.getTextField(R.TEXT_FIELDS.ALIAS))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.NOMBRE))
						.add(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.APELLIDO))
						.add(this.manager.getTextField(R.TEXT_FIELDS.APELLIDO))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.DESCRIPCION))
						.add(this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.EMAIL))
						.add(this.manager.getTextField(R.TEXT_FIELDS.EMAIL))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getLabel(R.LABELS.CONFIRM_EMAIL))
						.add(this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getButton(R.BUTTONS.GENERATE_PASSWORD))
						.add(this.manager.getTextField(R.TEXT_FIELDS.PASSWORD))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getCheckBox(R.CHECK_BOXES.ESTADO))

				)
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.ALIAS))
						.add(this.manager.getTextField(R.TEXT_FIELDS.ALIAS))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.NOMBRE))
						.add(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.APELLIDO))
						.add(this.manager.getTextField(R.TEXT_FIELDS.APELLIDO))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.DESCRIPCION))
						.add(this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.EMAIL))
						.add(this.manager.getTextField(R.TEXT_FIELDS.EMAIL))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getLabel(R.LABELS.CONFIRM_EMAIL))
						.add(this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getButton(R.BUTTONS.GENERATE_PASSWORD))
						.add(this.manager.getTextField(R.TEXT_FIELDS.PASSWORD))
				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getCheckBox(R.CHECK_BOXES.ESTADO))
				)
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getLabel(R.LABELS.ALIAS),
						this.manager.getLabel(R.LABELS.NOMBRE),
						this.manager.getLabel(R.LABELS.APELLIDO),
						this.manager.getLabel(R.LABELS.DESCRIPCION),
						this.manager.getLabel(R.LABELS.EMAIL),
						this.manager.getLabel(R.LABELS.CONFIRM_EMAIL),
						this.manager.getButton(R.BUTTONS.GENERATE_PASSWORD)
				},
				GroupLayout.HORIZONTAL
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getTextField(R.TEXT_FIELDS.ALIAS),
						this.manager.getTextField(R.TEXT_FIELDS.NOMBRE),
						this.manager.getTextField(R.TEXT_FIELDS.APELLIDO),
						this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION),
						this.manager.getTextField(R.TEXT_FIELDS.EMAIL),
						this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL),
						this.manager.getTextField(R.TEXT_FIELDS.PASSWORD)
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
	public Object getData ()
	{
		return null;
	}

	@Override
	public void receiveCallBack (int command)
	{

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
	public void doSettings ()
	{
		log.debug("Se efectua el doSettings de la pantalla del Editor Administración");
		GuiUtils.addTitledBorder(this.manager.getPanel(R.PANELS.LISTADO), "Panel de listado");
		GuiUtils.addTitledBorder(this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL), "Panel de ingreso de datos");
		TableColumn column ;
		column = this.manager.getTable(R.TABLES.MODULOS_PERFIL).getColumnModel().getColumn(0);
		column.setCellEditor(new DefaultCellEditor(this.manager.getBindedCombo(R.BINDED_COMBOS.MODULO)));
		column = this.manager.getTable(R.TABLES.MODULOS_PERFIL).getColumnModel().getColumn(1);
		column.setCellEditor(new DefaultCellEditor(this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_PERMISO)));
		column = this.manager.getTable(R.TABLES.PERFILES_USUARIO).getColumnModel().getColumn(0);
		column.setCellEditor(new DefaultCellEditor(this.manager.getBindedCombo(R.BINDED_COMBOS.PERFIL)));
		this.manager.getScrollPane(R.SCROLL_PANES.CROSS_DATA).setViewportView(this.manager.getTable(R.TABLES.PERFIL_CROSS_DATA));
		setPanelsSizes();
		createButtonGroupForMenu();
	}

	/**
	 * 
	 */
	private void setPanelsSizes ()
	{
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).setMinimumSize(new Dimension(220, 180));
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS).setMinimumSize(new Dimension(220, 170));
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA).setMinimumSize(new Dimension(220, 170));
		this.manager.getTable(R.TABLES.PERFILES_USUARIO).setMinimumSize(new Dimension(Short.MAX_VALUE, 100));
		this.manager.getTable(R.TABLES.MODULOS_PERFIL).setMinimumSize(new Dimension(Short.MAX_VALUE, 100));
	}

	@Override
	public JMenuBar getMenuBar ()
	{
		return this.manager.getMenuBar(R.MENU_BARS.EDITOR_ADMINISTRACION);
	}

	@Override
	public String getName ()
	{
		return AdministracionEditor.getName();
	}

	@Override
	public ToolBar getToolBar ()
	{
		return this.manager.getToolBar(R.TOOL_BARS.TOOLS);
	}

	/**
	 * 
	 */
	private void createButtonGroupForMenu ()
	{
		ButtonGroup group = new ButtonGroup();
		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.MODULO));
		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.TIPO_PERMISO));
		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.USUARIO));
		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.PERFIL));
	}

	@Override
	public Modulo getModule ()
	{
		return AdministracionEditor.getModule(getModuleName());
	}

	@Override
	protected boolean canClose ()
	{
		return true;
	}

	@Override
	protected void actionsOnClose ()
	{
		AEGuiControllerStrategy controller;
		if (AdministracionEditor.getContext() != null)
		{
			controller = AEGuiControllerStrategyFactory.getStrategy(this.manager, AdministracionEditor.getContext());
		}
		else
		{
			controller = AEGuiControllerStrategyFactory.getStrategy(this.manager, DEFAULT_CONTEXT);
		}
		controller.cancelAction();
		controller.resetUI();
		AdministracionEditor.setContext(null);
	}

	@Override
	protected Map<String, String> getToolsPermissionMap ()
	{
		if (this.tpm == null)
		{
			this.tpm = new HashMap<String, String>();
			this.tpm.put(R.BUTTONS.NEW, this.properties.getProperty(ProjectValues.PERMISSION_CREATE));
			this.tpm.put(R.BUTTONS.EDIT, this.properties.getProperty(ProjectValues.PERMISSION_EDIT));
			this.tpm.put(R.BUTTONS.DELETE, this.properties.getProperty(ProjectValues.PERMISSION_DELETE));
		}
		return this.tpm;
	}

	@Override
	protected String getMenuIconName ()
	{
		return this.properties.getProperty(ProjectValues.MENU_ICON);
	}

	private static final String DEFAULT_CONTEXT = R.RADIO_BUTTON_MENU_ITEMS.MODULO;
}
