package com.deportel.seguridad.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.deportel.administracion.modelo.Usuario;
import com.deportel.common.Command;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.seguridad.exception.InvalidUserCredentialsException;
import com.deportel.seguridad.main.SeguridadFacade;

/**
 * @author Emy
 */
public class SeguridadActionListener implements ActionListener, CallBackLauncher, CallBackListener
{
	private final GuiManager		manager;

	private final CallBackListener	listener;

	public SeguridadActionListener(GuiManager manager, CallBackListener listener)
	{
		this.listener = listener;
		this.manager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		String userName = (this.manager.getTextField(R.TEXT_FIELDS.USER)).getText();
		String password = (this.manager.getTextField(R.TEXT_FIELDS.PASS)).getText();
		if (event.getActionCommand().equalsIgnoreCase(Command.ACCEPT))
		{
			try
			{
				Usuario user = SeguridadFacade.getInstance().authenticateUser(userName, password);
				this.listener.receiveCallBack(Command.ACCEPT, user);
			}
			catch (InvalidUserCredentialsException e)
			{
				Popup.getInstance
				(
						e.getMessage(),
						Popup.POPUP_ERROR,
						SeguridadWindow.getInstance(this.listener),
						this
				)
				.showGui();
				this.listener.receiveCallBack(Command.ACCEPT, Command.NO_OK);
			}
		}
		else if (event.getActionCommand().equalsIgnoreCase(Command.CANCEL))
		{
			this.listener.receiveCallBack(Command.CANCEL, Command.NO_OK);
		}
	}

	@Override
	public void receiveCallBack(String action, CallBackLauncher laucher)
	{

	}

	@Override
	public Object getData ()
	{
		return null;
	}


	@Override
	public void doCallBack()
	{

	}

	@Override
	public void receiveCallBack(String action, Object data)
	{

	}

	@Override
	public void receiveCallBack(int data)
	{

	}
}
