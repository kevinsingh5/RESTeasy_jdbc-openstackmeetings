package assign.services;

import java.util.List;

import assign.domain.NewProject;


public interface OpenStackMeetingsService {

	public NewProject addProject(NewProject newProject) throws Exception;
		
	public NewProject updateProject(NewProject newProject) throws Exception;
	
	//public List<String> getData(String link, String value);

}
