package com.deportel.editor.common.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class EditorTableCellRenderer extends DefaultTableCellRenderer
{
	private static final long	serialVersionUID	= 1L;

	private final Color			fgcNormal;
	private final Color			bgcNormal;
	private final Font			font;

	private final Color			bgcSelected			= new Color(0x5555FF);
	private final Color			fgcSelected			= new Color(0xEEEEEE);


	public EditorTableCellRenderer (Font font, Integer fgc, Integer bgc)
	{
		this.fgcNormal = new Color(fgc);
		this.bgcNormal = new Color(bgc);
		this.font = font;
	}

	@Override
	public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value == null) return this;
		renderer.setFont(this.font);
		if (isSelected)
		{
			renderer.setBackground(this.bgcSelected);
			renderer.setForeground(this.fgcSelected);
		}
		else
		{
			renderer.setForeground(this.fgcNormal);
			renderer.setBackground(this.bgcNormal);
		}
		return renderer;
	}
}
