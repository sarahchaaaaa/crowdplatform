package crowdsense.cimoninterface.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;


@Path("/files")
public class FileUpload {

	private static final String upload_location = "/project/koios/data/";
	
	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(
			@FormDataParam("file") InputStream _inputStream,
			@FormDataParam("file") FormDataContentDisposition _contentDisposition, @Context HttpServletRequest request) {
		
		Response response = new Response();
		try {
			System.out.println("In upload file method:"+ _contentDisposition.getFileName());

			/*if(!getFileExtension(_contentDisposition.getFileName()).contains("wav")){
				System.out.println("returning status 400 unsupported file type");
				return Response.status(400).entity("unsupported file type").build();
			}*/
			
			//String uniqueFileId = UUID.randomUUID().toString(); 
			//String fileName = uniqueFileId + ".wav";
			//String filePath = upload_location	+ _contentDisposition.getFileName();
			String filePath = upload_location	+ _contentDisposition.getFileName();
			System.out.println("file path:"+ filePath);
			

			// save the file to the server
			saveFile(_inputStream, filePath);
			double fileSize = getFileSize(filePath);
			System.out.println("file size:"+ fileSize);
			/*if(fileSize > 30){
				//delete files and retrun error code
				deleteFile(filePath);
				return Response.status(400).entity("file size too long").build();
			}*/
			
			System.out.println("extension:"+ getFileExtension(filePath) + ", mime type:"+ getMIMEType(filePath));
			response.setCode(0);
			
		} catch (Exception e) {
			// TODO: handle exception
			response.setCode(-1);
		}

		return response;
		

	}
	
//	@OPTIONS
//	public Response uploadFile(){
//		return Response.status(200).entity("Success").build();
//	}

