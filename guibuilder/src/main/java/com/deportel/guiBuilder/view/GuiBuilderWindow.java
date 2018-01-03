package com.deportel.guiBuilder.view;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

import com.deportel.common.Command;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.guiBuilder.gui.component.Window;
import com.deportel.guiBuilder.main.GuiBuilder;
import com.deportel.guiBuilder.model.GuiBuilderAction;
import com.deportel.guiBuilder.model.ProjectValues;
import com.deportel.guiBuilder.view.listener.ButtonListener;
import com.deportel.guiBuilder.view.listener.MenuBarListener;

/**
 * Ventana principal del GuiBuilder
 * @author Emy
 * @since 22/01/2011
 */
public class GuiBuilderWindow extends Window implements CallBackListener
{
	private static GuiBuilderWindow instance;

	public static GuiBuilderWindow getInstance ()
	{
		if (instance == null)
		{
			instance = new GuiBuilderWindow();
		}
		return instance;
	}

	private GuiBuilderWindow ()
	{

	}

	private static final long	serialVersionUID	= 1L;

	private final Properties			properties			= GuiBuilder.getProperties();

	private JTextField			tfProjectPath;
	private JTextField			tfXmlName;
	private JTextField			tfPackageName;

	private JLabel				labProjectPath;
	private JLabel				labXmlName;
	private JLabel				labPackageName;

	private JButton				butGenerate;

	private JPanel				inputPanel;
	private JPanel				buttonPanel;

	private JMenuBar			menuBar;
	private JMenu				mProject;
	private JMenuItem			miSave;
	private JMenuItem			miOpen;

	private static Logger		log					= Logger.getLogger(GuiBuilderWindow.class);

	private int					width				= ProjectValues.DEFAULT_WIDTH;
	private int					height				= ProjectValues.DEFAULT_HEIGHT;


	@Override
	protected void arrange()
	{
		log.debug("Acomodando los componentes en pantalla");
		getLayout().setAutocreateContainerGaps(true);
		getLayout().setAutocreateGaps(true);

		getLayout().setHorizontalGroup
		(
				getLayout().createParallelGroup()
				.add(this.inputPanel)
				.add(this.buttonPanel)
		);

		getLayout().setVerticalGroup
		(
				getLayout().createSequentialGroup()
				.add(this.inputPanel)
				.add(this.buttonPanel)
		);
		arrangeButtonPanel();
		arrangeInputPanel();
	}

	@Override
	protected void init()
	{
		setResizable(false);

		this.tfProjectPath = new JTextField();
		this.tfProjectPath.setToolTipText("Ingresar la ruta que apunta a la carpeta principap para el cual desea generar la interfaz");
		this.tfXmlName = new JTextField();
		this.tfXmlName.setToolTipText("Ingresar el nombre del XML que se va usar para crear la interfaz");
		this.tfPackageName = new JTextField();
		this.tfPackageName.setToolTipText("Ingresar el nombre del paquete al que pertenecera la interfaz generada");

		this.labProjectPath = new JLabel("Path del proyecto");
		this.labXmlName = new JLabel("Archivo XML que define la GUI");
		this.labPackageName = new JLabel("Nombre del paquete");

		this.inputPanel = new JPanel();
		this.buttonPanel = new JPanel();

		this.butGenerate = new JButton("Generar");
		this.butGenerate.addActionListener(new ButtonListener(this.tfProjectPath, this.tfXmlName, this.tfPackageName));

		initMenuBar();
		loadDimensions();
	}

	/**
	 * Carga las dimensiones de la ventana desde las properties.
	 */
	private void loadDimensions ()
	{
		try
		{
			this.width = Integer.parseInt(this.properties.getProperty(ProjectValues.WIDTH));
			this.height = Integer.parseInt(this.properties.getProperty(ProjectValues.HEIGHT));
		}
		catch (NumberFormatException e)
		{
			log.error("Error cargando las dimensiones de la pantalla. Usando valores por default.");
		}
	}

