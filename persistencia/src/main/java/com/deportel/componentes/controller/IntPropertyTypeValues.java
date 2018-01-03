package com.deportel.componentes.controller;

public class IntPropertyTypeValues
{
	public IntPropertyTypeValues(int min, int max)
	{
		super();
		this.min = min;
		this.max = max;
	}

	int	min;
	int	max;

	public int getMin ()
	{
		return this.min;
	}

	public void setMin (int min)
	{
		this.min = min;
	}

	public int getMax ()
	{
		return this.max;
	}

	public void setMax (int max)
	{
		this.max = max;
	}
}
