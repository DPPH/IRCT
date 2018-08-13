/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package edu.harvard.hms.dbmi.bd2k.irct.cl.filter;

import edu.harvard.hms.dbmi.bd2k.irct.cl.util.CustomRequestWrapper;
import edu.harvard.hms.dbmi.bd2k.irct.cl.util.Utilities;
import edu.harvard.hms.dbmi.bd2k.irct.model.security.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * Creates a session filter for ensuring secure access
 */
@WebFilter(filterName = "medchain-query-filter", urlPatterns = { "/rest/*" })
public class MedChainQueryFilter implements Filter {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void init(FilterConfig fliterConfig){
    }

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		logger.debug("doFilter():medchain-query-filter Starting");
		HttpServletRequest request = (HttpServletRequest) req;

		// Only proceed if the servlet has been asked to run the query
		if (!request.getRequestURI().endsWith("/runQuery")) {
			// Do Nothing
			logger.debug("doFilter():medchain-query-filter URL is NOT filtered.");

			// Continue the filter chain
			fc.doFilter(req, res);
		} else {
			CustomRequestWrapper customRequestWrapper;

			// Wrap the original request, so that we can read the stream multiple times
			try {
				customRequestWrapper = new CustomRequestWrapper(request);
			} catch (Exception ex) {
				logger.error("doFilter():medchain-query-filter Unable to wrap the request", ex);
				throw new ServletException("Unable to wrap the request", ex);
			}

			// Check if the query has already been logged in the MedChain
			try {
				String queryToken = customRequestWrapper.getHeader("Query-Token");
				String queryLoggedInMedChain = Utilities.extractUserFromTokenIntrospection(customRequestWrapper, "query", "http://" + "omniledger" + ":8989/tokenIntrospectionQuery", "xxx", queryToken);
				if (customRequestWrapper.payload.equals(queryLoggedInMedChain)) {
					logger.debug("doFilter():medchain-query-filter Query was found to be logged on the MedChain");
				} else {
					throw new Exception("Query authentication through MedChain failed");
				}
				String userIdLoggedInMedChain = Utilities.extractUserFromTokenIntrospection(customRequestWrapper, "user_id", "http://" + "omniledger" + ":8989/tokenIntrospectionQuery", "xxx", queryToken);
				HttpSession session = ((HttpServletRequest) req).getSession();
				User user = (User) session.getAttribute("user");
				if (!user.getUserId().equals(userIdLoggedInMedChain)) {
					logger.debug("doFilter():medchain-query-filter Logged userId did not match with the current authenticated user");
					throw new Exception("Query authentication through MedChain failed");
				}
			} catch (Exception ex) {
				logger.error("doFilter():medchain-query-filter "  + customRequestWrapper.getHeader("Query-Token") + ex.getMessage());
				ex.printStackTrace();
				String errorMessage = "{\"status\":\"error\",\"message\":\"Query could not be authenticated through the MedChain. " + ex.getClass().getName() + " " + ex.getMessage()+"\"}";
				((HttpServletResponse) res).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				res.setContentType("application/json");
				res.getOutputStream()
						.write(errorMessage.getBytes());
				res.getOutputStream().close();
				return;
			}

			// continue the filter chain
			fc.doFilter(customRequestWrapper, res);
		}

	}

	@Override
	public void destroy() {

	}

}
