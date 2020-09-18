package com.dictionary;

public enum SortType {
	DISTANCE("distance"),
	RATING("rating"),
	TAG("tag");

	private String name;

	SortType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
