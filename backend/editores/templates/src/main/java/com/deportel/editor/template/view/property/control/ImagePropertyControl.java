package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.editor.template.controller.TEGuiController;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectTexts;
import com.deportel.editor.template.view.listener.SelectSourcesListener;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class ImagePropertyControl extends PropertyDataControl
{
	private static final long	serialVersionUID	= 1L;

	private JButton				control;

	public ImagePropertyControl(Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
	{
		super(parent, properties, propiedad, observer);
	}

	@Override
	protected void create ()
	{
		this.control = new JButton(TemplateEditor.getTexts().get(ProjectTexts.TE_MSG_SELECT_SOURCES));
	}

	@Override
	protected void setAtributes ()
	{
		TipoPropiedad tipoPropiedad = this.propiedad.getTipoPropiedad();
		String propTypeName = tipoPropiedad.getDNombre();
		this.control.setName(propTypeName);
	}

	@Deprecated
	@Override
	public String getValue ()
	{
		return this.control.getText();
	}

	@Deprecated
	@Override
	public void setValue (String value)
	{
	}

	@Override
	public JComponent getUIControl ()
	{
		return this.control;
	}

	@Override
	protected void addListeners ()
	{
		TEGuiController controller = (TEGuiController) this.observer;
		this.control.addActionListener(new SelectSourcesListener(controller, controller.getIeController()));
	}
}
