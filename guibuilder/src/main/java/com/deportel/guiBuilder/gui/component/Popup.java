package com.deportel.guiBuilder.gui.component;

import java.awt.Component;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.deportel.common.callback.CallBackListener;

/**
 * {@link Popup} extiende de {@link JFrame} y presta funcionalidad para la
 * creacion de ventanas emergentes utiles para el mostrado de mensajes al
 * usuario. <br/>
 * Se puede crear un {@link Popup} tal que sirva unicamente para mostrar un
 * mensaje al usuario donde el unico fin del Popup es tal. La otra opcion para
 * crear el {@link Popup} es que además de mostrar el mensaje al usuario sirva
 * para devolver informacion a quien invoco su creacion. En tal caso será
 * necesario que para que quien invoca al {@link Popup} reciba dicha
 * informacion, implemente la interfaz {@link CallBackListener}. Una vez que el
 * usuario seleccione alguna de las opciones (haga click en alguno de los
 * botones) se efectuara el callback invocando a alguno de los metodos presentes
 * en la intefaz.
 * 
 * @author Emy
 * @since 12/12/2010
 */
public class Popup extends JDialog
{
	//**************************************************************************************
	// CONSTRUCTOR
	//**************************************************************************************

	private static Popup	instance;

	private Popup ()
	{
	}

	public static Popup getInstance(Image image)
	{
		instance = new Popup();
		instance.listener = null;
		instance.type = POPUP_PICTURE;
		instance.message = null;
		instance.icon = new ImageIcon(image);
		instance.reference = null;
		return instance;
	}

	/**
	 * Devuelve una instancia de {@link Popup}.
	 * <p/>
	 * El popup puede ser alguno de estos tipos:
	 * <ul>
	 * <li>{@link #POPUP_FATAL} Sirve para indicar un error fatal en la
	 * aplicacion.
	 * <li>{@link #POPUP_ERROR} Sirve para indicar un error en la aplicacion.
	 * <li>{@link #POPUP_WARNING} Sirve para dar una adevertencia.
	 * <li>{@link #POPUP_INFO} Sirve para dar informacion.
	 * <li>{@link #POPUP_OK} Sirve para informar un proceso exitoso.
	 * <li>{@link #POPUP_ABOUT} Sirve para dar información de la aplicacion.
	 * </ul>
	 * Si el parametro reference es <tt>null</tt> el {@link Popup} se centrará
	 * en la pantalla.
	 * 
	 * @param message
	 *            El mensaje que se va a mostrar en el popup
	 * @param type
	 *            El tipo de popup.
	 * @return una instancia de {@link Popup}
	 */
	public static Popup getInstance(String message, PopupType type)
	{
		instance = new Popup();
		instance.listener = null;
		instance.type = type;
		instance.message = message;
		instance.icon = null;
		instance.reference = null;
		return instance;
	}

	/**
	 * Devuelve una instancia de {@link Popup}.
	 * <p/>
	 * El popup puede ser alguno de estos tipos:
	 * <ul>
	 * <li>{@link #POPUP_FATAL} Sirve para indicar un error fatal en la
	 * aplicacion.
	 * <li>{@link #POPUP_ERROR} Sirve para indicar un error en la aplicacion.
	 * <li>{@link #POPUP_WARNING} Sirve para dar una adevertencia.
	 * <li>{@link #POPUP_INFO} Sirve para dar informacion.
	 * <li>{@link #POPUP_OK} Sirve para informar un proceso exitoso.
	 * <li>{@link #POPUP_ABOUT} Sirve para dar información de la aplicacion.
	 * </ul>
	 * Si el parametro reference es <tt>null</tt> el {@link Popup} se centrará
	 * en la pantalla.
	 * @param message
	 *            El mensaje que se va a mostrar en el popup
	 * @param type
	 *            El tipo de popup.
	 * @param reference
	 *            Objeto que extiende de {@link Component} que se tomará de
	 *            referencia para ubicar al {@link Popup} en pantalla.
	 * @return una instancia de {@link Popup}
	 */
	public static Popup getInstance(String message, PopupType type, Component reference)
	{
		instance = new Popup();
		instance.listener = null;
		instance.type = type;
		instance.message = message;
		instance.icon = null;
		instance.reference = reference;
		return instance;
	}

