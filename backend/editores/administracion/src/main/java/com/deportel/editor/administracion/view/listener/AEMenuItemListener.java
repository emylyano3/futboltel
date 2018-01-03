package com.deportel.editor.administracion.view.listener;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import com.deportel.editor.administracion.controller.AEGuiControllerStrategy;
import com.deportel.editor.administracion.controller.AEGuiControllerStrategyFactory;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.model.ProjectTexts;
import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * Clase que maneja los eventos de los menu items de la interfaz de
 * usuario del editor del modulo administracion.
 * 
 * @author Emy
 * @since 30/08/2011
 */
public class AEMenuItemListener extends EditorActionListener
{
	public AEMenuItemListener (GuiManager manager)
	{
		super(manager);
	}

	@Override
	public void actionPerformed (ActionEvent e)
	{
		JMenuItem source = (JMenuItem) e.getSource();
		AdministracionEditor.setContext(source.getName());
		AEGuiControllerStrategy strategy = AEGuiControllerStrategyFactory.getStrategy(this.manager, AdministracionEditor.getContext());
		try
		{
			strategy.changeView();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_EDITOR_CHANGE_VIEW), Popup.POPUP_ERROR).showGui();
		}
	}

}
