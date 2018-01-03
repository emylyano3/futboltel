package com.deportel.guiBuilder.gui.component;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;

import org.apache.log4j.Logger;

import com.deportel.common.utils.Utils;

/**
 * Implementa el pintado del splash. La imagen es
 * cargada y mostrada automaticamente por la JVM, no
 * asi los mensajes del splash. Esta clase se encarga
 * de hacerlo.
 * 
 * @author Emy
 * @since 18/02/2011
 */
public abstract class Splash extends Thread
{
	protected static Logger		log			= Logger.getLogger(Splash.class);

	protected SplashScreen		splash;

	protected Graphics2D		g;

	protected String			text;

	protected Rectangle			bounds;

	//	protected long				splashTime	= Constants.DEFAULT_SPLASH_TIME;

	/**
	 * Inicializa el splash de la aplicacion.
	 * @return <tt>true</tt> si se pudo inicializar el
	 * splash correctamente y <tt>false</tt> en caso contrario.
	 * 
	 * @since 19/02/2011
	 */
	public boolean init ()
	{
		this.splash = SplashScreen.getSplashScreen();
		if (this.splash == null)
		{
			log.warn("El Splash en null");
			return false;
		}
		this.g = this.splash.createGraphics();
		this.bounds = this.splash.getBounds();
		this.text = "";
		return true;
	}

	/**
	 * Cierra el Splash
	 * @since 19/02/2011
	 */
	public synchronized void close()
	{
		if (this.splash != null)
		{
			this.splash.close();
		}
	}

	/**
	 * Actualiza el el estado del splash. La llamada
	 * a este metodo realiza un refresco que permite
	 * ver las modificaciones de pintado sobre el
	 * splash.
	 * @since 19/02/2011
	 */
	public synchronized void update()
	{
		if (this.splash != null)
		{
			this.splash.update();
		}
	}

	/**
	 * Actualiza el texto actual que se muestra en
	 * el proceso de carga.
	 * @since 19/02/2011
	 */
	public abstract void updateText(String text);

	/**
	 * Pinta el Splash.
	 * 
	 * @since 19/02/2011
	 */
	public abstract void render ();

	@Override
	public void run ()
	{
		while (this.splash!= null && this.splash.isVisible())
		{
			render();
			Utils.sleep(5);
		}
	}
}
