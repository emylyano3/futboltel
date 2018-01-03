package com.deportel.editor.common.core;

import java.util.List;

import javax.swing.JPanel;

import com.deportel.administracion.modelo.Modulo;
import com.deportel.seguridad.exception.InvalidUserAccessException;

/**
 * @author Emy
 */
public interface EditorContainer
{
	/**
	 * Recive de manera generica acciones de los
	 * editores que posee el contenedor en ejecucion.
	 * Procesara la accion segun el editor, que viene
	 * representado por su nombre.
	 * @param editorName El nombre del editor que
	 * realiza la llamada
	 * @param action La accion que el editor realizo.
	 */
	public void receiveEditorAction (String editorName, String action);

	/**
	 * Prepara el workbench para el editor que se vaya a abrir.
	 * Preparar significa que dicho workbench se va a poner en visible
	 * al usuario. El workbench a preparar debe ser previamente
	 * inicializado. Si a la hora de hacerlo visible el workbench no
	 * fue inicializado, la invocacion a este metodo no tendra efecto.
	 * 
	 * @param editorName
	 * @since
	 */
	public void openEditor (String editorName);

	/**
	 * Devuelve la barra donde se alojan los
	 * iconos de estado de los diferentes
	 * editores abiertos dentro del contenedor
	 * @return {@link JPanel} Que representa a
	 * la tray bar del contenedor.
	 */
	public JPanel getTrayBar ();

	/**
	 * 
	 * @param editor
	 */
	public void initTrayIcon (Editor editor);

	/**
	 * 
	 * @param editor
	 */
	public void removeTrayIcon (Editor editor);

	/**
	 * 
	 * @param module
	 * @throws InvalidUserAccessException
	 */
	public void challengeUserAccess (Modulo module) throws InvalidUserAccessException;

	/**
	 * Devuelve los permisos de un usuario para un determinado modulo
	 * 
	 * @param module
	 *            El modulo del que se quieren saber los permisos de usuario
	 * @return Listado con los permisos del usuario
	 */
	public List<String> getUserPermissions (Modulo module);

	/**
	 * Devuelve la URL del server de contenido.
	 * @return
	 */
	public String getServerURL ();

}
