package com.deportel.guiBuilder.gui.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

public class ImageButton extends JLabel implements MouseListener
{
	private Logger					log					= Logger.getLogger(this.getClass());

	private static final long		serialVersionUID	= 1L;

	protected ImageIcon				pressedIcon;

	protected ImageIcon				rolloverIcon;

	protected ImageIcon				normalIcon;

	protected ImageIcon				disabledIcon		= new ImageIcon();

	protected List<ActionListener>	listeners;

	protected ImageIcon				currentIcon;

	protected String				command;

	protected boolean				isMouseOver			= false;

	protected boolean				isPressed			= false;

	protected boolean				isHolded			= false;

	protected ImageButtonGroup		group;

	/**
	 * 
	 * @return
	 */
	public ImageButton ()
	{
		super();
		enableInputMethods(true);
		addMouseListener(this);
		setFocusable(true);
	}

	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		updateIcon();
		if (this.currentIcon != null && isEnabled() && isVisible())
		{
			this.setPreferredSize(new Dimension(this.currentIcon.getIconWidth(), this.currentIcon.getIconHeight()));
			this.setMaximumSize(new Dimension(this.currentIcon.getIconWidth(), this.currentIcon.getIconHeight()));
			this.setMinimumSize(new Dimension(this.currentIcon.getIconWidth(), this.currentIcon.getIconHeight()));
			g.drawImage(this.currentIcon.getImage(), 0, 0, null);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled (boolean enabled)
	{
		if (enabled)
		{
			setFocusable(true);
			this.currentIcon = this.normalIcon;
		}
		else
		{
			setFocusable(false);
			this.currentIcon = this.disabledIcon;
		}
	}

	/**
	 * 
	 */
	protected void updateIcon()
	{
		if (this.isPressed && this.isMouseOver)
		{
			if (this.pressedIcon != null)
			{
				this.currentIcon = this.pressedIcon;
			}
		}
		else if (this.isMouseOver)
		{
			if (this.rolloverIcon != null)
			{
				this.currentIcon = this.rolloverIcon;
			}
		}
		else
		{
			if (this.normalIcon != null)
			{
				this.currentIcon = this.normalIcon;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked (MouseEvent e)
	{
		this.log.debug("mouseClicked");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered (MouseEvent e)
	{
		if (isFocusable())
		{
			this.log.debug("mouseEntered");
			this.isMouseOver = true;
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited (MouseEvent e)
	{
		if (isFocusable())
		{
			this.log.debug("mouseExited");
			this.isMouseOver = false;
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed (MouseEvent e)
	{
		if (isFocusable())
		{
			this.log.debug("mousePressed");
			this.isPressed = true;
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased (MouseEvent e)
	{
		if (isFocusable())
		{
			this.log.debug("mouseReleased");
			this.isPressed = false;
			if (this.isMouseOver)
			{
				notifyListeners();
			}
			repaint();
		}
	}

	/**
	 * 
	 */
	protected void notifyListeners()
	{
		this.log.debug("Notificando a los listeners");
		if (this.listeners != null)
		{
			for (Iterator<ActionListener> it = this.listeners.iterator(); it.hasNext();)
			{
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.command);
				it.next().actionPerformed(event);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public ImageIcon getPressedIcon ()
	{
		return this.pressedIcon;
	}

	/**
	 * 
	 * @return
	 */
	public void setPressedIcon (ImageIcon pressedIcon, String description)
	{
		this.pressedIcon = pressedIcon;
		this.pressedIcon.setDescription(description);
		repaint();
	}

	/**
	 * 
	 * @return
	 */
	public void setDisabledIcon (ImageIcon icon, String description)
	{
		this.disabledIcon = icon;
		this.disabledIcon.setDescription(description);
		repaint();
	}

	/**
	 * 
	 * @return
	 */
	public ImageIcon getRolloverIcon ()
	{
		return this.rolloverIcon;
	}

	/**
	 * 
	 * @return
	 */
	public void setRolloverIcon (ImageIcon rolloverIcon, String description)
	{
		this.rolloverIcon = rolloverIcon;
		this.rolloverIcon.setDescription(description);
		repaint();
	}

	@Override
	public ImageIcon getIcon ()
	{
		return this.normalIcon;
	}

	/**
	 * 
	 * @return
	 */
	public void setNormalIcon (ImageIcon icon, String description)
	{
		this.normalIcon = icon;
		this.normalIcon.setDescription(description);
		this.currentIcon = icon;
		repaint();
	}

	/**
	 * 
	 * @return
	 */
	public void setActionCommand (String command)
	{
		this.command = command;
	}

	/**
	 * 
	 * @param actionListener
	 */
	public void addActionListener (ActionListener actionListener)
	{
		if (this.listeners == null)
		{
			this.listeners = new ArrayList<ActionListener>();
		}
		this.listeners.add(actionListener);
	}

	/**
	 * 
	 * @param group
	 */
	public void setGroup (ImageButtonGroup group)
	{
		this.group = group;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPressed ()
	{
		return this.isPressed;
	}

	/**
	 * 
	 * @return
	 */
	public void setNormal ()
	{
		this.isMouseOver = false;
		this.isPressed = false;
		this.isHolded = false;
	}

	/**
	 * 
	 * @param isPressed
	 */
	public void setPressed (boolean isPressed)
	{
		this.isPressed = isPressed;
	}

	/**
	 * 
	 * @param holded
	 */
	public void setHolded (boolean holded)
	{
		this.isHolded = true;
	}
}