package com.deportel.guiBuilder.model;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.deportel.common.Constants;
import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Converter;
import com.deportel.common.utils.Utils;
import com.deportel.common.utils.XMLParser;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.AutoBindedComboBox;
import com.deportel.guiBuilder.gui.component.ImageButton;
import com.deportel.guiBuilder.gui.component.ImageRadioButton;
import com.deportel.guiBuilder.gui.component.ImageToggleButton;
import com.deportel.guiBuilder.gui.component.StateBar;
import com.deportel.guiBuilder.gui.component.ToolBar;
import com.deportel.guiBuilder.gui.validator.TextLimitValidator;

/**
 * Clase singleton que contiene la logica necesaria para
 * cargar la configuracion de interfaz desde un archivo
 * XML.
 * 
 * @author Emy
 * @since 19/01/2011
 */
public class GuiLoader
{
	private static Logger		log				= Logger.getLogger(GuiLoader.class);

	private static GuiLoader	instance;

	private int					componentsCount	= 0;

	private Class<?>			loadingReference;

	private ClassLoader			loader;

	private GuiManager			manager;

	private String				currentComponentType;

	private String				currentComponentName;

	public static GuiLoader getInstance ()
	{
		if (instance == null)
		{
			instance = new GuiLoader();
		}
		return instance;
	}

	private GuiLoader ()
	{

	}

	/**
	 * Se le pasa un XML de configuracion y crea y carga los componentes de interfaz en memoria. La estructura
	 * usada es un Map de Map, donde la key del primero es un string que sería el equivalente al nombre de
	 * tipo de componente y el segundo tiene como key un String que sería el nombre de la variable del
	 * componente.
	 * 
	 * @param xmlFilePath
	 *            El path del XML
	 * @return Un Map con los componentes de interfaz.
	 * @throws UserShowableException
	 * @since 19/01/2011
	 */
	public Map <String, Map <String, JComponent>> loadGui (InputStream xmlFile, GuiManager manager, Class<?> clazz) throws UserShowableException
	{
		Utils.validateParameters(xmlFile);
		Map<String, Map<String, JComponent>> gui = new HashMap<String, Map<String, JComponent>>();
		this.loadingReference = clazz;
		this.loader = clazz.getClassLoader();
		this.manager = manager;
		log.debug("Cargando la interfaz de usuario desde: " + xmlFile + "...");
		Document xml = XMLParser.getDocument(xmlFile);
		Node nodo = xml.getFirstChild();
		loadComponents(nodo, gui);
		log.debug("Se cargaron " + this.componentsCount + " componentes");
		resetComponentsCount();
		return gui;
	}

	/**
	 * Se le pasa un XML de configuracion y crea y carga los componentes de interfaz en memoria. La estructura
	 * usada es un Map de Map, donde la key del primero es un string que sería el equivalente al nombre de
	 * tipo de componente y el segundo tiene como key un String que sería el nombre de la variable del
	 * componente.
	 * 
	 * @param xmlFilePath
	 *            El path del XML
	 * @return Un Map con los componentes de interfaz.
	 * @throws UserShowableException
	 * @since 19/01/2011
	 */
	public Map <String, Map <String, JComponent>> loadGui (File xmlFile) throws UserShowableException
	{
		Utils.validateParameters(xmlFile);
		log.debug("Cargando la interfaz desde: " + xmlFile + "...");
		InputStream is;
		try
		{
			is = new FileInputStream(xmlFile);
			return loadGui(is, null, this.getClass());
		}
		catch (FileNotFoundException e)
		{
			throw new UserShowableException("El archivo [" + xmlFile + "] no fue encontrado");
		}
	}

	/**
	 * Carga los componentes que componen la interfaz gráfica.
	 * 
	 * @param nodo
	 *            El nodo padre que contiene la definicion de todos
	 *            los componentes
	 * @since 22/01/2011
	 */
	private void loadComponents (Node nodo, Map<String, Map<String, JComponent>> gui) throws UserShowableException
	{
		log.debug("Cargando los componentes desde el nodo " + nodo.getNodeName() + "...");
		NodeList groups = nodo.getChildNodes();
		String groupName;
		for (int i = 0; i < groups.getLength(); i++)
		{
			Node group = groups.item(i);
			groupName = group.getNodeName();
			if (isGuiComponentGroup(groupName) && group.hasChildNodes())
			{
				loadComponentsGroup(group, gui);
			}
		}
	}

