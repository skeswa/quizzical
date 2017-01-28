package org.quizzical.backend.mail.api;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public abstract class MailPreparator {
		protected HtmlEmail sender;
		public MailPreparator(IMailService service, String subject) throws EmailException {
			this.sender = service.createHtmlSender(subject);
		}
		
		public String send() throws EmailException {
			Thread.currentThread().setContextClassLoader(javax.mail.Message.class.getClassLoader());
			return this.sender.send();
		}
		
		public abstract void prepare() throws Exception;
}
