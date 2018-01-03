package com.deportel.futboltel.editor.contenedor.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;

import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.gui.component.Splash;

/**
 * @author Emy
 */
public class ContenedorSplash extends Splash
{
	private static ContenedorSplash	instance;

	private Font					font;

	private double					realProgress		= 0;

	private double					virtualProgress		= 0;

	private static final int		X_MARGIN			= 5;

	private static final int		TEXT_Y_MARGIN		= 12;

	private static final int		PROGRESS_BAR_HEIGHT	= 6;

	private static final int		PROGRESS_STEP		= 1;

	private static final int		BORDER				= 25;


	/**
	 * Constructor por defecto
	 * @since 19/02/2011
	 */
	private ContenedorSplash ()
	{
		init();
	}

	/**
	 * Devuelve la instancia del splash. Al crear la
	 * instancia se inicializa creando el componente
	 * necesario para pintar el splash en pantalla.
	 * @return {@link Splash}
	 * 
	 * @since 19/02/2011
	 */
	public static ContenedorSplash getInstance ()
	{
		if (instance == null)
		{
			instance = new ContenedorSplash();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.Splash#render()
	 */
	@Override
	public synchronized void render ()
	{
		if (this.splash != null)
		{
			this.g.setComposite(AlphaComposite.Clear);
			this.g.fillRect
			(
					0,
					0,
					(int) this.bounds.getWidth(),
					(int) this.bounds.getHeight()
			);
			this.g.setPaintMode();
			this.g.setColor(Color.BLACK);
			this.g.setFont(this.font);
			this.g.drawString
			(
					this.text,
					0 + X_MARGIN,
					(int) this.bounds.getHeight() - TEXT_Y_MARGIN - BORDER
			);
			drawProgressBar();
			this.splash.update();
		}
	}

	/**
	 * Dibuja la barra de progreso del inicio de la aplicacion
	 * @since 07/04/2011
	 */
	private void drawProgressBar()
	{
		this.g.setColor(Color.BLACK);
		int y = (int) this.bounds.getHeight() - BORDER - PROGRESS_BAR_HEIGHT;
		int x = 0;
		int width = (int) (this.bounds.getWidth() * this.virtualProgress / 100);
		int height = PROGRESS_BAR_HEIGHT;
		incVirtualProgress();
		this.g.fillRect(x, y, width, height);
	}

	/**
	 * Incrementa el ancho de la barra virtual de progreso.
	 * @since 07/04/2011
	 */
	private void incVirtualProgress()
	{
		if (this.virtualProgress + PROGRESS_STEP <= this.realProgress)
		{
			this.virtualProgress += PROGRESS_STEP;
		}
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

	/**
	 * Actualiza el progreso de la barra de carga.
	 * @param progress El valor de progreso incremental
	 * @since 07/04/2011
	 */
	public synchronized void updateProgress (double progress)
	{
		if (this.realProgress + progress < 100)
		{
			this.realProgress += progress;
		}
		else
		{
			this.realProgress = 100;
		}
		log.debug("PROGRESS: " + (int) this.realProgress);
	}
}
