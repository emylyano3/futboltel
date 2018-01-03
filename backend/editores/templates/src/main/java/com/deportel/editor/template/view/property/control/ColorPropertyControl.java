package com.deportel.editor.template.view.property.control;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Properties;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.deportel.common.Constants;
import com.deportel.common.utils.Converter;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class ColorPropertyControl extends PropertyDataControl implements MouseListener
{
	private static final long	serialVersionUID	= 1L;

	private JTextField			control;
	private Color				color;

	public ColorPropertyControl(Container parent, Properties properties, Propiedad propiedad, ComponentObserver observer)
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
		this.control.setEnabled(false);
		this.control.setToolTipText("Clic para editar");
		setBackGroundColor();
	}

	private void setBackGroundColor ()
	{
		int color = Converter.stringToInt(this.propiedad.getDRegularData(), Constants.HEXA_DECIMAL);
		this.color = new Color(color);
		this.control.setBackground(this.color);
		this.control.setOpaque(true);
		this.control.updateUI();
	}

	@Override
	protected void addListeners ()
	{
		this.control.addMouseListener(this);
	}

	@Override
	public String getValue ()
	{
		return Converter.intToString(this.color.getRGB());
	}

	@Override
	public void setValue (String value)
	{
		int color = Converter.stringToInt(value, Constants.HEXA_DECIMAL);
		this.color = new Color(color);
	}

	@Override
	public JComponent getUIControl ()
	{
		return this.control;
	}

	private void updateUIControlColor ()
	{
		this.control.setBackground(this.color);
		this.control.updateUI();
		this.observer.notifyComponentUpdate();
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
		Color color = JColorChooser.showDialog(this.parent, "Selección de Color", this.color);
		if (color != null && !this.color.equals(color))
		{
			this.color = color;
			updateUIControlColor();
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
