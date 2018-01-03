package com.deportel.editor.common.core;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.deportel.guiBuilder.gui.component.StateBar;

/**
 * El Workbench representa el banco de trabajo necesario y basico para que los editores puedan trabajar. En el momento en que el contenedor
 * inicie a un editor, le deberá enviar como parámetro el workbench para que el editor tenga acceso a los componentes de interfaz de
 * usuario.
 * 
 * @author Emy
 */
public class Workbench
{
	private static final long serialVersionUID = 1L;

	public Workbench(Editor editor)
	{
		this.editor = editor;
		init();
		doSettings();
	}

	private final static Logger log = Logger.getLogger(Workbench.class);

	private final Editor		editor;

	private JPanel			  	workPanel;

	private StateBar			stateBar;

	private PowerBar			powerBar;

	/**
	 * 
	 */
	private void init()
	{
		this.powerBar = new PowerBar(this);
		this.workPanel = new JPanel();
		this.stateBar = new StateBar();
	}

	/**
	 * 
	 */
	public void uninit()
	{
	}

	/**
	 * 
	 */
	public void updateUI()
	{
		this.workPanel.updateUI();
	}

	/**
	 * 
	 */
	private void doSettings()
	{
		log.debug("Configurando el Workbench del editor: [" + this.editor.getName() + "]");
		this.powerBar.setMaximizeEnabled(false);
	}

	/**
	 * 
	 */
	public void minimize()
	{
		log.debug("Minimizando el workbench del editor " + this.editor.getName());
		this.editor.onMinimize();
	}

	/**
	 * 
	 */
	public void maximize()
	{
		log.debug("Maximizando el workbench del editor " + this.editor.getName());
	}

	/**
	 * 
	 */
	public void close()
	{
		log.debug("Cerrando el workbench del editor " + this.editor.getName());
		this.editor.onClose();
	}

	/**
	 * 
	 */
	public void restore()
	{
		log.debug("Restaurando el workbench del editor " + this.editor.getName());
	}

	public JPanel getWorkPanel()
	{
		return this.workPanel;
	}

	public void setWorkPanel(JPanel mainPanel)
	{
		this.workPanel = mainPanel;
	}

	public StateBar getStateBar()
	{
		return this.stateBar;
	}

	public void setStateBar(StateBar stateBar)
	{
		this.stateBar = stateBar;
	}

	public PowerBar getPowerBar()
	{
		return this.powerBar;
	}

}
