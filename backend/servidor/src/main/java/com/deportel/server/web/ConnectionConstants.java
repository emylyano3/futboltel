package com.deportel.server.web;

/**
 * @author Emy
 */
public interface ConnectionConstants
{
	public static final String	SPLITTER_ITEMS					= ",";
	public static final String	SPLITTER_RESPONSE				= ">|<";
	public static final String	SPLITTER_QUERY_PARAMS			= ",";
	public static final String	SPLITTER_QUERY_PARAM			= ":";
	public static final String	SPLITTER_TABLE_DATA				= "|";

	public static final String	CONN_PARAM_BJAE					= "bjae";
	public static final String	CONN_PARAM_RESULT				= "result";
	public static final String	CONN_PARAM_TARGET				= "target";
	public static final String	CONN_PARAM_SOURCE				= "source";
	public static final String	CONN_PARAM_SCW					= "scw";
	public static final String	CONN_PARAM_SCH					= "sch";
	public static final String	CONN_PARAM_REQ_TYPE				= "rtype";
	public static final String	CONN_PARAM_REQ_SUB_TYPE			= "rstype";
	public static final String	CONN_PARAM_USER_ID				= "uid";
	public static final String	CONN_PARAM_USER_NAME			= "name";
	public static final String	CONN_PARAM_APP_NAME				= "apname";
	public static final String	CONN_PARAM_APP_CUSTOMIZATION	= "apcust";
	public static final String	CONN_PARAM_ITEMS				= "items";
	public final static String	CONN_PARAM_MESSAGE				= "message";
	public final static String	CONN_PARAM_SERVER_CMD			= "cmd";
	public final static String	CONN_PARAM_SERVER_CMD_PARAMS	= "cparam";
	public final static String	CONN_PARAM_QUERY_PARAMS			= "qparam";
	public final static String	CONN_PARAM_QUERY_NAME			= "qname";

	public final static String	STATE_ERROR						= "error";
	public final static String	STATE_OK						= "ok";

	/** Utilizado para enviar informacion al server */
	public static final String	SEND_FORM						= "SEND_FORM";

	/** Utilizado para pedir al server que se traiga determinado contenido en forma de componentes */
	public static final String	CONTENT_REQUEST					= "CONT_REQU";

	/** Utilizado para pedir al server que se traigan los hijos de uno o varios componentes */
	public static final String	CHILDS_REQUEST					= "CHIL_REQU";

	/** Utilizado para pedir al server que se traigan las propiedades de uno o varios componentes */
	public static final String	COMPONENTS_UPDATE				= "COMP_UPDA";

	public static final String	SUSCRIPTION_FORM				= "susc";

	public static final String	RECOMMENDATION_FORM				= "reco";

}
