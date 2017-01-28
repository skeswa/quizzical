package org.quizzical.backend.contentrepository.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;

public interface IContentItemDAOService extends IBaseService {
	// ContentItems
	public List<ContentItem> findAll(int start, int end) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public ContentItem provide(ContentItem record) throws ApplicationException;
	
	public ContentItem update(ContentItem record) throws ApplicationException;
	
	public ContentItem delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public ContentItem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public ContentItem getByCode(String code) throws ApplicationException, NoSuchModelException;

	public ContentItem getByName(String name) throws ApplicationException, NoSuchModelException;
	
	//Misc
}
