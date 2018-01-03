package com.deportel.seguridad.view;

import java.awt.Dimension;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

import com.deportel.common.Command;
import com.deportel.common.Text;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.Window;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.guiBuilder.presentation.GuiBuilderPresentation;
import com.deportel.seguridad.main.Seguridad;
import com.deportel.seguridad.model.ProjectTexts;
import com.deportel.seguridad.model.ProjectValues;

/**
 * @author Emy
 */
public class SeguridadWindow extends Window implements CallBackListener
{

	private static SeguridadWindow instance;

	public static SeguridadWindow getInstance (CallBackListener listener)
	{
		if (instance == null)
		{
			instance = new SeguridadWindow(listener);
		}
		return instance;
	}

	private SeguridadWindow(CallBackListener listener)
	{
		init();
		this.listener = listener;
	}

	private final static Logger		log					= Logger.getLogger(SeguridadWindow.class);

	private static final long		serialVersionUID	= 1L;

	private static String			XML_FILE_PATH;

	private static GuiManager		manager;

	private final CallBackListener	listener;

	private int						windowWidth;

	private int						windowHeight;

	private int						textFieldWidth;

	private int						textFieldHeight;

	private Properties				properties;

	private Text					texts;

	@Override
	protected void arrange ()
	{
		log.debug("Agregando los componentes de la interfaz a la venta");
		arrangeGeneralPanel();
		// Agrego el panel general a la ventana
		getLayout().setAutocreateContainerGaps(true);
		getLayout().setVerticalGroup
		(
				getLayout().createSequentialGroup()
				.add(manager.getPanel(R.PANELS.GENERAL))
		);
		getLayout().setHorizontalGroup
		(
				getLayout().createParallelGroup()
				.add(manager.getPanel(R.PANELS.GENERAL))
		);
	}

	private void arrangeGeneralPanel ()
	{
		log.debug("Acomodando los componentes al panel general de login");
		JComponent space = GuiUtils.space(10);
		GuiUtils.addTitledBorder(manager.getPanel(R.PANELS.GENERAL), this.texts.get(ProjectTexts.LOGIN_PANEL));
		JPanel inputPanel = new JPanel();
		GroupLayout inputLayout;
		inputPanel.setLayout(inputLayout = new GroupLayout(inputPanel));
		inputLayout.setAutocreateGaps(true);
		inputLayout.setVerticalGroup
		(
				inputLayout.createSequentialGroup()
				.add(manager.getLabel(R.LABELS.USER))
				.add(manager.getTextField(R.TEXT_FIELDS.USER))
				.add(manager.getLabel(R.LABELS.PASS))
				.add(manager.getTextField(R.TEXT_FIELDS.PASS))
				.add(space)
		);
		inputLayout.setHorizontalGroup
		(
				inputLayout.createParallelGroup()
				.add(manager.getLabel(R.LABELS.USER))
				.add(manager.getTextField(R.TEXT_FIELDS.USER))
				.add(manager.getLabel(R.LABELS.PASS))
				.add(manager.getTextField(R.TEXT_FIELDS.PASS))
				.add(space)
		);
		GroupLayout gl =(GroupLayout)manager.getPanel(R.PANELS.GENERAL).getLayout();
		space = GuiUtils.space(10);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(inputPanel)
				.add
				(
						gl.createParallelGroup(GroupLayout.CENTER)
						.add(manager.getButton(R.BUTTONS.ACEPTAR))
						.add(manager.getButton(R.BUTTONS.CANCELAR))
				)
				.add(space)
				.add(manager.getLabel(R.LABELS.FORGOT_PASS))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup(GroupLayout.CENTER)
				.add(inputPanel)
				.add
				(
						gl.createSequentialGroup()
						.add(manager.getButton(R.BUTTONS.ACEPTAR))
						.add(manager.getButton(R.BUTTONS.CANCELAR))
				)
				.add(space)
				.add(manager.getLabel(R.LABELS.FORGOT_PASS))
		);
	}

	@Override
	public void doSettings ()
	{
		manager.getButton(R.BUTTONS.ACEPTAR).addActionListener(new SeguridadActionListener(manager, this));
		manager.getButton(R.BUTTONS.CANCELAR).addActionListener(new SeguridadActionListener(manager, this));
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setPreferredSize(new Dimension(this.windowWidth, this.windowHeight));
		manager.getTextField(R.TEXT_FIELDS.USER).setPreferredSize(new Dimension(this.textFieldWidth, this.textFieldHeight));
		manager.getTextField(R.TEXT_FIELDS.PASS).setPreferredSize(new Dimension(this.textFieldWidth, this.textFieldHeight));
		setTitle(this.texts.get(ProjectTexts.APP_NAME));
	}

	/**
	 * Carga las dimensiones de la ventana desde las properties.
	 */
	private void loadDimensions ()
	{
		try
		{
			this.windowWidth = Integer.parseInt(this.properties.getProperty(ProjectValues.MAIN_WINDOW_WIDTH));
			this.windowHeight = Integer.parseInt(this.properties.getProperty(ProjectValues.MAIN_WINDOW_HEIGHT));
			this.textFieldWidth = Integer.parseInt(this.properties.getProperty(ProjectValues.TF_WIDTH));
			this.textFieldHeight = Integer.parseInt(this.properties.getProperty(ProjectValues.TF_HEIGHT));
		}
		catch (NumberFormatException e)
		{
			log.warn("Error cargando las dimensiones de la pantalla. Usando valores por default.");
		}
	}

	/**
	 * Setea la operacion que se realiza al cerrar la ventana
	 * 
	 * @since 18/02/2011
	 */
	@Override
	public void setCloseOperation()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	protected void init ()
	{
		this.properties = Seguridad.getProperties();
		this.texts = Seguridad.getTexts();
		loadDimensions();
		XML_FILE_PATH = this.properties.getProperty(ProjectValues.GUI_DEFINITION_FILE_PATH);
		manager = GuiBuilderPresentation.getManager(XML_FILE_PATH, SeguridadWindow.class);
		this.isInitilized = true;
	}

	private void cleanInput()
	{
		manager.getTextField(R.TEXT_FIELDS.USER).setText("");
		manager.getTextField(R.TEXT_FIELDS.PASS).setText("");
	}

	public void openPassRecoveryWindow ()
	{
		log.info("Abriendo la pantalla de recuperacion de password");
		PasswordRecoveryWindow recoveryWindow = PasswordRecoveryWindow.getInstance(manager);
		recoveryWindow.showDialog(getContentPane());
	}

	@Override
	public void update ()
	{

	}

	@Override
	public Object getData()
	{
		return null;
	}


	@Override
	public void receiveCallBack(String action, CallBackLauncher laucher)
	{

	}

	@Override
	public void receiveCallBack(String action, Object data)
	{
		log.debug("Se recibio el mensaje: " + data);
		if (Command.CANCEL.equals(action))
		{
			log.debug("Se presiono cancelar.");
			log.info("Cerrando la aplicacion.");
			System.exit(0);
		}
		else if (Command.ACCEPT.equals(action))
		{
			if (Command.NO_OK.equals(data))
			{
				log.debug("Se presiono aceptar con error en los datos ingresados");
				cleanInput();
			}
			else
			{
				log.debug("Se presiono aceptar. Datos verificados correctamente");
				this.listener.receiveCallBack(Command.ACCEPT, data);
				this.dispose();
			}
		}
	}

	@Override
	public void receiveCallBack(int data)
	{

	}

	@Override
	protected void uninit ()
	{

	}
}
