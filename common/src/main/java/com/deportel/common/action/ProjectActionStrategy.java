package com.deportel.common.action;

/**
 * @author Emy
 */
public abstract class ProjectActionStrategy
{
	public abstract Object openProject ();

	public abstract Object saveProject (Object data);

	public abstract void openView (Object data);
}
