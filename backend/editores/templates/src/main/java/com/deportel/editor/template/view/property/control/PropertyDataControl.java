package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.util.Properties;

import javax.swing.JComponent;

import com.deportel.componentes.modelo.Propiedad;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public abstract class PropertyDataControl
{
	private static final long			serialVersionUID		= 1L;

	protected String					name;

	protected final Propiedad			propiedad;

	protected final Properties			properties;

	protected final ComponentObserver	observer;

	protected final Container 			parent;

	public static String				CURRENT_VALUE			= "current_value";

	public static String				EDITOR_CONTROL_NAMES	= "editor_control_names";

	public PropertyDataControl (Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
	{
		this.properties = properties;
		this.propiedad = propiedad;
		this.observer = observer;
		this.parent = parent;;
		initControl();
	}

	private void initControl()
	{
		create();
		setAtributes();
		addListeners();
	}

	protected abstract void create ();

	protected abstract void setAtributes ();

	protected abstract void addListeners ();

	public abstract JComponent getUIControl ();

	public abstract String getValue();

	public abstract void setValue(String value);

	public String getName ()
	{
		return this.name;
	}

	public ComponentObserver getObserver ()
	{
		return this.observer;
	}

}
