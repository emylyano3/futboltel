package com.deportel.editor.template.controller;

import com.deportel.editor.template.view.R;
import com.deportel.guiBuilder.model.GuiManager;

public class GuiControllerFactory
{
	private final GuiManager manager;

	public GuiControllerFactory (GuiManager manager)
	{
		this.manager = manager;
	}

	public BaseGuiController getController (String mode)
	{
		if (R.BUTTONS.TI_MODE.equals(mode))
		{
			return TIGuiController.getInstance(this.manager);
		}
		else if (R.BUTTONS.CE_EDIT_MODE.equals(mode))
		{
			return TEGuiController.getInstance(this.manager);
		}
		else
		{
			return null;
		}
	}
}
