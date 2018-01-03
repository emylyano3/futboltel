package com.deportel.guiBuilder.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.deportel.common.exception.UserShowableException;
import com.deportel.guiBuilder.gui.component.AutoBindedComboBox;
import com.deportel.guiBuilder.gui.component.ImageButton;
import com.deportel.guiBuilder.gui.component.ImageRadioButton;
import com.deportel.guiBuilder.gui.component.ImageToggleButton;
import com.deportel.guiBuilder.gui.component.StateBar;
import com.deportel.guiBuilder.gui.component.ToolBar;

/**
 * @author Emy
 */
public class GuiManager
{
	private final Map <String, Map <String, JComponent>> gui;

	public GuiManager (InputStream xmlFile, Class<?> clazz) throws UserShowableException
	{
		this.gui = GuiLoader.getInstance().loadGui(xmlFile, this, clazz);
	}

	public JButton getButton (String name)
	{
		return (JButton) this.gui.get(GuiConstants.BUTTONS).get(name);
	}

	public ImageButton getImageButton (String name)
	{
		return (ImageButton) this.gui.get(GuiConstants.IMAGE_BUTTONS).get(name);
	}

	public ImageToggleButton getImageToggleButton (String name)
	{
		return (ImageToggleButton) this.gui.get(GuiConstants.IMAGE_TOOGLE_BUTTONS).get(name);
	}

	public ImageRadioButton getImageRadioButton (String name)
	{
		return (ImageRadioButton) this.gui.get(GuiConstants.IMAGE_RADIO_BUTTONS).get(name);
	}

	public JLabel getLabel (String name)
	{
		return (JLabel) this.gui.get(GuiConstants.LABELS).get(name);
	}

	public JTextField getTextField (String name)
	{
		return (JTextField) this.gui.get(GuiConstants.TEXT_FIELDS).get(name);
	}

	public JPasswordField getPasswordField (String name)
	{
		return (JPasswordField) this.gui.get(GuiConstants.PASSWORD_FIELD).get(name);
	}

	public JTextArea getTextArea (String name)
	{
		return (JTextArea) this.gui.get(GuiConstants.TEXT_AREAS).get(name);
	}

	public JPanel getPanel (String name)
	{
		return (JPanel) this.gui.get(GuiConstants.PANELS).get(name);
	}

	public JTabbedPane getTabbedPanel(String name)
	{
		return (JTabbedPane) this.gui.get(GuiConstants.TABBED_PANE).get(name);
	}

	public JSplitPane getSplitPane(String name)
	{
		return (JSplitPane) this.gui.get(GuiConstants.SPLIT_PANES).get(name);
	}

	public JCheckBox getCheckBox (String name)
	{
		return (JCheckBox) this.gui.get(GuiConstants.CHECK_BOXES).get(name);
	}

	public JComboBox getComboBox (String name)
	{
		return (JComboBox) this.gui.get(GuiConstants.COMBO_BOXES).get(name);
	}

	public AutoBindedComboBox getBindedCombo (String name)
	{
		return (AutoBindedComboBox) this.gui.get(GuiConstants.BINDED_COMBOS).get(name);
	}

	public JScrollPane getScrollPane (String name)
	{
		return (JScrollPane) this.gui.get(GuiConstants.SCROLL_PANES).get(name);
	}

	public ToolBar getToolBar (String name)
	{
		return (ToolBar) this.gui.get(GuiConstants.TOOL_BARS).get(name);
	}

	public StateBar getStateBar (String name)
	{
		return (StateBar) this.gui.get(GuiConstants.STATE_BARS).get(name);
	}

	public JMenuBar getMenuBar (String name)
	{
		return (JMenuBar) this.gui.get(GuiConstants.MENU_BARS).get(name);
	}

	public JProgressBar getProgressBar (String name)
	{
		return (JProgressBar) this.gui.get(GuiConstants.PROGRESS_BARS).get(name);
	}

	public JSeparator getSeparator (String name)
	{
		return (JSeparator) this.gui.get(GuiConstants.SEPARATORS).get(name);
	}

	public JMenu getMenu (String name)
	{
		return (JMenu) this.gui.get(GuiConstants.MENUS).get(name);
	}

	public JMenuItem getMenuItem (String name)
	{
		return (JMenuItem) this.gui.get(GuiConstants.MENU_ITEMS).get(name);
	}

	public JRadioButtonMenuItem getRadioButtonMenuItem (String name)
	{
		return (JRadioButtonMenuItem) this.gui.get(GuiConstants.RADIO_BUTTON_MENU_ITEMS).get(name);
	}

	public JTable getTable (String name)
	{
		return (JTable) this.gui.get(GuiConstants.TABLES).get(name);
	}