	/**
	 * Devuelve una instancia de {@link Popup}.
	 * <p/>
	 * El popup puede ser alguno de estos tipos:
	 * <ul>
	 * <li>{@link #POPUP_FATAL} Sirve para indicar un error fatal en la
	 * aplicacion.
	 * <li>{@link #POPUP_ERROR} Sirve para indicar un error en la aplicacion.
	 * <li>{@link #POPUP_WARNING} Sirve para dar una adevertencia.
	 * <li>{@link #POPUP_INFO} Sirve para dar informacion.
	 * <li>{@link #POPUP_OK} Sirve para informar un proceso exitoso.
	 * <li>{@link #POPUP_ABOUT} Sirve para dar información de la aplicacion.
	 * </ul>
	 * Si el parametro reference es <tt>null</tt> el {@link Popup} se centrará
	 * en la pantalla.
	 * 
	 * @param message
	 *            El mensaje que se va a mostrar en el popup
	 * @param detail
	 *            El detalle del popup.
	 * @param type
	 *            El tipo de popup.
	 * @param reference
	 *            Objeto que extiende de {@link Component} que se tomará de
	 *            referencia para ubicar al {@link Popup} en pantalla.
	 * @param listener
	 *            Objeto que implemente {@link CallBackListener} que escuchara
	 *            el callback del popup
	 * @return una instancia de {@link Popup}
	 */
	public static Popup getInstance(String message, PopupType type, Component reference, CallBackListener listener)
	{
		instance = new Popup();
		instance.listener = listener;
		instance.type = type;
		instance.message = message;
		instance.icon = null;
		instance.reference = reference;
		return instance;
	}

	//**************************************************************************************
	// ATRIBUTOS
	//**************************************************************************************

	private static final long	serialVersionUID	= 1L;

	private CallBackListener	listener;

	private int 				option;

	private PopupType			type;

	private String				message;

	private Icon 				icon;

	private Component			reference;

	//**************************************************************************************
	// METODOS PUBLICOS
	//**************************************************************************************

	/**
	 * Muestra el {@link Popup} en modo Always on top
	 */
	public void showGui()
	{
		switch (this.type.getType())
		{
			case PopupType.WARNING:
			case PopupType.INFO:
			case PopupType.ERROR:
				JOptionPane.showMessageDialog
				(
						this.reference,
						this.message,
						this.type.getTitle(),
						this.type.getType()
				);
				break;
			case PopupType.CONFIRM:
				this.option = JOptionPane.showConfirmDialog
				(
						this.reference,
						this.message,
						this.type.getTitle(),
						JOptionPane.YES_NO_OPTION
				);
				notifyListener();
				break;
			case PopupType.PICTURE:
				JOptionPane.showMessageDialog
				(
						this.reference,
						"",
						"",
						JOptionPane.PLAIN_MESSAGE,
						this.icon
				);
				break;

			default:
				break;
		}
	}

	/**
	 * Muestra el {@link Popup}.
	 * 
	 * @param alwaysOnTop
	 *            Indica si el {@link Popup} se muestra por sobre
	 *            todos los demás componentes en pantalla
	 */
	public void showGui (boolean alwaysOnTop)
	{
		showGui();
	}

	//**************************************************************************************
	// METODOS PRIVADOS
	//**************************************************************************************

	private void notifyListener ()
	{
		switch (this.option)
		{
			case JOptionPane.YES_OPTION:
				this.listener.receiveCallBack(ACTION_CONFIRM);
			case JOptionPane.NO_OPTION:
				this.listener.receiveCallBack(ACTION_NOT_CONFIRM);
			case JOptionPane.CANCEL_OPTION:
				this.listener.receiveCallBack(ACTION_CANCEL);
			case JOptionPane.CLOSED_OPTION:
				this.listener.receiveCallBack(ACTION_CLOSE);
				break;

			default:
				break;
		}
	}

