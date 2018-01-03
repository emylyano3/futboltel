package com.deportel.editor.template.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.componentes.controller.TemaController;
import com.deportel.componentes.controller.TipoComponenteController;
import com.deportel.componentes.modelo.Tema;
import com.deportel.editor.common.core.EditorContainer;
import com.deportel.editor.common.core.EditorImpl;
import com.deportel.editor.template.controller.TEGuiController;
import com.deportel.editor.template.controller.TIGuiController;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectTexts;
import com.deportel.editor.template.model.ProjectValues;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.ToolBar;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.guiBuilder.presentation.GuiBuilderPresentation;

/**
 * Clase singleton que representa la ventana principal del editor.
 *
 * @author Emy
 * @since 10/01/2011
 */
public class TEWindow extends EditorImpl implements CallBackListener
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static TEWindow getInstance (EditorContainer container)
	{
		if (instance == null)
		{
			instance = new TEWindow(container);
		}
		return instance;
	}

	private TEWindow(EditorContainer container)
	{
		this.container = container;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long				serialVersionUID			= 1L;

	private static Logger					log							= Logger.getLogger(TEWindow.class);

	private final Properties				properties					= TemplateEditor.getProperties();

	private final String					XML_FILE_PATH				= this.properties.getProperty(ProjectValues.GUI_DEFINITION_FILE_PATH);

	private final GuiManager				manager						= GuiBuilderPresentation.getManager(this.XML_FILE_PATH, TEWindow.class);

	private final TipoComponenteController	tipoComponenteController	= TipoComponenteController.getInstance(true);

	private final TemaController			temaController				= TemaController.getInstance(true);

	private static TEWindow					instance;

	/**
	 * Map de asociacion de tipo permiso - herramienta.
	 */
	private Map<String, String>				tpm;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void arrangeComponents ()
	{
		log.debug("Acomodando los componentes en el workbench");
		GroupLayout gl = new GroupLayout(this.workbench.getWorkPanel());
		this.workbench.getWorkPanel().setLayout(gl);
		gl.setVerticalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.GLOBAL)));
		gl.setHorizontalGroup(gl.createParallelGroup().add(this.manager.getPanel(R.PANELS.GLOBAL)));
		arrangeGlobalPanel();
	}

	private void arrangeGlobalPanel ()
	{
		log.debug("Acomodando los componentes en el panel global");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.GLOBAL).getLayout();
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.IMPORT))
				.add(this.manager.getPanel(R.PANELS.COMPONENT_CREATE_GLOBAL))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.IMPORT))
				.add(this.manager.getPanel(R.PANELS.COMPONENT_CREATE_GLOBAL))
		);
		arrangeImportPanel();
		arrangeComponentCreationPanel();
	}

	private void arrangeImportPanel ()
	{
		log.debug("Acomodando los componentes en el panel de importacion");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.IMPORT).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.IMPORT_BROWSE))
				.add(this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.IMPORT_BROWSE))
				.add(this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF))
		);
		arrangeImportBrowsePanel();
		arrangeImportDebriefPanel();
	}

	private void arrangeImportBrowsePanel ()
	{
		log.debug("Acomodando los componentes en el panel de busqueda de archivo de la importacion");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.IMPORT_BROWSE).getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.BROWSE))
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getTextField(R.TEXT_FIELDS.BROWSE))
						.add(this.manager.getButton(R.BUTTONS.BROWSE))

				)
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getButton(R.BUTTONS.LOAD_FILE))
						.add(this.manager.getCheckBox(R.CHECK_BOXES.PARA_CONTENIDO))
				)
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getLabel(R.LABELS.BROWSE))
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getTextField(R.TEXT_FIELDS.BROWSE))
						.add(this.manager.getButton(R.BUTTONS.BROWSE))
				)
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getButton(R.BUTTONS.LOAD_FILE))
						.add(this.manager.getCheckBox(R.CHECK_BOXES.PARA_CONTENIDO))
				)
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getTextField(R.TEXT_FIELDS.BROWSE),
						this.manager.getButton(R.BUTTONS.BROWSE)
				},
				GroupLayout.VERTICAL
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getButton(R.BUTTONS.LOAD_FILE),
						this.manager.getButton(R.BUTTONS.CANCEL_IMPORT)
				},
				GroupLayout.HORIZONTAL
		);
	}

	private void arrangeImportDebriefPanel ()
	{
		log.debug("Acomodando los componentes en el panel de informacion de importacion");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF).getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.IMPORT_DEBRIEF_DETAILS))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.IMPORT_DEBRIEF_DETAILS))
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getButton(R.BUTTONS.OK_IMPORT))
						.add(this.manager.getButton(R.BUTTONS.CANCEL_IMPORT))
				)
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getLabel(R.LABELS.IMPORT_DEBRIEF_DETAILS))
				.add(this.manager.getScrollPane(R.SCROLL_PANES.IMPORT_DEBRIEF_DETAILS))
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getButton(R.BUTTONS.OK_IMPORT))
						.add(this.manager.getButton(R.BUTTONS.CANCEL_IMPORT))
				)
		);
	}

	private void arrangeComponentCreationPanel ()
	{
		log.debug("Acomodando los componentes en el panel de creacion de componente");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.COMPONENT_CREATE_GLOBAL).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.COMPONENT_SELECTION))
				.add(this.manager.getPanel(R.PANELS.COMPONENT_EDITION))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.COMPONENT_SELECTION))
				.add(this.manager.getPanel(R.PANELS.COMPONENT_EDITION))
		);
		arrangeComponentEditionPanel();
		arrangeComponentSelectionPanel();
	}

	private void arrangeComponentEditionPanel ()
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.COMPONENT_EDITION).getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getScrollPane(R.SCROLL_PANES.COMPONENT_EDITION))
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getButton(R.BUTTONS.CANCEL_EDIT))
				)
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getScrollPane(R.SCROLL_PANES.COMPONENT_EDITION))
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getButton(R.BUTTONS.CANCEL_EDIT))
				)
		);
	}

	private void arrangeComponentSelectionPanel ()
	{
		log.debug("Acomodando los componentes en el panel de creacion de componente");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.COMPONENT_SELECTION).getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.SEL_TEMA))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA))
				.add(this.manager.getLabel(R.LABELS.SEL_TIPO_COMPONENTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE))
				.add(this.manager.getLabel(R.LABELS.SEL_COMPONENTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getLabel(R.LABELS.SEL_TEMA))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA))
				.add(this.manager.getLabel(R.LABELS.SEL_TIPO_COMPONENTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE))
				.add(this.manager.getLabel(R.LABELS.SEL_COMPONENTE))
				.add(this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE))
		);
		gl.linkSize
		(
				new Component[] {
						this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA),
						this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE),
						this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE)
				},
				GroupLayout.VERTICAL
		);
	}

	@Override
	public JMenuBar getMenuBar ()
	{
		return null;
	}

	@Override
	public Object getData ()
	{
		return null;
	}

	@Override
	public void receiveCallBack (int action)
	{
	}

	@Override
	public void receiveCallBack (String action, CallBackLauncher laucher)
	{

	}

	@Override
	public void receiveCallBack (String action, Object data)
	{
	}

	@Override
	public String getName ()
	{
		return TemplateEditor.getName();
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
		this.manager.getScrollPane(R.SCROLL_PANES.IMPORT_DEBRIEF_DETAILS).setViewportView
		(
				this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF_DETAILS)
		);
		createPanelsBorder();
		this.manager.getPanel(R.PANELS.IMPORT_BROWSE).setMinimumSize(new Dimension(200, 50));
		this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF).setMinimumSize(new Dimension(300, 100));
		this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF_DETAILS).setMinimumSize(new Dimension(250, 100));
		bindCombos();
		setScrollpanelViewports();
	}

	private void setScrollpanelViewports ()
	{
		this.manager.getScrollPane(R.SCROLL_PANES.COMPONENT_EDITION).setViewportView
		(
				this.manager.getPanel(R.PANELS.COMPONENT_EDITION_SCROLL)
		);
	}

	private void bindCombos ()
	{
		this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA).bindWithList
		(
				this.temaController.findAll(), Tema.FIELD_NAME
		);
		this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE).bindWithList
		(
				this.tipoComponenteController.findAll(), Tema.FIELD_NAME
		);
	}

	@Override
	public void refreshCache ()
	{
		bindCombos();
	}

	/**
	 *
	 */
	private void createPanelsBorder ()
	{
		GuiUtils.addTitledBorder
		(
				this.manager.getPanel(R.PANELS.IMPORT_BROWSE), "Selección de archivo"
		);
		GuiUtils.addTitledBorder
		(
				this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF), "Información de carga"
		);
		GuiUtils.addTitledBorder
		(
				this.manager.getPanel(R.PANELS.COMPONENT_SELECTION), "Selección del componente a editar"
		);
		GuiUtils.addTitledBorder
		(
				this.manager.getPanel(R.PANELS.COMPONENT_EDITION), "Propiedades del componente"
		);
	}

	/**
	 *
	 */
	public void clearComponentCreationPanel ()
	{
		this.manager.getPanel(R.PANELS.COMPONENT_EDITION_SCROLL).removeAll();
		this.manager.getPanel(R.PANELS.COMPONENT_EDITION_SCROLL).updateUI();
	}

	/**
	 *
	 */
	public void arrangeComponentCreationPanel (Set<String> componentsName)
	{
		String componentName;
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.COMPONENT_EDITION_SCROLL).getLayout();
		ParallelGroup pg = gl.createParallelGroup();
		SequentialGroup sg = gl.createSequentialGroup();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		List<JComponent> controlComponents = new ArrayList<JComponent>();
		JComponent uiControl;
		for (Iterator<String> it = componentsName.iterator(); it.hasNext();)
		{
			componentName = it.next();
			uiControl = getUIControlComponent(componentName);
			JLabel label = this.manager.getLabel(componentName);
			pg.add(label);
			pg.add(uiControl);
			sg.add(label);
			sg.add(uiControl);
			controlComponents.add(uiControl);
		}
		gl.linkSize
		(
				controlComponents.toArray(new Component[controlComponents.size()]),
				GroupLayout.VERTICAL
		);
		gl.linkSize
		(
				controlComponents.toArray(new Component[controlComponents.size()]),
				GroupLayout.HORIZONTAL
		);
		gl.setHorizontalGroup(pg);
		gl.setVerticalGroup(sg);
	}

	private JComponent getUIControlComponent (String componentName)
	{
		return TEGuiController.getInstance(this.manager).getUIControl(componentName);
	}

	@Override
	protected boolean canClose ()
	{
		boolean needConfirmation = TEGuiController.getInstance(this.manager).isChangesToSave();
		if (needConfirmation)
		{
			int option = JOptionPane.showConfirmDialog
			(
					this.getWorkbench().getWorkPanel(),
					TemplateEditor.getTexts().get(ProjectTexts.WARN_CHANGES_NOT_SAVED),
					TemplateEditor.getTexts().get(ProjectTexts.TE_MSG_ATENTION),
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
		TEGuiController.getInstance(this.manager).editorIsClosing();
		TIGuiController.getInstance(this.manager).editorIsClosing();
	}

	@Override
	public ToolBar getToolBar ()
	{
		return this.manager.getToolBar(R.TOOL_BARS.TEMPLATES_EDITOR);
	}

	@Override
	public Modulo getModule ()
	{
		return TemplateEditor.getModule(getModuleName());
	}

	@Override
	protected Map<String, String> getToolsPermissionMap ()
	{
		if (this.tpm == null)
		{
			this.tpm = new HashMap<String, String>();
			this.tpm.put(R.BUTTONS.SAVE, this.properties.getProperty(ProjectValues.PERMISSION_CREATE));
			this.tpm.put(R.BUTTONS.CE_EDIT_MODE, this.properties.getProperty(ProjectValues.PERMISSION_EDIT));
			this.tpm.put(R.BUTTONS.TI_MODE, this.properties.getProperty(ProjectValues.PERMISSION_IMPORT));
		}
		return this.tpm;
	}

	@Override
	protected String getMenuIconName ()
	{
		return this.properties.getProperty(ProjectValues.MENU_ICON);
	}
}
