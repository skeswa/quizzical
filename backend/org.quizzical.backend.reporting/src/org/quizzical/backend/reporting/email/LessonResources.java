package org.quizzical.backend.reporting.email;

import java.util.ArrayList;
import java.util.List;

public class LessonResources {
	private List<LessonResourceItem> items = new ArrayList<>();
	public LessonResources() {
	}
	public LessonResources(List<LessonResourceItem> items) {
		this.items = items;
	}
	public List<LessonResourceItem> getItems() {
		return items;
	}
	public void setItems(List<LessonResourceItem> items) {
		this.items = items;
	}
	public String html() {
		StringBuilder sb = new StringBuilder();
		for (LessonResourceItem i : items) {
			sb.append(i.html()+"<br>");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return html();
	}
}
