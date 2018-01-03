package com.deportel.futboltel.editor.contenedor.view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

import com.deportel.administracion.controller.TipoPermisoController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.common.Command;
import com.deportel.common.Text;
import com.deportel.common.utils.Properties;
import com.deportel.editor.common.core.Editor;
import com.deportel.editor.common.core.EditorContainer;
import com.deportel.editor.common.core.EditorMenuBarListener;
import com.deportel.futboltel.editor.contenedor.main.Contenedor;
import com.deportel.futboltel.editor.contenedor.model.EditorGenericActionStrategy;
import com.deportel.futboltel.editor.contenedor.model.ProjectTexts;
import com.deportel.futboltel.editor.contenedor.model.ProjectValues;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.ImageButton;
import com.deportel.guiBuilder.gui.component.ImageButtonGroup;
import com.deportel.guiBuilder.gui.component.Window;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.guiBuilder.presentation.GuiBuilderPresentation;
import com.deportel.seguridad.exception.InvalidUserAccessException;
import com.deportel.seguridad.main.SeguridadFacade;

/**
 * Clase singleton que representa la ventana principal del editor.
 * Esta ventana contiene a todos los editores exitentes, es decir que
 * se presenta como un "integrador" de editores, por lo tanto se puede
 * ver como un IEE (Entorno de Edicion Integrado)
 * 
 * @author Emy
 * @since 10/01/2011
 */
