package com.deportel.guiBuilder.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import com.deportel.common.Constants;
import com.deportel.common.action.ProjectActionStrategy;
import com.deportel.common.exception.PropertiesNotFoundException;
import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.gui.component.FileBrowser;
import com.deportel.guiBuilder.main.GuiBuilder;

/**
 * @author Emy
 *
 */
public class GuiBuilderAction extends ProjectActionStrategy
{

	private final static Logger log = Logger.getLogger(GuiBuilderAction.class);

	/**
	 * @since 24/02/2011
	 */
	@Override
	public Object openProject ()
	{
		log.debug("Abriendo el proyecto gui builder");
		String description = GuiBuilder.getProperties().getProperty(ProjectValues.PROJECT_DESCRIPTION);
		String extension = GuiBuilder.getProperties().getProperty(ProjectValues.PROJECT_EXTENSION);
		FileBrowser fb = new FileBrowser(description, extension, FileBrowser.OPEN_DIALOG);
		String filePicked = fb.selectFile();
		if (filePicked != null && !filePicked.equals(""))
		{
			try
			{
				return Utils.loadProperties(filePicked);
			}
			catch (PropertiesNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @since 24/02/2011
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object saveProject (Object data)
	{
		log.debug("Guardando el proyecto gui builder");
		Map<String, String> mapData = (Map <String, String>) data;
		StringBuilder sb = new StringBuilder();
		sb.append(ProjectValues.PACKAGE_NAME + Constants.EQUALS + mapData.get(ProjectValues.PACKAGE_NAME) + Constants.RETURN);
		sb.append(ProjectValues.GUI_DEFINITION_NAME + Constants.EQUALS + mapData.get(ProjectValues.GUI_DEFINITION_NAME) + Constants.RETURN);
		sb.append(ProjectValues.PROJECT_PATH + Constants.EQUALS + mapData.get(ProjectValues.PROJECT_PATH) + Constants.RETURN);
		sb.toString().replace(Constants.PATH_SEPARATOR, Constants.PATH_SEPARATOR_ALT);
		String description = GuiBuilder.getProperties().getProperty(ProjectValues.PROJECT_DESCRIPTION);
		String extension = GuiBuilder.getProperties().getProperty(ProjectValues.PROJECT_EXTENSION);
		FileBrowser fb = new FileBrowser(description, extension, FileBrowser.SAVE_DIALOG);
		String filePicked = fb.selectFile();
		if (filePicked != null && !filePicked.equals(""))
		{
			File file = new File (filePicked);
			try
			{
				Writer w = new FileWriter(file);
				w.write(sb.toString());
				w.close();
				return filePicked;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.action.ProjectActionStrategy#openView(com.deportel.futboltel.common.View, java.lang.Object)
	 */
	@Override
	public void openView (Object data)
	{
		// TODO Auto-generated method stub
	}

}
