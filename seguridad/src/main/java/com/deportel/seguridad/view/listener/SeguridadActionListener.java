package com.deportel.seguridad.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.seguridad.view.PasswordRecoveryWindow;
import com.deportel.seguridad.view.R;

public class SeguridadActionListener implements ActionListener
{
	private static Logger		log	= Logger.getLogger(SeguridadActionListener.class);

	protected final GuiManager	manager;

	public SeguridadActionListener (GuiManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComponent component = (JComponent) e.getSource();
		log.debug("Se presiono el componente " + component.getName());
		if (R.BUTTONS.PR_CONFIRM.equals(component.getName()))
		{
			PasswordRecoveryWindow.getInstance(this.manager).recoverPassword();
		}
	}
}