	public JList getList (String name)
	{
		return (JList) this.gui.get(GuiConstants.LISTS).get(name);
	}

	public void putLabel (String name, JLabel label)
	{
		putComponent(GuiConstants.LABELS, name, label);
	}

	public void putButton (String name, JButton button)
	{
		putComponent(GuiConstants.BUTTONS, name, button);
	}

	public void putComboBox (String name, JComboBox combo)
	{
		putComponent(GuiConstants.COMBO_BOXES, name, combo);
	}

	public void putTextField (String name, JTextField textField)
	{
		putComponent(GuiConstants.TEXT_FIELDS, name, textField);
	}

	public void putMenuItem (String name, JMenuItem menuItem)
	{
		putComponent(GuiConstants.MENU_ITEMS, name, menuItem);
	}

	public void putComponent (String name, JComponent component)
	{
		String group = GROUPS_MAP.get(component.getClass());
		putComponent(group, name, component);
	}

	private void putComponent (String group, String name, JComponent component)
	{
		Map<String, JComponent> components = this.gui.get(group);
		if (components == null)
		{
			components = new HashMap<String, JComponent>(1);
			this.gui.put(group, components);
		}
		components.put(name, component);
		this.gui.get(group).put(name, component);
	}

	public JComponent removeLabel (String name)
	{
		if (this.gui.get(GuiConstants.LABELS) != null)
		{
			return this.gui.get(GuiConstants.LABELS).remove(name);
		}
		return null;
	}

	public JComponent removeTextField (String name)
	{
		if (this.gui.get(GuiConstants.TEXT_FIELDS) != null)
		{
			return this.gui.get(GuiConstants.TEXT_FIELDS).remove(name);
		}
		return null;
	}

	public JComponent removeButton (String name)
	{
		if (this.gui.get(GuiConstants.BUTTONS) != null)
		{
			return this.gui.get(GuiConstants.BUTTONS).remove(name);
		}
		return null;
	}

	public JComponent removeComboBox (String name)
	{
		if (this.gui.get(GuiConstants.COMBO_BOXES) != null)
		{
			return this.gui.get(GuiConstants.COMBO_BOXES).remove(name);
		}
		return null;
	}

	public JComponent removeList (String name)
	{
		return this.gui.get(GuiConstants.LIST).remove(name);
	}

	/**
	 * Busca un componente por nombre y por tipo.
	 * 
	 * @param name
	 *            El nombre del componente
	 * @param clazz
	 *            La clase del componente
	 * @return El componente que coincide con el nombre y tipo recibido, <tt>null</tt> en casi contrario.
	 */
	public JComponent getComponent (String name, Class<?> clazz)
	{
		JComponent component;
		for (String tipoComponente : GuiConstants.COMPONENTS_GROUP)
		{
			component = this.gui.get(tipoComponente).get(name);
			if (component != null && name.equals(component.getName()) && clazz.equals(component.getClass()))
			{
				return component;
			}
		}
		return null;
	}

	private static Map<Class<?>, String> GROUPS_MAP = new HashMap<Class<?>, String>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(JComboBox.class, GuiConstants.COMBO_BOXES);
			put(AutoBindedComboBox.class, GuiConstants.BINDED_COMBOS);
			put(JTextField.class, GuiConstants.TEXT_FIELDS);
			put(JTable.class, GuiConstants.TABLES);
			put(JLabel.class, GuiConstants.LABELS);
			put(JButton.class, GuiConstants.BUTTONS);
			put(JCheckBox.class, GuiConstants.CHECK_BOXES);
			put(JMenuItem.class, GuiConstants.MENU_ITEMS);
			put(JTextArea.class, GuiConstants.TEXT_AREAS);
			put(JSlider.class, GuiConstants.SLIDERS);
			put(JSpinner.class, GuiConstants.SPINNERS);
			put(ImageButton.class, GuiConstants.IMAGE_BUTTONS);
			put(JTabbedPane.class, GuiConstants.TABBED_PANES);
			put(ImageToggleButton.class, GuiConstants.IMAGE_TOOGLE_BUTTONS);
			put(ImageRadioButton.class, GuiConstants.IMAGE_RADIO_BUTTONS);
			put(JList.class, GuiConstants.LISTS);
			put(JMenuBar.class, GuiConstants.MENU_BARS);
			put(JMenu.class, GuiConstants.MENUS);
			put(JMenuItem.class, GuiConstants.MENU_ITEMS);
			put(JProgressBar.class, GuiConstants.PROGRESS_BARS);
			put(JPasswordField.class, GuiConstants.TEXT_FIELDS);
		}
	};
}