	// save uploaded file to a defined location on the server
	private void saveFile(InputStream _inputStream, String _location) {

		try {
			System.out.println("File write path:" + _location);
			OutputStream _outputStream = new FileOutputStream(new File(_location));
			int read = 0;
			byte[] bytes = new byte[1024];

			_outputStream = new FileOutputStream(new File(_location));
			while ((read = _inputStream.read(bytes)) != -1) {
				_outputStream.write(bytes, 0, read);
			}
			_outputStream.flush();
			_outputStream.close();
			System.out.println("File writing completed");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private double getFileSize(String filePath){
		try {
			File file =new File(filePath);
			if(file.exists()){

				double bytes = file.length();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				return megabytes;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}
	
	private String getFileExtension(String filePath){
		try {
			String extension = FilenameUtils.getExtension(filePath);
			System.out.println("file extension : "+ extension);
			return extension;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	private String getMIMEType(String filePath){
		try {
			File file = new File(filePath);
			if(file.exists()){
				return URLConnection.guessContentTypeFromName(file.getName());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";

	}
	
	private boolean deleteFile(String filePath){
		try {
			File file = new File(filePath);
			return file.delete();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test(){
		return new Response();
	}
	
	@POST
	@Path("upload2")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFiles(
			@FormDataParam("files") List<FormDataBodyPart> bodyParts,
			@FormDataParam("files") FormDataContentDisposition _contentDisposition, @Context HttpServletRequest request) {
		
		Response response = new Response();
		try {
			System.out.println("In upload file method:"+ _contentDisposition.getFileName());

			
			/* Save multiple files */

			for (int i = 0; i < bodyParts.size(); i++) {
				/*
				 * Casting FormDataBodyPart to BodyPartEntity, which can give us
				 * InputStream for uploaded file
				 */
				BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
				String fileName = bodyParts.get(i).getContentDisposition().getFileName();
				
				String filePath = upload_location	+ fileName;
				System.out.println("file path:"+ filePath);


				saveFile(bodyPartEntity.getInputStream(), fileName);

				double fileSize = getFileSize(filePath);
				System.out.println("file size:"+ fileSize);
				/*if(fileSize > 30){
					//delete files and retrun error code
					deleteFile(filePath);
					return Response.status(400).entity("file size too long").build();
				}*/
				
				System.out.println("extension:"+ getFileExtension(filePath) + ", mime type:"+ getMIMEType(filePath));
			}
			

			String filePath = upload_location	+ _contentDisposition.getFileName();
			System.out.println("file path:"+ filePath);
			

			response.setCode(0);
			
		} catch (Exception e) {
			// TODO: handle exception
			response.setCode(-1);
		}

		return response;
		

	}

	
	
}


//@Path("/files")
//public class FileUpload {
//
//	private static final String upload_location = "/database/dod/mobiledata/";
//	
//	@POST
//	@Path("upload")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response uploadFile(
//			@FormDataParam("file") InputStream _inputStream,
//			@FormDataParam("file") FormDataContentDisposition _contentDisposition, @Context HttpServletRequest request) {
//		
//		Response response = new Response();
//		try {
//			System.out.println("In upload file method:"+ _contentDisposition.getFileName());
//
//			/*if(!getFileExtension(_contentDisposition.getFileName()).contains("wav")){
//				System.out.println("returning status 400 unsupported file type");
//				return Response.status(400).entity("unsupported file type").build();
//			}*/
//			
//			//String uniqueFileId = UUID.randomUUID().toString(); 
//			//String fileName = uniqueFileId + ".wav";
//			//String filePath = upload_location	+ _contentDisposition.getFileName();
//			String filePath = upload_location	+ _contentDisposition.getFileName();
//			System.out.println("file path:"+ filePath);
//			
//
//			// save the file to the server
//			saveFile(_inputStream, filePath);
//			double fileSize = getFileSize(filePath);
//			System.out.println("file size:"+ fileSize);
//			/*if(fileSize > 30){
//				//delete files and retrun error code
//				deleteFile(filePath);
//				return Response.status(400).entity("file size too long").build();
//			}*/
//			
//			System.out.println("extension:"+ getFileExtension(filePath) + ", mime type:"+ getMIMEType(filePath));
//			response.setCode(0);
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			response.setCode(-1);
//		}
//
//		return response;
//		
//
//	}
//	
////	@OPTIONS
////	public Response uploadFile(){
////		return Response.status(200).entity("Success").build();
////	}
//
//	// save uploaded file to a defined location on the server
//	private void saveFile(InputStream _inputStream, String _location) {
//
//		try {
//			System.out.println("File write path:" + _location);
//			OutputStream _outputStream = new FileOutputStream(new File(_location));
//			int read = 0;
//			byte[] bytes = new byte[1024];
//
//			_outputStream = new FileOutputStream(new File(_location));
//			while ((read = _inputStream.read(bytes)) != -1) {
//				_outputStream.write(bytes, 0, read);
//			}
//			_outputStream.flush();
//			_outputStream.close();
//			System.out.println("File writing completed");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//	
//	private double getFileSize(String filePath){
//		try {
//			File file =new File(filePath);
//			if(file.exists()){
//
//				double bytes = file.length();
//				double kilobytes = (bytes / 1024);
//				double megabytes = (kilobytes / 1024);
//				return megabytes;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return -1;
//	}
//	
//	private String getFileExtension(String filePath){
//		try {
//			String extension = FilenameUtils.getExtension(filePath);
//			System.out.println("file extension : "+ extension);
//			return extension;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return "";
//	}
//	
//	private String getMIMEType(String filePath){
//		try {
//			File file = new File(filePath);
//			if(file.exists()){
//				return URLConnection.guessContentTypeFromName(file.getName());
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return "";
//
//	}
//	
//	private boolean deleteFile(String filePath){
//		try {
//			File file = new File(filePath);
//			return file.delete();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return false;
//	}
//	
//	@GET
//	@Path("test")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response test(){
//		return new Response();
//	}
//	
//	@POST
//	@Path("upload2")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response uploadFiles(
//			@FormDataParam("files") List<FormDataBodyPart> bodyParts,
//			@FormDataParam("files") FormDataContentDisposition _contentDisposition, @Context HttpServletRequest request) {
//		
//		Response response = new Response();
//		try {
//			System.out.println("In upload file method:"+ _contentDisposition.getFileName());
//
//			
//			/* Save multiple files */
//
//			for (int i = 0; i < bodyParts.size(); i++) {
//				/*
//				 * Casting FormDataBodyPart to BodyPartEntity, which can give us
//				 * InputStream for uploaded file
//				 */
//				BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
//				String fileName = bodyParts.get(i).getContentDisposition().getFileName();
//				
//				String filePath = upload_location	+ fileName;
//				System.out.println("file path:"+ filePath);
//
//
//				saveFile(bodyPartEntity.getInputStream(), fileName);
//
//				double fileSize = getFileSize(filePath);
//				System.out.println("file size:"+ fileSize);
//				/*if(fileSize > 30){
//					//delete files and retrun error code
//					deleteFile(filePath);
//					return Response.status(400).entity("file size too long").build();
//				}*/
//				
//				System.out.println("extension:"+ getFileExtension(filePath) + ", mime type:"+ getMIMEType(filePath));
//			}
//			
//
//			String filePath = upload_location	+ _contentDisposition.getFileName();
//			System.out.println("file path:"+ filePath);
//			
//
//			response.setCode(0);
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			response.setCode(-1);
//		}
//
//		return response;
//		
//
//	}
//
//	
//	
//}
