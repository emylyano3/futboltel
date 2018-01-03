package com.deportel.futboltel.editor.loader;

import com.deportel.editor.common.core.Editor;

public class EditorItem
{
	private Editor	editor;
	private String	editorName;

	/**
	 * @return the editor
	 */
	public Editor getEditor ()
	{
		return this.editor;
	}

	/**
	 * @param editor
	 *            the editor to set
	 */
	public void setEditor (Editor editor)
	{
		this.editor = editor;
	}

	/**
	 * @return the editorName
	 */
	public String getEditorName ()
	{
		return this.editorName;
	}

	/**
	 * @param editorName
	 *            the editorName to set
	 */
	public void setEditorName (String editorName)
	{
		this.editorName = editorName;
	}
}
