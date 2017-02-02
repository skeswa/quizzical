package org.quizzical.backend.reporting.email;

import org.quizzical.backend.reporting.analytics.impl.UserAnalyticsReportingConfiguration;

public class LessonResourceItem {
	private Long contentItemId;
	private String name;
	private UserAnalyticsReportingConfiguration config;
	public LessonResourceItem() {
	}
	public LessonResourceItem(UserAnalyticsReportingConfiguration config, Long contentItemId, String name) {
		super();
		this.contentItemId = contentItemId;
		this.name = name;
		this.config = config;
	}
	public Long getContentItem() {
		return contentItemId;
	}
	public void setContentItem(Long contentItemId) {
		this.contentItemId = contentItemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String html() {
		return String.format("&#x02139; <a href=\"%s/%scontent/id/%d/%s\" title=\"%s\">%s</a>",config.getBackendURLPrefix(),config.getBackendContextpath(),contentItemId,name,name,name);
	}
}