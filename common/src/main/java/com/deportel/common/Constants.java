package com.deportel.common;

public interface Constants
{
	public static final String		HABILITADO							= "S";
	public static final String		DESHABILITADO						= "N";

	// Error constants.
	public static final String		ERR_NONE							= "0";
	public static final String		ERR_NOT_A_FILE						= "1";
	public static final String		ERR_NOT_A_DIR						= "2";
	public static final String		ERR_FILE_NOT_FOUND					= "3";
	public static final String		ERR_PARSING_XML_FILE				= "4";
	public static final String		ERR_UPDATING_LAYOUTS_FILE			= "5";
	public static final String		ERR_GENERATING_INTERFACE			= "6";
	public static final String		ERR_LOADING_LAYOUT_FILE				= "7";
	public static final String		ERR_FILE_READING_FILE				= "8";
	public static final String		ERR_GENERATING_JSON_STRINGS			= "9";

	// Error levels.
	public static final int			ERR_LEV_ADVICE						= 0;
	public static final int			ERR_LEV_MESSAGE						= ERR_LEV_ADVICE + 1;
	public static final int			ERR_LEV_WARNING						= ERR_LEV_MESSAGE + 1;
	public static final int			ERR_LEV_ERROR						= ERR_LEV_WARNING + 1;
	public static final int			ERR_LEV_COUNT						= ERR_LEV_ERROR + 1;


	// String constants.
	public static final String		PATH_SEPARATOR						= "\\";
	public static final String		PATH_SEPARATOR_ALT					= "/";
	public static final String		BRACE_OPEN							= "{";
	public static final String		BRACE_CLOSE							= "}";
	public static final String		RETURN								= "\n";
	public static final String		SEMICOLON							= ";";
	public static final String		EQUALS								= "=";
	public static final String		SPACE								= " ";
	public static final String		TAB									= "\t";
	public static final String		DOUBLE_TAB							= TAB + TAB;
	public static final String		UNDER_SCORE							= "_";
	public static final String		YES									= "yes";
	public static final String		DOT									= ".";
	public static final String		COMMA								= ",";
	public static final String		MIDDLE_SCORE						= "-";
	public static final String		PACKAGE_SEPARATOR					= ".";
	public static final String		CLASS_EXTENSION						= "class";
	public static final String		JAR_EXTENSION						= "jar";
	public static final String		XML_EXTENSION						= "xml";
	public static final String		NULL								= "null";

	public static final String		INTERFACE_DECLARATION				= "public interface";
	public static final String		RESOURCES_INTERFACE					= "R";
	public static final String		PSI									= "public static int";
	public static final String		PSS									= "public static String";
	public static final String		PACKAGE								= "package";
	public static final String		RESOURCES_FOLDER					= "resources";
	public static final String		SETTINGS_FOLDER						= "settings";
	public static final String		SRC_FOLDER							= "src";
	public static final String		JAVA_EXT							= "java";

	public static final String		WINDOWS_LOOK_AND_FEEL				= "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	public static final String		SYNTHETICA_STANDARD_LOOK_AND_FEEL	= "de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel";

	public static final long		DEFAULT_SPLASH_TIME					= 3000;
	public static final int			HEXA_DECIMAL						= 16;
	public static final int			DECIMAL								= 10;
}
