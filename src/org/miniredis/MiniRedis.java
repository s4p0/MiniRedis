package org.miniredis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.miniredis.ValueTypes.BaseValue;
import org.miniredis.ValueTypes.SetValue;
import org.miniredis.ValueTypes.StringValue;
import org.miniredis.exceptions.NotIntegerOrOutOfRangeException;
import org.miniredis.exceptions.NotValidFloatException;
import org.miniredis.exceptions.WrongTypeException;

public class MiniRedis {

	private String Empty = "";
	private ConcurrentHashMap<String, BaseValue> db = new ConcurrentHashMap<String, BaseValue>();

	public String setCommand(String key, String value, String seconds) throws NotIntegerOrOutOfRangeException {
		int sec = 0;

		try {

			if (seconds != null && !seconds.equals(Empty))
				sec = Integer.parseInt(seconds);
		} catch (NumberFormatException ex) {
			throw new NotIntegerOrOutOfRangeException();
		}

		db.put(key, new StringValue(value, sec));
		return "OK";
	}

	public String setCommand(String key, String value) throws NotIntegerOrOutOfRangeException {
		return setCommand(key, value, null);
	}

	public String getCommand(String key) throws WrongTypeException {
		StringValue result = null;
		try {
			result = (StringValue) db.get(key);
		} catch (Exception ex) {
			throw new WrongTypeException();
		}

		if (result != null && result.isExpired()) {
			db.remove(key);
			result = null;
		}

		return result == null ? "(nil)" : String.format("\"%s\"", result.value);
	}

	public int delCommand(String[] keys) {
		int deleted = 0;
		for (String key : keys) {
			if (isValid(key) != null && db.remove(key) != null)
				deleted++;
		}

		return deleted;
	}

	public int DBSizeCommand() {
		db.forEach((k, v) -> isValid(k, v));
		return db.size();
	}

	public BaseValue isValid(String key, BaseValue value) {
		if (value != null && value.isExpired()) {
			db.remove(key);
			value = null;
		}
		return value;
	}

	public BaseValue isValid(String key) {
		BaseValue value = db.get(key);
		return isValid(key, value);
	}

	public int incCommand(String key) throws WrongTypeException, NotIntegerOrOutOfRangeException {
		StringValue result = null;
		try {
			result = (StringValue) isValid(key);
		} catch (Exception ex) {
			throw new WrongTypeException();
		}

		int incremented = 1;
		if (result != null) {
			try {
				incremented = Integer.parseInt((String) result.value) + 1;
			} catch (Exception e) {
				throw new NotIntegerOrOutOfRangeException();
			}
			result.value = Integer.toString(incremented);
			db.put(key, result);
			return incremented;
		} else {
			setCommand(key, Integer.toString(incremented));
		}
		return incremented;
	}

	public int zaddCommand(String key, String score, String member) throws WrongTypeException, NotValidFloatException {
		SetValue result = null;
		try {
			result = (SetValue) isValid(key);
		} catch (Exception ex) {
			throw new WrongTypeException();
		}

		if (result == null) {
			result = new SetValue();
			db.put(key, result);
		}

		try {

			return result.addMember(Float.parseFloat(score), member);
		} catch (NumberFormatException ex) {
			throw new NotValidFloatException();
		}
	}

	public int zcardCommand(String key) throws WrongTypeException {
		SetValue result = null;
		try {
			result = (SetValue) isValid(key);
		} catch (Exception ex) {
			throw new WrongTypeException();
		}

		if (result != null)
			return result.count();

		return 0;
	}

	public Object zrankCommand(String key, String member) throws WrongTypeException {
		SetValue result = null;
		try {
			result = (SetValue) isValid(key);
		} catch (Exception ex) {
			throw new WrongTypeException();
		}
		if (result != null)
			return result.rank(member);

		return null;
	}

	public List<String> zrangeCommand(String key, String _start, String _stop)
			throws WrongTypeException, NotIntegerOrOutOfRangeException {
		int start, stop;
		try {
			start = Integer.parseInt(_start);
			stop = Integer.parseInt(_stop);
		} catch (NumberFormatException e) {
			throw new NotIntegerOrOutOfRangeException();
		}

		SetValue result = null;
		try {
			result = (SetValue) isValid(key);
		} catch (Exception ex) {
			throw new WrongTypeException();
		}

		if (result != null) {
			return result.list(start, stop);
		}

		return new ArrayList<String>();
	}

}
