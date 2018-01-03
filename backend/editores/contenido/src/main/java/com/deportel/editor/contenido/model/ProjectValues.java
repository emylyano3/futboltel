package com.deportel.editor.contenido.model;

/**
 * @author Emy
 */
public interface ProjectValues
{
	public final static String	APP_NAME					= "APP_NAME";
	//	public final static String	MODULE_NAME					= "MODULE_NAME";
	public final static String	PROJECT_PATH				= "PROJECT_PATH";
	public final static String	PROJECT_NAME				= "PROJECT_NAME";
	public final static String	PACKAGE_NAME				= "PACKAGE_NAME";
	public final static String	GUI_DEFINITION_NAME			= "GUI_DEFINITION_NAME";
	public final static String	PROJECT_DESCRIPTION			= "GUI_BUILDER_PROJECT_DESCRIPTION";
	public final static String	PROJECT_EXTENSION			= "GUI_BUILDER_PROJECT_EXTENSION";
	public final static String	LANGUAGE					= "LANGUAGE";
	public final static String	TEXTS_FILE_PATH				= "TEXTS_FILE_PATH";
	public static final String	PROPERTIES_FILE_PATH		= "/settings/contenidoEditor.properties";
	public static final String	GUI_DEFINITION_FILE_PATH	= "GUI_DEFINITION_FILE_PATH";
	public static final String	QT_GUI_DEFINITION_FILE_PATH	= "QT_GUI_DEFINITION_FILE_PATH";
	public static final String	TRAY_ICON_DESCRIPTION		= "TRAY_ICON_DESCRIPTION";
	public static final String	LOG_PROPERTIES_PATH			= "/settings/log4j.properties";
	public static final String	SERVER_URL					= "SERVER_URL";
	public static final String	ROW_LIMIT					= "ROW_LIMIT";
	public static final String	COMP_TYPE_BUTTON			= "COMP_TYPE_BUTTON";
	public static final String	COMP_TYPE_LABEL				= "COMP_TYPE_LABEL";
	public static final String	COMP_TYPE_TABLE				= "COMP_TYPE_TABLE";
	public static final String	COMP_TYPE_SCREEN			= "COMP_TYPE_SCREEN";
	public static final String	COMP_TYPE_IMAGE				= "COMP_TYPE_IMAGE";
	public static final String	PERMISSION_CREATE			= "PERMISSION_CREATE";
	public static final String	PERMISSION_EDIT				= "PERMISSION_EDIT";
	public static final String	PERMISSION_DELETE			= "PERMISSION_DELETE";
	public static final String	MENU_ICON					= "MENU_ICON";

	public interface TRAY_ICON_PATH
	{
		public static final String	NORMAL		= "TRAY_ICON_PATH";
		public static final String	ROLL_OVER	= "TRAY_ICON_ROLLOVER_PATH";
		public static final String	PRESSED		= "TRAY_ICON_PRESSED_PATH";
	}
}
