package org.miniredis.ValueTypes;

import java.util.Calendar;

public abstract class Value<T> {
	public T value;
	int seconds;
	long creation;
	
	public Value()
	{
		seconds = 0;
		creation = Calendar.getInstance().getTimeInMillis();
	}
	
	public Value (T value, int seconds)
	{
		this();
		this.seconds = seconds;
		this.value = value;
	}
		
	public boolean isExpired()
	{
		if(seconds > 0)
		{
			long now = Calendar.getInstance().getTimeInMillis();
			return now > (creation + seconds * 1000);
		}
		return false;
	}
	
}
