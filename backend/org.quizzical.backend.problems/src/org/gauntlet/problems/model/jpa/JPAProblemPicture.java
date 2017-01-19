package org.gauntlet.problems.model.jpa;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+"problem_picture")
public class JPAProblemPicture extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -4417354637162556969L;

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

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] picture;
	
	private String mimeType;
	
	private long sizeInBytes;
	
	public JPAProblemPicture() {
		super();
	}

	public JPAProblemPicture(String name, String code) {
		this();
		this.name = name;
		this.code = code;
	}	
}

