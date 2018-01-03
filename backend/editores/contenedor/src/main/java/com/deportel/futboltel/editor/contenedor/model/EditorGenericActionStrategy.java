package com.deportel.futboltel.editor.contenedor.model;

import org.apache.log4j.Logger;

import com.deportel.common.action.ProjectActionStrategy;
import com.deportel.editor.common.core.Editor;

/**
 * @author Emy
 */
public class EditorGenericActionStrategy extends ProjectActionStrategy
{
	private final static Logger			log		= Logger.getLogger(EditorGenericActionStrategy.class);

	private final Editor				editor;

	public EditorGenericActionStrategy (Editor editor)
	{
		this.editor = editor;
	}

	@Override
	public Object openProject ()
	{
		return null;
	}

	@Override
	public Object saveProject (Object data)
	{
		return null;
	}

	@Override
	public void openView (Object data)
	{
		log.info("Abriendo la vista del editor");
		this.editor.open();
	}
}
