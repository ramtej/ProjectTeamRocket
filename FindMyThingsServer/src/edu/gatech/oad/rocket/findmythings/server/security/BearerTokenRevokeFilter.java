package edu.gatech.oad.rocket.findmythings.server.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import edu.gatech.oad.rocket.findmythings.server.util.HTTP;
import edu.gatech.oad.rocket.findmythings.server.util.Messages;
import edu.gatech.oad.rocket.findmythings.server.util.Responses;

public class BearerTokenRevokeFilter extends LogoutFilter {

	public BearerTokenRevokeFilter() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        subject.logout();
        HTTP.writeAsJSON(response,
        		Responses.STATUS, HTTP.Status.OK.toInt(),
        		Responses.MESSAGE, Messages.Status.OK.toString());
        return false;
    }

}
