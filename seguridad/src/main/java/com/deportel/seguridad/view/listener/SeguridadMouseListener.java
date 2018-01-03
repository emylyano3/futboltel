package com.deportel.seguridad.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.seguridad.view.R;
import com.deportel.seguridad.view.SeguridadWindow;

public class SeguridadMouseListener implements MouseListener
{
	private static Logger		log	= Logger.getLogger(SeguridadMouseListener.class);

	protected final GuiManager	manager;

	public SeguridadMouseListener (GuiManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void mouseClicked (MouseEvent e)
	{
	}

	@Override
	public void mousePressed (MouseEvent e)
	{
	}

	@Override
	public void mouseReleased (MouseEvent e)
	{
		JComponent component = (JComponent) e.getSource();
		log.debug("Se presiono el componente " + component.getName());
		if (R.LABELS.FORGOT_PASS.equals(component.getName()))
		{
			SeguridadWindow.getInstance(null).openPassRecoveryWindow();
		}
	}

	@Override
	public void mouseEntered (MouseEvent e)
	{
	}

	@Override
	public void mouseExited (MouseEvent e)
	{
	}
}
