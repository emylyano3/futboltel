package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.deportel.componentes.modelo.Propiedad;
import com.deportel.editor.template.view.property.editor.AlignPropertyEditor;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class AlignPropertyControl extends PropertyDataControl implements MouseListener
{
	private static final long	serialVersionUID		= 1L;

	private JTextField			control;
	private AlignPropertyEditor	alignEditor;

	public AlignPropertyControl(Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
	{
		super(parent, properties, propiedad, observer);
	}

	@Override
	protected void create ()
	{
		@SuppressWarnings("unchecked")
		List<String> editorControlNames = (List<String>) this.properties.get(EDITOR_CONTROL_NAMES);
		this.alignEditor = new AlignPropertyEditor(editorControlNames);
		String currentValue = (String) this.properties.get(CURRENT_VALUE);
		this.control = new JTextField(currentValue);
		this.control.setEditable(false);
	}

	@Override
	protected void setAtributes ()
	{
		this.control.setName(this.propiedad.getTipoPropiedad().getDNombre());
		this.control.setToolTipText("Click para editar");
	}

	@Override
	protected void addListeners ()
	{
		this.control.addMouseListener(this);
	}

	@Override
	public String getValue ()
	{
		return this.control.getText() == null ? "" : this.control.getText();
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
	public void mouseClicked (MouseEvent e)
	{

	}

	@Override
	public void mousePressed (MouseEvent e)
	{

	}

	@Override
	public void mouseReleased (MouseEvent e)
	{
		this.alignEditor.initialize(this.control.getText());
		Object response = this.alignEditor.showDialog(this.parent);
		if (!getValue().equals(response))
		{
			setValue(response.toString());
			this.observer.notifyComponentUpdate();
		}
	}

	@Override
	public void mouseEntered (MouseEvent e)
	{

	}

	@Override
	public void mouseExited (MouseEvent e)
	{

	}
}
