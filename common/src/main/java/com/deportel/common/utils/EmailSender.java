package com.deportel.common.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.deportel.common.callback.CallBackListener;

/**
 * Simple demonstration of using the javax.mail API.
 * 
 * Run from the command line. Please edit the implementation to use correct email addresses and host name.
 */
public final class EmailSender implements Runnable
{
	private static final Logger	log	= Logger.getLogger(EmailSender.class);
	private final Properties	mailServerConfig;
	private final Thread		task;
	private String				toAddres;
	private String				subject;
	private String				body;
	private CallBackListener	listener;

	public EmailSender(Properties mailServerConfig)
	{
		this.mailServerConfig = mailServerConfig;
		this.task = new Thread(this);
	}

	public EmailSender(CallBackListener listener, Properties mailServerConfig)
	{
		this.mailServerConfig = mailServerConfig;
		this.task = new Thread(this);
	}

	/**
	 * Send a single email.
	 */
	public void sendEmail (String toAddres, String subject, String body)
	{
		log.debug("TO: " + toAddres);
		log.debug("SUBJECT: " + subject);
		log.debug("BODY: " + body);
		this.toAddres = toAddres;
		this.subject = subject;
		this.body = body;
		this.task.start();
	}

	public static interface Configuration
	{
		public final static String	MAIL_USER_NAME	= "mail.user.name";
		public final static String	MAIL_USER_PASS	= "mail.user.password";
		public final static String	MAIL_SUBJECT	= "mail.subject";
		public final static String	MAIL_BODY		= "mail.body";
	}

	@Override
	public void run ()
	{
		Session session = Session.getInstance
		(
				this.mailServerConfig,
				new javax.mail.Authenticator()
				{
					@Override
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication
						(
								EmailSender.this.mailServerConfig.getProperty(Configuration.MAIL_USER_NAME),
								EmailSender.this.mailServerConfig.getProperty(Configuration.MAIL_USER_PASS)
						);
					}
				}
		);

		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.mailServerConfig.getProperty(Configuration.MAIL_USER_NAME)));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.toAddres));
			message.setSubject(this.subject);
			message.setText(this.body);
			Transport.send(message);
		}
		catch (MessagingException e)
		{
			notifyError(e);
		}
	}

	private void notifyError (Throwable e)
	{
		log.error("No se pudo enviar el email. " + e.getMessage());
		if (this.listener != null)
		{
			this.listener.receiveCallBack(SEND_MAIL, e.getMessage());
		}
	}

	public static final String SEND_MAIL = "SEND_MAIL";
}
