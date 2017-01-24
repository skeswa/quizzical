package org.quizzical.backend.gogo.service;

import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;

public class CommandTestDesignTemplates {

    private final ITestDesignTemplateDAOService tdService;

    public CommandTestDesignTemplates(ITestDesignTemplateDAOService tdService) {
        this.tdService = tdService;
    }

    public ITestDesignTemplateDAOService get() {
    	return tdService;
    }
}
