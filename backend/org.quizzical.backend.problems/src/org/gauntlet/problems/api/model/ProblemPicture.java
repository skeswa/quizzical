package org.gauntlet.problems.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProblemPicture extends BaseEntity implements Serializable {
	
	@JsonIgnore
	private byte[] picture;
	
	private String mimeType;
	
	private long sizeInBytes;
	
	public ProblemPicture() {
	}
	
	public ProblemPicture(String name, String code, byte[] picture, String mimeType, long sizeInBytes) {
		setName(name);
		setCode(code);
		this.picture = picture;
		this.mimeType = mimeType;
		this.sizeInBytes = sizeInBytes;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}
}
