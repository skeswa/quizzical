package org.quizzical.backend.security.authorization.api.model.user;

/**
 * @author <a href="mailto:amdatu-developers@amdatu.org">Amdatu Project Team</a>
 */
public interface UserProfile {

    /**
     * @return the email addresses that identify this user, cannot be <code>null</code>.
     */
    String getEmail();

    /**
     * @return the full name of the user, cannot be <code>null</code>.
     */
    String getName();

    /**
     * @return an internal identifier for this user, cannot be <code>null</code>.
     */
    String getUserID();

}
