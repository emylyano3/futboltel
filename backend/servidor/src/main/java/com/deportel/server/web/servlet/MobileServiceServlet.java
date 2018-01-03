package com.deportel.server.web.servlet;

import static com.deportel.server.web.Resources.Constants.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.deportel.common.utils.Encoder;
import com.deportel.componentes.controller.ClienteController;
import com.deportel.componentes.controller.TransaccionController;
import com.deportel.componentes.modelo.Cliente;
import com.deportel.componentes.modelo.Transaccion;
import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;
import com.deportel.server.web.ConnectionConstants;
import com.deportel.server.web.MobileRequestProcessor;
import com.deportel.server.web.ResourceManager;

/**
 *
 * @author Emy
 */
public class MobileServiceServlet extends HttpServlet
{
	private static final long		serialVersionUID	= 1L;

	private static final Logger		log					= Logger.getLogger(MobileServiceServlet.class);

	private final ClienteController	clienteController	= ClienteController.getInstance(false);

	private final ResourceManager	rm					= ResourceManager.getInstance();

	static
	{
		ComponentesSessionFactoryUtil.getInstance().getSessionFactory();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * Procesa el request recibido desde algun dispositivo movil.
	 *
	 * @param request
	 *            Request recibido
	 * @param response
	 *            Response a enviar
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InvalidParameterException, IllegalArgumentException
	{
		long start = System.currentTimeMillis();
		log.info("Request de App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "] User [" + request.getAttribute(this.rm.getConst(CONN_PARAM_USER_NAME)) + "]");
		MobileRequestProcessor processor = new MobileRequestProcessor();
		ByteArrayOutputStream processResult = processor.processRequest(request);
		ServletOutputStream sos = response.getOutputStream();
		sos.write(Encoder.encode(processResult.toByteArray()));
		sos.close();
		long elapsed = System.currentTimeMillis() - start;
		log.info("Se envio la respuesta al request de la App: [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "]. Time elapsed: " + elapsed + "ms");
		registerTransaccion(request, elapsed);
	}

	private void registerTransaccion (HttpServletRequest request, long elapsed)
	{
		TransaccionController transaccionController = TransaccionController.getInstance(false);
		String clientId = (String) request.getAttribute(ConnectionConstants.CONN_PARAM_USER_ID);
		Cliente client = findClient(clientId);
		Date date = new Date(System.currentTimeMillis());
		Transaccion transaccion = new Transaccion(client, date, elapsed);
		transaccionController.create(transaccion);
	}

	private Cliente findClient (String clientId)
	{
		log.debug("Buscando el cliente con id " + clientId);
		try
		{
			Integer id = Integer.parseInt(clientId);
			return this.clienteController.findById(id);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
