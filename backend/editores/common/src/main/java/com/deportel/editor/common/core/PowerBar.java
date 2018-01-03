package com.deportel.editor.common.core;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.deportel.common.Command;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.ImageButton;

/**
 * @author Emy
 */
public class PowerBar extends JPanel
{
	public PowerBar(Workbench workbench)
	{
		this.minimize = new ImageButton();
		this.maximize = new ImageButton();
		this.close = new ImageButton();
		this.workbench = workbench;
		doSettings();
		arrange();
	}

	private static final long	serialVersionUID	= 1L;

	private static final Logger	log					= Logger.getLogger(PowerBar.class);

	private final Workbench		workbench;

	private ImageButton			minimize;

	private ImageButton			maximize;

	private ImageButton			close;

	/**
	 * 
	 */
	private void doSettings ()
	{
		setImages();
		setActionCommands();
		setListener();
	}

	/**
	 * 
	 */
	private void setImages ()
	{
		this.maximize.setNormalIcon(GuiUtils.loadIcon("/images/maximize.png", this.getClass()), "Powerbar Maximize Normal Icon");
		this.maximize.setPressedIcon(GuiUtils.loadIcon("/images/maximize_pressed.png", this.getClass()), "Powerbar Maximize Pressed Icon");
		this.maximize.setRolloverIcon(GuiUtils.loadIcon("/images/maximize_mouse_over.png", this.getClass()), "Powerbar Maximize Rollover Icon");
		this.close.setNormalIcon(GuiUtils.loadIcon("/images/close.png", this.getClass()), "Powerbar Close Normal Icon");
		this.close.setPressedIcon(GuiUtils.loadIcon("/images/close_pressed.png", this.getClass()), "Powerbar Close Pressed Icon");
		this.close.setRolloverIcon(GuiUtils.loadIcon("/images/close_mouse_over.png", this.getClass()), "Powerbar Close Rollover Icon");
		this.minimize.setNormalIcon(GuiUtils.loadIcon("/images/minimize.png", this.getClass()), "Powerbar Minimize Normal Icon");
		this.minimize.setPressedIcon(GuiUtils.loadIcon("/images/minimize_pressed.png", this.getClass()), "Powerbar Minimize Pressed Icon");
		this.minimize.setRolloverIcon(GuiUtils.loadIcon("/images/minimize_mouse_over.png", this.getClass()), "Powerbar Minimize Rollover Icon");
	}

	/**
	 * 
	 */
	private void setActionCommands ()
	{
		this.minimize.setActionCommand(Command.MINIMIZE_VIEW);
		this.maximize.setActionCommand(Command.MAXIMIZE_VIEW);
		this.close.setActionCommand(Command.CLOSE_VIEW);
	}

	/**
	 * 
	 */
	private void setListener ()
	{
		this.minimize.addActionListener(new TrayListener());
		this.maximize.addActionListener(new TrayListener());
		this.close.addActionListener(new TrayListener());
	}

	/**
	 * 
	 */
	private void arrange ()
	{
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(this.minimize);
		this.add(this.maximize);
		this.add(this.close);
	}

	/**
	 * 
	 */
	public void setMaximizeEnabled (boolean enabled)
	{
		this.maximize.setVisible(enabled);
		this.maximize.setEnabled(enabled);
		repaint();
	}

	/**
	 * 
	 */
	public ImageButton getMinimize ()
	{
		return this.minimize;
	}

	public void setMinimize (ImageButton minimize)
	{
		this.minimize = minimize;
	}

	public ImageButton getMaximize ()
	{
		return this.maximize;
	}

	public void setMaximize (ImageButton maximize)
	{
		this.maximize = maximize;
	}

	public ImageButton getClose ()
	{
		return this.close;
	}

	public void setClose (ImageButton close)
	{
		this.close = close;
	}

	protected class TrayListener implements ActionListener
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed (ActionEvent e)
		{
			log.debug("Command: " + e.getActionCommand());
			if (e.getActionCommand().equals(Command.MINIMIZE_VIEW))
			{
				PowerBar.this.minimize.setNormal();
				PowerBar.this.workbench.minimize();
			}
			else if (e.getActionCommand().equals(Command.MAXIMIZE_VIEW))
			{
				PowerBar.this.workbench.maximize();
			}
			else if (e.getActionCommand().equals(Command.CLOSE_VIEW))
			{
				PowerBar.this.workbench.close();
			}
			else if (e.getActionCommand().equals(Command.RESTORE_VIEW))
			{
				PowerBar.this.workbench.restore();
			}
		}
	}
}
