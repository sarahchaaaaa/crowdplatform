package services.auth;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import commons.Response;


@Path("/{udid}")
public class TestService {


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
	
	public static void main(String[] args) {
		
		
	    ClientConfig config = new DefaultClientConfig();
	    Client client = Client.create(config);
		String url = "http://10.32.10.188:8080/mcsservice/auth/signup";

		WebResource service = client.resource(url);
		
		User user = new User("uttam028@gmail.com", "1");
		
	    String json = "{\"email\":\"ahossain@nd.edu\", \"oldPassword\":\"old\", \"newPassword\":\"new\"}";
		ClientResponse clientResponse = service.type(MediaType.APPLICATION_XML).post(ClientResponse.class, user);
		System.out.println("response:"+ clientResponse);
		String jsonResponse = clientResponse.getEntity(String.class);
		System.out.println("json response:"+ jsonResponse);
	   
	   
	    
	}

	
}
