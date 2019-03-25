package org.miniredis.valuetypes;

public class BaseValue extends Value<Object> {
	public BaseValue()
	{
		super();
	}
	
	public BaseValue(Object value, int seconds)
	{
		super(value, seconds);
	}
}