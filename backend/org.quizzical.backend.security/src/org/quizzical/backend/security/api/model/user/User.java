package org.quizzical.backend.security.api.model.user;

import java.util.Date;

import org.gauntlet.core.model.BaseEntity;

public class User extends BaseEntity{
	private String uuid;
	private String originalUuid;
	private long userId;
	private String userUuid;
	private long originalUserId;
	private boolean setOriginalUserId;
	private long companyId;
	private long originalCompanyId;
	private boolean setOriginalCompanyId;
	private Date createDate;
	private Date modifiedDate;
	private boolean defaultUser;
	private boolean originalDefaultUser;
	private boolean setOriginalDefaultUser;
	private long contactId;
	private long originalContactId;
	private boolean setOriginalContactId;
	private String password;
	private boolean passwordEncrypted;
	private boolean passwordReset;
	private Date passwordModifiedDate;
	private String digest;
	private String reminderQueryQuestion;
	private String reminderQueryAnswer;
	private int graceLoginCount;
	private String screenName;
	private String originalScreenName;
	private String emailAddress;
	private String originalEmailAddress;
	private long facebookId;
	private long originalFacebookId;
	private boolean setOriginalFacebookId;
	private String openId;
	private String originalOpenId;
	private long portraitId;
	private long originalPortraitId;
	private boolean setOriginalPortraitId;
	private String languageId;
	private String timeZoneId;
	private String greeting;
	private String comments;
	private String firstName;
	private String middleName;
	private String lastName;
	private String jobTitle;
	private Date loginDate;
	private String loginIP;
	private Date lastLoginDate;
	private String lastLoginIP;
	private Date lastFailedLoginDate;
	private int failedLoginAttempts;
	private boolean lockout;
	private Date lockoutDate;
	private boolean agreedToTermsOfUse;
	private boolean emailAddressVerified;
	private int status;
	private int originalStatus;
	private boolean setOriginalStatus;
	private long columnBitmask;
	
	public User() {
		super();
	}
	

