package com.deportel.futboltel.editor.torneo.model;

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
	public static final String	PROPERTIES_FILE_PATH		= "/settings/torneoEditor.properties";
	public static final String	GUI_DEFINITION_FILE_PATH	= "GUI_DEFINITION_FILE_PATH";
	public static final String	TRAY_ICON_DESCRIPTION		= "TRAY_ICON_DESCRIPTION";


	public interface TRAY_ICON_PATH
	{
		public static final String	NORMAL		= "TRAY_ICON_PATH";
		public static final String	ROLL_OVER	= "TRAY_ICON_ROLLOVER_PATH";
		public static final String	PRESSED		= "TRAY_ICON_PRESSED_PATH";
	}

}
