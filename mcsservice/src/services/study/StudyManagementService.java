package services.study;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import commons.DatabaseUtil;
import commons.Response;
import commons.ServiceUtil;

@Path("study")
public class StudyManagementService {

	private Connection connection;
	private PreparedStatement preparedStatement;
	
	
	@Path("create")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createStudy(JAXBElement<Study> jaxbElement){
		Response response = new Response();
		try {
			Study study = jaxbElement.getValue();
			connection = DatabaseUtil.connectToDatabase();
			
			String query = "select * from mcs.study where created_by=? and name=?";
			System.out.println("query:" + query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, study.getCreatedBy());
			preparedStatement.setString(2, study.getName());
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				response.setCode(1);
				response.setMessage("Name already exist");
			} else {
				query = "insert into mcs.study (name, description, created_by, creation_time, creation_time_zone, is_public, instruction, icon_url) "
						+ " values (?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, study.getName());
				preparedStatement.setString(2, study.getDescription());
				preparedStatement.setString(3, study.getCreatedBy());
				preparedStatement.setString(4, study.getCreationTime());
				preparedStatement.setString(5, study.getCreationTimeZone());
				preparedStatement.setInt(6, study.getIsPublic());
				preparedStatement.setString(7, study.getInstruction());
				preparedStatement.setString(8, study.getIconUrl());
				
				preparedStatement.execute();
				
				query = "select last_insert_id() as id";
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				String id = "";
				if(resultSet.next()){
					id = resultSet.getString("id");
				}
				
				response.setCode(0);
				response.setMessage(id);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return response;

	}
	
	
	@Path("mylist/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Study> getStudies(@PathParam("email") String email){
		ArrayList<Study>  studyList = new ArrayList<>();
		
		if(ServiceUtil.isEmptyString(email)){
			
		}else{
			try {
				
				connection = DatabaseUtil.connectToDatabase();
				String query = "select * from mcs.study where created_by='"+ email +"'  order by creation_time desc";
				preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					try {
						Study study = new Study();
						int id = resultSet.getInt("id");
						String name = resultSet.getString("name");
						String description = resultSet.getString("description");
						int state = resultSet.getInt("state");
						
						study.setId(id);
						study.setName(name);
						study.setDescription(description);
						study.setCreatedBy(email);
						study.setState(state);
						
						studyList.add(study);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		return studyList;
		
		
	}
	
	@Path("{studyId}/participant")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestMessage(@PathParam("studyId") String studyId) {
		System.out.println("receive the path test "+ studyId);
		return new Response(0, "Test Message");
	}
	

}
