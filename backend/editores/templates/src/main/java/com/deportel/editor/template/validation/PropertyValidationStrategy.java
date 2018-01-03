package com.deportel.editor.template.validation;

import com.deportel.editor.template.exception.InvalidPropertyException;

public abstract class PropertyValidationStrategy
{
	public abstract void validate (String value) throws InvalidPropertyException;
}