	/**
	 * Carga un grupo de componentes. Un grupo esta compuesto por componentes del mismo tipo, es decir, todos
	 * botones o todos check box, etc.
	 * 
	 * @param groupNode
	 * @return Un {@link Map} con todos los componentes del mismo tipo, donde la key es un string con el
	 *         nombre del componente
	 * @throws UserShowableException
	 * @since 22/01/2011
	 */
	private void loadComponentsGroup (Node groupNode, Map<String, Map<String, JComponent>> gui) throws UserShowableException
	{
		log.debug("Cargando los componentes del grupo " + groupNode.getNodeName() + "...");
		NodeList groupMembers = groupNode.getChildNodes();
		String type = groupNode.getNodeName();
		String name;
		Node componentNode;
		JComponent component;
		for (int i = 0; i < groupMembers.getLength(); i++)
		{
			componentNode = groupMembers.item(i);
			if (isGuiSimpleComponent(componentNode.getNodeName()))
			{
				log.debug("Cargando un " + componentNode.getNodeName());
				component = createSingleComponent(componentNode);
				name = component.getName();
				putComponentInGui(name, type, component, gui);
			}
			else if (isGuiComplexComponent(componentNode.getNodeName()))
			{
				log.debug("Cargando un " + componentNode.getNodeName());
				component = createComplexComponent(componentNode, gui);
				name = component.getName();
				putComponentInGui(name, type, component, gui);
			}
		}
	}

