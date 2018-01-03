package com.deportel.editor.common.core;

/**
 * Contiene las acciones genericas que pueden
 * ser invocadas por el usuario a través de los
 * controles de la pantalla.
 * @author Emy
 * @since 03/02/2011
 */
public interface UserActions
{
	public final static byte	CREATE			= 0;
	public final static byte	DELETE			= 1;
	public final static byte	COPY			= 2;
	public final static byte	PASTE			= 3;
	public final static byte	MOVE			= 4;
	public final static byte	UPDATE			= 5;
	public final static byte	OPEN			= 6;
	public final static byte	CLOSE			= 7;
	public final static byte	BACK			= 7;

	public final static byte 	NEUTRAL_ACTION 	= 99;
}
