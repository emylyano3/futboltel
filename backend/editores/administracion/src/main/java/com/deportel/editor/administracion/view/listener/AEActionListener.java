package com.deportel.editor.administracion.view.listener;

import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;

import javax.swing.JComponent;

import com.deportel.editor.administracion.controller.AEGuiControllerStrategy;
import com.deportel.editor.administracion.controller.AEGuiControllerStrategyFactory;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.view.R;
import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * 
 * @author Emy
 * @since 30/08/2011
 */
public class AEActionListener extends EditorActionListener
{
	public AEActionListener(GuiManager manager)
	{
		super(manager);
	}

	private AEGuiControllerStrategy	strategy;

	private static boolean			okAlta			= false;
	private static boolean			okModificacion	= false;

	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.strategy = AEGuiControllerStrategyFactory.getStrategy(this.manager, AdministracionEditor.getContext());
		this.log.debug("Se presiono el componente [" + source.getName() + "] del modulo [" + AdministracionEditor.getContext() + "] del editor de administracion");
		if (source.getName().equalsIgnoreCase(R.BUTTONS.NEW))
		{
			okAlta = true;
			okModificacion = false;
			this.strategy.prepareAdd();
		}
		else if (source.getName().equalsIgnoreCase(R.BUTTONS.EDIT))
		{
			okModificacion = true;
			okAlta = false;
			this.strategy.prepareModify();
		}
		else if (source.getName().equalsIgnoreCase(R.BUTTONS.DELETE))
		{
			this.strategy.prepareDelete();
		}
		else if (source.getName().equalsIgnoreCase(R.BUTTONS.CANCEL))
		{
			this.strategy.cancelAction();
		}
		else if (source.getName().equalsIgnoreCase(R.BUTTONS.OK))
		{
			try
			{
				if (okAlta)
				{
					this.log.debug("Se presionó el botón Ok del Alta");
					this.strategy.safeAdd();
				}
				else if(okModificacion)
				{
					this.log.debug("Se presionó el botón Ok de Modificación");
					this.strategy.safeModify();
				}
			}
			catch (InvalidParameterException exec)
			{
				Popup.getInstance(exec.getMessage(), Popup.POPUP_ERROR).showGui();
			}
		}
		else
		{
			this.log.warn("Componente no existente: [" + source.getName() + "]");
		}
	}

}
