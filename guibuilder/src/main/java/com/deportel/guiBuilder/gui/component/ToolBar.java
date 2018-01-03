package com.deportel.guiBuilder.gui.component;

import java.util.List;

import javax.swing.JToolBar;

public class ToolBar extends JToolBar
{
	private static final long	serialVersionUID	= 1L;

	private List <?>			tools;

	public List <?> getTools()
	{
		return tools;
	}

	public void setTools(List <?> tools)
	{
		this.tools = tools;
	}
}
