package org.quizzical.backend.gogo.service;

import org.apache.commons.io.IOUtils;
import org.apache.felix.service.command.Descriptor;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.io.File;
import java.io.FileInputStream;


public class ContentItemCommands {
    public final static String SCOPE = "cntrepo";
    public final static String[] FUNCTIONS = new String[] { "show","addpdf"};

    @Descriptor("Shows a content item by name")
    public static String show(@Descriptor("Unique content item name") String name) throws Exception {
    	IContentItemDAOService svc = (IContentItemDAOService)createServiceFromServiceType(IContentItemDAOService.class);
        return svc.getByName(name).toString();
    }
    
    @Descriptor("Add a content item")
    public static Long addpdf(@Descriptor("Unique content item name") String name, 
    						@Descriptor("Unique content item filePath") String filePath) throws Exception {
    	IContentItemDAOService svc = (IContentItemDAOService)createServiceFromServiceType(IContentItemDAOService.class);
        
    	ContentItem ci = null;
    	
    	FileInputStream fis = null;
    	byte[] content = null;
		try {
			File fi = new File(filePath);
			fis = new FileInputStream(fi);
			content = IOUtils.toByteArray(fis);
			
			String ct = "application/pdf";
			long cs = fi.length();
			
			final String code = Long.toString(System.currentTimeMillis()) + name;
			ci = new ContentItem(name, code, content, ct, cs);
			ci = svc.provide(ci);
		} finally {
			if (fis != null)
				fis.close();
		}
		
		return ci.getId();
    }
}
