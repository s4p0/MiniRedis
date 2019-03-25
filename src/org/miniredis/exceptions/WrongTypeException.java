package org.miniredis.exceptions;

@SuppressWarnings("serial")

public class WrongTypeException extends Exception {
	public WrongTypeException() {
		super("WRONGTYPE Operation against a key holding the wrong kind of value");
	}
}
