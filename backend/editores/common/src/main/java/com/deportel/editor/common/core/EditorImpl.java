package com.deportel.editor.common.core;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.deportel.common.Command;
import com.deportel.editor.common.exception.InvalidCloseStateException;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.IconType;
import com.deportel.guiBuilder.gui.component.ImageRadioButton;
import com.deportel.guiBuilder.gui.component.StateBar;

/**
 * @author Emy
 */
public abstract class EditorImpl implements Editor
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long				serialVersionUID	= 1L;

	private final static Logger				log					= Logger.getLogger(EditorImpl.class);

	private boolean							isInitialized		= false;

	private boolean							isOpened			= false;

	private boolean							isMinimized			= false;

	public Workbench						workbench;

	protected EditorContainer				container;

	protected ImageRadioButton				trayIcon;

	protected String						moduleName;

	private final Map<String, ImageIcon>	icons				= new HashMap<String, ImageIcon>();

	// *********************************************************************************************************************
	// Implementacion de Editor
	// *********************************************************************************************************************

	@Override
	public final void init ()
	{
		this.workbench = new Workbench(this);
		arrangeComponents();
		initializeToolBar();
		doSettings();
		this.isInitialized = true;
	}

	@Override
	public void uninit ()
	{
		this.isInitialized = false;
		this.workbench = null;
	}

	@Override
	public void open ()
	{
		log.debug("Abriendo el Editor: [" + getName() + "]");
		if (!this.isInitialized)
		{
			init();
		}
		this.container.receiveEditorAction(getName(), Command.OPEN_VIEW);
		this.isOpened = true;
		this.isMinimized = false;
	}

	@Override
	public int onClose () throws InvalidCloseStateException
	{
		log.debug("Se cierra el Editor: [" + getName() + "]");
		if (canClose())
		{
			actionsOnClose();
			this.container.receiveEditorAction(this.getName(), Command.CLOSE_VIEW);
			this.isMinimized = false;
			this.isOpened = false;
			uninit();
			return CLOSED;
		}
		else
		{
			return INVALID_STATE;
		}
	}

	@Override
	public void onMinimize ()
	{
		log.debug("Se minimiza el Editor: [" + getName() + "]");
		this.container.receiveEditorAction(getName(), Command.MINIMIZE_VIEW);
		this.isMinimized = true;
		this.isOpened = false;
	}

	@Override
	public void restore ()
	{
		log.debug("Se restaura el Editor: [" + getName() + "]");
		this.container.receiveEditorAction(getName(), Command.RESTORE_VIEW);
		this.isMinimized = false;
		this.isOpened = true;
	}

	@Override
	public JPanel getPowerBar ()
	{
		return this.workbench.getPowerBar();
	}

	@Override
	public boolean isInitialized ()
	{
		return this.isInitialized;
	}

	@Override
	public boolean isOpened ()
	{
		return this.isOpened;
	}

	@Override
	public boolean isMinimized ()
	{
		return this.isMinimized;
	}

	@Override
	public JPanel getMainPanel ()
	{
		return this.workbench.getWorkPanel();
	}

	@Override
	public StateBar getStateBar ()
	{
		return this.workbench.getStateBar();
	}

	@Override
	public ImageRadioButton getTrayButton ()
	{
		if (this.trayIcon == null)
		{
			createTrayIcon();
		}
		return this.trayIcon;
	}

	/**
	 * 
	 */
	private void createTrayIcon ()
	{
		this.trayIcon = new ImageRadioButton();
		this.trayIcon.setName(getName());
		this.trayIcon.setNormalIcon(getIcon(IconType.NORMAL), getTrayIconDescription(IconType.NORMAL));
		this.trayIcon.setPressedIcon(getIcon(IconType.PRESSED), getTrayIconDescription(IconType.PRESSED));
		this.trayIcon.setRolloverIcon(getIcon(IconType.ROLL_OVER), getTrayIconDescription(IconType.ROLL_OVER));
	}

	public ImageIcon getIcon(String type)
	{
		if (this.icons.get(type) == null)
		{
			ImageIcon icon = null;
			icon = GuiUtils.loadIcon(getTrayIconName(type), this.getClass());
			if (icon != null)
			{
				this.icons.put(type, icon);
			}
		}
		return this.icons.get(type);
	}

	@Override
	public Workbench getWorkbench()
	{
		return this.workbench;
	}

	/**
	 * 
	 * @param type
	 * @return el nombre del tray icon del editor
	 */
	protected abstract String getTrayIconName (String type);

	/**
	 * 
	 * @param type
	 * @return el nombre del tray icon del editor
	 */
	protected abstract boolean canClose ();

	/**
	 * 
	 * @param type
	 * @return el nombre del tray icon del editor
	 */
	protected abstract void actionsOnClose ();

	/**
	 * 
	 * @return
	 */
	protected abstract Map<String, String> getToolsPermissionMap ();

	/**
	 * 
	 * @param type
	 * @return la descripcion del tray icon del editor
	 */
	protected abstract String getTrayIconDescription (String type);

	/**
	 * @return the container
	 */
	public EditorContainer getContainer ()
	{
		return this.container;
	}

	/**
	 * 
	 */
	protected void initializeToolBar ()
	{
		Component[] tools = getToolBar().getComponents();
		List<String> permisos = this.container.getUserPermissions(getModule());
		for (Component tool : tools)
		{
			String permision = getToolsPermissionMap().get(tool.getName());
			if (permisos.contains(permision))
			{
				tool.setVisible(tool.isVisible() && true);
			}
			else
			{
				tool.setVisible(false);
				tool.setEnabled(false);
			}
		}
	}

	/**
	 * 
	 * @param toolName
	 */
	public void makeToolVisible (JComponent tool)
	{
		List<String> userPermsInModule = this.container.getUserPermissions(getModule());
		String permNeeded = getToolsPermissionMap().get(tool.getName());
		if (userPermsInModule.contains(permNeeded))
		{
			tool.setVisible(true);
		}
	}

	/**
	 * 
	 * @param toolName
	 */
	public void makeToolInvisible (JComponent tool)
	{
		tool.setVisible(false);
	}

	@Override
	public void refreshCache ()
	{

	}

	@Override
	public Icon getMenuIcon ()
	{
		return GuiUtils.loadIcon(getMenuIconName(), this.getClass());
	}

	@Override
	public String getModuleName ()
	{
		return this.moduleName;
	}

	@Override
	public void setModuleName (String moduleName)
	{
		this.moduleName = moduleName;;
	}

	protected abstract String getMenuIconName ();
}
