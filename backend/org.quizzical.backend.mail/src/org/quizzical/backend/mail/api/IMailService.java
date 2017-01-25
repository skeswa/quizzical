package org.quizzical.backend.mail.api;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public interface IMailService {
	public HtmlEmail createHtmlSender(String subject) throws EmailException;
}
