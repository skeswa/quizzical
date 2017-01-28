package org.quizzical.backend.gogo.service;

import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;

public class CommandContentItems {

    private final IContentItemDAOService contentService;

    public CommandContentItems(IContentItemDAOService contentService) {
        this.contentService = contentService;
    }

    public IContentItemDAOService get() {
    	return contentService;
    }
}
