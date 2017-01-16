package org.gauntlet.core.service.impl;

public class AttrPair {
	private Class attrClass;
	private String attrName;
	private Object attrValue;

	public AttrPair() {
	}	
	
	public AttrPair(Class attrClass, String attrName, Object attrValue) {
		super();
		this.attrClass = attrClass;
		this.attrName = attrName;
		this.attrValue = attrValue;
	}

	public Class getAttrClass() {
		return attrClass;
	}

	public void setAttrClass(Class attrClass) {
		this.attrClass = attrClass;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public Object getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(Object attrValue) {
		this.attrValue = attrValue;
	}
}
