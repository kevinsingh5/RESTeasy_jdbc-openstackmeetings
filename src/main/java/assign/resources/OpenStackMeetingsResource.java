package assign.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import assign.domain.ErrorHandler;
import assign.domain.Meeting;
import assign.domain.Meetings;
import assign.domain.Project;
import assign.domain.Projects;
import assign.services.OpenStackMeetingsService;
import assign.services.OpenStackMeetingsServiceImpl;

@Path("/projects")
public class OpenStackMeetingsResource {
	
	// Placeholder for OpenStackMeetings service;
	OpenStackMeetingsService osmService;
	String link = "http://eavesdrop.openstack.org/meetings";
	
	public OpenStackMeetingsResource() {
		this.osmService = new OpenStackMeetingsServiceImpl();
	}
	
	// Use this for unit testing
	protected void setOpenStackMeetingsService(OpenStackMeetingsService osmService) {
		this.osmService = osmService;
	}
	
	// Default landing page for /projects - shows all projects
	@GET
	@Path("")
	@Produces("application/xml")
	public Projects getAllProjects() throws Exception {
		//String link = "http://eavesdrop.openstack.org/meetings";
		String value = "";
		Projects projects = new Projects();
		List<String> projectList = osmService.getData(link, value);
		projects.setProjects(projectList);
		
		return projects;    
	}	
	
	// return the meetings for a specific project
	@GET
	@Path("/{project_id}/meetings")
	@Produces("application/xml")
	public StreamingOutput getMeeting(@PathParam("project_id") String project_id) throws Exception {
		final Meetings meetings = new Meetings();
		List<String> meetingList = osmService.getData(link, project_id);	
		
		if(meetingList != null) {
			meetings.setMeetings(meetingList);
			return new StreamingOutput() {
		         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		            outputMeetings(outputStream, meetings);
		         }
		    };
		} else {
			final ErrorHandler error = new ErrorHandler();
			error.setError("Project " + project_id + " does not exist");
			return new StreamingOutput() {
		         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		            outputMeetings(outputStream, error);
		         }
		    };
		}	    
	}

	// outputs the meetings from getMeetings()
	protected void outputMeetings(OutputStream os, Meetings meetings) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Meetings.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(meetings, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}	
	
	// outputs the error from getMeetings()
	protected void outputMeetings(OutputStream os, ErrorHandler error) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(ErrorHandler.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(error, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
}