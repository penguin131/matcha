package com.dictionary;

public enum SortType {
	DISTANCE("distance"),
	RATING("rating"),
	TAGS("tags");

	private String name;

	public String getName() {
		return name;
	}

	SortType(String name) {
		this.name = name;
	}
}
