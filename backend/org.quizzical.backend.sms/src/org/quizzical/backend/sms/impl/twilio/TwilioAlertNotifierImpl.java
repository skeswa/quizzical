package org.quizzical.backend.sms.impl.twilio;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;
import org.quizzical.backend.sms.api.AlertNotificationException;
import org.quizzical.backend.sms.api.IAlertNotifier;
import org.quizzical.backend.sms.api.NotificationMessage;

public class TwilioAlertNotifierImpl implements IAlertNotifier, ManagedService {
	public static final String FROM_NUM_PROP = "org.quizzical.backend.sms.from";
	public static final String ACCOUNT_SID_PROP = "org.quizzical.backend.sms.account.sid";
	public static final String AUTH_TOKEN_PROP = "org.quizzical.backend.sms.auth.token";
	
	private volatile LogService logger;

	private TwilioRestClient m_client;
	
	private String smsAccountSid;
	private String authToken;
	private String from;

	public void start() {
		m_client = new TwilioRestClient(smsAccountSid, authToken);
	}
	
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		this.smsAccountSid = (String)properties.get(ACCOUNT_SID_PROP);
		this.authToken = (String)properties.get(AUTH_TOKEN_PROP);
		this.from =  (String)properties.get(FROM_NUM_PROP);
	}


	@Override
	public void notifyViaSMS(NotificationMessage message) throws AlertNotificationException {
		message.setFrom(this.from);
		
		// Build a filter for the MessageList
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Body", message.getBody()));

		// Create To list
		List<String> list = message.getTo();
		StringBuffer sb = new StringBuffer();
		if (list.size() == 1)
			sb.append(list.get(0));
		else {
			for (int i = 0; i < list.size(); i++) {
				if (i != (list.size() - 1))
					sb.append(list.get(i) + ",");
				else
					sb.append(list.get(i));
			}
		}

		params.add(new BasicNameValuePair("To", sb.toString()));

		params.add(new BasicNameValuePair("From", message.getFrom()));

		try {
			MessageFactory messageFactory = m_client.getAccount().getMessageFactory();
			Message smsMessage = messageFactory.create(params);
			System.out.println(smsMessage.getSid());
		} catch (TwilioRestException e) {
			throw new AlertNotificationException(e);
		}
	}

	@Override
	public void notifyViaEmail(NotificationMessage message) throws AlertNotificationException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void testSMS(final String mobileNumber) throws AlertNotificationException {
		NotificationMessage msg = new NotificationMessage(Collections.singletonList(mobileNumber),"Testing @ "+new Date().toString(),"q7l test");
		notifyViaSMS(msg);
	}
}
