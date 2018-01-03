package com.deportel.guiBuilder.gui.component;

import javax.swing.Icon;
import javax.swing.JLabel;

public class StateBar extends JLabel
{
	private static final long	serialVersionUID	= 1L;

	public String getMessage()
	{
		return super.getText();
	}

	public void setMessage(String message)
	{
		super.setText(message);
	}

	@Override
	public void setIcon (Icon icon)
	{
		super.setIcon(icon);
	}

	@Override
	public Icon getIcon ()
	{
		return super.getIcon();
	}


}
