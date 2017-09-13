package edu.harvard.hms.dbmi.bd2k.irct;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.json.Json;
import javax.json.JsonObject;

import edu.harvard.hms.dbmi.bd2k.irct.util.Utilities;

@Path("/token")
public class TokenValidation {
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public boolean validateToken(String token) throws IllegalArgumentException, UnsupportedEncodingException{
		Utilities.extractEmailFromJWT(token, null);
		return false;
	}
	
	@POST
	@Path("/validate")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getEmailFromToken(Token token) throws IOException{
		
		String email = null;
		 
		try {
			email = Utilities.extractEmailFromJWT(token.getToken(), null);
		
		} catch (NotAuthorizedException ex) {
			String errorMessage = "{\"status\":\"error\",\"message\":\"Could not establish the user identity from request headers. "+ex.getMessage()+"\"}";
			return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(errorMessage).build();
		
		} catch (Exception e) {
			String errorMessage = "{\"status\":\"error\",\"message\":\"Could not establish the user identity from request headers. "+e.getMessage()+"\"}"; 
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(errorMessage).build();
		}
		
		JsonObject responseJSON = Json.createObjectBuilder()
				.add("email", email)
				.build();
		return Response.ok().type("application/json").entity(responseJSON.toString()).build();
			
	}
	
}
