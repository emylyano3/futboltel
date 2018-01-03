package com.deportel.server.service.form;

import static com.deportel.server.web.Resources.Constants.*;
import com.deportel.server.service.form.strategies.MobileDummyFormStrategy;
import com.deportel.server.service.form.strategies.MobileFormManagerStrategy;
import com.deportel.server.service.form.strategies.MobileRecommendationFormStrategy;
import com.deportel.server.service.form.strategies.MobileSuscriptionFormStrategy;
import com.deportel.server.web.ResourceManager;

/**
 * @author EMY
 */
public class MobileFormManagerFactory
{
	private final ResourceManager			rm	= ResourceManager.getInstance();

	private static MobileFormManagerFactory	instance;

	private MobileFormManagerFactory()
	{

	}

	public static MobileFormManagerFactory getInstance ()
	{
		synchronized (MobileFormManagerFactory.class)
		{
			if (instance == null)
			{
				instance = new MobileFormManagerFactory();
			}
		}
		return instance;
	}

	public MobileFormManagerStrategy getStrategy (String formType)
	{
		if (this.rm.getConst(SUSCRIPTION_FORM).equals(formType))
		{
			return new MobileSuscriptionFormStrategy();
		}
		else if (this.rm.getConst(RECOMMENDATION_FORM).equals(formType))
		{
			return new MobileRecommendationFormStrategy();
		}
		else
		{
			return new MobileDummyFormStrategy();
		}
	}

}
