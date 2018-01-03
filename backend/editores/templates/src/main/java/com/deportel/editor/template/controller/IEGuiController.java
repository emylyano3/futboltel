package com.deportel.editor.template.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.deportel.common.Text;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.common.utils.Converter;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.FuenteContenidoController;
import com.deportel.componentes.controller.PropiedadController;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.editor.template.exception.TemplateException;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectTexts;
import com.deportel.editor.template.model.ProjectValues;
import com.deportel.editor.template.view.R;
import com.deportel.editor.template.view.SSWindow;
import com.deportel.guiBuilder.gui.component.FileBrowser;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * Image Edition GUI Controller
 * @author Emy
 */
public class IEGuiController implements CallBackListener
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger				log							= Logger.getLogger(IEGuiController.class);
	private final GuiManager				manager;
	private final SSWindow					window;
	private final TipoPropiedadController	tipoPropiedadController		= TipoPropiedadController.getInstance(true);
	private final FuenteContenidoController	fuenteContenidoController	= FuenteContenidoController.getInstance(true);
	private final PropiedadController		propiedadController			= PropiedadController.getInstance(true);
	private boolean							changesToSave				= false;
	private boolean							initialized					= false;
	private Componente						selectedComponent;
	private final Map<Integer, Propiedad>	imageSources				= new HashMap<Integer, Propiedad>();
	private final List<Propiedad>			imageSourcesDeleted			= new ArrayList<Propiedad>();
	private final Text						texts						= TemplateEditor.getTexts();
	private final FuenteContenido			webSource					= this.fuenteContenidoController.findByXmlTag(FuenteContenido.TAG_WEB);
	private final TipoPropiedad				imageType					= this.tipoPropiedadController.findByXmlTag(TipoPropiedad.TAG_IMAGE_SOURCE);

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static IEGuiController			instance;

	private IEGuiController(GuiManager manager)
	{
		this.manager = manager;
		this.window = SSWindow.getInstance(manager);
	}

	public static IEGuiController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new IEGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public void enableMode()
	{
		if (!isInitialized())
		{
			lookForComponentImages();
			clearImageEditionDetails();
			initImageEditionDetails();
			setInitialized(true);
		}
		this.window.createAndShowGui();
	}

	private void initImageEditionDetails()
	{
		for (Integer key : this.imageSources.keySet())
		{
			addNewImageDetailControls(key);
		}
		this.window.initImageEditionDetails(this.imageSources.keySet());
	}

	public void sourcesSelectionDone ()
	{
		log.debug("Se finalico la seleccion de sources para la imagen");
		this.window.dispose();
	}

	public void addNewImageDetailControls()
	{
		if (isComponentSelected())
		{
			Integer key = this.imageSources.size();
			this.imageSources.put(key, createNewProperty());
			addNewImageDetailControls(key);
			this.window.clearImageEditionDetails();
			this.window.initImageEditionDetails(this.imageSources.keySet());
			setChangesToSave(true);
		}
		else
		{
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.IE_WARN_SELECT_COMPONENT),
					Popup.POPUP_WARNING,
					this.window
			)
			.showGui();
		}
	}

	private void addNewImageDetailControls(Integer key)
	{
		this.manager.putLabel(LBL_WIDTH + "_" + key, new JLabel("Ancho pantalla"));
		this.manager.putLabel(LBL_HEIGHT + "_" + key, new JLabel("Alto pantalla"));
		JComboBox combo;
		combo = new JComboBox(screenDimensions);
		combo.addActionListener(new ControlListener());
		combo.setName(CBB_WIDTH + "_" + key);
		this.manager.putComboBox(CBB_WIDTH + "_" + key, combo);
		combo = new JComboBox(screenDimensions);
		combo.addActionListener(new ControlListener());
		combo.setName(CBB_HEIGHT + "_" + key);
		this.manager.putComboBox(CBB_HEIGHT + "_" + key, combo);
		Propiedad propiedad = this.imageSources.get(key);
		String alto = Integer.toString(propiedad.getNAltoPantalla());
		String ancho = Integer.toString(propiedad.getNAnchoPantalla());
		this.manager.getComboBox(CBB_WIDTH + "_" + key).setSelectedIndex(Arrays.asList(screenDimensions).indexOf(ancho));
		this.manager.getComboBox(CBB_HEIGHT + "_" + key).setSelectedIndex(Arrays.asList(screenDimensions).indexOf(alto));
		JButton button;
		button = new JButton("Explorar");
		button.setName(BTN_BROWSE + "_" + key);
		button.addActionListener(new ControlListener());
		this.manager.putButton(BTN_BROWSE + "_" + key, button);
		button = new JButton("Eliminar");
		button.setName(BTN_DELETE + "_" + key);
		button.addActionListener(new ControlListener());
		this.manager.putButton(BTN_DELETE + "_" + key, button);
		button = new JButton("Ver imagen");
		button.setName(BTN_SEE + "_" + key);
		button.addActionListener(new ControlListener());
		this.manager.putButton(BTN_SEE + "_" + key, button);
	}

	private void removeImageDetailControls (Integer key)
	{
		this.window.clearImageEditionDetails();
		removeImageDetailControlsFromManager(key);
		this.imageSourcesDeleted.add(this.imageSources.remove(key));
		this.window.initImageEditionDetails(this.imageSources.keySet());
	}

	private void removeImageDetailControlsFromManager (Integer key)
	{
		log.debug("Se eliminan los controles para la imagen " + key + " del componente actual");
		this.manager.removeLabel(LBL_WIDTH + "_" + key);
		this.manager.removeLabel(LBL_HEIGHT + "_" + key);
		this.manager.removeComboBox(CBB_WIDTH + "_" + key);
		this.manager.removeComboBox(CBB_HEIGHT + "_" + key);
		this.manager.removeButton(BTN_BROWSE + "_" + key);
		this.manager.removeButton(BTN_DELETE + "_" + key);
		this.manager.removeButton(BTN_SEE + "_" + key);
	}

	private void screenHeightChanged (Integer key)
	{
		String height = (String) this.manager.getComboBox(CBB_HEIGHT + "_" + key).getSelectedItem();
		this.imageSources.get(key).setNAltoPantalla(Converter.stringToInt(height));
	}

	private void screenWidthChanged (Integer key)
	{
		String width = (String) this.manager.getComboBox(CBB_WIDTH + "_" + key).getSelectedItem();
		this.imageSources.get(key).setNAnchoPantalla(Converter.stringToInt(width));
	}

	private Propiedad createNewProperty ()
	{
		Propiedad propiedad = new Propiedad();
		propiedad.setComponente(this.selectedComponent);
		propiedad.setTipoPropiedad(this.imageType);
		propiedad.setFuenteContenido(this.webSource);
		propiedad.setNAltoPantalla(Converter.stringToInt(screenDimensions[0]));
		propiedad.setNAnchoPantalla(Converter.stringToInt(screenDimensions[0]));
		return propiedad;
	}

	private void clearImageEditionDetails ()
	{
		this.window.clearImageEditionDetails();
	}

	private void lookForComponentImages()
	{
		log.debug("Se buscan las imagenes para el componente seleccionado");
		List<Propiedad> aux = this.propiedadController.propertiesFilteredByComponentTypeAndSource
		(
				this.selectedComponent,
				this.imageType,
				this.webSource
		);
		Collections.sort(aux);
		this.imageSources.clear();
		for (int i = 0; i < aux.size(); i++)
		{
			this.imageSources.put(i, aux.get(i));
		}
	}

	private void showImage (Integer key)
	{
		log.debug("Se abre el popup para mostrar la imagen");
		byte[] imageData = this.imageSources.get(key).getBinaryData();
		if (imageData != null)
		{
			InputStream is = new ByteArrayInputStream(imageData);
			Image image = null;
			try
			{
				image = ImageIO.read(is);
				Popup.getInstance(image).showGui();
			}
			catch (IOException e)
			{
				Popup.getInstance
				(
						this.texts.get(ProjectTexts.IE_ERROR_READING_IMAGE),
						Popup.POPUP_ERROR,
						this.window
				)
				.showGui();
				e.printStackTrace();
			}
		}
		else
		{
			Popup.getInstance
			(
					this.texts.get(ProjectTexts.IE_ERROR_IMAGE_DATA_NULL),
					Popup.POPUP_INFO,
					this.window
			)
			.showGui();
		}
	}

	private void browseForImage(Integer key)
	{
		log.debug("Se abre la ventana de navegacion de imagen");
		FileBrowser fb = new FileBrowser("Archivo de imagen", "png", FileBrowser.OPEN_DIALOG);
		String filePath = fb.selectFile();
		if (!Utils.isNullOrEmpty(filePath))
		{
			File file = new File(filePath);
			try
			{
				InputStream is = new FileInputStream(file);
				InputStream bis = new BufferedInputStream(is);
				this.imageSources.get(key).setBinaryData(Utils.isToByteArray(bis));
			}
			catch (FileNotFoundException e)
			{
				throw new TemplateException("El archivo " + filePath + " no existe.");
			}
		}
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

	public void setChangesToSave (boolean changesToSave)
	{
		this.changesToSave = changesToSave;
		this.manager.getButton(R.BUTTONS.SAVE).setEnabled(changesToSave);
	}

	public boolean isChangesToSave ()
	{
		return this.changesToSave;
	}

	public void setSelectedComponent (Componente selectedComponent)
	{
		this.selectedComponent = selectedComponent;
	}

	public Componente getSelectedComponent ()
	{
		return this.selectedComponent;
	}

	private boolean isComponentSelected ()
	{
		return this.selectedComponent != null;
	}

	public Map<Integer, Propiedad> getImageSources ()
	{
		return this.imageSources;
	}

	public List<Propiedad> getImageSourcesDeleted ()
	{
		return this.imageSourcesDeleted;
	}

	public void setInitialized (boolean initialized)
	{
		this.initialized = initialized;
	}

	public boolean isInitialized ()
	{
		return this.initialized;
	}

	public static final String	BTN_DELETE				= "IC_BTND";
	public static final String	BTN_SEE					= "IC_BTNS";
	public static final String	BTN_BROWSE				= "IC_BTNB";
	public static final String	CBB_WIDTH				= "IC_CBBW";
	public static final String	CBB_HEIGHT				= "IC_CBBH";
	public static final String	LBL_WIDTH				= "IC_LBLW";
	public static final String	LBL_HEIGHT				= "IC_LBLH";

	private static String[] 	screenDimensions 		= ((String)TemplateEditor.getProperties().get(ProjectValues.SCREENS_DIMENSIONS)).split(",");

	protected class ControlListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			JComponent source = (JComponent) e.getSource();
			if (source != null && source.getName() != null)
			{
				String key = null;
				int index = source.getName().lastIndexOf('_');
				if (index != -1)
				{
					key = source.getName().substring(index + 1, source.getName().length());
					if (source.getName().startsWith(BTN_BROWSE))
					{
						try
						{
							browseForImage(Integer.parseInt(key));
							setChangesToSave(true);
						}
						catch (TemplateException te)
						{
							Popup.getInstance
							(
									te.getMessage(),
									Popup.POPUP_ERROR,
									IEGuiController.this.window
							)
							.showGui();
						}
					}
					else if (source.getName().startsWith(BTN_SEE))
					{
						showImage(Integer.parseInt(key));
					}
					else if (source.getName().startsWith(BTN_DELETE))
					{
						removeImageDetailControls(Integer.parseInt(key));
						setChangesToSave(true);
					}
					else if (source.getName().startsWith(CBB_WIDTH))
					{
						screenWidthChanged(Integer.parseInt(key));
						setChangesToSave(true);
					}
					else if (source.getName().startsWith(CBB_HEIGHT))
					{
						screenHeightChanged(Integer.parseInt(key));
						setChangesToSave(true);
					}
				}
			}
		}
	}
}
