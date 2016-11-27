package org.gauntlet.core.model;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;



public abstract class BaseEntity extends SuperEntity
{
	@JsonIgnore
	protected ObjectMapper mapper = new ObjectMapper();
	
	public static final String BASIC_ENTITY_ATTRIBUTE_ID = "id";
	public static final String BASIC_ENTITY_ATTRIBUTE_NAME = "name";
	public static final String BASIC_ENTITY_ATTRIBUTE_CODE = "code";
	public static final String BASIC_ENTITY_ATTRIBUTE_DATE_CREATED = "dateCreated";
	public static final String BASIC_ENTITY_ATTRIBUTE_DATE_LAST_UPDATED = "dateLastUpdated";

	
    protected Long id;
    protected boolean temporary = true;
    protected String processId;
    
    protected Long version;
    protected Boolean issystem;
    protected Date dateCreated = new Date();
    protected Date dateLastUpdated = new Date();
    protected Long ownerEntityId;
    protected Long ownerEntityTypeId;
    protected Boolean active = true;
    protected String externalName;
    protected String externalCode;
    protected String externalRefId;
    protected Boolean synchonized = false;    
    protected String parentRefId;
    protected String name;
    protected String code;
    protected String refId;
    protected String description;
    protected String portalId;
    protected String schedulerJobId;    
    protected String repositoryid;
    protected Long tenantId;
    
    protected boolean tenantWide = false;



	/**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(Long value) {
        this.version = value;
    }
    

    public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean isTemporary) {
		this.temporary = isTemporary;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
     * Gets the value of the issystem property.
     * 
     */
    public Boolean isIssystem() {
        return issystem;
    }

    /**
     * Sets the value of the issystem property.
     * 
     */
    public void setIssystem(Boolean value) {
        this.issystem = value;
    }

    /**
     * Gets the value of the dateCreated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the value of the dateCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreated(Date value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of the dateLastUpdated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    /**
     * Sets the value of the dateLastUpdated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateLastUpdated(Date value) {
        this.dateLastUpdated = value;
    }

    /**
     * Gets the value of the ownerEntityId property.
     * 
     */
    public Long getOwnerEntityId() {
        return ownerEntityId;
    }

    /**
     * Sets the value of the ownerEntityId property.
     * 
     */
    public void setOwnerEntityId(Long value) {
        this.ownerEntityId = value;
    }

    /**
     * Gets the value of the ownerEntityTypeId property.
     * 
     */
    public Long getOwnerEntityTypeId() {
        return ownerEntityTypeId;
    }

    /**
     * Sets the value of the ownerEntityTypeId property.
     * 
     */
    public void setOwnerEntityTypeId(Long value) {
        this.ownerEntityTypeId = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(Boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the externalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalName() {
        return externalName;
    }
    
	/**
     * Sets the value of the externalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalName(String value) {
        this.externalName = value;
    }

    /**
     * Gets the value of the externalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalCode() {
        return externalCode;
    }

    /**
     * Sets the value of the externalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalCode(String value) {
        this.externalCode = value;
    }

    /**
     * Gets the value of the externalRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalRefId() {
        return externalRefId;
    }

    /**
     * Sets the value of the externalRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalRefId(String value) {
        this.externalRefId = value;
    }

    /**
     * Gets the value of the parentRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentRefId() {
        return parentRefId;
    }

    /**
     * Sets the value of the parentRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentRefId(String value) {
        this.parentRefId = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the refId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefId() {
        return refId;
    }

    /**
     * Sets the value of the refId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefId(String value) {
        this.refId = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the portalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortalId() {
        return portalId;
    }

    /**
     * Sets the value of the portalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortalId(String value) {
        this.portalId = value;
    }

    /**
     * Gets the value of the repositoryid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryid() {
        return repositoryid;
    }

    /**
     * Sets the value of the repositoryid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryid(String value) {
        this.repositoryid = value;
    }

	public String getSchedulerJobId() {
		return schedulerJobId;
	}

	public void setSchedulerJobId(String schedulerJobId) {
		this.schedulerJobId = schedulerJobId;
	}

	public Boolean getSynchonized() {
		return synchonized;
	}

	public void setSynchonized(Boolean synchonized) {
		this.synchonized = synchonized;
	}
	
	@Override
	public int compareTo(Object o) {
		return getId().compareTo(((BaseEntity)o).getId());
	}
	
	public boolean isNew() {
		return this.getId() == null;
	}
	
    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public boolean isTenantWide() {
		return tenantWide;
	}

	public void setTenantWide(boolean tenantWide) {
		this.tenantWide = tenantWide;
	}

	public Object getIdentifierPropertyId() {
		return null;
	}

	public Object getEntityPropertyId() {
		return null;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}
}
