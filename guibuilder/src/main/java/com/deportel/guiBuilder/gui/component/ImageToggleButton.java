package com.deportel.guiBuilder.gui.component;

import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;

public class ImageToggleButton extends ImageButton
{
	private Logger				log					= Logger.getLogger(this.getClass());

	private static final long	serialVersionUID	= 1L;

	/**
	 * Constructor
	 */
	public ImageToggleButton()
	{
		super();
		enableInputMethods(true);
		setFocusable(true);
	}

	@Override
	protected void updateIcon ()
	{
		this.log.debug("Actualizando el icono");
		if (this.isHolded || (this.isPressed && this.isMouseOver))
		{
			this.currentIcon = this.pressedIcon;
		}
		else
		{
			if (this.isMouseOver)
			{
				this.currentIcon = this.rolloverIcon;
			}
			else
			{
				this.currentIcon = this.normalIcon;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseEntered (MouseEvent e)
	{
		this.log.debug("mouseEntered");
		this.isMouseOver = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseExited (MouseEvent e)
	{
		this.log.debug("mouseExited");
		this.isMouseOver = false;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mousePressed (MouseEvent e)
	{
		this.log.debug("mousePressed");
		this.isPressed = true;
		repaint();
	}

	@Override
	public void mouseReleased (MouseEvent e)
	{
		this.log.debug("mouseReleased");
		if (this.isMouseOver)
		{
			if ( !this.isHolded)
			{
				this.isHolded = true;
				notifyListeners();
			}
			else
			{
				this.isHolded = false;
			}
		}
		this.isPressed = false;
		repaint();
	}

	/**
	 * 
	 * @param holded
	 */
	@Override
	public void setHolded (boolean isHolded)
	{
		this.isHolded = isHolded;
	}
}
