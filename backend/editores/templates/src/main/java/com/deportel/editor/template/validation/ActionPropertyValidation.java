package com.deportel.editor.template.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.AccionComponenteController;
import com.deportel.componentes.modelo.AccionComponente;
import com.deportel.editor.template.exception.InvalidPropertyException;

public class ActionPropertyValidation extends PropertyValidationStrategy
{
	private static final String	ACTIONS_SPLITTER	= ",";
	private static Set<String>	availAcions;

	static
	{
		List<AccionComponente> acciones = AccionComponenteController.getInstance(true).findAll();
		List<String> nombresAcciones = AccionComponenteController.getInstance(true).getAsNames(acciones);
		availAcions = new HashSet<String>(nombresAcciones);
	}

	@Override
	public void validate (String value) throws InvalidPropertyException
	{
		if (!Utils.isNullOrEmpty(value))
		{
			String[] actions = value.split(ACTIONS_SPLITTER);
			for (String string : actions)
			{
				if (!availAcions.contains(string))
				{
					throw new InvalidPropertyException("Se especificó una acción inexistente en la propiedad 'Action'");
					//					throw new InvalidPropertyException(ProjectTexts.TE_ERROR_INVALID_TEXT_PROP);
				}
			}
		}
	}

}
