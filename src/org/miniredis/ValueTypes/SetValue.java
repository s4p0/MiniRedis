package org.miniredis.valuetypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class SetValue extends BaseValue {

	public SetValue() {
		super();
		// value = new TreeMap<Float, String>();
		value = new HashMap<String, Float>();
	}

	public SetValue(Map<String, Float> value, int seconds) {
		super(value, seconds);
	}

	private HashMap<String, Float> getMap() {
		try {
			if (this.value == null)
				new HashMap<String, Float>();
			return (HashMap<String, Float>) this.value;
		} catch (Exception ex) {
		}
		return null;
	}

	public int addMember(Float score, String member) {
		if (getMap() != null && !getMap().containsKey(score)) {
			getMap().put(member, score);
			return 1;
		}
		return 0;
	}

	public int count() {
		return getMap() != null ? getMap().size() : 0;
	}

	public Object rank(String member) {

		if (getMap() != null) {
			int n = 0;
			getMap().entrySet().stream().sorted(comparingByValue()).anyMatch(obj -> checkForMember(obj, member, n));
			return n;
		}

		return null;
	}

	private boolean checkForMember(Entry<String, Float> obj, String member, int n) {
		if (obj.getKey().equalsIgnoreCase(member))
			return true;
		n = n + 1;
		return false;
	}

	public List<Entry<String, Float>> list(int start, int stop) {
		if (getMap() != null) {
			try {
				return getMap().entrySet().stream().sorted(comparingByValue()).skip(start).limit(stop - start).collect(Collectors.toList());
			} catch (Exception e) {
			}
		}

		return new ArrayList<Entry<String, Float>>();
	}
}
