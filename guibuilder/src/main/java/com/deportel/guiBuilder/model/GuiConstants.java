package com.deportel.guiBuilder.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
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

import com.deportel.guiBuilder.gui.component.AutoBindedComboBox;
import com.deportel.guiBuilder.gui.component.ImageButton;
import com.deportel.guiBuilder.gui.component.ImageRadioButton;
import com.deportel.guiBuilder.gui.component.ImageToggleButton;
import com.deportel.guiBuilder.gui.component.MenuBar;
import com.deportel.guiBuilder.gui.component.StateBar;
import com.deportel.guiBuilder.gui.component.ToolBar;

/**
 * Interfaz que contiene todas las constantes referentes
 * a los tipos de componentes de interfaz grafica de
 * usuario.
 * @author Emy
 * @since 22/01/2011
 */
public final class GuiConstants
{
	public static final String	BUTTON							= "button";
	public static final String	IMAGE_BUTTON					= "image_button";
	public static final String	IMAGE_RADIO_BUTTON				= "image_radio_button";
	public static final String	IMAGE_TOGGLE_BUTTON				= "image_toogle_button";
	public static final String	COMBO_BOX						= "combo_box";
	public static final String	BINDED_COMBO					= "binded_combo";
	public static final String	SCROLL_PANE						= "scroll_pane";
	public static final String	CHECK_BOX						= "check_box";
	public static final String	TEXT_FIELD						= "text_field";
	public static final String	PASSWORD_FIELD					= "password_field";
	public static final String	TEXT_AREA						= "text_area";
	public static final String	PANEL							= "panel";
	public static final String	TABBED_PANE						= "tabbed_pane";
	public static final String	SPLIT_PANE						= "split_pane";
	public static final String	LABEL							= "label";
	public static final String	TOOL_BAR						= "tool_bar";
	public static final String	STATE_BAR						= "state_bar";
	public static final String	MENU_BAR						= "menu_bar";
	public static final String	MENU							= "menu";
	public static final String	MENU_ITEM						= "menu_item";
	public static final String	RADIO_BUTTON_MENU_ITEM			= "radio_button_menu_item";
	public static final String	TABLE							= "table";
	public static final String	PROGRESS_BAR					= "progress_bar";
	public static final String	SEPARATOR						= "separator";
	public static final String	LIST							= "list";
	public static final String	SLIDER							= "slider";
	public static final String	SPINNER							= "spinner";

	public static final String	BUTTONS							= "buttons";
	public static final String	IMAGE_BUTTONS					= "image_buttons";
	public static final String	IMAGE_RADIO_BUTTONS				= "image_radio_buttons";
	public static final String	IMAGE_TOOGLE_BUTTONS			= "image_toogle_buttons";
	public static final String	COMBO_BOXES						= "combo_boxes";
	public static final String	BINDED_COMBOS					= "binded_combos";
	public static final String	SCROLL_PANES					= "scroll_panes";
	public static final String	CHECK_BOXES						= "check_boxes";
	public static final String	TEXT_FIELDS						= "text_fields";
	public static final String	TEXT_AREAS						= "text_areas";
	public static final String	PANELS							= "panels";
	public static final String	TABBED_PANES					= "tabbed_panes";
	public static final String	SPLIT_PANES						= "split_panes";
	public static final String	LABELS							= "labels";
	public static final String	TOOL_BARS						= "tool_bars";
	public static final String	STATE_BARS						= "state_bars";
	public static final String	MENU_BARS						= "menu_bars";
	public static final String	MENUS							= "menus";
	public static final String	MENU_ITEMS						= "menu_items";
	public static final String	RADIO_BUTTON_MENU_ITEMS			= "radio_button_menu_items";
	public static final String	TABLES							= "tables";
	public static final String	PROGRESS_BARS					= "progress_bars";
	public static final String	SEPARATORS						= "separators";
	public static final String	LISTS							= "lists";
	public static final String	SLIDERS							= "sliders";
	public static final String	SPINNERS						= "spinner";

