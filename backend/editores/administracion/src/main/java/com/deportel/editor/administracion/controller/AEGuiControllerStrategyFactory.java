package com.deportel.editor.administracion.controller;

import com.deportel.editor.administracion.view.R;
import com.deportel.guiBuilder.model.GuiManager;


/**
 * @author Emy
 */
public abstract class AEGuiControllerStrategyFactory
{
	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public static AEGuiControllerStrategy getStrategy (GuiManager manager, String context)
	{
		if (context.equals(R.RADIO_BUTTON_MENU_ITEMS.MODULO))
		{
			return ModuloGuiController.getInstance(manager);
		}
		else if (context.equals(R.RADIO_BUTTON_MENU_ITEMS.TIPO_PERMISO))
		{
			return TipoPermisoGuiController.getInstance(manager);
		}
		else if (context.equals(R.RADIO_BUTTON_MENU_ITEMS.PERFIL))
		{
			return PerfilGuiController.getInstance(manager);
		}
		else if (context.equals(R.RADIO_BUTTON_MENU_ITEMS.USUARIO))
		{
			return UsuarioGuiController.getInstance(manager);
		}
		else
		{
			//TODO Lanzar una exceptcion
			return null;
		}
	}
}
