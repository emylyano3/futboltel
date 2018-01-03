package com.deportel.guiBuilder.gui.validator;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class TextLimitValidator extends PlainDocument
{
	private static final long		serialVersionUID	= 1L;

	private final Integer			textLimit;

	public TextLimitValidator(JTextComponent component, Integer textLimit)
	{
		this.textLimit = textLimit;
	}

	@Override
	public void insertString (int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
		{
			return;
		}
		if ((getLength() + str.length()) <= this.textLimit)
		{
			super.insertString(offset, str, attr);
		}
	}
}