	public static final Vector <String>	SIMPLE_COMPONENTS = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add (BUTTON);
			add (IMAGE_BUTTON);
			add (IMAGE_RADIO_BUTTON);
			add (IMAGE_TOGGLE_BUTTON);
			add (COMBO_BOX);
			add (BINDED_COMBO);
			add (SCROLL_PANE);
			add (CHECK_BOX);
			add (TEXT_FIELD);
			add (PASSWORD_FIELD);
			add (TEXT_AREA);
			add (PANEL);
			add (TABBED_PANE);
			add (LABEL);
			add (STATE_BAR);
			add (MENU_ITEM);
			add (RADIO_BUTTON_MENU_ITEM);
			add (TABLE);
			add (PROGRESS_BAR);
			add (SEPARATOR);
			add (LIST);
		}
	};

	public static final Vector <String>	COMPLEX_COMPONENTS = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add (SPLIT_PANE);
			add (TOOL_BAR);
			add (MENU_BAR);
			add (MENU);
		}
	};

	public static final Vector <String>	COMPONENTS_GROUP = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add (BUTTONS);
			add (IMAGE_BUTTONS);
			add (IMAGE_RADIO_BUTTONS);
			add (IMAGE_TOOGLE_BUTTONS);
			add (COMBO_BOXES);
			add (BINDED_COMBOS);
			add (SCROLL_PANES);
			add (CHECK_BOXES);
			add (TEXT_FIELDS);
			add (TEXT_AREAS);
			add (PANELS);
			add (TABBED_PANES);
			add (SPLIT_PANES);
			add (LABELS);
			add (TOOL_BARS);
			add (STATE_BARS);
			add (MENU_BARS);
			add (TABLES);
			add (PROGRESS_BARS);
			add (SEPARATORS);
			add (LISTS);
		}
	};

	public static final Vector <String> BUTTON_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
			add(ATT.TOOL_TIP);
			add(ATT.ACTION_LISTENER);
		}
	};

	public static final Vector <String> IMAGE_BUTTON_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.NORMAL_ICON);
			add(ATT.ROLLOVER_ICON);
			add(ATT.PRESSED_ICON);
			add(ATT.DISABLED_ICON);
			add(ATT.ACTION_LISTENER);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> IMAGE_RADIO_BUTTON_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.NORMAL_ICON);
			add(ATT.ROLLOVER_ICON);
			add(ATT.PRESSED_ICON);
			add(ATT.DISABLED_ICON);
			add(ATT.ACTION_LISTENER);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> IMAGE_TOOGLE_BUTTON_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.NORMAL_ICON);
			add(ATT.ROLLOVER_ICON);
			add(ATT.PRESSED_ICON);
			add(ATT.DISABLED_ICON);
			add(ATT.ACTION_LISTENER);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> LABEL_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.ICON);
			add(ATT.BACKGROUND_COLOR);
			add(ATT.FOREGROUND_COLOR);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> PANEL_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TITLE);
			add(ATT.LAYOUT);
			add(ATT.BACKGROUND);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.TOOL_TIP);
			add(ATT.BORDER);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> TABBED_PANE_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TITLE);
			add(ATT.LAYOUT);
			add(ATT.BACKGROUND);
			add(ATT.TOOL_TIP);
			add(ATT.BORDER);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> SPLIT_PANE_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.ORIENTATION);
			add(ATT.CONTINUOUS_LAYOUT);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> CHECK_BOX_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.TOOL_TIP);
			add(ATT.SELECTED);
			add(ATT.ACTION_LISTENER);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> BINDED_COMBO_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.TOOL_TIP);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.ACTION_LISTENER);
			add(ATT.LIST);
			add(ATT.FIRST_ITEM_EMPTY);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> SCROLL_PANE_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> COMBO_BOX_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TOOL_TIP);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.LIST);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> TEXT_AREA_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.TEXT_LIMIT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.FOCUS_LISTENER);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> TEXT_FIELD_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.TEXT_LIMIT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.FOCUS_LISTENER);
			add(ATT.TOOL_TIP);
			add(ATT.FOCUS_LISTENER);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> TABLE_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.COLUMN_NAMES);
			add(ATT.COLUMN_WIDTHS);
			add(ATT.CELL_RENDERER);
			add(ATT.BACKGROUND_COLOR);
			add(ATT.FOREGROUND_COLOR);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.HEADER_FONT_NAME);
			add(ATT.HEADER_FONT_STYLE);
			add(ATT.HEADER_FONT_SIZE);
			add(ATT.CELL_RENDERER);
			add(ATT.ACTION_LISTENER);
			add(ATT.FOCUS_LISTENER);
			add(ATT.SELECTION_LISTENER);
			add(ATT.TOOL_TIP);
			add(ATT.EDITABLE);
			add(ATT.MODEL);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> PASSWORD_FIELD_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> STATE_BAR_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> TOOL_BAR_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> MENU_BAR_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TOOL_TIP);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> MENU_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> MENU_ITEM_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.TOOL_TIP);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.ACTION_LISTENER);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> RADIO_BUTTON_MENU_ITEMS_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TEXT);
			add(ATT.TOOL_TIP);
			add(ATT.FONT_NAME);
			add(ATT.FONT_STYLE);
			add(ATT.FONT_SIZE);
			add(ATT.ACTION_LISTENER);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> PROGRESS_BAR_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.TOOL_TIP);
			add(ATT.INDETERMINATED);
			add(ATT.STRINGPAINTED);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> SEPARATOR_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.ORIENTATION);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Vector <String> LIST_ATTRIBUTES = new Vector <String> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			add(ATT.NAME);
			add(ATT.LIST);
			add(ATT.TOOL_TIP);
			add(ATT.ENABLED);
			add(ATT.VISIBLE);
		}
	};

	public static final Map <String, GuiTagMapping> COMPONENTS_MAPPING = new HashMap <String, GuiTagMapping> ()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put (BUTTON, new GuiTagMapping(BUTTON, JButton.class, BUTTONS, "Button", BUTTON_ATTRIBUTES));
			put (IMAGE_BUTTON, new GuiTagMapping(IMAGE_BUTTON, ImageButton.class, IMAGE_BUTTONS, "Image Button", IMAGE_BUTTON_ATTRIBUTES));
			put (IMAGE_RADIO_BUTTON, new GuiTagMapping(IMAGE_RADIO_BUTTON, ImageRadioButton.class, IMAGE_RADIO_BUTTONS, "Image Radio Button", IMAGE_RADIO_BUTTON_ATTRIBUTES));
			put (IMAGE_TOGGLE_BUTTON, new GuiTagMapping(IMAGE_TOGGLE_BUTTON, ImageToggleButton.class, IMAGE_TOOGLE_BUTTONS, "Image Toggle Button", IMAGE_TOOGLE_BUTTON_ATTRIBUTES));
			put (COMBO_BOX, new GuiTagMapping(COMBO_BOX, JComboBox.class, COMBO_BOXES, "Combo Box", COMBO_BOX_ATTRIBUTES));
			put (BINDED_COMBO, new GuiTagMapping(BINDED_COMBO, AutoBindedComboBox.class, BINDED_COMBOS, "Binded Combo Box", BINDED_COMBO_ATTRIBUTES));
			put (SCROLL_PANE, new GuiTagMapping(SCROLL_PANE, JScrollPane.class, SCROLL_PANES, "Scroll Pane", SCROLL_PANE_ATTRIBUTES));
			put (CHECK_BOX, new GuiTagMapping(CHECK_BOX, JCheckBox.class, CHECK_BOXES, "Check Box", CHECK_BOX_ATTRIBUTES));
			put (TEXT_FIELD, new GuiTagMapping(TEXT_FIELD, JTextField.class, TEXT_FIELDS, "Text Field", TEXT_FIELD_ATTRIBUTES));
			put (PASSWORD_FIELD, new GuiTagMapping(PASSWORD_FIELD, JPasswordField.class, PASSWORD_FIELD, "Password Field", PASSWORD_FIELD_ATTRIBUTES));
			put (TEXT_AREA, new GuiTagMapping(TEXT_AREA, JTextArea.class, TEXT_AREAS, "Text Area", TEXT_AREA_ATTRIBUTES));
			put (PANEL, new GuiTagMapping(PANEL, JPanel.class, PANELS, "Panel", PANEL_ATTRIBUTES));
			put (TABBED_PANE, new GuiTagMapping(TABBED_PANE, JTabbedPane.class, TABBED_PANES, "Tabbed Pane", TABBED_PANE_ATTRIBUTES));
			put (SPLIT_PANE, new GuiTagMapping(SPLIT_PANE, JSplitPane.class, SPLIT_PANES, "Split Pane", SPLIT_PANE_ATTRIBUTES));
			put (LABEL, new GuiTagMapping(LABEL, JLabel.class, LABELS, "Label", LABEL_ATTRIBUTES));
			put (TOOL_BAR, new GuiTagMapping(TOOL_BAR, ToolBar.class, TOOL_BARS, "Tool Bar", TOOL_BAR_ATTRIBUTES));
			put (STATE_BAR, new GuiTagMapping(STATE_BAR, StateBar.class, STATE_BARS, "State Bar", STATE_BAR_ATTRIBUTES));
			put (MENU_BAR, new GuiTagMapping(MENU_BAR, MenuBar.class, MENU_BARS, "Menu Bar", MENU_BAR_ATTRIBUTES));
			put (MENU, new GuiTagMapping(MENU, JMenu.class, MENUS, "Menu", MENU_ATTRIBUTES));
			put (MENU_ITEM, new GuiTagMapping(MENU_ITEM, JMenuItem.class, MENU_ITEMS, "Menu Item", MENU_ITEM_ATTRIBUTES));
			put (RADIO_BUTTON_MENU_ITEM, new GuiTagMapping(RADIO_BUTTON_MENU_ITEM, JRadioButtonMenuItem.class, RADIO_BUTTON_MENU_ITEMS, "Radio Button Menu Item", RADIO_BUTTON_MENU_ITEMS_ATTRIBUTES));
			put (TABLE, new GuiTagMapping(TABLE, JTable.class, TABLES, "Table", TABLE_ATTRIBUTES));
			put (PROGRESS_BAR, new GuiTagMapping(PROGRESS_BAR, JProgressBar.class, PROGRESS_BARS, "Progress Bar", PROGRESS_BAR_ATTRIBUTES));
			put (SEPARATOR, new GuiTagMapping(SEPARATOR, JSeparator.class, SEPARATORS, "Separator", SEPARATOR_ATTRIBUTES));
			put (LIST, new GuiTagMapping(LIST, JList.class, LISTS, "List", LIST_ATTRIBUTES));
		}
	};

	public static String getComponentGroup (String component)
	{
		return COMPONENTS_MAPPING.get(component).getGroup();
	}

	public static interface ATT
	{
		public static final String	NAME				= "name";
		public static final String	ACTION_LISTENER		= "action_listener";
		public static final String	MOUSE_LISTENER		= "mouse_listener";
		public static final String	SELECTION_LISTENER	= "selection_listener";
		public static final String	ICON				= "icon";
		public static final String	NORMAL_ICON			= "normal_icon";
		public static final String	ROLLOVER_ICON		= "rollover_icon";
		public static final String	PRESSED_ICON		= "pressed_icon";
		public static final String	DISABLED_ICON		= "disabled_icon";
		public static final String	TEXT				= "text";
		public static final String	TEXT_LIMIT			= "text_limit";
		public static final String	TITLE				= "title";
		public static final String	COLUMN_NAMES		= "column_names";
		public static final String	COLUMN_WIDTHS		= "column_widths";
		public static final String	TOOL_TIP			= "tool_tip";
		public static final String	ENABLED				= "enabled";
		public static final String	LIST				= "list";
		public static final String	LAYOUT				= "layout";
		public static final String	MODEL				= "model";
		public static final String	BACKGROUND			= "background";
		public static final String	BORDER				= "border";
		public static final String	VISIBLE				= "visible";
		public static final String	MODE				= "mode";
		public static final String	PROGRESS_RANGE		= "progress_range";
		public static final String	PROGRESS			= "progress";
		public static final String	INDETERMINATED		= "indeterminated";
		public static final String	STRINGPAINTED		= "string_painted";
		public static final String	ORIENTATION			= "orientation";
		public static final String	FOCUS_LISTENER		= "focus_listener";
		public static final String	FIRST_ITEM_EMPTY	= "first_item_empty";
		public static final String	SELECTED			= "selected";
		public static final String	CONTINUOUS_LAYOUT	= "continuous_layout";
		public static final String	CELL_RENDERER		= "cell_renderer";
		public static final String	FOREGROUND_COLOR	= "foreground_color";
		public static final String	BACKGROUND_COLOR	= "background_color";
		public static final String	FONT_NAME			= "font_name";
		public static final String	FONT_STYLE			= "font_style";
		public static final String	FONT_SIZE			= "font_size";
		public static final String	HEADER_FONT_NAME	= "header_font_name";
		public static final String	HEADER_FONT_STYLE	= "header_font_style";
		public static final String	HEADER_FONT_SIZE	= "header_font_size";
		public static final String	EDITABLE			= "editable";
	}

}
