package com.deportel.componentes.controller;

public abstract class ComponentesController
{
	public ComponentesController(boolean uniqueSession)
	{
		this.uniqueSession = uniqueSession;
	}

	protected boolean uniqueSession = false;
}
