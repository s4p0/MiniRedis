package org.miniredis.exceptions;

@SuppressWarnings("serial")
public class NotValidFloatException extends Exception{
	public NotValidFloatException()
	{
		super("ERR value is not a valid float");
	}
}
