package com.deportel.editor.template.controller;

import java.awt.Font;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.deportel.common.Constants;
import com.deportel.common.Text;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.FuenteContenidoController;
import com.deportel.componentes.controller.PropiedadController;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.modelo.TipoComponente;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.editor.common.core.EditorImpl;
import com.deportel.editor.template.exception.InvalidPropertyException;
import com.deportel.editor.template.exception.TemplateException;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectTexts;
import com.deportel.editor.template.view.R;
import com.deportel.editor.template.view.TEWindow;
import com.deportel.editor.template.view.property.control.PropertyDataControl;
import com.deportel.editor.template.view.property.control.PropertyDataControlFactory;
import com.deportel.editor.template.view.property.converter.PropertyDataConverter;
import com.deportel.editor.template.view.property.converter.PropertyDataConverterFactory;
import com.deportel.guiBuilder.gui.component.FileBrowser;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * Template Edition GUI Controller
 * @author Emy
 */
public class TEGuiController extends BaseGuiController
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger						log							= Logger.getLogger(TEGuiController.class);
	private final ComponenteController				componenteController		= ComponenteController.getInstance(true);
	private final TEWindow							window;
	private final Set<String>						propertiesName				= new TreeSet<String>();
	private boolean									changesToSave				= false;
	private Componente								selectedComponent;
	private final PropiedadController				propiedadController			= PropiedadController.getInstance(true);
	private final FuenteContenidoController			fuenteContenidoController	= FuenteContenidoController.getInstance(true);
	private final IEGuiController					ieController;
	private final Text								texts						= TemplateEditor.getTexts();
	private final Font								controlFont					= new Font("Arial", Font.PLAIN, 13);
	private int										currentAction;
	private final Map<String, PropertyDataControl>	propertyDataUIControls		= new HashMap<String, PropertyDataControl>();

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static TEGuiController			instance;

	private TEGuiController(GuiManager manager)
	{
		super(manager);
		this.ieController = IEGuiController.getInstance(manager);
		this.window = TEWindow.getInstance(null);
	}

	public static TEGuiController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new TEGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void enableMode ()
	{
		log.debug("Habilitando el modo de edicion de componentes");
		setChangesToSave(this.changesToSave);
		this.manager.getPanel(R.PANELS.IMPORT).setVisible(false);
		this.window.makeToolVisible(this.manager.getButton(R.BUTTONS.SAVE));
		this.manager.getPanel(R.PANELS.COMPONENT_CREATE_GLOBAL).setVisible(true);
	}

	public void selectionDone (String selectionTrigger)
	{
		log.debug("Se selecciono el componente " + selectionTrigger);
		if (selectionTrigger.equals(R.BINDED_COMBOS.SEL_COMPONENTE))
		{
			confirmComponentChanged();
		}
		else if (selectionTrigger.equals(R.BINDED_COMBOS.SEL_TEMA))
		{
			confirmThemeChanged();
		}
		else if (selectionTrigger.equals(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE))
		{
			confirmComponentTypeChanged();
		}
	}

	private void confirmComponentChanged ()
	{
		if (!isChangesToSave())
		{
			componentChanged();
		}
		else
		{
			setCurrentAction(ACTION_CHANGE_COMPONENT);
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_CHANGES_NOT_SAVED),
					Popup.POPUP_CONFIRM,
					this.window.getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
		}
	}

	private void componentChanged ()
	{
		if (this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).hasValidSelection())
		{
			prepareInitComponentEditionPanel();
		}
		else
		{
			cancelComponentEdition();
		}
	}

	private void confirmComponentTypeChanged ()
	{
		if (!isChangesToSave())
		{
			componentTypeChanged();
		}
		else
		{
			setCurrentAction(ACTION_CHANGE_TYPE);
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_CHANGES_NOT_SAVED),
					Popup.POPUP_CONFIRM,
					this.window.getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
		}
	}

	private void componentTypeChanged ()
	{
		if (this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE).hasValidSelection())
		{
			List<Componente> componentes = this.componenteController.findByIdTemaAndIdTipoComponente
			(
					(Tema) this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA).getSelectedItem(),
					(TipoComponente) this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE).getSelectedItem()
			);
			List<Componente> componentesWeb = this.componenteController.getEditableComponents(componentes);
			List<Componente> componentesContenido = this.componenteController.filterComponentsByContentFlag(componentes, Constants.HABILITADO);
			componentes.clear();
			componentes.addAll(componentesWeb);
			componentes.addAll(componentesContenido);
			List<Propiedad> properties = this.componenteController.getComponentsPropertyFiltered
			(
					componentes,
					TipoPropiedad.TAG_NAME
			);
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).bindWithList
			(
					properties,
					Propiedad.FIELD_REGULAR_DATA
			);
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).setEnabled(true);
		}
		else
		{
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).setSelectedIndex(0);
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).setEnabled(false);
		}
	}

	private void confirmThemeChanged ()
	{
		if (!isChangesToSave())
		{
			themeChanged();
		}
		else
		{
			setCurrentAction(ACTION_CHANGE_THEME);
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_CHANGES_NOT_SAVED),
					Popup.POPUP_CONFIRM,
					this.window.getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
		}
	}

	private void themeChanged ()
	{
		boolean validSelection = this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA).hasValidSelection();
		if (validSelection)
		{
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE).setEnabled(true);
		}
		else
		{
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE).setEnabled(false);
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE).setSelectedIndex(0);
		}
	}

	@Override
	public void save ()
	{
		log.debug("Guardando componentes");
		if (isChangesToSave())
		{
			try
			{
				convertComponentProperties();
				validatePropertiesValues();
				saveProperties();
				updateServerCache(this.window.getContainer().getServerURL());
				setChangesToSave(false);
			}
			catch (TemplateException e)
			{
				Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR, this.window.getWorkbench().getWorkPanel()).showGui();
			}
		}
	}

	/**
	 * Valida los valores ingresados por el usuario segun el tipo de propiedad. Si el valor ingresado es
	 * correcto, se setea a la propiedad el valor ingresado.
	 *
	 * @throws TemplateException
	 *             Si al menos uno de los valores ingrsados no se corresponde con el tipo de propiedad.
	 */
	private void convertComponentProperties () throws TemplateException
	{
		String propName;
		String propValue;
		for (String name : this.propertiesName)
		{
			if (!TipoPropiedadController.NOT_REGULAR_PROPERTIES.containsKey(name))
			{
				propName = this.manager.getLabel(name).getText();
				propValue = this.propertyDataUIControls.get(name).getValue();
				for (Propiedad propiedad : getSelectedComponent().getPropiedades())
				{
					if (propName.equals(propiedad.getTipoPropiedad().getDNombre()))
					{
						PropertyDataConverter converter = PropertyDataConverterFactory.getConverter(propiedad);
						propValue = converter.doConversion(propValue);
						propiedad.setDRegularData(propValue);
						break;
					}
				}
			}
		}
	}

	private void validatePropertiesValues()
	{
		validateImageDataNotNull();
		validateActionParams();
	}

	private void validateActionParams()
	{
		Componente componente = getSelectedComponent();
		Propiedad actions = componente.getPropiedadByTagXml(TipoPropiedad.TAG_ACTION);
		if (actions != null)
		{
			Propiedad params = componente.getPropiedadByTagXml(TipoPropiedad.TAG_ACTION_PARAMS);
			String[] actionsString = actions.getDRegularData().split(",");
			for (String action : actionsString)
			{
				if (!params.getDRegularData().contains(action))
				{
					throw new InvalidPropertyException(this.texts.get(ProjectTexts.TE_ERROR_WRONG_PROP_PARAMS));
				}
			}
		}
	}

	private boolean validateImageDataNotNull ()
	{
		Collection<Propiedad> properties = getSourcesProperties();
		for (Propiedad property : properties)
		{
			if (property.getBinaryData() == null)
			{
				return false;
			}
		}
		return true;
	}

	private void saveProperties () throws TemplateException
	{
		Set<Propiedad> propetiesToSave = new HashSet<Propiedad>();
		propetiesToSave.addAll(this.getSelectedComponent().getPropiedades());
		propetiesToSave.addAll(getSourcesProperties());
		this.propiedadController.persistProperties(propetiesToSave);
		this.propiedadController.removeProperties(getRemovedSourcesProperties());
	}

	private Collection<Propiedad> getSourcesProperties ()
	{
		if (this.getIeController() != null)
		{
			return this.getIeController().getImageSources().values();
		}
		return null;
	}

	private List<Propiedad> getRemovedSourcesProperties ()
	{
		if (this.getIeController() != null)
		{
			return this.getIeController().getImageSourcesDeleted();
		}
		return null;
	}

	public void browseFile()
	{
		log.debug("Abriendo el browser para la carga del archivo");
		FileBrowser fb = new FileBrowser("Descriptor de plantilla", "Xml", FileBrowser.OPEN_DIALOG);
		String filePath = fb.selectFile();
		this.manager.getTextField(R.TEXT_FIELDS.BROWSE).setText(filePath);
	}

	public void cancelComponentEdition ()
	{
		log.debug("Cancelando la edicion del componente");
		if (isChangesToSave())
		{
			setCurrentAction(ACTION_EDITION_CANCEL);
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_CHANGES_NOT_SAVED),
					Popup.POPUP_CONFIRM,
					this.window.getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
		}
		else
		{
			clearComponentCreationPanel();
			setSelectedComponent(null);
		}
	}

	private void resetComponentSelection ()
	{
		this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_TEMA).setSelectedIndex(0);
	}

	private void clearComponentCreationPanel ()
	{
		for (String name : this.propertiesName)
		{
			this.manager.removeLabel(name);
		}
		this.propertiesName.clear();
		this.propertyDataUIControls.clear();
		this.window.clearComponentCreationPanel();
	}

	private void prepareInitComponentEditionPanel ()
	{
		if (isChangesToSave())
		{
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.WARN_CHANGES_NOT_SAVED),
					Popup.POPUP_CONFIRM,
					this.window.getWorkbench().getWorkPanel(),
					this
			)
			.showGui();
			setCurrentAction(ACTION_EDITION_INIT);
		}
		else
		{
			initComponentEditionPanel();
			setChangesToSave(false);
		}
	}

	private void initComponentEditionPanel ()
	{
		log.debug("Se inicializa el panel de edicion de componente");
		if (this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).hasValidSelection())
		{
			clearComponentCreationPanel();
			Propiedad property = (Propiedad) this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).getSelectedItem();
			Set<Propiedad> properties = filterProperties(property.getComponente());
			for (Propiedad propiedad : properties)
			{
				String propTypeName = propiedad.getTipoPropiedad().getDNombre();
				addDataControl(propiedad);
				this.propertiesName.add(propTypeName);
			}
			setSelectedComponent(((Propiedad) this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).getSelectedItem()).getComponente());
			this.window.arrangeComponentCreationPanel(this.propertiesName);
		}
	}

	private void addDataControl (Propiedad propiedad)
	{
		TipoPropiedad tipoPropiedad = propiedad.getTipoPropiedad();
		String propTypeName = tipoPropiedad.getDNombre();
		//		JComponent control;
		//		if (TipoPropiedadController.NOT_REGULAR_PROPERTIES.containsKey(propTypeName))
		//		{
		//			control = getNotRegularPropertyControl(propiedad);
		//		}
		//		else
		//		{
		PropertyDataControl propControl = getRegularPropertyControl(propiedad);
		//		control = propControl.getUIControl();
		this.propertyDataUIControls.put(propTypeName, propControl);
		//		}
		JLabel label = new JLabel();
		label.setName(propTypeName);
		label.setText(propTypeName);
		label.setFont(this.controlFont);
		this.manager.putLabel(propTypeName, label);
	}

	//	private JComponent getNotRegularPropertyControl (Propiedad propiedad)
	//	{
	//		TipoPropiedad tipoPropiedad = propiedad.getTipoPropiedad();
	//		String propTypeName = tipoPropiedad.getDNombre();
	//		final JButton control;
	//		control = new JButton(this.texts.get(ProjectTexts.TE_MSG_SELECT_SOURCES));
	//		control.setName(propTypeName);
	//		control.addActionListener(new SelectSourcesListener(this, this.ieController));
	//		control.setFont(this.controlFont);
	//		return control;
	//	}

	private PropertyDataControl getRegularPropertyControl (Propiedad propiedad)
	{
		PropertyDataControl dataControl = PropertyDataControlFactory.getControl(this.window.getWorkbench().getWorkPanel(), propiedad, this);
		dataControl.getUIControl().setFont(this.controlFont);
		return dataControl;
	}

	private Set<Propiedad> filterProperties (Componente component)
	{
		Set<Propiedad> result = new HashSet<Propiedad>();
		Set<Propiedad> properties = component.getPropiedades();
		String tag;
		for (Propiedad propiedad : properties)
		{
			tag = propiedad.getTipoPropiedad().getDTagXml();
			if (TipoPropiedadController.TAGS_EDITABLE_PROPERTIES.containsKey(tag))
			{
				result.add(propiedad);
			}
		}
		if (Constants.DESHABILITADO.equals(component.getMContenido()))
		{
			FuenteContenido source = this.fuenteContenidoController.findByName("WEB");
			result = this.propiedadController.filterPropertiesBySource(result, source);
		}
		return result;
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
				switch (getCurrentAction())
				{
					case ACTION_EDITION_CANCEL:
						clearComponentCreationPanel();
						setChangesToSave(false);
						setSelectedComponent(null);
						break;

					case ACTION_EDITION_INIT:
						initComponentEditionPanel();
						setChangesToSave(false);
						break;

					case ACTION_CHANGE_THEME:
						setChangesToSave(false);
						themeChanged();
						break;

					case ACTION_CHANGE_TYPE:
						setChangesToSave(false);
						componentTypeChanged();
						break;

					case ACTION_CHANGE_COMPONENT:
						setChangesToSave(false);
						componentChanged();
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
		log.debug("Cambió el valor de la propiedad de un componente");
		setChangesToSave(true);
	}

	public boolean isChangesToSave ()
	{
		return this.changesToSave || this.getIeController().isChangesToSave();
	}

	private void setChangesToSave (boolean changesToSave)
	{
		this.changesToSave = changesToSave;
		this.getIeController().setChangesToSave(changesToSave);
		this.manager.getButton(R.BUTTONS.SAVE).setEnabled(changesToSave);
	}

	/**
	 * Realiza las acciones necesarias por el cierre del editor.
	 */
	public void editorIsClosing ()
	{
		setChangesToSave(false);
		cancelComponentEdition();
		resetComponentSelection();
		this.manager.getPanel(R.PANELS.COMPONENT_CREATE_GLOBAL).setVisible(false);
		this.window.makeToolInvisible(this.manager.getButton(R.BUTTONS.SAVE));
	}

	private int getCurrentAction ()
	{
		return this.currentAction;
	}

	private void setCurrentAction (int currentAction)
	{
		this.currentAction = currentAction;
	}

	public void setSelectedComponent (Componente selectedComponent)
	{
		this.selectedComponent = selectedComponent;
	}

	public Componente getSelectedComponent ()
	{
		return this.selectedComponent;
	}

	public JComponent getUIControl (String name)
	{
		return this.propertyDataUIControls.get(name).getUIControl();
	}

	public IEGuiController getIeController ()
	{
		return this.ieController;
	}

	private static final int					ACTION_EDITION_INIT			= 0;
	private static final int					ACTION_EDITION_CANCEL		= 1;
	private static final int					ACTION_CHANGE_THEME			= 3;
	private static final int					ACTION_CHANGE_TYPE			= 4;
	private static final int					ACTION_CHANGE_COMPONENT		= 5;

	@Override
	public EditorImpl getWindow ()
	{
		return this.window;
	}

}
