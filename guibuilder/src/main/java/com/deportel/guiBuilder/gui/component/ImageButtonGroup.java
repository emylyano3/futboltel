package com.deportel.guiBuilder.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

/**
 * @author Emy
 */
public class ImageButtonGroup extends JComponent implements ActionListener
{
	private static final long				serialVersionUID	= 1L;

	private Map<ImageButton, ImageButton>	buttons				= new HashMap<ImageButton, ImageButton>();

	private ImageButton						selected;

	/**
	 * 
	 * @param button
	 */
	public void add (ImageButton button)
	{
		this.buttons.put(button, button);
		this.selected = button;
		this.selected.setGroup(this);
	}

	/**
	 * 
	 * @param button
	 */
	public void remove (ImageButton button)
	{
		this.buttons.remove(button);
	}

	/**
	 * 
	 * @param button
	 */
	public void setSelected (ImageButton button)
	{
		if (hasSelected())
		{
			this.selected.setHolded(false);
		}
		this.selected = button;
		this.selected.setHolded(true);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasSelected ()
	{
		return this.selected != null;
	}

	/**
	 * 
	 */
	public void unSelectAll ()
	{
		if (hasSelected())
		{
			this.selected.setHolded(false);
			this.selected = null;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent e)
	{

	}

}
