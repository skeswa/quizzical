package org.quizzical.backend.security.user.rest;

import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN;
import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX;

import java.util.Dictionary;
import java.util.Hashtable;

import org.amdatu.security.authentication.authservice.AuthenticationHandler;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.UserProfileService;

public class Activator extends DependencyActivatorBase {

    @Override
    public void init(BundleContext context, DependencyManager dm) throws Exception {
        Dictionary<String, Object> props = new Hashtable<>();
        props.put(HTTP_WHITEBOARD_CONTEXT_NAME, "my_app");

        dm.add(createComponent()
            .setInterface(Object.class.getName(), props)
            .setImplementation(SecuredResource.class)
            .add(createServiceDependency().setService(UserProfileService.class).setRequired(true))
            .add(createServiceDependency().setService(LogService.class).setRequired(false)));

        props = new Hashtable<>();
        props.put("org.amdatu.web.wink.rest.path", "/testing");
        props.put(HTTP_WHITEBOARD_CONTEXT_PATH, "/");
        props.put(HTTP_WHITEBOARD_CONTEXT_NAME, "my_app");

        dm.add(createComponent()
            .setInterface(ServletContextHelper.class.getName(), props)
            .setImplementation(SecuredResourceContextHelper.class)
            .add(createServiceDependency().setService(AuthenticationHandler.class).setRequired(true))
            .add(createServiceDependency().setService(LogService.class).setRequired(false)));

        props = new Hashtable<>();
        props.put(HTTP_WHITEBOARD_RESOURCE_PREFIX, "/res");
        props.put(HTTP_WHITEBOARD_RESOURCE_PATTERN, "/*");
        props.put(HTTP_WHITEBOARD_CONTEXT_SELECT, "(osgi.http.whiteboard.context.name=my_app)");
        dm.add(createComponent()
            .setInterface(Object.class.getName(), props)
            .setImplementation(new Object()));
    }

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
