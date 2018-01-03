package com.deportel.editor.common.gui;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class EditorTableModel extends DefaultTableModel
{
	private static final long	serialVersionUID	= 1L;

	private final boolean		isEditable;

	public EditorTableModel(int row, int col, Boolean isEditable)
	{
		super(row, col);
		this.isEditable = isEditable;
	}

	public EditorTableModel(Object[][] data, Object[] colNames, Boolean isEditable)
	{
		super(data, colNames);
		this.isEditable = isEditable;
	}

	@Override
	public boolean isCellEditable (int rowIndex, int columnIndex)
	{
		return this.isEditable;
	}

	public TableColumn getColumn (int a)
	{
		return null;
	}

}