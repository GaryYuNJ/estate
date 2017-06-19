package com.property.data;

import java.util.List;

public class NodeTree {
	private String id;
	private String text;
	private String type;
	private int level;
	private List<NodeTree> children ;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<NodeTree> getChildren() {
		return children;
	}
	public void setChildren(List<NodeTree> children) {
		this.children = children;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
