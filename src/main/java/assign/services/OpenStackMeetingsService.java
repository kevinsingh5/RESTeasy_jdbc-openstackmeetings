package assign.services;

import java.util.List;

import assign.domain.NewProject;


public interface OpenStackMeetingsService {

	NewProject addProject(NewProject newProject) throws Exception;
		
	//public List<String> getData(String link, String value);

}
