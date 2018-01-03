package com.deportel.guiBuilder.gui.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

public class MenuBar extends JMenuBar
{
	private static final long	serialVersionUID	= 1L;

	private static Logger log = Logger.getLogger(MenuBar.class);

	private List <String> menus = new ArrayList <String> ();

	/**
	 * Agrega un item de menu al menu especificado.
	 * Si el menu especificado no existe, la invocacion
	 * a este metodo no tendrá efecto alguno,
	 * @param menuName El nombre del menu al que se le va a agregar el item
	 * @param item El item a agregar
	 * @author Emy
	 * @since 03/02/2011
	 */
	public void addMenuItem (String menuName, JMenuItem item)
	{
		try
		{
			this.getMenu(findMenuIndex(menuName));
		}
		catch (NullPointerException e)
		{
			log.error("El menu especificado: " + menuName + " no se encontro, asi que el item: " + item.getName() + " no fue agregado al menu");
		}
	}

	/**
	 * Busca el nombre del menu en los menues agregados
	 * y devuelve la posicion en la que fue agregado
	 * @param menuName El nombre del menu buscado
	 * @return La posicion del menu
	 * @since 03/02/2011
	 */
	private int findMenuIndex (String menuName)
	{
		for (int i = 0; i < this.menus.size(); i++)
		{
			if (this.menus.get(i).equals(menuName))
			{
				return i;
			}
		}
		return -1;
	}
}
