package org.miniredis.ValueTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SetValue extends BaseValue {

	public SetValue() {
		super();
		value = new TreeMap<Float, String>();
	}

	public SetValue(TreeMap<Float, String> value, int seconds) {
		super(value, seconds);
	}

	public int addMember(Float score, String member) {
		TreeMap<Float, String> map = (TreeMap<Float, String>) value;
		if (!map.containsKey(score)) {
			map.put(score, member);
			return 1;
		}
		return 0;
	}

	public int count() {
		TreeMap<Float, String> map = (TreeMap<Float, String>) value;
		return map.size();
	}

	public Object rank(String member) {
		int n = 0;
		TreeMap<Float, String> map = (TreeMap<Float, String>) value;
		for (String mem : map.values()) {
			if (mem.equals(member))
				return n;
			n++;
		}
		return null;
	}

	public List<String> list(int start, int stop) {
		TreeMap<Float, String> map = (TreeMap<Float, String>) value;
		try {
			return map.values().stream().skip(start).limit(stop - start).collect(Collectors.toList());
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
}
