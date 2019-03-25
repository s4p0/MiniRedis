package org.miniredis.valuetypes;

import java.util.Comparator;

public class TreeCompare implements Comparator<Float> {

	@Override
	public int compare(Float arg0, Float arg1) {
		return arg0.compareTo(arg1);
	}
	
}
