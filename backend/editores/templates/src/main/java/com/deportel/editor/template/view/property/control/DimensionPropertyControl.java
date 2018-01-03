package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.deportel.common.utils.Converter;
import com.deportel.componentes.controller.IntPropertyTypeValues;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class DimensionPropertyControl extends PropertyDataControl implements ChangeListener
{
	private static final long	serialVersionUID	= 1L;

	private JSpinner control;

	public DimensionPropertyControl(Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
	{
		super(parent, properties, propiedad, observer);
	}

	@Override
	protected void create ()
	{
		this.control = new JSpinner();
	}

	@Override
	protected void setAtributes ()
	{
		this.name = this.propiedad.getTipoPropiedad().getDNombre();
		this.control.setName(this.name);
		IntPropertyTypeValues tpv = TipoPropiedadController.INT_PROPERTY_TYPE_VALUES.get(this.propiedad.getTipoPropiedad().getDTagXml());
		int value = Converter.stringToInt(this.propiedad.getDRegularData());
		SpinnerModel sm = new SpinnerNumberModel(value, tpv.getMin(), tpv.getMax(), 1);
		this.control.setModel(sm);
	}

	@Override
	protected void addListeners ()
	{
		this.control.addChangeListener(this);
	}

	@Override
	public String getValue ()
	{
		return this.control.getValue().toString();
	}

	@Override
	public void setValue (String value)
	{
		this.control.setValue(Converter.stringToInt(value));
	}

	@Override
	public JComponent getUIControl ()
	{
		return this.control;
	}

	@Override
	public void stateChanged (ChangeEvent e)
	{
		this.observer.notifyComponentUpdate();
	}
}
