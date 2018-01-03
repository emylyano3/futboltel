package com.deportel.server.service.form.strategies;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

import com.deportel.common.utils.Utils;
import com.deportel.server.service.form.Form;
import com.deportel.server.service.form.exception.FormServiceException;
import com.deportel.server.web.ResourceManager;

/**
 * @author EMY
 */
public abstract class MobileFormManagerStrategy
{
	protected ResourceManager	rm	= ResourceManager.getInstance();

	protected Form				formTemplate;
	protected String			userId;
	protected String			application;
	protected String			customization;

	public MobileFormManagerStrategy()
	{
		this.formTemplate = new Form(Utils.loadProperties(getFormTemplateFile(), MobileRecommendationFormStrategy.class));
	}

	public void loadForm (HttpServletRequest request) throws FormServiceException, HibernateException
	{
		doLoad(request);
		validateFormData();
		doStuff();
	}

	public abstract String getResponse ();

	protected abstract void doLoad (HttpServletRequest request) throws FormServiceException;

	protected abstract void doStuff () throws FormServiceException;

	protected abstract void validateFormData () throws FormServiceException;

	protected abstract String getFormTemplateFile ();
}
