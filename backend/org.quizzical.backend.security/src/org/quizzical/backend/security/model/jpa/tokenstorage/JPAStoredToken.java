package org.quizzical.backend.security.model.jpa.tokenstorage;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.quizzical.backend.security.api.tokenstorage.StoredToken;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
+"sec_token")
public class JPAStoredToken {
    @Id
    @Column(name = "id", scale = 0)
    @GeneratedValue(strategy = GenerationType.AUTO)	
    protected Long id;
    
    // The token
    private String m_token;

    // The secret associated with the token, can be used to claim ownership of this token
    private String m_tokenSecret;

    // Timestamp of when the token was generated, used for tokens that a limited lifetime
    private long m_timestamp;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private Map<String, String> m_properties = new HashMap<String, String>();

	public JPAStoredToken() {
		this(null, null, 0);
	}
	
	public JPAStoredToken(StoredToken token) {
		this(token.getToken(), token.getTokenSecret(), token.getTimestamp());
		this.setProperties(token.getProperties());
	}
	
    /**
     * Constructor. Creates a new token
     * 
     * @param token The uniquely random generated String
     * @param tokenSecret The secret, that is supposed to authenticate the owner of the token.
     *        The token may be publicly available (i.e. send to the client in a http response) but
     *        the secret is only known by the owner.
     * @param timestamp The time at which the token was generated
     */
    public JPAStoredToken(final String token, final String tokenSecret, final long timestamp) {
        m_token = token;
        m_tokenSecret = tokenSecret;
        m_timestamp = timestamp;
    }

    /**
     * Returns the token. The token is publicly known.
     * 
     * @return the token.
     */
    public String getToken() {
        return m_token;
    }

    /**
     * Sets the token.
     * 
     * @param token the token to set
     */
    public void setToken(final String token) {
        m_token = token;
    }

    /**
     * Returns the token secret. The token secret associated with this token
     * is only known by the owner.
     * 
     * @return the secret that only the token owner knows
     */
    public String getTokenSecret() {
        return m_tokenSecret;
    }

    /**
     * Sets the token secret.
     * 
     * @param tokenSecret the token secret to set
     */
    public void setTokenSecret(final String tokenSecret) {
        m_tokenSecret = tokenSecret;
    }

    /**
     * Returns the timestamp in milliseconds of the time at which the token was created.
     * 
     * @return the timestamp in milliseconds
     */
    public long getTimestamp() {
        System.currentTimeMillis();
        return m_timestamp;
    }

    /**
     * Sets the timestamp.
     * 
     * @param timestamp
     */
    public void setTimestamp(final long timestamp) {
        m_timestamp = timestamp;
    }

    /**
     * Returns the map of arbitrary properties.
     * 
     * @return
     */
    public Map<String, String> getProperties() {
        return m_properties;
    }

    public String getProperty(final String key) {
        if (m_properties == null) {
            return null;
        }
        return m_properties.get(key);
    }

    public void setProperties(final Map<String, String> properties) {
        m_properties = properties;
    }

    public void setProperty(final String key, final String value) {
        if (m_properties == null) {
            m_properties = new HashMap<String, String>();
        }
        m_properties.put(key, value);
    }

    /**
     * Verifies if the token is expired comparing its timestamp with the system time.
     * Returns true of the timestamp of this token + the provided validity duration equals
     * or is higher then the current time.
     * 
     * @param validityDuration The amount of milliseconds the token is valid. Can be 0. If the
     *        validityDuration is smaller then 0, this method always returns false.
     * @return
     */
    public boolean isExpired(final long validityDuration) {
        if (validityDuration < 0) {
            return false;
        }
        else if (validityDuration == 0) {
            return true;
        }
        long expiryDate = m_timestamp + validityDuration;
        return System.currentTimeMillis() >= expiryDate;
    }

    public StoredToken clone() {
    	StoredToken clone = new  StoredToken(m_token, m_tokenSecret, m_timestamp);
        if (getProperties() != null) {
            clone.setProperties(clone(getProperties()));
        }
        return clone;
    }

    private Map<String, String> clone(final Map<String, String> map) {
        HashMap<String, String> newMap = new HashMap<String, String>();
        for (String key : map.keySet()) {
            newMap.put(key, map.get(key));
        }
        return newMap;
    }	

	public Long getId() {
		return id;
	}
}

