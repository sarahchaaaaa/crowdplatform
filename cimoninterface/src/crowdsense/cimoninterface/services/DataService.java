package crowdsense.cimoninterface.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import crowdsense.cimoninterface.util.Response;
import crowdsense.cimoninterface.util.ServiceUtil;

@Path("/data")
public class DataService {
	
	
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@GET
	@Path("location")
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(@QueryParam("uuid") String uuid, @QueryParam("email") String email, @QueryParam("lat") String latitude, @QueryParam("long") String longitude
			, @QueryParam("sourcetime") String sourceTime, @QueryParam("accuracy") String accuracy){
		Response response = new Response();
		if(ServiceUtil.isEmptyString(uuid) || ServiceUtil.isEmptyString(email)){
			response.setMessage("Invalid input");
			return response;
		}
		
		System.out.println("uuid:" + uuid + ", email:" + email + ", latitude:" + latitude + ", longitude:" + longitude + ", time:" + sourceTime + ", accuracy:" + accuracy);
		
		response.setCode(0);
		return response;

	}
	

}
