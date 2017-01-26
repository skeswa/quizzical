package org.quizzical.backend.mail.itests;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.amdatu.bndtools.test.BaseOSGiServiceTest;
import org.apache.commons.mail.EmailException;
import org.apache.felix.dm.DependencyManager;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.gauntlet.core.commons.util.StringUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogService;
import org.quizzical.backend.mail.api.IMailService;
import org.quizzical.backend.mail.api.LogServiceChute;
import org.quizzical.backend.mail.api.MailPreparator;

public class HTMLMailTest extends BaseOSGiServiceTest<LogService> {
	private volatile DependencyManager m_manager;
	private final BundleContext m_bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
	private IMailService emailService;
	private LogService logger;
	private VelocityEngine ve;
	
	public HTMLMailTest() {
		super(LogService.class);
	}
	
	
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		addServiceDependencies(LogService.class);
		getService(IMailService.class);
		Thread.currentThread().setContextClassLoader(javax.mail.Session.class.getClassLoader());


		this.ve = new VelocityEngine();
		this.ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,new LogServiceChute(instance));
		this.ve.init();
		
		
		this.emailService = getService(IMailService.class);
		this.logger = getService(LogService.class);
	}
	
	public void testPrepareAndSend() throws Exception {
		WelcomeMessagePreparator prep = new WelcomeMessagePreparator(this.emailService, this.logger, "Mandi", "Mandi", "mazagroup32@gmail.com", "Welcome to Q7L", "123", "http://www.q7l.io");
		prep.prepare();
		prep.send();
	}
	
	public class WelcomeMessagePreparator extends MailPreparator {

		private String emailAddress;
		private String fullName;
		private String password;
		private String firstName;
		private String loginUrl;
		private LogService logger;

		public WelcomeMessagePreparator(
				IMailService service, 
				LogService logger,
				String firstName,
				String fullName, 
				String emailAddress,
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
		}

		@Override
		public void prepare() throws Exception {
			super.sender.addTo(emailAddress, fullName);

			
			// -- Repare body
			InputStream is = HTMLMailTest.class
					.getResourceAsStream("/email_templates/welcome/welcome-to-quizzical-cid.html");
			String templateString = StringUtil.read(is);

			VelocityContext vec = new VelocityContext();
			StringWriter out = new StringWriter();
			ve.init();
			


			Map<String, Object> vars = new HashMap<String, Object>();
			URL img = HTMLMailTest.class.getResource(
					"/email_templates/images/cloud.gif");
			String cid = super.sender.embed(img, "img1");
			vec.put("cloud_cid", cid);
			
			img = HTMLMailTest.class.getResource(
					"/email_templates/images/quizzical-header-logo-small.png");
			cid = super.sender.embed(img, "img2");
			vec.put("logo_cid", cid);
			
			
			img = HTMLMailTest.class.getResource(
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
	
}
