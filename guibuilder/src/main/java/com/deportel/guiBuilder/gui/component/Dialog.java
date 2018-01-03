package com.deportel.guiBuilder.gui.component;

import javax.swing.JDialog;
import javax.swing.JMenuBar;

import org.jdesktop.layout.GroupLayout;

/**
 * Clase abstracta que implmenta el patrón Template Method.
 * Aloja la lógica comun para la creacion de la ventana principal
 * de una interfaz de usuario.
 * Por default, el layout de la ventana es {@link GroupLayout}.
 * @author Emy
 * @since 18/01/2011
 */
public abstract class Dialog extends JDialog
{
	private static final long	serialVersionUID	= 1L;

	private GroupLayout			layout;

	protected boolean			isInitilized		= false;

	protected abstract void init ();

	protected abstract void uninit ();

	protected abstract void arrange ();

	public abstract void update ();

	public abstract void doSettings ();

	/**
	 * Crea y muestra la interfaz de usuario.
	 * Realiza los seteos minimos para que la
	 * ventana sea visible. Si la interfaz no
	 * esta inicializada, se encarga de hacerlo.
	 */
	public final void createAndShowGui ()
	{
		if (!this.isInitilized)
		{
			init();
			doSettings();
			setCloseOperation();
			arrange();
			pack();
		}
		setVisible(true);
	}

	@Override
	public final GroupLayout getLayout ()
	{
		if (this.layout == null)
		{
			this.layout = new GroupLayout (this.getContentPane());
			this.setLayout(this.layout);
		}
		return this.layout;
	}

	/**
	 * Setea la operacion que se realiza al cerrar la ventana
	 * @since 18/02/2011
	 */
	public abstract void setCloseOperation ();

	/**
	 * 
	 * @param mb
	 */
	public final void setMenuBar (JMenuBar mb)
	{
		super.setJMenuBar(mb);
	}

}
