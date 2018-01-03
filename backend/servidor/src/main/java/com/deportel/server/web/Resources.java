package com.deportel.server.web;

public interface Resources
{
	public static interface Texts
	{
		public static final String	VAL_ERROR_EMAIL					= "VAL_ERROR_EMAIL";
		public static final String	VAL_ERROR_CEL_NUMBER			= "VAL_ERROR_CEL_NUMBER";
		public static final String	VAL_ERROR_NULL_NAME				= "VAL_ERROR_NULL_NAME";
		public static final String	FORM_ERROR_SUSCR_LOAD			= "FORM_ERROR_SUSCR_LOAD";
		public static final String	FORM_ERROR_RECOM_SEND			= "FORM_ERROR_RECOM_SEND";
		public static final String	MOBILE_REQUEST_NO_DATA			= "MOBILE_REQUEST_NO_DATA";
		public static final String	ERROR_NO_QUERY_FOR_NAME			= "ERROR_NO_QUERY_FOR_NAME";
		public static final String	ERROR_NO_QUERY_FOR_SOURCE		= "ERROR_NO_QUERY_FOR_SOURCE";
		public static final String	ERROR_NO_QUERY_FOUND			= "ERROR_NO_QUERY_FOUND";
		public static final String	ERROR_MISS_PARAMS_DYNA_QUERY	= "ERROR_MISS_PARAMS_DYNA_QUERY";
		public static final String	ERROR_SERVER_INTERNAL			= "ERROR_SERVER_INTERNAL";
	}

	public static interface Constants
	{
		public static final String	SPLITTER_ITEMS					= "SPLITTER_ITEMS";
		public static final String	SPLITTER_RESPONSE				= "SPLITTER_RESPONSE";
		public static final String	SPLITTER_QUERY_PARAMS			= "SPLITTER_QUERY_PARAMS";
		public static final String	SPLITTER_QUERY_PARAM			= "SPLITTER_QUERY_PARAM";
		public static final String	SPLITTER_TABLE_DATA				= "SPLITTER_TABLE_DATA";

		public static final String	CONN_PARAM_BJAE					= "CONN_PARAM_BJAE";
		public static final String	CONN_PARAM_RESULT				= "CONN_PARAM_RESULT";
		public static final String	CONN_PARAM_TARGET				= "CONN_PARAM_TARGET";
		public static final String	CONN_PARAM_SOURCE				= "CONN_PARAM_SOURCE";
		public static final String	CONN_PARAM_SCW					= "CONN_PARAM_SCW";
		public static final String	CONN_PARAM_SCH					= "CONN_PARAM_SCH";
		public static final String	CONN_PARAM_REQ_TYPE				= "CONN_PARAM_REQ_TYPE";
		public static final String	CONN_PARAM_REQ_SUB_TYPE			= "CONN_PARAM_REQ_SUB_TYPE";
		public static final String	CONN_PARAM_USER_ID				= "CONN_PARAM_USER_ID";
		public static final String	CONN_PARAM_USER_NAME			= "CONN_PARAM_USER_NAME";
		public static final String	CONN_PARAM_APP_NAME				= "CONN_PARAM_APP_NAME";
		public static final String	CONN_PARAM_APP_CUSTOMIZATION	= "CONN_PARAM_APP_CUSTOMIZATION";
		public static final String	CONN_PARAM_ITEMS				= "CONN_PARAM_ITEMS";
		public final static String	CONN_PARAM_MESSAGE				= "CONN_PARAM_MESSAGE";
		public final static String	CONN_PARAM_SERVER_CMD			= "CONN_PARAM_SERVER_CMD";
		public final static String	CONN_PARAM_SERVER_CMD_PARAMS	= "CONN_PARAM_SERVER_CMD_PARAMS";
		public final static String	CONN_PARAM_QUERY_PARAMS			= "CONN_PARAM_QUERY_PARAMS";
		public final static String	CONN_PARAM_QUERY_NAME			= "CONN_PARAM_QUERY_NAME";

		public final static String	STATE_ERROR						= "STATE_ERROR";
		public final static String	STATE_OK						= "STATE_OK";

		/** Utilizado para enviar informacion al server */
		public static final String	SEND_FORM						= "SEND_FORM";

		/** Utilizado para pedir al server que se traiga determinado contenido en forma de componentes */
		public static final String	CONTENT_REQUEST					= "CONTENT_REQUEST";

		/** Utilizado para pedir al server que se traigan los hijos de uno o varios componentes */
		public static final String	CHILDS_REQUEST					= "CHILDS_REQUEST";

		/** Utilizado para pedir al server que se traigan las propiedades de uno o varios componentes */
		public static final String	COMPONENTS_UPDATE				= "COMPONENTS_UPDATE";

		public static final String	SUSCRIPTION_FORM				= "SUSCRIPTION_FORM";

		public static final String	RECOMMENDATION_FORM				= "RECOMMENDATION_FORM";
	}
}
