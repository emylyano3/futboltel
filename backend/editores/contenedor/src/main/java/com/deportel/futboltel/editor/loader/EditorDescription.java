package com.deportel.futboltel.editor.loader;

import java.util.Properties;

import com.deportel.editor.common.core.Editor;

public class EditorDescription
{
	private final Editor		editor;
	private final Properties	descriptor;

	public EditorDescription(Editor editor, Properties desc)
	{
		this.editor = editor;
		this.descriptor = desc;
	}

	/**
	 * @return the editor
	 */
	public Editor getEditor ()
	{
		return this.editor;
	}

	/**
	 * @return the descriptor
	 */
	public Properties getDescriptor ()
	{
		return this.descriptor;
	}
}

