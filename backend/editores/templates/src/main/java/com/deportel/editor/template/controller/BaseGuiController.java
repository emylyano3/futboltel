package com.deportel.editor.template.controller;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.deportel.common.callback.CallBackListener;
import com.deportel.controller.CacheController;
import com.deportel.editor.common.core.EditorImpl;
import com.deportel.editor.common.core.ServerNotifier;
import com.deportel.guiBuilder.gui.component.ComponentObserver;
import com.deportel.guiBuilder.model.GuiManager;

public abstract class BaseGuiController implements CallBackListener, ComponentObserver
{
	private final ServerNotifier	serverNotifier	= new ServerNotifier();

	private static final Logger		log				= Logger.getLogger(BaseGuiController.class);

	protected final GuiManager		manager;

	protected final CacheController cacheController;

	public BaseGuiController(GuiManager manager)
	{
		this.manager = manager;
		this.cacheController = new CacheController();
	}

	public abstract void save ();

	public abstract void enableMode ();

	public abstract EditorImpl getWindow ();

	public void updateServerCache (String url)
	{
		this.serverNotifier.updateServerCache(url);
	}

	public void refreshCache ()
	{
		try
		{
			this.cacheController.reloadCache();
			getWindow().refreshCache();
		}
		catch (IOException e)
		{
			log.warn("No se pudo refrescar correctamente la CACHE");
		}
	}
}
