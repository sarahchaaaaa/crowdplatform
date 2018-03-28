package crowdsense.cimoninterface.services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import crowdsense.cimoninterface.util.Response;

@Path("/{udid}")
public class TestService {

	private Connection connection = null;
	private Statement statement;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@GET
	@Path("signup1")
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(@QueryParam("email") String email){
		Response response = new Response();
		response.setMessage("This is signup method "+ email);
		return response;
	}
	
	@GET
	@Path("verifytoken1")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyToken(@QueryParam("email") String email, @QueryParam("token") String token){
		Response response = new Response();
		response.setMessage("This is verify token method "+ email + ", token:" + token);
		return response;
	}
	
	
}
