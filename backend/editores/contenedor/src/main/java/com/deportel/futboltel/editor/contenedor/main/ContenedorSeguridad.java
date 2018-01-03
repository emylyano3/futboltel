package com.deportel.futboltel.editor.contenedor.main;

import java.util.HashMap;
import java.util.Map;

import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.common.Command;
import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.editor.common.core.Editor;
import com.deportel.seguridad.main.Seguridad;

/**
 * Clase que interactua con la capa se seguridad del sistema.
 * 
 * @author Emy
 */
public class ContenedorSeguridad implements CallBackListener
{
	private final Seguridad				security;

	private final Map<Modulo, Editor>	modules;

	public ContenedorSeguridad()
	{
		this.security = Seguridad.newInstance(this);
		this.modules = new HashMap<Modulo, Editor>();
	}

	/**
	 * 
	 */
	public void authenticateUser ()
	{
		this.security.start();
	}

	/**
	 * 
	 * @param module
	 */
	public void addModule (Modulo module, Editor editor)
	{
		this.modules.put(module, editor);
	}

	@Override
	public Object getData ()
	{
		return null;
	}

	@Override
	public void receiveCallBack (int data)
	{

	}

	@Override
	public void receiveCallBack (String action, CallBackLauncher laucher)
	{

	}

	@Override
	public void receiveCallBack (String action, Object data)
	{
		if (Command.ACCEPT.equals(action) && data != null)
		{
			Contenedor.start((Usuario) data);
		}
	}
}
