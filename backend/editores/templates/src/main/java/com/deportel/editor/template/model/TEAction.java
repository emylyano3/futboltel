package com.deportel.editor.template.model;

import org.apache.log4j.Logger;

import com.deportel.common.action.ProjectActionStrategy;

/**
 * @author Emy
 */
public class TEAction extends ProjectActionStrategy
{

	private Logger log = Logger.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * @see com.deportel.futboltel.common.action.ProjectActionStrategy#openProject()
	 */
	@Override
	public Object openProject ()
	{
		log.debug("Abriendo el poryecto");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.deportel.futboltel.common.action.ProjectActionStrategy#saveProject(java.lang.Object)
	 */
	@Override
	public Object saveProject (Object data)
	{
		log.debug("Guardando el proyecto");
		return null;
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.action.ProjectActionStrategy#openView(com.deportel.futboltel.common.View, java.lang.Object)
	 */
	@Override
	public void openView (Object data)
	{
		// TODO Auto-generated method stub
		log.debug("Abriendo la vista");
	}

}
