package com.deportel.editor.common.core;

/**
 * Realiza el log sobre la barra de estado del editor para el cual se utilice. Es util para enviar mensajes al
 * usuario.
 * 
 * @author Emy
 * @since 17/03/2011
 */
public class EditorLog
{

	/**
	 * Constructor.
	 * 
	 * @param editor
	 *            {@link Editor} al que estara asociado el logger.
	 */
	private EditorLog(Editor editor)
	{
		this.editor = editor;
	}

	/**
	 * Devuelve una instancia de la clase.
	 * 
	 * @param editor
	 *            El editor al que estara asociado el logger.
	 * @return {@link EditorLog} Una instancia de la clase.
	 */
	public static EditorLog getLogger (Editor editor)
	{
		return new EditorLog(editor);
	}

	private final Editor	editor;

	/**
	 * Imprime un mensaje en la barra de estado del editor. El mensaje se muestra con la font por default del
	 * editor.
	 * 
	 * @param message
	 *            El mensaje que se desea mostrar
	 */
	public void debug (String message)
	{
		this.editor.getStateBar().setMessage(message);
	}
}
