package com.deportel.editor.administracion.model;

/**
 * @author Emy
 */
public interface ProjectValues
{
	public final static String	APP_NAME					= "APP_NAME";
	public final static String	PROJECT_PATH				= "PROJECT_PATH";
	public final static String	PROJECT_NAME				= "PROJECT_NAME";
	public final static String	PACKAGE_NAME				= "PACKAGE_NAME";
	public final static String	GUI_DEFINITION_NAME			= "GUI_DEFINITION_NAME";
	public final static String	PROJECT_DESCRIPTION			= "GUI_BUILDER_PROJECT_DESCRIPTION";
	public final static String	PROJECT_EXTENSION			= "GUI_BUILDER_PROJECT_EXTENSION";
	public final static String	LANGUAGE					= "LANGUAGE";
	public final static String	TEXTS_FILE_PATH				= "TEXTS_FILE_PATH";
	public static final String	PROPERTIES_FILE_PATH		= "/settings/administracionEditor.properties";
	public static final String	GUI_DEFINITION_FILE_PATH	= "GUI_DEFINITION_FILE_PATH";
	public static final String	TRAY_ICON_DESCRIPTION		= "TRAY_ICON_DESCRIPTION";
	public static final String	LOG_PROPERTIES_PATH			= "/settings/log4j.properties";
	public static final String	MENU_ICON					= "MENU_ICON";
	public static final String	EMAIL_SERVER_CONFIG			= "EMAIL_SERVER_CONFIG";

	public static final String	PERMISSION_CREATE			= "PERMISSION_CREATE";
	public static final String	PERMISSION_EDIT				= "PERMISSION_EDIT";
	public static final String	PERMISSION_DELETE			= "PERMISSION_DELETE";
	public static final String	PASS_PLACEHOLDER			= "PASS_PLACEHOLDER";
	public static final String	USER_PLACEHOLDER			= "USER_PLACEHOLDER";

	public interface TRAY_ICON_PATH
	{
		public static final String	NORMAL		= "TRAY_ICON_PATH";
		public static final String	ROLL_OVER	= "TRAY_ICON_ROLLOVER_PATH";
		public static final String	PRESSED		= "TRAY_ICON_PRESSED_PATH";
	}

	// TABLES
	public static final int		COL_USUARIO_ID 				= 0;
	public static final int		COL_USUARIO_ALIAS			= 1;
	public static final int		COL_USUARIO_NOMBRE			= 2;
	public static final int		COL_USUARIO_APELLIDO		= 3;
	public static final int		COL_USUARIO_ESTADO			= 4;

	public static final int		COL_TIPO_PER_ID				= 0;
	public static final int		COL_TIPO_PER_NOMBRE			= 1;
	public static final int		COL_TIPO_PER_DESCRIPCION	= 2;

	public static final int		COL_MODULO_ID				= 0;
	public static final int		COL_MODULO_NOMBRE			= 1;
	public static final int		COL_MODULO_DESCRIPCION		= 2;
	public static final int		COL_MODULO_ESTADO			= 3;

	public static final int		COL_PERFIL_ID				= 0;
	public static final int		COL_PERFIL_NOMBRE			= 1;
	public static final int		COL_PERFIL_DESCRIPCION		= 2;
	public static final int		COL_PERFIL_MODULO			= 3;
	public static final int		COL_PERFIL_ESTADO			= 4;

	public static interface Texts
	{
		public static final String	ERROR_INPUT_DATA_LENGTH	= "ERROR_INPUT_DATA_LENGTH";
		public static final String	ERROR_DATA_CONSTRAINT	= "ERROR_DATA_CONSTRAINT";
		public static final String	ERROR_DATA_CREATION		= "ERROR_DATA_CREATION";
	}
}