	public User(String emailAddress, String firstName, String lastName) {
		super();
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
	}



	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getOriginalUuid() {
		return originalUuid;
	}
	public void setOriginalUuid(String originalUuid) {
		this.originalUuid = originalUuid;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	public long getOriginalUserId() {
		return originalUserId;
	}
	public void setOriginalUserId(long originalUserId) {
		this.originalUserId = originalUserId;
	}
	public boolean isSetOriginalUserId() {
		return setOriginalUserId;
	}
	public void setSetOriginalUserId(boolean setOriginalUserId) {
		this.setOriginalUserId = setOriginalUserId;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public long getOriginalCompanyId() {
		return originalCompanyId;
	}
	public void setOriginalCompanyId(long originalCompanyId) {
		this.originalCompanyId = originalCompanyId;
	}
	public boolean isSetOriginalCompanyId() {
		return setOriginalCompanyId;
	}
	public void setSetOriginalCompanyId(boolean setOriginalCompanyId) {
		this.setOriginalCompanyId = setOriginalCompanyId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public boolean isDefaultUser() {
		return defaultUser;
	}
	public void setDefaultUser(boolean defaultUser) {
		this.defaultUser = defaultUser;
	}
	public boolean isOriginalDefaultUser() {
		return originalDefaultUser;
	}
	public void setOriginalDefaultUser(boolean originalDefaultUser) {
		this.originalDefaultUser = originalDefaultUser;
	}
	public boolean isSetOriginalDefaultUser() {
		return setOriginalDefaultUser;
	}
	public void setSetOriginalDefaultUser(boolean setOriginalDefaultUser) {
		this.setOriginalDefaultUser = setOriginalDefaultUser;
	}
	public long getContactId() {
		return contactId;
	}
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	public long getOriginalContactId() {
		return originalContactId;
	}
	public void setOriginalContactId(long originalContactId) {
		this.originalContactId = originalContactId;
	}
	public boolean isSetOriginalContactId() {
		return setOriginalContactId;
	}
	public void setSetOriginalContactId(boolean setOriginalContactId) {
		this.setOriginalContactId = setOriginalContactId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isPasswordEncrypted() {
		return passwordEncrypted;
	}
	public void setPasswordEncrypted(boolean passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
	}
	public boolean isPasswordReset() {
		return passwordReset;
	}
	public void setPasswordReset(boolean passwordReset) {
		this.passwordReset = passwordReset;
	}
	public Date getPasswordModifiedDate() {
		return passwordModifiedDate;
	}
	public void setPasswordModifiedDate(Date passwordModifiedDate) {
		this.passwordModifiedDate = passwordModifiedDate;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getReminderQueryQuestion() {
		return reminderQueryQuestion;
	}
	public void setReminderQueryQuestion(String reminderQueryQuestion) {
		this.reminderQueryQuestion = reminderQueryQuestion;
	}
	public String getReminderQueryAnswer() {
		return reminderQueryAnswer;
	}
	public void setReminderQueryAnswer(String reminderQueryAnswer) {
		this.reminderQueryAnswer = reminderQueryAnswer;
	}
	public int getGraceLoginCount() {
		return graceLoginCount;
	}
	public void setGraceLoginCount(int graceLoginCount) {
		this.graceLoginCount = graceLoginCount;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getOriginalScreenName() {
		return originalScreenName;
	}
	public void setOriginalScreenName(String originalScreenName) {
		this.originalScreenName = originalScreenName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getOriginalEmailAddress() {
		return originalEmailAddress;
	}
	public void setOriginalEmailAddress(String originalEmailAddress) {
		this.originalEmailAddress = originalEmailAddress;
	}
	public long getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}
	public long getOriginalFacebookId() {
		return originalFacebookId;
	}
	public void setOriginalFacebookId(long originalFacebookId) {
		this.originalFacebookId = originalFacebookId;
	}
	public boolean isSetOriginalFacebookId() {
		return setOriginalFacebookId;
	}
	public void setSetOriginalFacebookId(boolean setOriginalFacebookId) {
		this.setOriginalFacebookId = setOriginalFacebookId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getOriginalOpenId() {
		return originalOpenId;
	}
	public void setOriginalOpenId(String originalOpenId) {
		this.originalOpenId = originalOpenId;
	}
	public long getPortraitId() {
		return portraitId;
	}
	public void setPortraitId(long portraitId) {
		this.portraitId = portraitId;
	}
	public long getOriginalPortraitId() {
		return originalPortraitId;
	}
	public void setOriginalPortraitId(long originalPortraitId) {
		this.originalPortraitId = originalPortraitId;
	}
	public boolean isSetOriginalPortraitId() {
		return setOriginalPortraitId;
	}
	public void setSetOriginalPortraitId(boolean setOriginalPortraitId) {
		this.setOriginalPortraitId = setOriginalPortraitId;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getTimeZoneId() {
		return timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getLastLoginIP() {
		return lastLoginIP;
	}
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	public Date getLastFailedLoginDate() {
		return lastFailedLoginDate;
	}
	public void setLastFailedLoginDate(Date lastFailedLoginDate) {
		this.lastFailedLoginDate = lastFailedLoginDate;
	}
	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}
	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}
	public boolean isLockout() {
		return lockout;
	}
	public void setLockout(boolean lockout) {
		this.lockout = lockout;
	}
	public Date getLockoutDate() {
		return lockoutDate;
	}
	public void setLockoutDate(Date lockoutDate) {
		this.lockoutDate = lockoutDate;
	}
	public boolean isAgreedToTermsOfUse() {
		return agreedToTermsOfUse;
	}
	public void setAgreedToTermsOfUse(boolean agreedToTermsOfUse) {
		this.agreedToTermsOfUse = agreedToTermsOfUse;
	}
	public boolean isEmailAddressVerified() {
		return emailAddressVerified;
	}
	public void setEmailAddressVerified(boolean emailAddressVerified) {
		this.emailAddressVerified = emailAddressVerified;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getOriginalStatus() {
		return originalStatus;
	}
	public void setOriginalStatus(int originalStatus) {
		this.originalStatus = originalStatus;
	}
	public boolean isSetOriginalStatus() {
		return setOriginalStatus;
	}
	public void setSetOriginalStatus(boolean setOriginalStatus) {
		this.setOriginalStatus = setOriginalStatus;
	}
	public long getColumnBitmask() {
		return columnBitmask;
	}
	public void setColumnBitmask(long columnBitmask) {
		this.columnBitmask = columnBitmask;
	}
	
	public String getName() {
		if (getFirstName() != null && getLastName() != null)
			return getFirstName()+" "+getLastName();
		else if (getFirstName() != null)
			return getFirstName();
		else if (getLastName() != null)
			return getLastName();
		else
			return null;
	}
}
