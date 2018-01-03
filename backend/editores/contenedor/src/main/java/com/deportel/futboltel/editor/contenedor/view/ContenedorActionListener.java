package com.deportel.futboltel.editor.contenedor.view;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class ContenedorActionListener extends EditorActionListener
{
	/**
	 * @param manager
	 */
	public ContenedorActionListener(GuiManager manager)
	{
		super(manager);
	}

	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.log.debug("Se presiono el componente [" + source.getName() + "] del editor de contenido");
		if (R.MENU_ITEMS.SALIR.equals(source.getName()))
		{
			ContenedorWindow.getInstance().safeExit();
		}
	}

}
