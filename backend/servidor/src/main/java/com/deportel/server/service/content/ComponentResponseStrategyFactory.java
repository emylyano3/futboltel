package com.deportel.server.service.content;

import com.deportel.server.service.content.strategies.ButtonResponseStrategy;
import com.deportel.server.service.content.strategies.ComponentResponseStrategy;
import com.deportel.server.service.content.strategies.LabelResponseStrategy;
import com.deportel.server.service.content.strategies.TableResponseStrategy;


/**
 * @author Emy
 */
public class ComponentResponseStrategyFactory
{
	public ComponentResponseStrategy getStrategy (String componentType)
	{
		if ("Button".equalsIgnoreCase(componentType))
		{
			return new ButtonResponseStrategy();
		}
		else if ("Table".equalsIgnoreCase(componentType))
		{
			return new TableResponseStrategy();
		}
		else if ("Label".equalsIgnoreCase(componentType))
		{
			return new LabelResponseStrategy();
		}
		return null;
	}

}
