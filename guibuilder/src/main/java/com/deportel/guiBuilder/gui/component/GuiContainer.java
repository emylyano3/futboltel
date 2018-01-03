package com.deportel.guiBuilder.gui.component;

import java.awt.Container;

import org.jdesktop.layout.GroupLayout;

/**
 * Clase que representa un contenedor grafico para alojar
 * componentes de interfaz. Esta clase extiende la clase
 * {@link Container} y posee como layout por default al de
 * la clase {@link GroupLayout}.
 * 
 * @author Emy
 * @since 10/01/2011
 */
public abstract class GuiContainer extends Container
{
	private static final long	serialVersionUID	= 1L;

	private GroupLayout 		layout;

	protected abstract void init ();

	protected abstract void arrange ();

	protected abstract void update ();

	@Override
	public final void show ()
	{
		init();
		arrange();
	}

	@Override
	public final GroupLayout getLayout ()
	{
		if (layout == null)
		{
			layout = new GroupLayout (this);
			this.setLayout(layout);
		}
		return layout;
	}
}
