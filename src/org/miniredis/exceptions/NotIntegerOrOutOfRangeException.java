package org.miniredis.exceptions;

@SuppressWarnings("serial")
public class NotIntegerOrOutOfRangeException extends Exception {
	public NotIntegerOrOutOfRangeException()
	{
		super("ERR value is not an integer or out of range");
	}
}
