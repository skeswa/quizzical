package org.quizzical.backend.security.user.rest;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amdatu.security.authentication.authservice.AuthenticationHandler;
import org.amdatu.security.authentication.authservice.RejectInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.log.LogService;

public class SecuredResourceContextHelper extends ServletContextHelper {

    private final Bundle m_bundle;
    // Injected by Felix DM...
    private volatile AuthenticationHandler m_authHandler;
    private volatile LogService m_log;

    /**
     * Creates a new {@link SecuredResourceContextHelper} instance.
     */
    public SecuredResourceContextHelper() {
        m_bundle = FrameworkUtil.getBundle(getClass());
    }

    @Override
    public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	Optional<RejectInfo> rejectInfo = m_authHandler.handleSecurity(request, response);

        rejectInfo.ifPresent(ri -> {
            m_log.log(LogService.LOG_INFO, "Denied access for request: " + request.getRequestURL());

            try {
                ri.applyTo(response);
            }
            catch (IOException e) {
                m_log.log(LogService.LOG_WARNING, "Failed to redirect to " + ri.getFailureURL(), e);
            }
        });

        boolean auth = false;
        if (rejectInfo.isPresent()) {
            RejectInfo info = rejectInfo.get();

            info.getFailureURL();
        } else {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || "/".equals(pathInfo)) {
                response.sendRedirect("/index.html");
            }
            auth = true;
        }

        return auth;
    }

    @Override
    public URL getResource(String name) {
        if (name == null) {
            return null;
        }
        return m_bundle.getResource(name);
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        if (path == null) {
            return null;
        }
        Set<String> result = new LinkedHashSet<String>();
        Enumeration<URL> e = m_bundle.findEntries(path, "*", false /* recurse */);
        if (e != null) {
            while (e.hasMoreElements()) {
                result.add(e.nextElement().getPath());
            }
        }
        return result;
    }
}
