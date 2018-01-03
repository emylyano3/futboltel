package com.deportel.guiBuilder.view.listener;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import org.apache.log4j.Logger;

import com.deportel.common.Command;
import com.deportel.common.action.ProjectActionStrategy;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;

public class MenuBarListener implements CallBackLauncher, Action
{
	private final Logger				log			= Logger.getLogger(this.getClass());

	private Object						fileLoaded;

	private final CallBackListener		listener;

	private final ProjectActionStrategy	strategy;

	private final Map<String, Object>	properties	= new HashMap<String, Object>();

	public MenuBarListener (CallBackListener listener, ProjectActionStrategy strategy)
	{
		this.listener = listener;
		this.strategy = strategy;
	}

	public void actionPerformed (ActionEvent event)
	{
		if (event.getActionCommand().equalsIgnoreCase(Command.OPEN_PROJECT))
		{
			this.log.debug("Abriendo el proyecto");
			this.fileLoaded = this.strategy.openProject();
			if (this.fileLoaded != null)
			{
				doCallBack();
			}
		}
		else if (event.getActionCommand().equalsIgnoreCase(Command.SAVE_PROJECT))
		{
			this.log.debug("Guardando el proyecto");
			this.strategy.saveProject(this.listener.getData());
		}
		else if (event.getActionCommand().equalsIgnoreCase(Command.OPEN_VIEW))
		{
			this.log.debug("Abriendo la vista");
			// TODO Corregir el parametro
			this.strategy.openView(this.listener.getData());
		}
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.gui.utils.PopupListener#popupCallBack(int)
	 */
	public void doCallBack ()
	{
		this.listener.receiveCallBack(Command.OPEN_PROJECT, this);
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.gui.utils.CallBackLauncher#getData()
	 */
	public Object getData ()
	{
		this.log.debug("getData");
		return this.fileLoaded;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener (PropertyChangeListener listener)
	{
		this.log.debug("addPropertyChangeListener");
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#getValue(java.lang.String)
	 */
	public Object getValue (String key)
	{
		this.log.debug("Key: " + key);
		return this.properties.get(key);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#isEnabled()
	 */
	public boolean isEnabled ()
	{
		this.log.debug("isEnabled");
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#putValue(java.lang.String, java.lang.Object)
	 */
	public void putValue (String key, Object value)
	{
		this.log.debug("putValue");
		this.properties.put(key, value);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener (PropertyChangeListener listener)
	{
		this.log.debug("removePropertyChangeListener");
	}

	/* (non-Javadoc)
	 * @see javax.swing.Action#setEnabled(boolean)
	 */
	public void setEnabled (boolean b)
	{
		this.log.debug("isEnabled");
	}
}
