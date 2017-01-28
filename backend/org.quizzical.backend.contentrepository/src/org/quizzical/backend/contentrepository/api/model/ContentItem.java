package org.quizzical.backend.contentrepository.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ContentItem extends BaseEntity implements Serializable {
	
	@JsonIgnore
	private byte[] content;
	
	private String mimeType;
	
	private long sizeInBytes;
	
	public ContentItem() {
	}
	
	public ContentItem(String name, String code, byte[] content, String mimeType, long sizeInBytes) {
		setName(name);
		setCode(code);
		this.content = content;
		this.mimeType = mimeType;
		this.sizeInBytes = sizeInBytes;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
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
	
	@Override
	public String toString() {
		return String.format("{%s=%d;%s=%s}", "id",getId(),"description",getDescription());
	}
}
