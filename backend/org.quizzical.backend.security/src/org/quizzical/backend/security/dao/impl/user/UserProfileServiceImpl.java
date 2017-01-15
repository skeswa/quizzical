package org.quizzical.backend.security.dao.impl.user;

import static org.amdatu.security.account.AccountConstants.KEY_ACCOUNT_ACTION;
import static org.amdatu.security.account.AccountConstants.KEY_ACCOUNT_EMAIL;
import static org.amdatu.security.account.AccountConstants.KEY_ACCOUNT_ID;
import static org.amdatu.security.account.AccountConstants.KEY_NOTIFY_URL;
import static org.amdatu.security.account.AccountConstants.TOPIC_ACCOUNT_CREATED;
import static org.amdatu.security.account.AccountConstants.TOPIC_ACCOUNT_REMOVED;
import static org.amdatu.security.account.AccountConstants.TOPIC_ACCOUNT_RESET_CREDENTIALS;
import static org.amdatu.security.account.AccountConstants.TOPIC_ACCOUNT_UPDATED;
import static org.amdatu.security.account.AccountConstants.TOPIC_NOTIFY_ACCOUNT_OWNER;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.amdatu.security.authentication.authservice.AuthenticationService;
import org.amdatu.security.authentication.idprovider.UserLookupService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.UserProfileService;
import org.quizzical.backend.security.api.model.user.UserProfile;

/**
 * Mock implementation of {@link AuthenticationService}.
 */
public class UserProfileServiceImpl implements UserLookupService, UserProfileService, EventHandler {
    // Managed by Felix DM...
    private volatile LogService m_log;
    // Locally managed...
    private final Map<String, UserProfile> m_profiles;

    /**
     * Creates a new {@link UserProfileServiceImpl} instance.
     */
    public UserProfileServiceImpl() {
        m_profiles = new HashMap<>();

        addUserMapping("mandisakeswa999@gmail.com");
        addUserMapping("test@gmail.com");
    }

    @Override
    public Optional<String> getUserId(Map<String, String> credentials) {
        String email = getEmail(credentials);

        return m_profiles.values().stream()
            .filter(p -> p.getEmail().equals(email))
            .map(UserProfile::getUserID)
            .findAny();
    }

    @Override
    public UserProfile getUserProfile(String userID) {
        return m_profiles.get(userID);
    }

    @Override
    public void handleEvent(Event event) {
        String accountId = (String) event.getProperty(KEY_ACCOUNT_ID);

        String topic = event.getTopic();
        if (TOPIC_ACCOUNT_CREATED.equals(topic)) {
            addUserMapping(accountId);

            m_log.log(LogService.LOG_INFO, "Account created for: " + accountId);
        }
        else if (TOPIC_ACCOUNT_REMOVED.equals(topic)) {
            removeUserMapping(accountId);

            m_log.log(LogService.LOG_INFO, "Account removed for: " + accountId);
        }
        else if (TOPIC_ACCOUNT_RESET_CREDENTIALS.equals(topic)) {
            m_log.log(LogService.LOG_INFO, "Credential reset requested for: " + accountId);
        }
        else if (TOPIC_ACCOUNT_UPDATED.equals(topic)) {
            m_log.log(LogService.LOG_INFO, "Account updated for: " + accountId);
        }
        else if (TOPIC_NOTIFY_ACCOUNT_OWNER.equals(topic)) {
            String action = (String) event.getProperty(KEY_ACCOUNT_ACTION);
            String email = (String) event.getProperty(KEY_ACCOUNT_EMAIL);
            String url = (String) event.getProperty(KEY_NOTIFY_URL);

            sendEmail(action, accountId, email, url);
        }
    }

    private void addUserMapping(String email) {
        String newUserId = UUID.randomUUID().toString();

        m_profiles.computeIfAbsent(newUserId, key -> new UserProfileImpl(key, email));
    }

    private String getEmail(Map<String, String> credentials) {
        String email = credentials.get("upn");
        if (email == null) {
            email = credentials.get("email");
        }
        return email;
    }

    private void removeUserMapping(String email) {
        getUserId(Collections.singletonMap("email", email))
            .ifPresent(userId -> m_profiles.remove(userId));
    }

    private void sendEmail(String action, String accountId, String email, String location) {
        String body;
        String subject;
        if ("signup_success".equals(action)) {
            subject = "Please verify your account";
            body = String.format("Use the following URL to verify your account: <%s>%n", location);
        }
        else if ("reset_requested".equals(action)) {
            subject = "Reset your credentials";
            body = String.format("Use the following URL to reset your credentials: <%s>%n", location);
        }
        else if ("account_verified".equals(action)) {
            subject = "Account verified";
            body = "This is a notification that your account has been successfully verified.";
        }
        else {
            subject = "Account updated";
            body = "This is a notification that your account has been successfully updated.";
        }

        System.out.printf("EMAIL:%n%nFrom: accounts@localhost%nTo: %s%nSubject: %s%n%n%s%n", email, subject, body);
    }

    static class UserProfileImpl implements UserProfile {
        private final String m_name;
        private final String m_userID;
        private final String m_email;

        public UserProfileImpl(String userID, String email) {
            this(userID, email, email.replaceFirst("@.*$", ""));
        }

        public UserProfileImpl(String userID, String email, String name) {
            m_userID = userID;
            m_email = email;
            m_name = name;
        }

        @Override
        public String getUserID() {
            return m_userID;
        }

        @Override
        public String getEmail() {
            return m_email;
        }

        @Override
        public String getName() {
            return m_name;
        }
    }
}
