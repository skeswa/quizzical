package org.quizzical.backend.gogo.service;

import org.apache.felix.service.command.Descriptor;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.Problem;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;

import static org.quizzical.backend.gogo.service.ServiceUtil.createServiceFromServiceType;

import java.util.Dictionary;


public class ContentItemCommands {
    public final static String SCOPE = "cntrepo";
    public final static String[] FUNCTIONS = new String[] { "show"};

    @Descriptor("Shows a content item by name")
    public static String show(@Descriptor("Unique content item name") String name) throws Exception {
    	IContentItemDAOService svc = (IContentItemDAOService)createServiceFromServiceType(IContentItemDAOService.class);
        return svc.getByName(name).toString();
    }
}
