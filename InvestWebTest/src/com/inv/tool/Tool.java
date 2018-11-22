package com.inv.tool;

public class Tool {
	
	public int getLayer(long count) {
		int layer = (int) Math.ceil(Math.log(count)/Math.log(2));
		return layer;
	}
}
