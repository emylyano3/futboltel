package com.deportel.editor.common.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class ServerNotifier
{
	private static Logger log = Logger.getLogger(ServerNotifier.class);

	public void updateServerCache (String urlString)
	{
		try
		{
			log.info("Notificando al servidor el cambio de datos. URL: " + urlString);
			final URL url = new URL(urlString);
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			final BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			log.debug(response.readLine());
		}
		catch (final IOException e)
		{
			log.warn("No se pudo refrescar la cache del server en la url: " + urlString + ". Error causado por: " + e.getMessage());
		}
		catch (final Exception e)
		{
			log.error("No se pudo refrescar la cache del server en la url: " + urlString + ". Error causado por: " + e.getMessage());
		}
	}
}
