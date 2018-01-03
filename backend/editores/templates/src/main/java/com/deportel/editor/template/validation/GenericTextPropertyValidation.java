package com.deportel.editor.template.validation;

import org.apache.commons.validator.routines.RegexValidator;

import com.deportel.editor.template.exception.InvalidPropertyException;
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectValues;

public class GenericTextPropertyValidation extends PropertyValidationStrategy
{
	private static final String PROP_VALIDATION_REGEX_TEXT;

	static
	{
		PROP_VALIDATION_REGEX_TEXT = TemplateEditor.getProperties().getProperty
		(
				ProjectValues.PROP_VALIDATION_REGEX_TEXT
		);
	}

	@Override
	public void validate (String value) throws InvalidPropertyException
	{
		RegexValidator rv = new RegexValidator(PROP_VALIDATION_REGEX_TEXT);
		if (rv.validate(value) == null)
		{
			throw new InvalidPropertyException("Alguna de las propiedades de texto tiene caracteres inválidos");
			//					throw new InvalidPropertyException(ProjectTexts.TE_ERROR_INVALID_TEXT_PROP);
		}
	}
}
