package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.deportel.componentes.modelo.Propiedad;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class TextPropertyControl extends PropertyDataControl implements DocumentListener
{
	private static final long	serialVersionUID	= 1L;

	private JTextField			control;

	public TextPropertyControl(Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
	{
		super(parent, properties, propiedad, observer);
	}

	@Override
	protected void create ()
	{
		this.control = new JTextField();
	}

	@Override
	protected void setAtributes ()
	{
		this.control.setName(this.propiedad.getTipoPropiedad().getDNombre());
		this.control.setText(this.propiedad.getDRegularData());
	}

	@Override
	protected void addListeners ()
	{
		this.control.getDocument().addDocumentListener(this);
	}

	@Override
	public String getValue ()
	{
		return this.control.getText();
	}

	@Override
	public void setValue (String value)
	{
		this.control.setText(value);
	}

	@Override
	public JComponent getUIControl ()
	{
		return this.control;
	}

	@Override
	public void insertUpdate (DocumentEvent e)
	{
		this.observer.notifyComponentUpdate();
	}

	@Override
	public void removeUpdate (DocumentEvent e)
	{
		this.observer.notifyComponentUpdate();
	}

	@Override
	public void changedUpdate (DocumentEvent e)
	{
		this.observer.notifyComponentUpdate();
	}
}
