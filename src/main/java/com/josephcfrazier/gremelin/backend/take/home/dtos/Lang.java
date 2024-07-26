package com.josephcfrazier.gremelin.backend.take.home.dtos;

import java.util.stream.Stream;

public enum Lang {
	ENGISH("en", "English"), 
	RUSSIAN("ru", "Russian");

	private final String id;
	private final String name;

	private Lang(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String id() {
		return id;
	}

	public static Lang of(String name) {
		return Stream.of(values()).filter(v -> v.name.equalsIgnoreCase(name)).findFirst().orElse(ENGISH);
	}

}
