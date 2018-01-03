package com.deportel.persistencia.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.hibernate.Query;

import com.deportel.common.utils.Utils;

/**
 * @author Emy
 */
public abstract class ConsultaUtils
{

	/**
	 * 
	 * @param query
	 * @param params
	 */
	public static void setQueryParamsInOrder (Query query, List<QueryParam> params)
	{
		for (int i = 0; i < params.size(); i++)
		{
			setQueryParam(query, i, params.get(i));
		}
	}

	/**
	 * 
	 * @param query
	 * @param params
	 */
	public static void setQueryParams (Query query, List<QueryParam> params)
	{
		for (int i = 0; i < params.size(); i++)
		{
			setQueryParam(query, params.get(i));
		}
	}

	/**
	 * 
	 * @param query
	 * @param index
	 * @param param
	 */
	public static void setQueryParam (Query query, int index, QueryParam param)
	{
		try
		{
			if (INTEGER.equals(param.getType()))
			{
				query.setBigDecimal(index, new BigDecimal((String) param.getValue()));
			}
			else if (DATE.equals(param.getType()))
			{
				query.setDate(index, Utils.parseDate((String) param.getValue()));
			}
			else if (VARCHAR.equals(param.getType()))
			{
				query.setString(index, (String) param.getValue());
			}
		}
		catch (ParseException e)
		{
			/*
			 * Si fallo el parseo al tipo date, lo seteo como
			 * texto. De ultima que explote la query al ir a 
			 * la base.
			 */
			query.setString(index, (String) param.getValue());
		}
		catch (NumberFormatException e)
		{
			/*
			 * Si fallo la credacion del bigdecimal, lo seteo como
			 * texto. De ultima que explote la query al ir a 
			 * la base.
			 */
			query.setString(index, (String) param.getValue());
		}
	}

	/**
	 * 
	 * @param query
	 * @param index
	 * @param param
	 */
	public static void setQueryParam (Query query, QueryParam param)
	{
		try
		{
			if (INTEGER.equals(param.getType()))
			{
				query.setBigDecimal(param.getName(), new BigDecimal((String) param.getValue()));
			}
			else if (DATE.equals(param.getType()))
			{
				query.setDate(param.getName(), Utils.parseDate((String) param.getValue()));
			}
			else if (VARCHAR.equals(param.getType()))
			{
				query.setString(param.getName(), (String) param.getValue());
			}
		}
		catch (ParseException e)
		{
			/*
			 * Si fallo el parseo al tipo date, lo seteo como
			 * texto. De ultima que explote la query al ir a 
			 * la base.
			 */
			query.setString(param.getName(), (String) param.getValue());
		}
		catch (NumberFormatException e)
		{
			/*
			 * Si fallo la credacion del bigdecimal, lo seteo como
			 * texto. De ultima que explote la query al ir a 
			 * la base.
			 */
			query.setString(param.getName(), (String) param.getValue());
		}
	}

	public static final String	INTEGER	= "Entero";
	public static final String	VARCHAR	= "Texto";
	public static final String	DATE	= "Fecha";
	public static final String	ENTITY	= "Entidad";
}
