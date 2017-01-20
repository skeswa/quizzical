package org.quizzical.backend.testdesign.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class TestDesignTemplateSection extends BaseEntity implements Serializable {
	private Integer ordinal;
	
	private TestDesignTemplateSectionType type;
	
	private TestDesignTemplate template;
	
	private List<TestDesignTemplateItem>  items;
	
	public TestDesignTemplateSection() {
	}
	
	public TestDesignTemplateSection(final String name, final String code) {
		this.name = name;
		this.code = code;		
	}
	
	public TestDesignTemplateSection(final TestDesignTemplateSectionType type, final TestDesignTemplate template, final Integer ordinal) {
		setItems(items);
		setOrdinal(ordinal);
		setType(type);
		final String code = String.format("%s-sec-%d",template.getCode(),getOrdinal());
		setName(code);
		setCode(code);
		
		items = new ArrayList<>();
	}
	
	public TestDesignTemplateSection(final TestDesignTemplateSectionType type, final List<TestDesignTemplateItem> items, final TestDesignTemplate template, final Integer ordinal) {
		this(type,template,ordinal);
		this.items = items;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public TestDesignTemplate getTemplate() {
		return template;
	}

	public void setTemplate(TestDesignTemplate template) {
		this.template = template;
	}

	public List<TestDesignTemplateItem> getItems() {
		return items;
	}

	public void setItems(List<TestDesignTemplateItem> items) {
		this.items = items;
	}

	public TestDesignTemplateSectionType getType() {
		return type;
	}

	public void setType(TestDesignTemplateSectionType type) {
		this.type = type;
	}
	
	public List<TestDesignTemplateItem> getOrderedItems() {
		List<TestDesignTemplateItem> orderedItems = getItems();
		Collections.sort(orderedItems, new Comparator<TestDesignTemplateItem>() {
			@Override
			public int compare(TestDesignTemplateItem o1, TestDesignTemplateItem o2) {
				if  (o1.getOrdinal() < o2.getOrdinal())
					return -1;
				else if (o1.getOrdinal() > o2.getOrdinal())
					return  1;
				else 
					return 0;//they must be the same
			}
		});
		return orderedItems;
	}
}
