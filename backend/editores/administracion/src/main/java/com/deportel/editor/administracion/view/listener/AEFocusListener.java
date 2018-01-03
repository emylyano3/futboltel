package com.deportel.editor.administracion.view.listener;

import java.awt.event.FocusEvent;

import javax.swing.JComponent;

import com.deportel.editor.administracion.controller.AEGuiControllerStrategy;
import com.deportel.editor.administracion.controller.AEGuiControllerStrategyFactory;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.common.core.EditorFocusListener;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class AEFocusListener extends EditorFocusListener
{
	/**
	 * @param manager
	 */
	public AEFocusListener(GuiManager manager)
	{
		super(manager);
	}

	public void focusGained (FocusEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		AEGuiControllerStrategy strategy = AEGuiControllerStrategyFactory.getStrategy(this.manager, AdministracionEditor.getContext());
		if (strategy.getListTable().equals(source))
		{
			strategy.listGridGainedFocus();
		}
	}

	public void focusLost (FocusEvent e)
	{
		//Do Nothing
	}

}
