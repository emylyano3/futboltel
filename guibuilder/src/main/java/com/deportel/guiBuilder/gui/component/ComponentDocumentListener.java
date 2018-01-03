package com.deportel.guiBuilder.gui.component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ComponentDocumentListener implements DocumentListener
{
	private final ComponentObserver	componentObserver;

	public ComponentDocumentListener(ComponentObserver componentObserver)
	{
		this.componentObserver = componentObserver;
	}

	@Override
	public void changedUpdate (DocumentEvent e)
	{
		this.componentObserver.notifyComponentUpdate();
	}

	@Override
	public void removeUpdate (DocumentEvent e)
	{
		this.componentObserver.notifyComponentUpdate();
	}

	@Override
	public void insertUpdate (DocumentEvent e)
	{
		this.componentObserver.notifyComponentUpdate();
	}

}
