package org.quizzical.backend.sms.api;

import java.util.Arrays;
import java.util.List;

public class NotificationMessage {
	private String from;
	private List<String> to;
	private String body;
	private String title;
	public NotificationMessage(String to, String body, String title) {
		super();
		this.to = Arrays.asList(to);
		this.body = body;
		this.title = title;
	}
	public NotificationMessage(List<String> to, String body, String title) {
		super();
		this.to = to;
		this.body = body;
		this.title = title;
	}
	public NotificationMessage(String from, List<String> to, String body, String title) {
		super();
		this.from = from;
		this.to = to;
		this.body = body;
		this.title = title;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public List<String> getTo() {
		return to;
	}
	public void setTo(List<String> to) {
		this.to = to;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
