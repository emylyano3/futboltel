package com.deportel.editor.template.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.deportel.common.utils.Utils;
import com.deportel.editor.template.exception.InvalidPropertyException;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectValues;

public class ActionParamsPropertyValidation extends PropertyValidationStrategy
{
	//	private static final String PROP_VALIDATION_REGEX_APARAM = TemplateEditor.getProperties().getProperty(ProjectValues.PROP_VALIDATION_REGEX_APARAM);
	private static final String PROP_VALIDATION_PARAMS_AVAIL = TemplateEditor.getProperties().getProperty(ProjectValues.PROP_VALIDATION_PARAMS_AVAIL);

	private static final List<String> availParams;

	static
	{
		availParams = Arrays.asList(PROP_VALIDATION_PARAMS_AVAIL.split(","));
	}

	@Override
	public void validate (String value) throws InvalidPropertyException
	{
		//		RegexValidator rv = new RegexValidator(PROP_VALIDATION_REGEX_APARAM);
		//		if (rv.validate(value) == null)
		//		{
		//			throw new InvalidPropertyException();
		//		}
		// TODO Arreglar la validacion por regex de los caracteres de las aparams
		if (!Utils.isNullOrEmpty(value))
		{
			try
			{
				String[] actionsParams = splitActionsParams(value);
				if (!Utils.isNullOrEmpty(actionsParams))
				{
					for (String actionParams : actionsParams)
					{
						if (!Utils.isNullOrEmpty(actionParams))
						{
							List<String[]> actionParamsSplitted = getActionParams(actionParams);
							if (!Utils.isNullOrEmpty(actionParamsSplitted))
							{
								for (String[] actionParam : actionParamsSplitted)
								{
									if (Utils.isNullOrEmpty(actionParam[0]) || !availParams.contains(actionParam[0]))
									{
										throw new InvalidPropertyException("El parámetro " + actionParam[0] + " no está dentro del conjunto de parámetros posibles.");
									}
								}
							}
							else
							{
								throw new InvalidPropertyException("Los parámetros están mal formados. Una de las action no declara parámetros.");
							}
						}
						else
						{
							throw new InvalidPropertyException("Los parámetros están mal formados. Una de las action no declara parámetros.");
						}
					}
				}
				else
				{
					throw new InvalidPropertyException("Los parámetros están mal formados. Una de las action no declara parámetros.");
				}
			}
			catch (InvalidPropertyException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new InvalidPropertyException("Los parámetros están mal formados");
			}
		}
	}

	private List<String[]> getActionParams (String value)
	{
		int start, end;
		List<String[]> result = new ArrayList<String[]>();
		if ((start = value.indexOf('[')) != -1 && (end = value.indexOf(']')) != -1)
		{
			if (start < end)
			{
				String paramsString = value.substring(start + 1, end);
				String[] params = paramsString.split(";");
				for (String param : params)
				{
					String[] yoke = param.split("=");
					result.add(yoke);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	private String[] splitActionsParams (String actionsParams)
	{
		List<String> params = new ArrayList<String>();
		String param;
		int begin = actionsParams.indexOf('{');
		int end = begin;
		while (begin < actionsParams.length() - 1 && begin != -1 && end != -1 && actionsParams.charAt(begin) == '{')
		{
			end = actionsParams.indexOf('}', begin);
			if (end != -1)
			{
				param = actionsParams.substring(begin + 1, end);
				params.add(param);
				begin = end + 1;
			}
		}
		return params.toArray(new String[params.size()]);
	}

}
