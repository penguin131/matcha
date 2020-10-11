package com.dictionary;

public enum SortType {
	DISTANCE("distance"),
	RATING("rating"),
	TAGS("tags"),
	AGE("age"),
	AGE_DESC("ageDesc");

	private String name;

	public String getName() {
		return name;
	}

	SortType(String name) {
		this.name = name;
	}
}
