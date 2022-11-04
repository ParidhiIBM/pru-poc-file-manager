package com.fileManager.fileManager.jwt;

public class Role {
	private Integer id;
	private String name;

	public Role() { }
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
