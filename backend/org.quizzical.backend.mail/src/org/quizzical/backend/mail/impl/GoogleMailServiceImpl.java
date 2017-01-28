package org.quizzical.backend.mail.impl;

import java.util.Dictionary;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;


public class GoogleMailServiceImpl implements IMailService, ManagedService {
	public static final String MAIL_SMTP_SERVER_HOSTNAME = "mail.smtp.server.hostname";
	public static final String MAIL_SMTP_SERVER_PORT= "mail.smtp.server.port";
	public static final String MAIL_SMTP_LOGIN_USERNAME= "mail.smtp.login.username";
	public static final String MAIL_SMTP_LOGIN_PASSWORD= "mail.smtp.login.password";
	public static final String MAIL_SMTP_FROM_EMAIL= "mail.sender.from.email";
	public static final String MAIL_SMTP_FROM_NAME= "mail.sender.from.name";
	
	
	private volatile LogService logger;
	
	private String hostname;
	private Integer port;
	private String username;
	private String password;
	private String email;
	private String name;
	
	@Override
	public void updated(Dictionary props) throws ConfigurationException {
		this.hostname = (String)props.get(MAIL_SMTP_SERVER_HOSTNAME);
		this.port = Integer.valueOf((String)props.get(MAIL_SMTP_SERVER_PORT));
		this.username = (String)props.get(MAIL_SMTP_LOGIN_USERNAME);
		this.password = (String)props.get(MAIL_SMTP_LOGIN_PASSWORD);
		this.email = (String)props.get(MAIL_SMTP_FROM_EMAIL);
		this.name = (String)props.get(MAIL_SMTP_FROM_NAME);
	}	
	
	@Override
	synchronized public HtmlEmail createHtmlSender(String subject) throws EmailException {
/*		Properties mailServerProperties = new Properties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");*/
		
		//Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
 
		
		HtmlEmail mailSender = new HtmlEmail();
		mailSender.setHostName(this.hostname);
		mailSender.setSmtpPort(this.port);
		mailSender.setAuthenticator(new DefaultAuthenticator(this.username,this.password));
		mailSender.setSSLOnConnect(true);
		mailSender.setStartTLSEnabled(true);
		mailSender.setFrom(this.email);
		mailSender.setSubject(subject);
		
		return mailSender;
	}

}
