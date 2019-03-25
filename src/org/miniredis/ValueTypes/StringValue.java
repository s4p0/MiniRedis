package org.miniredis.ValueTypes;

public class StringValue extends BaseValue {
	public StringValue()
	{
		super();
	}
	
	public StringValue(String value, int seconds)
	{
		super(value, seconds);
	}
}