	/**
	 * Recibe un nodo que representa a un componente y genera el componente de interfaz correspondiente
	 * utizando los valores obtenidos del nodo
	 * 
	 * @param nodeComponent
	 * @return
	 * @throws UserShowableException
	 * @since 22/01/2011
	 */
	private JComponent createSingleComponent (Node nodeComponent) throws UserShowableException
	{
		log.debug("Creando un " + nodeComponent.getNodeName());
		this.currentComponentType = nodeComponent.getNodeName();
		NamedNodeMap attributes = nodeComponent.getAttributes();
		JComponent abstractComponent = null;
		try
		{
			this.currentComponentName = getTextValue(attributes, GuiConstants.ATT.NAME);
			if (this.currentComponentType.equalsIgnoreCase(GuiConstants.BUTTON))
			{
				JButton component = new JButton();
				component.setName(this.currentComponentName);
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setFont(loadFont(attributes));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				component.setIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.ICON)));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.IMAGE_BUTTON))
			{
				ImageButton component = new ImageButton();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				try
				{
					component.setRolloverIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.ROLLOVER_ICON)), "");
					component.setNormalIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.NORMAL_ICON)), "");
					component.setPressedIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.PRESSED_ICON)), "");
					component.setPressedIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.PRESSED_ICON)), "");
				}
				catch (NullPointerException e)
				{
					/*
					 * TODO Mejorar esto. Este try catch esta puesto porque cuando se trata
					 * de cargar el icono y la invocacion es desde el gui builder y no desde
					 * algun editor para generar la interfaz, es decir que no existe la
					 * referencia para la carga de recursos, se lanza un NullPointer en el
					 * set del icono.
					 */
				}
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.IMAGE_RADIO_BUTTON))
			{
				ImageRadioButton component = new ImageRadioButton();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				try
				{
					component.setRolloverIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.ROLLOVER_ICON)), "");
					component.setNormalIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.NORMAL_ICON)), "");
					component.setPressedIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.PRESSED_ICON)), "");
				}
				catch (NullPointerException e)
				{
					/*
					 * TODO Mejorar esto. Este try catch esta puesto porque cuando se trata
					 * de cargar el icono y la invocacion es desde el gui builder y no desde
					 * algun editor para generar la interfaz, es decir que no existe la
					 * referencia para la carga de recursos, se lanza un NullPointer en el
					 * set del icono.
					 */
				}
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.IMAGE_TOGGLE_BUTTON))
			{
				ImageToggleButton component = new ImageToggleButton();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				try
				{
					component.setRolloverIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.ROLLOVER_ICON)), "");
					component.setNormalIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.NORMAL_ICON)), "");
					component.setPressedIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.PRESSED_ICON)), "");
				}
				catch (NullPointerException e)
				{
					log.error(e.getMessage());
					/*
					 * TODO Mejorar esto. Este try catch esta puesto porque cuando se trata
					 * de cargar el icono y la invocacion es desde el gui builder y no desde
					 * algun editor para generar la interfaz, es decir que no existe la
					 * referencia para la carga de recursos, se lanza un NullPointer en el
					 * set del icono.
					 */
				}
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.LABEL))
			{
				JLabel component = new JLabel();
				component.setName(this.currentComponentName);
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setFont(loadFont(attributes));
				setBackgroundColor(component, attributes);
				setForegroundColor(component, attributes);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addMouseListener((MouseListener) loadInterfaceListener(attributes, GuiConstants.ATT.MOUSE_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				component.setIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.ICON)));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.TEXT_FIELD))
			{
				JTextField component = new JTextField();
				component.setName(this.currentComponentName);
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setFont(loadFont(attributes));
				setTextLimit(attributes, component);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addFocusListener((FocusListener) loadInterfaceListener(attributes, GuiConstants.ATT.FOCUS_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.TEXT_AREA))
			{
				JTextArea component = new JTextArea();
				component.setName(this.currentComponentName);
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setFont(loadFont(attributes));
				setTextLimit(attributes, component);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addFocusListener((FocusListener) loadInterfaceListener(attributes, GuiConstants.ATT.FOCUS_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.PASSWORD_FIELD))
			{
				JPasswordField component = new JPasswordField();
				component.setName(this.currentComponentName);
				setTextLimit(attributes, component);
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setFont(loadFont(attributes));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.CHECK_BOX))
			{
				JCheckBox component = new JCheckBox();
				component.setName(this.currentComponentName);
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setFont(loadFont(attributes));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setSelected(loadBooleanValue(attributes, GuiConstants.ATT.SELECTED, true));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.COMBO_BOX))
			{
				String [] stringArray = loadStringArray(attributes, GuiConstants.ATT.LIST);
				JComboBox component = new JComboBox(stringArray);
				component.setName(this.currentComponentName);
				component.setFont(loadFont(attributes));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.BINDED_COMBO))
			{
				List <String> list = loadList (attributes, GuiConstants.ATT.LIST);
				AutoBindedComboBox component = new AutoBindedComboBox(list);
				component.setName(this.currentComponentName);
				component.setFont(loadFont(attributes));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setFirstItemEmpty(loadBooleanValue(attributes, GuiConstants.ATT.FIRST_ITEM_EMPTY, true));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.SCROLL_PANE))
			{
				JScrollPane component = new JScrollPane();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.PANEL))
			{
				JPanel component = new JPanel();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setLayout(loadLayout(attributes, component));
				component.setBorder(loadBorder());
				component.setFont(loadFont(attributes));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.TABBED_PANE))
			{
				JTabbedPane component = new JTabbedPane();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setLayout(loadLayout(attributes, component));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.STATE_BAR))
			{
				StateBar component = new StateBar();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.MENU_ITEM))
			{
				JMenuItem component = new JMenuItem();
				component.setName(this.currentComponentName);
				component.setFont(loadFont(attributes));
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				component.setIcon(loadIcon(getTextValue(attributes, GuiConstants.ATT.ICON)));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.RADIO_BUTTON_MENU_ITEM))
			{
				JRadioButtonMenuItem component = new JRadioButtonMenuItem();
				component.setName(this.currentComponentName);
				component.setFont(loadFont(attributes));
				component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
				component.setFont(loadFont(attributes));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.addActionListener((ActionListener) loadInterfaceListener(attributes, GuiConstants.ATT.ACTION_LISTENER));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.TABLE))
			{
				JTable component = new JTable();
				component.setName(this.currentComponentName);
				component.addFocusListener((FocusListener) loadInterfaceListener(attributes, GuiConstants.ATT.FOCUS_LISTENER));
				component.setFillsViewportHeight(true);
				setTableModel(attributes, component);
				setCellRenderer(attributes, component);
				setHeaderModel(attributes, component);
				setTableColumnWidths(attributes, component);
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				ListSelectionListener listener = (ListSelectionListener) loadInterfaceListener(attributes, GuiConstants.ATT.SELECTION_LISTENER, component);
				component.getSelectionModel().addListSelectionListener(listener);
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.PROGRESS_BAR))
			{
				JProgressBar component = new JProgressBar();
				component.setName(this.currentComponentName);
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setIndeterminate(isIndeterminated(attributes));
				component.setStringPainted(isStringPainted(attributes));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				//TODO Completar el seteo de atributos
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.SEPARATOR))
			{
				JSeparator component = new JSeparator();
				component.setName(this.currentComponentName);
				component.setOrientation(loadOrientation(attributes));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
			else if (this.currentComponentType.equalsIgnoreCase(GuiConstants.LIST))
			{
				List <String> list = loadList (attributes, GuiConstants.ATT.LIST);
				JList component = new JList(list.toArray());
				component.setName(this.currentComponentName);
				component.setFont(loadFont(attributes));
				component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
				component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
				component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
				abstractComponent = component;
			}
		}
		catch(DOMException e)
		{
			throw new UserShowableException("Falto el atributo [" + nodeComponent.getNodeValue() + "] en el componente tipo [" + this.currentComponentType + "] con nombre [" + this.currentComponentName + "]");
		}
		catch (NullPointerException e)
		{
			throw new UserShowableException("Falto el atributo [" + nodeComponent.getNodeValue() + "] en el componente tipo [" + this.currentComponentType + "] con nombre [" + this.currentComponentName + "]");
		}
		return abstractComponent;
	}

	private void setTextLimit (NamedNodeMap attributes, JTextComponent component)
	{
		Integer textLimit = loadIntegerValue(attributes, GuiConstants.ATT.TEXT_LIMIT, Constants.DECIMAL);
		if (textLimit > 0)
		{
			PlainDocument doc = new TextLimitValidator(component, textLimit);
			component.setDocument(doc);
		}
	}

	private void setTableModel (NamedNodeMap attributes, JTable component)
	{
		AbstractTableModel model = loadTableModel(attributes);
		if (model != null)
		{
			component.setModel(model);
		}
	}

	private void setCellRenderer (NamedNodeMap attributes, JTable component)
	{
		try
		{
			String[] columnsWidths = loadStringArray(attributes, GuiConstants.ATT.COLUMN_WIDTHS);
			String[] columnsNames = loadStringArray(attributes, GuiConstants.ATT.COLUMN_NAMES);
			DefaultTableCellRenderer tcr = loadCellRenderer(attributes);
			int rowsLimit = Math.min(columnsNames.length, columnsWidths.length);
			for (int i = 0; i < rowsLimit; i++)
			{
				TableColumn column = component.getColumnModel().getColumn(i);
				column.setCellRenderer(tcr);
			}
		}
		catch (Exception e)
		{
			log.info("No se especifico el atributo [" + GuiConstants.ATT.COLUMN_WIDTHS + " para el componente [Table] con nombre [" + component.getName() + "]");
			component.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}
	}

	private void setHeaderModel (NamedNodeMap attributes, JTable component)
	{
		JTableHeader header = component.getTableHeader();
		Font font = loadHeaderFont(attributes);
		header.setFont(font);
	}

	private void setTableColumnWidths (NamedNodeMap attributes, JTable component)
	{
		try
		{
			String[] columnsWidths = loadStringArray(attributes, GuiConstants.ATT.COLUMN_WIDTHS);
			String[] columnsNames = loadStringArray(attributes, GuiConstants.ATT.COLUMN_NAMES);
			int rowsLimit = Math.min(columnsNames.length, columnsWidths.length);
			component.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			for (int i = 0; i < rowsLimit; i++)
			{
				String stringWidth = columnsWidths[i];
				int width = Converter.stringToInt(stringWidth);
				TableColumn column = component.getColumnModel().getColumn(i);
				column.setWidth(width);
				column.setPreferredWidth(width);
			}
		}
		catch (Exception e)
		{
			log.info("No se especifico el atributo [" + GuiConstants.ATT.COLUMN_WIDTHS + " para el componente [Table] con nombre [" + component.getName() + "]");
			component.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}
	}

	/**
	 * Recibe un nodo que representa a un componente y genera el
	 * componente de interfaz correspondiente utizando los valores
	 * obtenidos del nodo
	 * 
	 * @param node
	 * @return
	 * @throws UserShowableException
	 * @since 22/01/2011
	 */
	private JComponent createComplexComponent (Node node, Map<String, Map<String, JComponent>> gui) throws UserShowableException
	{
		log.debug("Creando un " + node.getNodeName() + "");
		String componentType = node.getNodeName();
		NamedNodeMap attributes = node.getAttributes();
		NodeList childNodes = node.getChildNodes();
		Node child;
		JComponent abstractComponent = null;
		if (componentType.equalsIgnoreCase(GuiConstants.SPLIT_PANE))
		{
			JComponent[] subComponents = new JComponent[2];
			JComponent subComponent;
			int count = 0;
			for (int i = 0; i < childNodes.getLength() && count < 2; i++)
			{
				child = childNodes.item(i);
				if (isGuiSimpleComponent(child.getNodeName()))
				{
					subComponent = createSingleComponent(child);
					subComponents[count++] = subComponent;
					putComponentInGui(subComponent.getName(), GuiConstants.getComponentGroup(child.getNodeName()), subComponent, gui);
				}
				else if (isGuiComplexComponent(child.getNodeName()))
				{
					log.warn("No se agregará el componente [" + child.getNodeName() + "]. Solo son soportados los componentes del tipo Panel y todos sus subtipos");
				}
			}
			JSplitPane component;
			if (count <= 1)
			{
				log.info("No se puede crear un Split Pane con un solo panel dentro. Se creara el Split Pane sin paneles");
				component = new JSplitPane
				(
						loadSplitOrientation(attributes),
						loadBooleanValue(attributes, GuiConstants.ATT.CONTINUOUS_LAYOUT, true)
				);
			}
			else
			{
				component = new JSplitPane
				(
						loadSplitOrientation(attributes),
						loadBooleanValue(attributes, GuiConstants.ATT.CONTINUOUS_LAYOUT, true),
						subComponents[0],
						subComponents[1]
				);
			}
			component.setName(getTextValue(attributes, GuiConstants.ATT.NAME));
			component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
			component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
			component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
			abstractComponent = component;
		}
		else if (componentType.equalsIgnoreCase(GuiConstants.MENU_BAR))
		{
			JMenuBar component = new JMenuBar();
			component.setName(getTextValue(attributes, GuiConstants.ATT.NAME));
			component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
			component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
			JMenu menu;
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				child = childNodes.item(i);
				if (isGuiSimpleComponent(child.getNodeName()))
				{
					log.warn("No se pueden agregar menu items a la menu bar, debe agregarlos al menu");
				}
				else if (isGuiComplexComponent(child.getNodeName()))
				{
					log.debug("Cargando un " + child.getNodeName());
					menu = (JMenu) createComplexComponent(child, gui);
					putComponentInGui(menu.getName(), GuiConstants.getComponentGroup(child.getNodeName()), menu, gui);
					component.add(menu);
				}
			}
			abstractComponent = component;
		}
		else if (componentType.equalsIgnoreCase(GuiConstants.MENU))
		{
			JMenu component = new JMenu();
			component.setName(getTextValue(attributes, GuiConstants.ATT.NAME));
			component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
			component.setText(getTextValue(attributes, GuiConstants.ATT.TEXT));
			component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
			component.setFont(loadFont(attributes));
			JMenuItem menuItem;
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				child = childNodes.item(i);
				if (isGuiSimpleComponent(child.getNodeName()))
				{
					log.debug("Cargando un " + child.getNodeName());
					menuItem = (JMenuItem) createSingleComponent(child);
					putComponentInGui(menuItem.getName(), GuiConstants.getComponentGroup(child.getNodeName()), menuItem, gui);
					component.add(menuItem);
				}
			}
			abstractComponent = component;
		}
		else if (componentType.equalsIgnoreCase(GuiConstants.TOOL_BAR))
		{
			ToolBar component = new ToolBar();
			component.setName(getTextValue(attributes, GuiConstants.ATT.NAME));
			component.setEnabled(loadBooleanValue(attributes, GuiConstants.ATT.ENABLED, true));
			component.setVisible(loadBooleanValue(attributes, GuiConstants.ATT.VISIBLE, true));
			component.setToolTipText(getToolTipText(attributes, GuiConstants.ATT.TOOL_TIP));
			JComponent button;
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				child = childNodes.item(i);
				abstractComponent = component;
				if (isGuiSimpleComponent(child.getNodeName()))
				{
					log.debug("Cargando un " + child.getNodeName());
					button = createSingleComponent(child);
					putComponentInGui(button.getName(), GuiConstants.getComponentGroup(child.getNodeName()), button, gui);
					component.add(button);
				}
			}
			abstractComponent = component;
		}
		return abstractComponent;
	}

	/**
	 * Agrega un componente en la estructura gui respetando el grupo
	 * (tipo) de componente.<br/>
	 * <b>Ejemplo:<b/> Le envio un componente boton, donde el tipo es
	 * buttons y el nombre es boton1. El boton es agregado al grupo
	 * buttons y si no existia previamente el grupo buttons, se lo
	 * crea y se lo agrega a gui que es el atributo que almacena toda
	 * la interfaz.
	 * 
	 * @param name
	 *            El nombre del componente
	 * @param type
	 *            El tipo de componente
	 * @param component
	 *            El componente
	 * @since 06/02/2011
	 */
	private void putComponentInGui (String name, String type, JComponent component, Map<String, Map<String, JComponent>> gui)
	{
		if (!gui.containsKey(type))
		{
			log.debug("El grupo " + type + " no existe todavia en memoria, asi que lo creo");
			gui.put(type, new HashMap <String, JComponent> ());
		}
		Map <String, JComponent> group = gui.get(type);
		group.put(name, component);
		this.componentsCount++;
	}

	/**
	 * 
	 * @param attributes
	 * @return
	 * 
	 * @since 06/02/2011
	 */
	private String [] loadStringArray (NamedNodeMap attributes, String attributeName)
	{
		String attribute = attributes.getNamedItem(attributeName).getNodeValue();
		String [] stringArray = attribute.split(Constants.COMMA);
		return stringArray;
	}

	/**
	 * 
	 * @param attributes
	 * @return
	 * 
	 * @since 06/02/2011
	 */
	private List <String> loadList (NamedNodeMap attributes, String attributeName)
	{
		String [] stringArray = loadStringArray(attributes, attributeName);
		List <String> list = new ArrayList<String>();
		for (String string : stringArray)
		{
			list.add(string);
		}
		return list;
	}

	/**
	 * 
	 */
	private Border loadBorder ()
	{
		//TODO Implementar
		return null;
	}

	/**
	 * 
	 */
	private LayoutManager loadLayout (NamedNodeMap attributes, Container component)
	{
		try
		{
			String className = attributes.getNamedItem(GuiConstants.ATT.LAYOUT).getNodeValue();
			Constructor<?> constructor = this.loader.loadClass(className).getConstructor(Container.class);
			return (LayoutManager) constructor.newInstance(component);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 
	 */
	private DefaultTableCellRenderer loadCellRenderer (NamedNodeMap attributes)
	{
		try
		{
			String className = attributes.getNamedItem(GuiConstants.ATT.CELL_RENDERER).getNodeValue();
			if (!Utils.isNullOrEmpty(className))
			{
				Class<?> clazz = this.loader.loadClass(className);
				Constructor<?> constructor = clazz.getConstructor(new Class[]{Font.class, Integer.class, Integer.class});
				Font font = loadFont(attributes);
				Integer backgroundColor = loadIntegerValue(attributes, GuiConstants.ATT.BACKGROUND_COLOR, Constants.HEXA_DECIMAL);
				Integer foregroundColor = loadIntegerValue(attributes, GuiConstants.ATT.FOREGROUND_COLOR, Constants.HEXA_DECIMAL);
				DefaultTableCellRenderer tcr = (DefaultTableCellRenderer) constructor.newInstance(font, foregroundColor, backgroundColor);
				return tcr;
			}
		}
		catch (NullPointerException e)
		{
			log.info("No se indica el atributo [cell_renderer] para el componente tipo [Table] con nombre [" + this.currentComponentName + "]");
		}
		catch (ClassNotFoundException e)
		{
			log.info("No se encontro la clase especificada como [cell_renderer] para el componente tipo [Table] con nombre [" + this.currentComponentName + "]");
		}
		catch (SecurityException e)
		{
			log.info("No es posible acceder a la clase especificada como [cell_renderer] para el componente tipo [Table] con nombre [" + this.currentComponentName + "]");
		}
		catch (NoSuchMethodException e)
		{
			log.info("No se encontro el constuctor necesario para la clase especificada como [cell_renderer] para el componente tipo [Table] con nombre [" + this.currentComponentName + "]");
		}
		catch (IllegalArgumentException e)
		{
			log.info("Se invoco el constuctor con parametros incorrectos para la clase especificada como [cell_renderer] para el componente tipo [Table] con nombre [" + this.currentComponentName + "]");
		}
		catch (InstantiationException e)
		{
			log.info("No se puedo instanciar la clase especificada como [cell_renderer] para el componente tipo [Table] con nombre [" + this.currentComponentName + "]");
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param attributes
	 * @return
	 */
	private Font loadFont (NamedNodeMap attributes)
	{
		String name = getFontName(attributes, GuiConstants.ATT.FONT_NAME);
		int size = getFontSize(attributes,GuiConstants.ATT.FONT_SIZE);
		int style = getFontStyle(attributes, GuiConstants.ATT.FONT_STYLE);
		Font font = new Font(name, style, size);
		return font;
	}

	private Font loadHeaderFont (NamedNodeMap attributes)
	{
		String name = getFontName(attributes, GuiConstants.ATT.HEADER_FONT_NAME);
		int size = getFontSize(attributes, GuiConstants.ATT.HEADER_FONT_SIZE);
		int style = getFontStyle(attributes, GuiConstants.ATT.HEADER_FONT_STYLE);
		Font f = new Font(name, style, size);
		return f;
	}

	private String getFontName (NamedNodeMap attributes, String whichFont)
	{
		String name = loadStringValue(attributes, whichFont);
		return name = name == null || name.isEmpty() ? "Arial" : name;
	}

	private int getFontSize (NamedNodeMap attributes, String whichFont)
	{
		int size = loadIntegerValue(attributes, whichFont, Constants.DECIMAL);
		return size = size == 0 ? 13 : size;
	}

	private int getFontStyle (NamedNodeMap attributes, String whichFont)
	{
		String stringStyle = loadStringValue(attributes, whichFont);
		int style = Font.PLAIN;
		if ("BOLD".equalsIgnoreCase(stringStyle))
		{
			style = Font.BOLD;
		}
		else if ("ITALIC".equalsIgnoreCase(stringStyle))
		{
			style = Font.ITALIC;
		}
		else if ("BOLD_ITALIC".equalsIgnoreCase(stringStyle))
		{
			style = Font.ITALIC | Font.BOLD;
		}
		return style;
	}

	/**
	 * 
	 */
	private AbstractTableModel loadTableModel (NamedNodeMap attributes)
	{
		try
		{
			String className = attributes.getNamedItem(GuiConstants.ATT.MODEL).getNodeValue();
			if (!Utils.isNullOrEmpty(className))
			{
				Boolean isEditable = loadBooleanValue(attributes, GuiConstants.ATT.EDITABLE, false);
				Class<?> clazz = this.loader.loadClass(className);
				Object[][] data = new Object[][] {};
				Object[] columnsNames = loadStringArray(attributes, GuiConstants.ATT.COLUMN_NAMES);
				Constructor<?> constructor = clazz.getConstructor(new Class[]{(new Object[][]{}).getClass(), (new Object[]{}).getClass(), Boolean.class});
				AbstractTableModel atm = (AbstractTableModel) constructor.newInstance(data, columnsNames, isEditable);
				return atm;
			}
		}
		catch (DOMException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param name El nombre del icono
	 * @return
	 * 
	 * @since 25/01/20111
	 */
	private ImageIcon loadIcon (String name)
	{
		if (hasLoadingContext())
		{
			return GuiUtils.loadIcon(name, this.loadingReference);
		}
		return null;
	}

	/**
	 * 
	 */
	private boolean hasLoadingContext ()
	{
		return this.loadingReference != null && this.loadingReference != this.getClass();
	}

	/**
	 * 
	 * @param attributes
	 * @param name
	 * @return
	 */
	private String getTextValue (NamedNodeMap attributes, String name)
	{
		try
		{
			return attributes.getNamedItem(name).getNodeValue();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * 
	 * @param attributes
	 * @param name
	 * @return
	 */
	private String getToolTipText (NamedNodeMap attributes, String name)
	{
		try
		{
			String text = getTextValue(attributes, name);
			if (text == "")
			{
				return null;
			}
			return text;
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	/**
	 * 
	 * @param attributes
	 */
	private EventListener loadInterfaceListener (NamedNodeMap attributes, String propertyName)
	{
		try
		{
			String className = attributes.getNamedItem(propertyName).getNodeValue();
			if (!hasLoadingContext())
			{
				log.info("Se intento cargar el Listener [" + className + "] pero no existe un contexto de carga válido");
				return null;
			}
			if (!Utils.isNullOrEmpty(className))
			{
				Class<?> listenerClass = this.loader.loadClass(className);
				Constructor<?> constructor = listenerClass.getConstructor(new Class[] {this.manager.getClass()});
				return (EventListener) constructor.newInstance(this.manager);
			}
		}
		catch (DOMException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (NullPointerException e)
		{
			log.info("Probablemente el atributo [" + propertyName + "] no fue seteado para el componente [" + this.currentComponentType + "] con nombre [" + this.currentComponentName + "]");
			return null;
		}
		catch (Exception e)
		{
			log.error("Sucedio una excepcion no controlada cargando el atributo [" + propertyName + "] para el componente [" + this.currentComponentType + "] con nombre [" + this.currentComponentName + "]");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param attributes
	 */
	private Object loadInterfaceListener (NamedNodeMap attributes, String propertyName, JComponent component)
	{
		try
		{
			String className = attributes.getNamedItem(propertyName).getNodeValue();
			if (!hasLoadingContext())
			{
				log.info("Se intento cargar el Listener [" + className + "] pero no existe un contexto de carga válido");
				return null;
			}
			if (!Utils.isNullOrEmpty(className))
			{
				Class<?> listenerClass = this.loader.loadClass(attributes.getNamedItem(propertyName).getNodeValue());
				Constructor<?> constructor = listenerClass.getConstructor(new Class[] {GuiManager.class, component.getClass()});
				return constructor.newInstance(this.manager, component);
			}
		}
		catch (DOMException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (NullPointerException e)
		{
			log.info("Probablemente el atributo [" + propertyName + "] no fue seteado para el componente [" + this.currentComponentType + "] con nombre [" + this.currentComponentName + "]");
			return null;
		}
		catch (Exception e)
		{
			log.error("Sucedio una excepcion no controlada cargando el atributo [" + propertyName + "] para el componente [" + this.currentComponentType + "] con nombre [" + this.currentComponentName + "]");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Obtiene del nodo el valor correspondiente a la habilitacion del
	 * compomenente.
	 * 
	 * @param attributes
	 *            {@link NamedNodeMap} con los atributos del
	 *            componente
	 * @return <tt>true</tt> si el componente fue declarado como
	 *         habilitado <tt>false</tt> si el componente no fue
	 *         declarado como habilitado o si el atributo esta ausente
	 *         en la decalracion de componente
	 */
	private boolean loadBooleanValue (NamedNodeMap attributes, String attName, boolean defaultVal)
	{
		try
		{
			Node node = attributes.getNamedItem(attName);
			String enabled = node.getNodeValue();
			return Boolean.parseBoolean(enabled);
		}
		catch (NullPointerException e)
		{
			return defaultVal;
		}
	}

	private void setBackgroundColor (JComponent component, NamedNodeMap attributes)
	{
		try
		{
			Node node = attributes.getNamedItem(GuiConstants.ATT.BACKGROUND_COLOR);
			String value = node.getNodeValue();
			int color = Converter.stringToInt(value, Constants.HEXA_DECIMAL);
			component.setBackground(new Color(color));
		}
		catch (Exception e)
		{
			log.info("El " + this.currentComponentType + this.currentComponentName + " no tiene el atributo [background color]");
		}
	}

	private void setForegroundColor (JComponent component, NamedNodeMap attributes)
	{
		try
		{
			Node node = attributes.getNamedItem(GuiConstants.ATT.FOREGROUND_COLOR);
			String value = node.getNodeValue();
			int color = Converter.stringToInt(value, Constants.HEXA_DECIMAL);
			component.setForeground(new Color(color));
		}
		catch (Exception e)
		{
			log.info("El " + this.currentComponentType + this.currentComponentName + " no tenia el atributo [foreground color]");
		}
	}

	/**
	 * Obtiene del nodo el valor correspondiente a la habilitacion del
	 * compomenente.
	 * 
	 * @param attributes
	 *            {@link NamedNodeMap} con los atributos del
	 *            componente
	 * @return <tt>true</tt> si el componente fue declarado como
	 *         habilitado <tt>false</tt> si el componente no fue
	 *         declarado como habilitado o si el atributo esta ausente
	 *         en la decalracion de componente
	 */
	private Integer loadIntegerValue (NamedNodeMap attributes, String attName, int base)
	{
		try
		{
			Node node = attributes.getNamedItem(attName);
			String value = node.getNodeValue();
			return Converter.stringToInt(value, base);
		}
		catch (NullPointerException e)
		{
			return 0;
		}
	}

	/**
	 * Obtiene del nodo el valor correspondiente a la habilitacion del
	 * compomenente.
	 * 
	 * @param attributes
	 *            {@link NamedNodeMap} con los atributos del
	 *            componente
	 * @return <tt>true</tt> si el componente fue declarado como
	 *         habilitado <tt>false</tt> si el componente no fue
	 *         declarado como habilitado o si el atributo esta ausente
	 *         en la decalracion de componente
	 */
	private String loadStringValue (NamedNodeMap attributes, String attName)
	{
		try
		{
			Node node = attributes.getNamedItem(attName);
			return node.getNodeValue();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	/**
	 * 
	 * @param attributes
	 *            {@link NamedNodeMap} con los atributos del
	 *            componente
	 * @return <tt>true</tt> o <tt>false</tt>
	 */
	private boolean isIndeterminated (NamedNodeMap attributes)
	{
		try
		{
			Node node = attributes.getNamedItem(GuiConstants.ATT.INDETERMINATED);
			String enabled = node.getNodeValue();
			return Boolean.parseBoolean(enabled);
		}
		catch (NullPointerException e)
		{
			return true;
		}
	}

	/**
	 * 
	 * @param attributes
	 *            {@link NamedNodeMap} con los atributos del
	 *            componente
	 * @return <tt>true</tt> o <tt>false</tt>
	 */
	private boolean isStringPainted (NamedNodeMap attributes)
	{
		try
		{
			Node node = attributes.getNamedItem(GuiConstants.ATT.STRINGPAINTED);
			String enabled = node.getNodeValue();
			return Boolean.parseBoolean(enabled);
		}
		catch (NullPointerException e)
		{
			return true;
		}
	}

	/**
	 * 
	 * @param attributes
	 * @return
	 */
	private int loadSplitOrientation (NamedNodeMap attributes)
	{
		try
		{
			Node node = attributes.getNamedItem(GuiConstants.ATT.ORIENTATION);
			String orientation = node.getNodeValue();
			if ("horizontal".equalsIgnoreCase(orientation))
			{
				return JSplitPane.HORIZONTAL_SPLIT;
			}
			else if ("vertical".equalsIgnoreCase(orientation))
			{
				return JSplitPane.VERTICAL_SPLIT;
			}
			else
			{
				return JSplitPane.HORIZONTAL_SPLIT;
			}
		}
		catch (NullPointerException e)
		{
			return JSplitPane.HORIZONTAL_SPLIT;
		}
	}

	/**
	 * 
	 * @param attributes
	 * @return
	 */
	private int loadOrientation (NamedNodeMap attributes)
	{
		try
		{
			Node node = attributes.getNamedItem(GuiConstants.ATT.ORIENTATION);
			String orientation = node.getNodeValue();
			if ("horizontal".equalsIgnoreCase(orientation))
			{
				return SwingConstants.HORIZONTAL;
			}
			else if ("vertical".equalsIgnoreCase(orientation))
			{
				return SwingConstants.VERTICAL;
			}
			else
			{
				return SwingConstants.HORIZONTAL;
			}
		}
		catch (NullPointerException e)
		{
			return SwingConstants.HORIZONTAL;
		}
	}

	//	/**
	//	 * Obtiene del nodo el valor correspondiente a la visibilidad del
	//	 * compomenente.
	//	 *
	//	 * @param attributes
	//	 *            {@link NamedNodeMap} con los atributos del
	//	 *            componente
	//	 * @return <tt>true</tt> si el componente fue declarado como
	//	 *         visible <tt>false</tt> si el componente no fue
	//	 *         declarado como visible o si el atributo esta ausente en
	//	 *         la decalracion de componente
	//	 */
	//	private boolean isVisible (NamedNodeMap attributes)
	//	{
	//		try
	//		{
	//			Node node = attributes.getNamedItem(GuiConstants.ATT.VISIBLE);
	//			String visible = node.getNodeValue();
	//			return Boolean.parseBoolean(visible);
	//		}
	//		catch (NullPointerException e)
	//		{
	//			return true;
	//		}
	//	}

	/**
	 * Dice si el nombre enviado pertenece a un grupo de componentes.
	 * Es decir que determina si el nombre es del grupo botones, etiquetas, campos de tecto, etc.
	 * @param name El nombre del grupo a verificar
	 * @return Si el nombre enviado es de un grupo de componentes o no
	 * 
	 * @since 22/01/2011
	 */
	private boolean isGuiComponentGroup (String name)
	{
		for (String string : GuiConstants.COMPONENTS_GROUP)
		{
			if (string.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Dice si el nombre enviado pertenece a un grupo de componentes.
	 * Es decir que determina si el nombre es del grupo botones, etiquetas, campos de tecto, etc.
	 * @param name El nombre del grupo a verificar
	 * @return Si el nombre enviado es de un grupo de componentes o no
	 * 
	 * @since 22/01/2011
	 */
	private boolean isGuiSimpleComponent (String name)
	{
		for (String string : GuiConstants.SIMPLE_COMPONENTS)
		{
			if (string.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Dice si el nombre enviado pertenece a un grupo de componentes.
	 * Es decir que determina si el nombre es del grupo botones, etiquetas, campos de tecto, etc.
	 * @param name El nombre del grupo a verificar
	 * @return Si el nombre enviado es de un grupo de componentes o no
	 * 
	 * @since 22/01/2011
	 */
	private boolean isGuiComplexComponent (String name)
	{
		for (String string : GuiConstants.COMPLEX_COMPONENTS)
		{
			if (string.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Vuelve la cuenta de carga de componentes
	 * a cero.
	 */
	private void resetComponentsCount()
	{
		this.componentsCount = 0;
	}
}
