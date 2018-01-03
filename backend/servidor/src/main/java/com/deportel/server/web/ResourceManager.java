package com.deportel.server.web;

import java.util.Properties;

import com.deportel.common.utils.Utils;

public class ResourceManager
{
	private static ResourceManager	instance;

	private static final String		TEXT_PROPERTIES_FILE		= "/config/core/server-texts.properties";
	private static final String		CONSTANTS_PROPERTIES_FILE	= "/config/core/server-constants.properties";

	private final Properties		texts;
	private final Properties		constants;

	private ResourceManager()
	{
		this.texts = Utils.loadProperties(TEXT_PROPERTIES_FILE, ResourceManager.class);
		this.constants = Utils.loadProperties(CONSTANTS_PROPERTIES_FILE, ResourceManager.class);
	}

	public static ResourceManager getInstance ()
	{
		if (instance == null)
		{
			instance = new ResourceManager();
		}
		return instance;
	}

	public String getText (String name)
	{
		return this.texts.getProperty(name);
	}

	public String getConst (String name)
	{
		return this.constants.getProperty(name);
	}

}
