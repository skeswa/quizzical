package org.quizzical.backend.security.user.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.osgi.service.http.context.ServletContextHelper.REMOTE_USER;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.quizzical.backend.security.api.dao.user.UserProfileService;
//import org.quizzical.backend.security.api.model.user.UserProfile;


@Path("/")
public class SecuredResource {
    // Injected by Felix DM...
    //private volatile UserProfileService m_userProfileSrv;

    @GET
    @Path("whoami")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResource(@Context HttpServletRequest req) {
        String userID = (String) req.getAttribute(REMOTE_USER);

/*        UserProfile profile = m_userProfileSrv.getUserProfile(userID);
        if (profile == null) {
            return Response.status(NOT_FOUND).build();
        }
*/
        Map<String, String> user = new HashMap<>();
/*        user.put("email", profile.getEmail());
        user.put("userID", profile.getUserID());
        user.put("name", profile.getEmail().replaceFirst("@.*$", ""));*/

        return Response.ok(user).build();
    }

}
