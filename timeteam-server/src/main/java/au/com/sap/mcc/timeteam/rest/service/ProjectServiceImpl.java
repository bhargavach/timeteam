package au.com.sap.mcc.timeteam.rest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.sap.mcc.timeteam.dao.ProjectDao;
import au.com.sap.mcc.timeteam.rest.model.Project;
import au.com.sap.mcc.timeteam.rest.model.ProjectRequest;
import au.com.sap.mcc.timeteam.rest.model.ProjectResponse;

public class ProjectServiceImpl implements ProjectService {
	
	private Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired 
	ProjectDao projectDao;
	
	@Override
	public ProjectResponse get(String id) {
		log.debug(">> get("+ id+ ")");
		ProjectResponse response = new ProjectResponse();
		try {
			au.com.sap.mcc.timeteam.model.Project jpaProject = projectDao.fetchById(id);
			response.setProjects(Arrays.asList(Project.convert(jpaProject)));
		} catch(Exception e) {
			log.error("getProject",e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	@Override
	public ProjectResponse list() {
		ProjectResponse response = new ProjectResponse();
		try {
			List<au.com.sap.mcc.timeteam.model.Project> jpaProjects = projectDao.findAll();
			List<Project> projects = new ArrayList<Project>();
			
			for(au.com.sap.mcc.timeteam.model.Project jpaProject : jpaProjects) {
				Project project = Project.convert(jpaProject);
				projects.add(project);
			}
			
			response.setProjects(projects);
			
		} catch(Exception e) {
			log.error("listAll", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	@Override
	public ProjectResponse create(ProjectRequest request) {
		ProjectResponse response = new ProjectResponse();
		try {
			au.com.sap.mcc.timeteam.model.Project jpaProject = new au.com.sap.mcc.timeteam.model.Project();
			
			jpaProject.setName(request.getProject().getName());
			jpaProject.setShortname(request.getProject().getShortname());			
			
			Project project =  Project.convert(projectDao.save(jpaProject));
			
			response.setProjects(Arrays.asList(project));
		} catch(Exception e) {
			log.error("listAll", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}
	
	@Override
	public ProjectResponse update(ProjectRequest request) {
		ProjectResponse response = new ProjectResponse();
		try {
			au.com.sap.mcc.timeteam.model.Project jpaProject = projectDao.fetchById(request.getProject().getId());
			
			jpaProject.setName(request.getProject().getName());
			jpaProject.setShortname(request.getProject().getShortname());
			jpaProject.setVersion(request.getProject().getVersion());
			
			Project project =  Project.convert(projectDao.save(jpaProject));
			
			response.setProjects(Arrays.asList(project));
		} catch(Exception e) {
			log.error("listAll", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}

	@Override
	public ProjectResponse delete(ProjectRequest request) {
		ProjectResponse response = new ProjectResponse();
		try {
			au.com.sap.mcc.timeteam.model.Project jpaProject = projectDao.fetchById(request.getProject().getId());
			projectDao.delete(jpaProject);
		} catch(Exception e) {
			log.error("delete", e);
			response.setSuccess(false);
			response.setErrorMessage(e.getClass() + ":" + e.getMessage());
		}
		return response;
	}
}
