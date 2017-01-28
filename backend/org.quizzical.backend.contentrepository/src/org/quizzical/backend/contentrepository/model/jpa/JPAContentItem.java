package org.quizzical.backend.contentrepository.model.jpa;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.contentrepository.api.Constants.CONTENT_REPOSITORY_TABLE_QUALIFIER+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"contentitem")
public class JPAContentItem extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -1076968288936728265L;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] content;
	
	private String mimeType;
	
	private long sizeInBytes;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] picture) {
		this.content = picture;
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

	public JPAContentItem() {
		super();
	}

	public JPAContentItem(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

