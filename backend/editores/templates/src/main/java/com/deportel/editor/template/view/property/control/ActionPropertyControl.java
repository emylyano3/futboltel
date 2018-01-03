package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.deportel.componentes.modelo.Propiedad;
import com.deportel.editor.template.view.property.editor.ActionPropertyEditor;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class ActionPropertyControl extends PropertyDataControl implements MouseListener
{
	private static final long		serialVersionUID	= 1L;

	private JTextField				control;
	private ActionPropertyEditor	actionEditor;

	public ActionPropertyControl(Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
	{
		super(parent, properties, propiedad, observer);
	}

	@Override
	protected void create ()
	{
		@SuppressWarnings("unchecked")
		List<String> editorControlsNames = (List<String>) this.properties.get(EDITOR_CONTROL_NAMES);
		this.actionEditor = new ActionPropertyEditor(editorControlsNames);
		String currentValue = (String) this.properties.get(CURRENT_VALUE);
		this.control = new JTextField(currentValue);
		this.control.setToolTipText("Click para editar");
		this.control.setEditable(false);
	}

	@Override
	protected void setAtributes ()
	{
		this.control.setName(this.propiedad.getTipoPropiedad().getDNombre());
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
		this.control.updateUI();
	}

	@Override
	public JComponent getUIControl ()
	{
		return this.control;
	}

	@Override
	protected void addListeners ()
	{
		this.control.addMouseListener(this);
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
		this.actionEditor.initialize(Arrays.asList(this.control.getText().split(",")));
		Object response = this.actionEditor.showDialog(this.parent);
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
