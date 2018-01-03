package com.deportel.editor.common.core;

import java.awt.Panel;

import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.View;
import com.deportel.editor.common.exception.InvalidCloseStateException;
import com.deportel.guiBuilder.gui.component.ImageRadioButton;
import com.deportel.guiBuilder.gui.component.StateBar;
import com.deportel.guiBuilder.gui.component.ToolBar;

/**
 * Interfaz que implementan todos los editores del proyecto para poseer una funcionalidad basica commun.
 * 
 * @author Emy
 * @since 27/12/2010
 */
public interface Editor extends View
{
	/**
	 * Inicializa el editor. Setea el estado de iniciado en <tt>true</tt>
	 */
	public void init ();

	/**
	 * Desinicializa el editor.Setea el estado de iniciado en <tt>false</tt>
	 */
	public void uninit ();

	/**
	 * Realiza el acomodamiento de los componentes del editor en el espacio de contencion asignado.
	 */
	public void arrangeComponents ();

	/**
	 * Realiza la apertura del editor. En esta accion el editor se hace visible al usuario.
	 * 
	 * @since 13/12/2010
	 */
	public void open ();

	/**
	 * Realiza el cierre del editor. En esta accion el editor deja de ser visble por el usuario y deja de
	 * estar activo.
	 * 
	 * @since 17/03/2011
	 */
	public int onClose () throws InvalidCloseStateException;

	/**
	 * Realiza el minimizado del editor. En esta accion el editor deja de ser visble por el usuario aunque
	 * permanece activo.
	 * 
	 * @since 17/03/2011
	 */
	public void onMinimize ();

	/**
	 * Realiza la restauracion del editor. En esta accion el editor vuelve a ser visble. Es la operacion
	 * inversa al minimizado.
	 * 
	 * @since 17/03/2011
	 */
	public void restore ();

	/**
	 * Devuelve el panel principal de la ventana del editor. Esa panel es donde se realiza todo el alojado de
	 * los componentes de interfaz grafica.
	 * 
	 * @return {@link Panel} El panel principal del editor
	 * @since 13/12/2010
	 */
	public JPanel getMainPanel ();

	/**
	 * Devuelve el la barra de estado de la ventana del editor. La barra de estado es donde se muestran los
	 * mensajes al usuario.
	 * 
	 * @return {@link StateBar} La barra de estado del editor
	 * @since 13/12/2010
	 */
	public StateBar getStateBar ();

	/**
	 * Devuelve el la barra de herramientas de la ventana del editor. La barra de herramientas es donde se
	 * alojan los botones que prestaran la funcionalidad del editor al usuario.
	 * 
	 * @return {@link ToolBar} La barra de herramientas del editor
	 * @since 13/12/2010
	 */
	public ToolBar getToolBar ();

	/**
	 * Devuelve el marco de trabajo completo que esta utilizadno el editor.
	 * 
	 * @return {@link Workbench}
	 * @since 13/03/2011
	 */
	public Workbench getWorkbench ();

	/**
	 * Devuelve el nombre del editor.
	 * 
	 * @return String con el nombre del editor
	 * @since 13/03/2011
	 */
	public String getName ();

	/**
	 * Devuelve la power bar del editor.
	 * 
	 * @return {@link JPanel} La power bar del editor.
	 */
	public JPanel getPowerBar ();

	/**
	 * Devuelve la barra de menú del editor.
	 * 
	 * @return {@link JMenuBar} La barra de menu del editor.
	 */
	public JMenuBar getMenuBar ();

	/**
	 * Devuelve el icono del editor que se va a utilizar para colocar en la tray bar del contenedor.
	 * 
	 * @return {@link ImageRadioButton} El icono que representa al editor.
	 */
	public ImageRadioButton getTrayButton ();

	/**
	 * Indica si el editor ya ha sido inicializado.
	 * 
	 * @return <tt>true</tt> si el editor se encuentra inicializado, <tt>false</tt> en caso contrario.
	 */
	public boolean isInitialized ();

	/**
	 * Indica si el editor se encuentra actualmente minimizado.
	 * 
	 * @return <tt>true</tt> si el editor se encuentra minimizado, <tt>false</tt> en caso contrario.
	 */
	public boolean isMinimized ();

	/**
	 * Indica si el editor se encuentra actualmente abierto.
	 * 
	 * @return <tt>true</tt> si el editor se encuentra abierto, <tt>false</tt> en caso contrario.
	 */
	public boolean isOpened ();

	/**
	 * Se realiza el seteo necesario para los componentes de la ventana
	 */
	public void doSettings ();

	/**
	 * Se realiza la inicializacion del workbench del editor.
	 * 
	 * @see Workbench
	 */
	public Icon getMenuIcon ();

	/**
	 * Devuelve el nombre del modulo asociado al editor.
	 * 
	 * @return el nombre del modulo asociado al editor.
	 */
	public abstract String getModuleName ();

	/**
	 * Asigna el nombre del modulo asociado al editor.
	 * 
	 * @param moduleName
	 *            El nombre del modulo asociado al editor
	 */
	public abstract void setModuleName (String moduleName);

	/**
	 * Refresca los datos que se obtienen desde algun data source definido y se muestran en algunos de los
	 * controles de la interfaz.
	 */
	public abstract void refreshCache ();

	/**
	 * 
	 */
	public Modulo getModule ();

	public interface Commands
	{
		public static final int	CREATE	= 0;
		public static final int	REMOVE	= 1;
		public static final int	EDIT	= 2;
	}

	public static int	CLOSED			= 0;
	public static int	INVALID_STATE	= -1;
}