public class ContenedorWindow extends Window implements EditorContainer, ActionListener
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static ContenedorWindow getInstance (String name)
	{
		if (instance == null)
		{
			instance = new ContenedorWindow(name);
		}
		return instance;
	}

	public static ContenedorWindow getInstance ()
	{
		return getInstance("");
	}

	private ContenedorWindow (String name)
	{
		setTitle(name);
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long				serialVersionUID	= 1L;

	private static Logger					log					= Logger.getLogger(ContenedorWindow.class);

	private final Properties				properties			= Contenedor.getProperties();

	private final String					XML_FILE_PATH		= this.properties.getProperty(ProjectValues.GUI_DEFINITION_FILE_PATH);

	private final GuiManager				manager				= GuiBuilderPresentation.getManager(this.XML_FILE_PATH, ContenedorWindow.class);

	private final Text						texts				= Contenedor.getTexts();

	private static int						WINDOW_PREF_WIDTH;

	private static int						WINDOW_PREF_HEIGHT;

	private static int						WINDOW_MIN_WIDTH;

	private static int						WINDOW_MIN_HEIGHT;

	private static int						MAIN_PANEL_MIN_WIDTH;

	private static int						MAIN_PANEL_MIN_HEIGHT;

	private static int						BAR_HEIGHT;

	private final ImageButtonGroup			trayButtonGroup		= new ImageButtonGroup();

	private final Map<ImageButton, Editor>	editorButtonMap		= new HashMap<ImageButton, Editor>();

	private static ContenedorWindow			instance;

	/**
	 * Contiene los editores que utilizaran todos los editores
	 * abiertos desde el contenedor
	 * 
	 * @see Editor
	 */
	private final Map<String, Editor>		editors				= new HashMap<String, Editor>();

	private final ArrayList<String>			currentEditorMenus	= new ArrayList<String>();

	/**
	 * Hace referencia al editor que se esta utilizando actualmente
	 */
	private Editor							currentEditor;

	// *********************************************************************************************************************
	// Implementacion de SplashWindow
	// *********************************************************************************************************************

	@Override
	protected void arrange ()
	{
		GroupLayout gl = getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.TOP_PANEL_BAR))
				.add(this.manager.getSeparator(R.SEPARATORS.TOP_BAR_SEPARATOR))
				.add(this.manager.getPanel(R.PANELS.MAIN_PANEL))
				.add(this.manager.getSeparator(R.SEPARATORS.BOTTOM_BAR_SEPARATOR))
				.add(this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.TOP_PANEL_BAR))
				.add(this.manager.getSeparator(R.SEPARATORS.TOP_BAR_SEPARATOR))
				.add(this.manager.getPanel(R.PANELS.MAIN_PANEL))
				.add(this.manager.getSeparator(R.SEPARATORS.BOTTOM_BAR_SEPARATOR))
				.add(this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR))
		);
		this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR).add(getTrayBar(), BorderLayout.LINE_END);
	}

	@Override
	public void init ()
	{
		loadDimensions();
	}

	@Override
	public void update ()
	{

	}

	@Override
	public void doSettings ()
	{
		log.debug("Se efectua el doSettings de la pantalla del Contenedor");
		setPreferredSize(new Dimension(WINDOW_PREF_WIDTH, WINDOW_PREF_HEIGHT));
		setMinimumSize(new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT));

		this.manager.getPanel(R.PANELS.EDITORS_TRAY_BAR).setLayout(new FlowLayout());
		this.manager.getPanel(R.PANELS.TOP_PANEL_BAR).setLayout(new BorderLayout());
		this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR).setLayout(new BorderLayout());

		this.manager.getPanel(R.PANELS.TOP_PANEL_BAR).setMaximumSize(new Dimension(Short.MAX_VALUE, BAR_HEIGHT));
		this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR).setMaximumSize(new Dimension(Short.MAX_VALUE, BAR_HEIGHT));
		this.manager.getPanel(R.PANELS.MAIN_PANEL).setMinimumSize(new Dimension(MAIN_PANEL_MIN_WIDTH, MAIN_PANEL_MIN_HEIGHT));

		this.manager.getSeparator(R.SEPARATORS.TOP_BAR_SEPARATOR).setMaximumSize
		(
				new Dimension(Short.MAX_VALUE, 5)
		);
		this.manager.getSeparator(R.SEPARATORS.BOTTOM_BAR_SEPARATOR).setMaximumSize
		(
				new Dimension(Short.MAX_VALUE, 5)
		);
		createButtonGroupForMenu();
		try
		{
			setMenuBar(this.manager.getMenuBar(R.MENU_BARS.PRINCIPAL));
			setTray();
		}
		catch (AWTException e)
		{
			log.warn("No se pudo agregar el TrayIcon");
		}
	}

	@Override
	public void setCloseOperation ()
	{
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	// *********************************************************************************************************************
	// Metodos Privados
	// *********************************************************************************************************************

	/**
	 * Crea el tray de la aplicacion.
	 * 
	 * @throws AWTException
	 * @since 18/02/2011
	 * @see {@link SystemTray}
	 */
	private void setTray () throws AWTException
	{
		if (SystemTray.isSupported())
		{
			log.debug("El servicio de tray icon es soportado por el sistema, se procede a setear el mismo");
			final PopupMenu popup = createPopUpMenu();
			final TrayIcon trayIcon = new TrayIcon
			(
					GuiUtils.createImage
					(
							this.properties.getProperty(ProjectValues.TRAY_ICON_PATH),
							this.properties.getProperty(ProjectValues.TRAY_ICON_DESCRIPTION),
							ContenedorWindow.class
					)
			);
			final SystemTray tray = SystemTray.getSystemTray();
			trayIcon.setImageAutoSize(true);
			trayIcon.setPopupMenu(popup);
			tray.add(trayIcon);
		}
		else
		{
			log.warn("System Tray no soportado");
		}
	}

	/**
	 * 
	 */
	private void createButtonGroupForMenu ()
	{
		//		ButtonGroup group = new ButtonGroup();
		//		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.BLACK));
		//		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.BLUE));
		//		group.add(this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.SKY));
		//		this.manager.getRadioButtonMenuItem(R.RADIO_BUTTON_MENU_ITEMS.BLACK).setSelected(true);
	}

	/**
	 * Realiza un merge entre la barra de menus del contenerdor y la
	 * barra de menu del editor. El cometido es agregar funcionalidad
	 * especifica del editor a la barra de menu del conenedor sin
	 * perder la funcionalidad existente original.
	 * 
	 * @param name
	 *            El nombre del editor
	 */
	protected void mergeMenuBars (Editor editor)
	{
		JMenuBar menuBar = editor.getMenuBar();
		if (menuBar != null)
		{
			for (int i = 0 ; i < menuBar.getMenuCount() ; i++)
			{
				this.currentEditorMenus.add(menuBar.getMenu(i).getName());
				getJMenuBar().add(menuBar.getMenu(i));
			}
		}
	}

	/**
	 * Quita de la menu bar del contenedor, los menues que
	 * corresponden al editor.
	 * 
	 * @param name
	 *            El nombre del editor
	 */
	protected void removeMenuBars (String name)
	{
		Editor editor = this.editors.get(name);
		for (int i = 0; i < getJMenuBar().getMenuCount(); i++)
		{
			for (String string : this.currentEditorMenus)
			{
				if (getJMenuBar().getMenu(i).getName().equalsIgnoreCase(string))
				{
					editor.getMenuBar().add(getJMenuBar().getMenu(i));
				}
			}
		}
		this.currentEditorMenus.clear();
	}

	/**
	 * Carga las dimensiones de la ventana desde las properties.
	 */
	private void loadDimensions ()
	{
		try
		{
			WINDOW_PREF_WIDTH = this.properties.getPropertyInt(ProjectValues.PREFERRED_WIDTH, ProjectValues.DEFAULT_WIDTH);
			WINDOW_PREF_HEIGHT = this.properties.getPropertyInt(ProjectValues.PREFERRED_HEIGHT, ProjectValues.DEFAULT_HEIGHT);
			WINDOW_MIN_WIDTH = this.properties.getPropertyInt(ProjectValues.MINIMUM_WIDTH, ProjectValues.DEFAULT_WIDTH);
			WINDOW_MIN_HEIGHT = this.properties.getPropertyInt(ProjectValues.MINIMUM_HEIGHT, ProjectValues.DEFAULT_HEIGHT);
			BAR_HEIGHT = this.properties.getPropertyInt(ProjectValues.BAR_HEIGHT, ProjectValues.DEFAULT_BAR_HEIGHT);
			MAIN_PANEL_MIN_WIDTH = this.properties.getPropertyInt(ProjectValues.MAIN_PANEL_MIN_WIDTH, ProjectValues.DEFAULT_MAIN_PANEL_MIN_WIDTH);
			MAIN_PANEL_MIN_HEIGHT = this.properties.getPropertyInt(ProjectValues.MAIN_PANEL_MIN_HEIGHT, ProjectValues.DEFAULT_MAIN_PANEL_MIN_HEIGHT);
		}
		catch (NumberFormatException e)
		{
			log.warn("Error cargando las dimensiones de la pantalla. Usando valores por default.");
		}
	}

	/**
	 * Remueve el editor bajo el nombre enviado por parametro.
	 * 
	 * @param name El nombre del editor que se quiere remover
	 * @since 18/03/2011
	 */
	private void closeEditor (String name)
	{
		Editor editor = this.editors.get(name);
		this.manager.getSeparator(R.SEPARATORS.TOP_BAR_SEPARATOR).setVisible(false);
		this.manager.getSeparator(R.SEPARATORS.BOTTOM_BAR_SEPARATOR).setVisible(false);
		if (editor != null && editor == this.currentEditor)
		{
			log.info("Cerrando el editor: [" + editor.getName() + "]");
			removeEditor(editor);
			getTrayBar().remove(this.currentEditor.getTrayButton());
			setMenuBar(this.manager.getMenuBar(R.MENU_BARS.PRINCIPAL));
			validate();
		}
		this.currentEditor = null;
	}

	/**
	 * Crea el popup menu que se mostrara al hacer click en el icono
	 * presente en la barra del sistema del computador.
	 * 
	 * @return {@link PopupMenu} el menu de popup.
	 * @since 18/03/2011
	 * @see SystemTray
	 */
	private PopupMenu createPopUpMenu ()
	{
		log.debug("Se crea el popup menu para el tray icon");
		PopupMenu popup = new PopupMenu();
		MenuItem exit = new MenuItem(this.texts.get(ProjectTexts.QUIT));
		MenuItem open = new MenuItem(this.texts.get(ProjectTexts.OPEN));
		open.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent e)
			{
				instance.setVisible(true);
				instance.update();
			}
		});
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent e)
			{
				safeExit();
			}
		});
		popup.add(open);
		popup.add(exit);
		return popup;
	}

	public void safeExit ()
	{
		if (this.currentEditor == null || this.currentEditor.onClose() == Editor.CLOSED)
		{
			System.exit(NORMAL);
		}
	}

	/**
	 * 
	 * @param editor
	 */
	public Modulo addEditor (Editor editor, String menuItem)
	{
		log.info("Inicializo el editor: [" + editor.getName() + "]");
		EditorMenuBarListener administracionEditorListener = new EditorMenuBarListener(this, editor, new EditorGenericActionStrategy(editor));
		this.manager.getMenuItem(menuItem).addActionListener(administracionEditorListener);
		this.manager.getMenuItem(menuItem).setActionCommand(Command.OPEN_VIEW);
		initTrayIcon(editor);
		putEditor(editor);
		return editor.getModule();
	}

	/**
	 * @since 18/03/2011
	 */
	private void minimizeEditor (String name)
	{
		log.info("Se minimiza el editor: [" + name + "]");
		Editor editor = this.editors.get(name);
		if (editor != null && editor == this.currentEditor)
		{
			removeEditor(editor);
			setMenuBar(this.manager.getMenuBar(R.MENU_BARS.PRINCIPAL));
			this.trayButtonGroup.unSelectAll();
		}
		this.currentEditor = null;
		repaint();
	}

	/**
	 * 
	 * @param name
	 */
	private void restoreEditor (String name)
	{
		log.info("Se restaura el editor: [" + name + "]");
		Editor editor = this.editors.get(name);
		this.manager.getSeparator(R.SEPARATORS.TOP_BAR_SEPARATOR).setVisible(true);
		this.manager.getSeparator(R.SEPARATORS.BOTTOM_BAR_SEPARATOR).setVisible(true);
		if (editor != null && this.currentEditor != editor)
		{
			if (this.currentEditor != null)
			{
				this.currentEditor.onMinimize();
			}
			setCurrentEditor(editor);
		}
		repaint();
	}

	/**
	 * Pone disponible para el usuario el editor recibido por
	 * parametro.
	 * 
	 * @param editor {@link Editor} a ser puesto como disponible.
	 * @since 18/03/2011
	 */
	private void setCurrentEditor (Editor editor)
	{
		log.debug("Seteando el editor: [" + editor.getName() + "] como el actual y disponible");
		this.currentEditor = editor;
		setTopBars(this.currentEditor);
		setMainPanel(this.currentEditor);
		setBottomBars(this.currentEditor);
		setTrayIcon(this.currentEditor);
		mergeMenuBars(this.currentEditor);
	}

	/**
	 * 
	 */
	private void setTopBars (Editor editor)
	{
		JPanel topBarsPanel = this.manager.getPanel(R.PANELS.TOP_PANEL_BAR);
		if (editor.getToolBar() != null)
		{
			topBarsPanel.add(editor.getToolBar(), BorderLayout.LINE_START);
		}
		topBarsPanel.add(editor.getPowerBar(), BorderLayout.LINE_END);
		topBarsPanel.setMaximumSize
		(
				new Dimension
				(
						Short.MAX_VALUE,
						this.properties.getPropertyInt(ProjectValues.TOP_BAR_HEIGHT, 30)
				)
		);
		topBarsPanel.setMinimumSize
		(
				new Dimension
				(
						this.properties.getPropertyInt(ProjectValues.TOP_BAR_WIDTH, 100),
						this.properties.getPropertyInt(ProjectValues.TOP_BAR_HEIGHT, 30)
				)
		);
		topBarsPanel.repaint();
	}

	/**
	 * 
	 */
	private void setBottomBars (Editor editor)
	{
		JPanel bottomBarsPanel = this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR);
		bottomBarsPanel.add(editor.getStateBar(), BorderLayout.LINE_START);
		bottomBarsPanel.setMaximumSize
		(
				new Dimension
				(
						Short.MAX_VALUE,
						this.properties.getPropertyInt(ProjectValues.BOTTOM_BAR_HEIGHT, 30)
				)
		);
		bottomBarsPanel.setMinimumSize
		(
				new Dimension
				(
						this.properties.getPropertyInt(ProjectValues.BOTTOM_BAR_WIDTH, 100),
						this.properties.getPropertyInt(ProjectValues.BOTTOM_BAR_HEIGHT, 30)
				)
		);
		bottomBarsPanel.repaint();
	}

	/**
	 * 
	 */
	private void setMainPanel (Editor editor)
	{
		JPanel mainPanel = this.manager.getPanel(R.PANELS.MAIN_PANEL);
		GroupLayout gl = (GroupLayout) mainPanel.getLayout();
		gl.setAutocreateContainerGaps(true);

		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(editor.getMainPanel())
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(editor.getMainPanel())
		);
		mainPanel.setMaximumSize
		(
				new Dimension
				(
						Short.MAX_VALUE,
						Short.MAX_VALUE
				)
		);
	}

	/**
	 * Remueve el editor actual
	 */
	private void removeEditor (Editor editor)
	{
		if (editor != null)
		{
			log.info("Se procede a remover el editor: [" + editor.getName() + "]");
			this.manager.getPanel(R.PANELS.MAIN_PANEL).removeAll();
			this.manager.getPanel(R.PANELS.TOP_PANEL_BAR).removeAll();
			this.manager.getPanel(R.PANELS.BOTTOM_PANEL_BAR).remove(editor.getStateBar());
			removeMenuBars(editor.getName());
		}
	}

	// *********************************************************************************************************************
	// Implementacion de EditorContainer
	// *********************************************************************************************************************

	@Override
	public void openEditor (String editorName)
	{
		log.info("Seteando el editor: [" + editorName + "] como el actual y disponible");
		Editor editor = getEditor(editorName);
		this.manager.getSeparator(R.SEPARATORS.TOP_BAR_SEPARATOR).setVisible(true);
		this.manager.getSeparator(R.SEPARATORS.BOTTOM_BAR_SEPARATOR).setVisible(true);
		if (this.currentEditor != editor && !editor.isMinimized())
		{
			if (this.currentEditor != null)
			{
				removeMenuBars(this.currentEditor.getName());
				this.currentEditor.onMinimize();
			}
			setCurrentEditor(editor);
		}
		else if (editor.isMinimized())
		{
			editor.restore();
			mergeMenuBars(editor);
		}
		else
		{
			log.info("El workbench del editor: [" + editorName + "] es el actual. No se realiza el cambio de editor");
		}
	}

	@Override
	public void receiveEditorAction (String editorName, String action)
	{
		log.info("Se recibio la notificacion " + action + " del editor: [" + editorName + "]");
		if (action.equals(Command.CLOSE_VIEW))
		{
			closeEditor(editorName);
		}
		else if (action.equals(Command.OPEN_VIEW))
		{
			openEditor(editorName);
		}
		else if (action.equals(Command.MINIMIZE_VIEW))
		{
			minimizeEditor(editorName);
		}
		else if (action.equals(Command.RESTORE_VIEW))
		{
			restoreEditor(editorName);
		}
	}

	@Override
	public JPanel getTrayBar ()
	{
		return this.manager.getPanel(R.PANELS.EDITORS_TRAY_BAR);
	}

	@Override
	public void initTrayIcon (Editor editor)
	{
		log.info("Inicializando el boton de tray del editor: [" + editor.getName() + "]");
		ImageButton button = editor.getTrayButton();
		button.addActionListener(this);
		button.setActionCommand(Command.RESTORE_VIEW);
		this.editorButtonMap.put(button, editor);
		this.trayButtonGroup.add(button);
	}

	@Override
	public void removeTrayIcon (Editor editor)
	{
		log.info("Eliminado del boton del editor: [" + editor.getName() + "]" + " de la barra de tray");
		ImageButton button = editor.getTrayButton();
		getTrayBar().remove(button);
		this.trayButtonGroup.remove(button);
	}

	// *********************************************************************************************************************
	// Implementacion de ActionListener
	// *********************************************************************************************************************

	@Override
	public void actionPerformed (ActionEvent e)
	{
		ImageButton source = ((ImageButton) e.getSource());
		String command = e.getActionCommand();

		if (command.equals(Command.RESTORE_VIEW))
		{
			this.editorButtonMap.get(source).restore();
		}
		else if (command.equals(Command.MINIMIZE_VIEW))
		{
			this.editorButtonMap.get(source).onMinimize();
		}
	}

	// *********************************************************************************************************************
	// Metodos Privados
	// *********************************************************************************************************************

	/**
	 * @param name
	 * @return
	 */
	private Editor getEditor (String name)
	{
		return this.editors.get(name);
	}

	/**
	 * @param editor
	 * @return
	 */
	private Editor putEditor (Editor editor)
	{
		return this.editors.put(editor.getName(), editor);
	}

	/**
	 * 
	 * @param editor
	 */
	private void setTrayIcon (Editor editor)
	{
		log.debug("Agregado del boton del editor: [" + editor.getName() + "] a la barra de tray");
		ImageButton button = editor.getTrayButton();
		if (!editor.isMinimized())
		{
			getTrayBar().add(button);
		}
		this.trayButtonGroup.setSelected(button);
	}

	@Override
	protected void uninit ()
	{

	}

	@Override
	public void challengeUserAccess (Modulo module) throws InvalidUserAccessException
	{
		String permissionName = this.properties.getProperty(ProjectValues.EDITOR_ACCESS_PERMISSION);
		TipoPermiso tipoPermiso = TipoPermisoController.getInstance().findByName(permissionName);
		SeguridadFacade.getInstance().challengeUser(Contenedor.getLoggedUser(), module, tipoPermiso);
	}

	@Override
	public List<String> getUserPermissions (Modulo module)
	{
		Set<Perfil> perfiles = Contenedor.getLoggedUser().getPerfiles();
		List<String> permisos = new ArrayList<String>();
		Set<ModuloPerfil> modulosPerfil;
		for (Perfil perfil : perfiles)
		{
			modulosPerfil = perfil.getModulosPerfil();
			for (ModuloPerfil moduloPerfil : modulosPerfil)
			{
				if (module.equals(moduloPerfil.getModulo()))
				{
					permisos.add(moduloPerfil.getTipoPermiso().getDNombre());
				}
			}
		}
		return permisos;
	}

	@Override
	public String getServerURL ()
	{
		return this.properties.getProperty(ProjectValues.SERVER_URL);
	}

	/**
	 * Agrega un nuevo item al menu de editores
	 * 
	 * @param name
	 *            El nombre del item de menu
	 * @param text
	 *            El texto del item de menu
	 * @param tooltip
	 *            El tooltip del item de menu
	 * @param icon
	 *            El icono del item de menu
	 */
	public void addNewEditorMenuItem (String name, String text, String tooltip, Icon icon)
	{
		JMenu menu = this.manager.getMenu(R.MENUS.EDITORS);
		JMenuItem lastItem = menu.getItem(menu.getItemCount() - 1);
		JMenuItem menuItem = new JMenuItem();
		menuItem.setName(name);
		menuItem.setText(text);
		menuItem.setToolTipText(tooltip);
		menuItem.setFont(menu.getFont());
		menuItem.setEnabled(true);
		menuItem.setVisible(true);
		menuItem.setIcon(icon);
		menu.remove(menu.getItemCount() - 1);
		menu.add(menuItem);
		menu.add(lastItem);
		this.manager.putMenuItem(name, menuItem);
	}
}