	/**
	 * Inicializa la barra de menu de la aplicacion.
	 * 
	 * @since 24/02/2011
	 */
	private void initMenuBar ()
	{
		this.menuBar = new JMenuBar();
		this.mProject = new JMenu("Proyecto");
		this.miSave = new JMenuItem("Guardar Ctrl+S");
		this.miOpen = new JMenuItem("Abrir Ctrl+O");

		this.menuBar.add(this.mProject);

		this.mProject.add(this.miOpen);
		this.mProject.add(this.miSave);

		GuiBuilderAction gba = new GuiBuilderAction();

		MenuBarListener openListener = new MenuBarListener(this, gba);
		MenuBarListener saveListener = new MenuBarListener(this, gba);

		this.miOpen.addActionListener(openListener);
		this.miOpen.setActionCommand(Command.OPEN_PROJECT);
		this.miSave.addActionListener(saveListener);
		this.miSave.setActionCommand(Command.SAVE_PROJECT);

		this.miOpen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control O"), Command.OPEN_PROJECT);
		this.miSave.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), Command.SAVE_PROJECT);

		this.miOpen.getActionMap().put(Command.OPEN_PROJECT, openListener);
		this.miSave.getActionMap().put(Command.SAVE_PROJECT, saveListener);

		this.setMenuBar(this.menuBar);
	}

	/**
	 * Ordena el panel de botones según el diseño
	 * que se haya elegido.
	 * 
	 * @since 10/01/2011
	 */
	private void arrangeButtonPanel ()
	{
		GroupLayout buttonPanelLayout = new GroupLayout (this.buttonPanel);
		this.buttonPanel.setLayout(buttonPanelLayout);

		buttonPanelLayout.setAutocreateContainerGaps(true);
		buttonPanelLayout.setAutocreateGaps(true);

		buttonPanelLayout.setHorizontalGroup
		(
				buttonPanelLayout.createParallelGroup()
				.add(this.butGenerate)
		);

		buttonPanelLayout.setVerticalGroup
		(
				buttonPanelLayout.createSequentialGroup()
				.add(this.butGenerate)
		);
	}

	/**
	 * Acomoda los componentes del panel de ingreso
	 * de datos.
	 * 
	 * @since 10/01/2011
	 */
	private void arrangeInputPanel ()
	{
		GroupLayout inputPanelLayout = new GroupLayout (this.inputPanel);
		this.inputPanel.setLayout(inputPanelLayout);

		inputPanelLayout.setAutocreateContainerGaps(true);
		inputPanelLayout.setAutocreateGaps(true);

		inputPanelLayout.setHorizontalGroup
		(
				inputPanelLayout.createParallelGroup()
				.add(this.labXmlName)
				.add(this.tfXmlName)
				.add(this.labPackageName)
				.add(this.tfPackageName)
				.add(this.labProjectPath)
				.add(this.tfProjectPath)
		);
		inputPanelLayout.setVerticalGroup
		(
				inputPanelLayout.createSequentialGroup()
				.add(this.labXmlName)
				.add(this.tfXmlName)
				.add(this.labPackageName)
				.add(this.tfPackageName)
				.add(this.labProjectPath)
				.add(this.tfProjectPath)
		);
	}

	/**
	 * Recibe un map de strings de los cuales obtiene los
	 * valores para setear el texto de cada uno de los
	 * componentes de interfaz a traves de los cuales el
	 * usuario ingresa los datos necesarios para correr la
	 * herramienta.
	 * @param values Los valores del proyecto
	 * 
	 * @since 24/01/2011
	 */
	private void setProject (Properties values)
	{
		this.tfPackageName.setText(values.getProperty(ProjectValues.PACKAGE_NAME));
		this.tfProjectPath.setText(values.getProperty(ProjectValues.PROJECT_PATH));
		this.tfXmlName.setText(values.getProperty(ProjectValues.GUI_DEFINITION_NAME));
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.gui.Window#doSettings()
	 */
	@Override
	public void doSettings ()
	{
		this.tfPackageName.setMinimumSize(new Dimension(150, 20));
		this.tfPackageName.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		this.tfProjectPath.setMinimumSize(new Dimension(150, 20));
		this.tfProjectPath.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		this.tfXmlName.setMinimumSize(new Dimension(150, 20));
		this.tfXmlName.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		this.setMinimumSize(new Dimension(this.width, this.height));
		this.setTitle(GuiBuilder.getName());
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.gui.components.Window#update()
	 */
	@Override
	public void update ()
	{

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.deportel.futboltel.common.gui.utils.CallBackListener#callBack(int)
	 */
	@Override
	public void receiveCallBack(String action, CallBackLauncher launcher)
	{
		setProject((Properties) launcher.getData());
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.gui.utils.CallBackListener#getData()
	 */
	@Override
	public Object getData ()
	{
		Map <String, String> values = new HashMap <String, String> ();
		values.put(ProjectValues.GUI_DEFINITION_NAME, this.tfXmlName.getText());
		values.put(ProjectValues.PACKAGE_NAME, this.tfPackageName.getText());
		values.put(ProjectValues.PROJECT_PATH, this.tfProjectPath.getText().replace('\\', '/'));
		return values;
	}

	@Override
	public void receiveCallBack(String action, Object data)
	{

	}

	@Override
	public void receiveCallBack(int data)
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.Window#uninit()
	 */
	@Override
	protected void uninit ()
	{

	}

}