	//**************************************************************************************
	// CONSTANTES
	//**************************************************************************************

	public static final int			ACTION_CANCEL		= 0;
	public static final int			ACTION_CONFIRM		= ACTION_CANCEL + 1;
	public static final int			ACTION_NOT_CONFIRM	= ACTION_CONFIRM + 1;
	public static final int			ACTION_KNOWLEDGE	= ACTION_NOT_CONFIRM + 1;
	public static final int			ACTION_CLOSE		= ACTION_KNOWLEDGE + 1;

	public static final PopupType	POPUP_WARNING		= new PopupType("Advertencia", "/images/warning.png", PopupType.WARNING, new PopupButton("Ok", ACTION_KNOWLEDGE));
	public static final PopupType	POPUP_FATAL			= new PopupType("Error Fatal", "/images/fatal.png", PopupType.FATAL, new PopupButton("Cancel", ACTION_CANCEL));
	public static final PopupType	POPUP_ERROR			= new PopupType("Error", "/images/error.png", PopupType.ERROR, new PopupButton("Ok", ACTION_KNOWLEDGE));
	public static final PopupType	POPUP_INFO			= new PopupType("Información", "/images/info.png", PopupType.INFO, new PopupButton("Ok", ACTION_KNOWLEDGE));
	public static final PopupType	POPUP_CONFIRM		= new PopupType("Confirmación", "/images/confirm.png", PopupType.CONFIRM, new PopupButton("Si", ACTION_CONFIRM), new PopupButton("No", ACTION_NOT_CONFIRM));
	public static final PopupType	POPUP_ABOUT			= new PopupType("Acerca de", "/images/about.png", PopupType.ABOUT, new PopupButton("Ok", ACTION_KNOWLEDGE));
	public static final PopupType	POPUP_OK			= new PopupType("Éxito", "/images/ok.png", PopupType.OK, new PopupButton("Ok", ACTION_KNOWLEDGE));
	public static final PopupType	POPUP_PICTURE		= new PopupType("Imagen", null, PopupType.PICTURE, new PopupButton[]{});

	//**************************************************************************************
	// Inner Classes
	//**************************************************************************************

	/**
	 * Clase que guarda la configuracion básica de un {@link Popup}
	 * 
	 * @author emiliano
	 */
	private static class PopupType
	{
		static final int	WARNING	= JOptionPane.WARNING_MESSAGE;
		static final int	FATAL	= JOptionPane.ERROR_MESSAGE;
		static final int	ERROR	= JOptionPane.ERROR_MESSAGE;
		static final int	INFO	= JOptionPane.INFORMATION_MESSAGE;
		static final int	CONFIRM	= JOptionPane.QUESTION_MESSAGE;
		static final int	ABOUT	= JOptionPane.INFORMATION_MESSAGE;
		static final int	OK		= JOptionPane.INFORMATION_MESSAGE;
		static final int	PICTURE	= JOptionPane.PLAIN_MESSAGE;

		/**
		 * Constructor de {@link PopupType}.
		 * @param title El titulo del {@link Popup}
		 * @param iconPath El path del popup.
		 * @param buttons Los botones que va a tener el popup.
		 */
		public PopupType(String title, String iconPath, int type, PopupButton...buttons)
		{
			this.title = title;
			this.type = type;
		}

		private final String			title;
		private final int				type;

		public int getType ()
		{
			return this.type;
		}
		public String getTitle()
		{
			return this.title;
		}
	}

	/**
	 * Clase que contiene la configuracion básica de un boton del {@link Popup}.
	 * Posee el nombre del boton y comando.
	 * 
	 * @author emiliano
	 */
	private static class PopupButton
	{
		/**
		 * 
		 * @param name
		 * @param command
		 */
		public PopupButton(String name, int command)
		{
		}
	}
}
