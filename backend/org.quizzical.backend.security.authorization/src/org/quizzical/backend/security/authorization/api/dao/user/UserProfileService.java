package org.quizzical.backend.security.authorization.api.dao.user;

import org.quizzical.backend.security.authorization.api.model.user.UserProfile;

/**
 * @author <a href="mailto:amdatu-developers@amdatu.org">Amdatu Project Team</a>
 */
public interface UserProfileService {

    UserProfile getUserProfile(String userID);
}
