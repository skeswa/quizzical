package org.quizzical.backend.security.authorization.dao.impl.user.email;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.gauntlet.core.commons.util.StringUtil;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.mail.api.LogServiceChute;
import org.quizzical.backend.mail.api.MailPreparator;

public class WelcomeMessagePreparator extends MailPreparator {

	private String emailAddress;
	private String fullName;
	private String password;
	private String firstName;
	private String loginUrl;
	private LogService logger;
	private List<String> bccEmails;

	public WelcomeMessagePreparator(
			IMailService service, 
			LogService logger,
			String firstName,
			String fullName, 
			String emailAddress,
			List<String> bccEmails,
			String subject,
			String password,
			String loginUrl) throws EmailException {
		super(service,subject);
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.firstName = firstName;
		this.loginUrl = loginUrl;
		this.logger = logger;
		this.bccEmails = bccEmails;
	}

	@Override
	public void prepare() throws Exception  {
		super.sender.addTo(emailAddress, fullName);
		if (this.bccEmails != null) {
			for (String bcc : bccEmails) {
				super.sender.addBcc(bcc);
			}
		}
		
		final VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,new LogServiceChute(this.logger));
		ve.init();

		
		// -- Repare body
		InputStream is = WelcomeMessagePreparator.class
				.getResourceAsStream("/email_templates/welcome/welcome-to-quizzical-cid.html");
		String templateString = StringUtil.read(is);

		VelocityContext vec = new VelocityContext();
		StringWriter out = new StringWriter();
		ve.init();
		


		Map<String, Object> vars = new HashMap<String, Object>();
		URL img = WelcomeMessagePreparator.class.getResource(
				"/email_templates/images/cloud.gif");
		String cid = super.sender.embed(img, "img1");
		vec.put("cloud_cid", cid);
		
		img = WelcomeMessagePreparator.class.getResource(
				"/email_templates/images/quizzical-header-logo-small.png");
		cid = super.sender.embed(img, "img2");
		vec.put("logo_cid", cid);
		
		
		img = WelcomeMessagePreparator.class.getResource(
				"/email_templates/images/blank.png");
		cid = super.sender.embed(img, "img3");
		vec.put("blank_cid", cid);
		
		vec.put("firstName", firstName);
		vec.put("username", emailAddress);
		vec.put("password", password);
		vec.put("loginUrl", loginUrl);



		out = new StringWriter();
		ve.evaluate(vec, out, "mystring", templateString);
		String body = out.toString();

		// set the html message
		super.sender.setHtmlMsg(body);
	}
}
