package com.deportel.editor.template.controller;

import java.awt.Font;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

import com.deportel.common.Constants;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Utils;
import com.deportel.editor.common.core.EditorImpl;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectTexts;
import com.deportel.editor.template.view.R;
import com.deportel.editor.template.view.TEWindow;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * Template Import GUI Controller
 * @author Emy
 */
public class TIGuiController extends BaseGuiController
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger			log				= Logger.getLogger(TIGuiController.class);
	private final TEWindow				window;
	private boolean						alreadyLoaded	= false;
	private final ComponentsImporter	importer		= new ComponentsImporter();

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static TIGuiController		instance;

	private TIGuiController(GuiManager manager)
	{
		super(manager);
		this.window = TEWindow.getInstance(null);
	}

	public static TIGuiController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new TIGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void enableMode()
	{
		log.debug("Habilitando el modo de importacion de componentes");
		this.window.makeToolInvisible(this.manager.getButton(R.BUTTONS.SAVE));
		this.manager.getPanel(R.PANELS.COMPONENT_CREATE_GLOBAL).setVisible(false);
		this.manager.getPanel(R.PANELS.IMPORT).setVisible(true);
	}

	public void loadComponentsDescriptorFile()
	{
		log.debug("Realizando la carga de componentes desde el XML");
		String filePath = this.manager.getTextField(R.TEXT_FIELDS.BROWSE).getText();
		cleanDebriefPanel();
		if (!Utils.isNullOrEmpty(filePath))
		{
			if (Constants.XML_EXTENSION.equalsIgnoreCase(Utils.getFileExtension(filePath)))
			{
				File componentsDescriptorFile = new File(filePath);
				try
				{
					Map<String, List<String>> loadResult = this.importer.processComponentsDescriptor(componentsDescriptorFile);
					setLoaded(true);
					debriefLoadInfo(loadResult);
				}
				catch (UserShowableException e)
				{
					Popup.getInstance
					(
							e.getMessage(),
							Popup.POPUP_ERROR,
							this.window.getWorkbench().getWorkPanel()
					)
					.showGui();
				}
			}
			else
			{
				Popup.getInstance
				(
						TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_INVALID_FILE_TYPE),
						Popup.POPUP_ERROR,
						this.window.getWorkbench().getWorkPanel()
				)
				.showGui();
			}
		}
		else
		{
			Popup.getInstance
			(
					TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_FILE_NOT_SELECTED),
					Popup.POPUP_INFO,
					this.window.getWorkbench().getWorkPanel()
			)
			.showGui();
		}
	}

	/**
	 * @deprecated No implementa ninguna funcionalidad
	 */
	@Deprecated
	@Override
	public void save ()
	{

	}

	public void cancelComponentsImport ()
	{
		log.debug("Cancelando la importacion de componentes");
		this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF_DETAILS).removeAll();
		this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF_DETAILS).updateUI();
		setLoaded(false);
	}

	public void importComponentsDescriptor()
	{
		log.debug("Importando los componentes desde el XML");
		String filePath = this.manager.getTextField(R.TEXT_FIELDS.BROWSE).getText();
		if (isLoaded())
		{
			if (Constants.XML_EXTENSION.equalsIgnoreCase(Utils.getFileExtension(filePath)))
			{
				File componentsDescriptorFile = new File(filePath);
				try
				{
					boolean contentComponents = this.manager.getCheckBox(R.CHECK_BOXES.PARA_CONTENIDO).isSelected();
					this.importer.importComponentsDescriptor(componentsDescriptorFile, contentComponents);
					cleanDebriefPanel();
					updateServerCache(this.window.getContainer().getServerURL());
					refreshCache();
					setLoaded(false);
					Popup.getInstance
					(
							TemplateEditor.getTexts().get(ProjectTexts.TI_MSG_LOAD_OK),
							Popup.POPUP_INFO,
							this.window.getWorkbench().getWorkPanel()
					)
					.showGui();
				}
				catch (UserShowableException e)
				{
					Popup.getInstance
					(
							e.getMessage(),
							Popup.POPUP_ERROR,
							this.window.getWorkbench().getWorkPanel()
					)
					.showGui();
				}
			}
			else
			{
				Popup.getInstance
				(
						TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_INVALID_FILE_TYPE),
						Popup.POPUP_ERROR,
						this.window.getWorkbench().getWorkPanel()
				)
				.showGui();
			}
		}
		else
		{
			Popup.getInstance
			(
					TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_FILE_NOT_LOADED),
					Popup.POPUP_INFO,
					this.window.getWorkbench().getWorkPanel()
			)
			.showGui();
		}
	}

	private void debriefLoadInfo (Map<String, List<String>> loadResult)
	{
		log.debug("Mostrando la informaciond de carga al usuario");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF_DETAILS).getLayout();
		gl.setAutocreateGaps(true);
		JLabel lblTipoComponente;
		JLabel lblCantidad;
		SequentialGroup sequentialGroup = gl.createSequentialGroup();
		ParallelGroup parallelGroup = gl.createParallelGroup();
		Font font = new Font("Arial", Font.PLAIN, 13);
		Font fontBold = new Font("Arial", Font.BOLD, 13);
		for (String tipoComponente : loadResult.keySet())
		{
			lblTipoComponente = new JLabel(tipoComponente + ": ");
			lblTipoComponente.setFont(fontBold);
			lblCantidad = new JLabel("" + loadResult.get(tipoComponente).size());
			lblCantidad.setFont(font);
			sequentialGroup.add
			(
					gl.createParallelGroup()
					.add(lblTipoComponente)
					.add(lblCantidad)
			);
			parallelGroup.add
			(
					gl.createSequentialGroup()
					.add(lblTipoComponente)
					.add(lblCantidad)
			);
		}

		gl.setVerticalGroup(sequentialGroup);
		gl.setHorizontalGroup(parallelGroup);
	}


	private void cleanDebriefPanel ()
	{
		this.manager.getPanel(R.PANELS.IMPORT_DEBRIEF_DETAILS).removeAll();
	}

	private void setLoaded (boolean loaded)
	{
		this.alreadyLoaded = loaded;
		this.manager.getButton(R.BUTTONS.CANCEL_IMPORT).setEnabled(loaded);
		this.manager.getButton(R.BUTTONS.OK_IMPORT).setEnabled(loaded);
	}

	/**
	 * Realiza las acciones necesarias por el cierre del editor.
	 */
	public void editorIsClosing ()
	{
		this.manager.getPanel(R.PANELS.IMPORT).setVisible(false);
	}

	private boolean isLoaded ()
	{
		return this.alreadyLoaded;
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
	public void notifyComponentUpdate ()
	{
		log.debug("Cambió el valor de la propiedad de un componente");
	}

	@Override
	public EditorImpl getWindow ()
	{
		return this.window;
	}
}
