package com.deportel.editor.contenido.controller;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.deportel.common.utils.Converter;
import com.deportel.controller.GlobalConsultasController;
import com.deportel.editor.contenido.view.QTWindow;
import com.deportel.editor.contenido.view.R;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.persistencia.utils.QueryParam;

/**
 * @author Emy
 */
public class QTController
{
	private final static Logger				log					= Logger.getLogger(QTController.class);

	private final GuiManager				manager;

	private final QTWindow					window;

	private final CEGuiController			ceGuiController;

	private final GlobalConsultasController	consultasController	= new GlobalConsultasController(true);

	private static QTController				instance;

	private QTController (GuiManager manager, CEGuiController ceGuiController)
	{
		this.manager = manager;
		this.ceGuiController = ceGuiController;
		this.window = QTWindow.getInstance(this.manager, this);
	}

	public static synchronized QTController getInstance (GuiManager manager, CEGuiController ceGuiController)
	{
		if (instance == null)
		{
			instance = new QTController(manager, ceGuiController);
		}
		return instance;
	}

	public void openWindow ()
	{
		try
		{
			this.window.createAndShowGui();
		}
		catch (InvalidParameterException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void testQueryInWindow ()
	{
		String query = "";
		try
		{
			query = this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText();
			this.window.setInParamsValues();
			List<?> result = this.consultasController.executeCustomQuery(query, getQueryInParams());
			int rowLimit = Converter.stringToInt(this.manager.getTextField(R.TEXT_FIELDS.ROW_LIMIT).getText());
			if (rowLimit != 0 && rowLimit < result.size())
			{
				result = result.subList(0, rowLimit);
			}
			this.window.dumpResultIntoGrid(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String params = "", message;
			for (QueryParam param : getQueryInParams())
			{
				params += "(Name:" + param.getName() + " Type:" + param.getType() + " Value:" + param.getValue() + ")";
			}
			message = "Error ejecutando la query: [" + query + "] con los siguientes parametros: [" + params + "]";
			log.error(message);
			Popup.getInstance
			(
					message,
					Popup.POPUP_ERROR
			)
			.showGui();
		}
	}

	public void testQuery () throws Exception
	{
		String query = "";
		query = this.manager.getTextArea(R.TEXT_AREAS.SQL_CONSULTA).getText();
		this.window.setInParamsValues();
		this.consultasController.executeCustomQuery(query, getQueryInParams());
	}

	/**
	 * Toma los parametros de entrada desde el controller del editor
	 * de contenidos y los inserta en un array list.
	 */
	public List<QueryParam> getQueryInParams()
	{
		return new ArrayList<QueryParam>(this.ceGuiController.getQueryInParams().values());
	}

	public Map<String, QueryParam> getQueryOutParams ()
	{
		return this.ceGuiController.getQueryOutParams();
	}

}
