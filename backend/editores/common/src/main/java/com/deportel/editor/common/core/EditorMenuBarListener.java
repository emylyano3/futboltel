package com.deportel.editor.common.core;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import org.apache.log4j.Logger;

import com.deportel.common.Command;
import com.deportel.common.action.ProjectActionStrategy;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.seguridad.exception.InvalidUserAccessException;

public class EditorMenuBarListener implements Action
{
	private final static Logger 		log			= Logger.getLogger(EditorMenuBarListener.class);

	private final EditorContainer		container;

	private final Editor				editor;

	private final ProjectActionStrategy	strategy;

	private final Map<String, Object>	properties	= new HashMap<String, Object>();

	public EditorMenuBarListener(EditorContainer container, Editor editor, ProjectActionStrategy strategy)
	{
		this.container = container;
		this.strategy = strategy;
		this.editor = editor;
	}

	@Override
	public void actionPerformed (ActionEvent event)
	{
		if (Command.OPEN_PROJECT.equalsIgnoreCase(event.getActionCommand()))
		{
			log.debug("Abriendo el proyecto");
			//TODO Add code here
		}
		else if (Command.SAVE_PROJECT.equalsIgnoreCase(event.getActionCommand()))
		{
			log.debug("Guardando el proyecto");
			//TODO Add code here
		}
		else if (Command.OPEN_VIEW.equalsIgnoreCase(event.getActionCommand()))
		{
			log.debug("Abriendo la vista");
			openView();
		}
	}

	/**
	 * 
	 */
	private void openView ()
	{
		try
		{
			this.container.challengeUserAccess(this.editor.getModule());
			this.strategy.openView(null);
		}
		catch (InvalidUserAccessException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Action#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener (PropertyChangeListener listener)
	{
		log.debug("addPropertyChangeListener");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Action#getValue(java.lang.String)
	 */
	@Override
	public Object getValue (String key)
	{
		log.debug("Key: " + key);
		return this.properties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled ()
	{
		log.debug("isEnabled");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Action#putValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putValue (String key, Object value)
	{
		log.debug("putValue");
		this.properties.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Action#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener (PropertyChangeListener listener)
	{
		log.debug("removePropertyChangeListener");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Action#setEnabled(boolean)
	 */
	@Override
	public void setEnabled (boolean b)
	{
		log.debug("isEnabled");
	}
}
