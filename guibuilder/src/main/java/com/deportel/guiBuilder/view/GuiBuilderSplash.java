package com.deportel.guiBuilder.view;

import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.gui.component.Splash;

/**
 * @author Emy
 */
public class GuiBuilderSplash extends Splash
{
	private static GuiBuilderSplash	instance;

	/**
	 *  Emy
	 * @since 19/02/2011
	 */
	private GuiBuilderSplash ()
	{
		init();
	}

	/**
	 * Devuelve la instancia del splash. Al crear la
	 * instancia se inicializa creando el componente
	 * necesario para pintar el splash en pantalla.
	 * @return {@link Splash}
	 * 
	 *  Emy
	 * @since 19/02/2011
	 */
	public static GuiBuilderSplash getInstance ()
	{
		if (instance == null)
		{
			instance = new GuiBuilderSplash();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.Splash#render()
	 */
	@Override
	public synchronized void render ()
	{
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.Splash#updateText(java.lang.String)
	 */
	@Override
	public synchronized void updateText (String text)
	{
		log.debug(text.toUpperCase());
		this.text = text;
		Utils.sleep(200);
	}
}
