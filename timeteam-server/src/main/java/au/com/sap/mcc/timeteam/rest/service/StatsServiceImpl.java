package au.com.sap.mcc.timeteam.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import au.com.sap.mcc.timeteam.dao.ProjectDao;
import au.com.sap.mcc.timeteam.model.Project;
import au.com.sap.mcc.timeteam.model.Task;
import au.com.sap.mcc.timeteam.rest.model.DataSet;
import au.com.sap.mcc.timeteam.rest.model.StatsResponse;

public class StatsServiceImpl implements StatsService {

	@Autowired
	protected ProjectDao projectDao;

	public StatsResponse timeAllocationByProject() {
		List<Project> projects = projectDao.findAll();
		List<DataSet> datasets = new ArrayList<DataSet>();
		StatsResponse statsResponse = new StatsResponse();

		if (projects != null) {
			for (Project project : projects) {
				DataSet dataset = new DataSet();
				dataset.setName(project.getName());
				dataset.setValue(String.valueOf(project.totalDuration()));
				datasets.add(dataset);
			}
			statsResponse.setDatasets(datasets);
		}
		
		return statsResponse;
	}

	public StatsResponse timeAllocationByTask(String id) {
		Project project = projectDao.fetchById(id);
		List<DataSet> datasets = new ArrayList<DataSet>();
		StatsResponse statsResponse = new StatsResponse();

		if (project.getTasks() != null) {
			for (Task task : project.getTasks()) {
				DataSet dataset = new DataSet();
				dataset.setName(task.getName());
				dataset.setValue(String.valueOf(task.totalDuration()));
				datasets.add(dataset);
			}
			statsResponse.setDatasets(datasets);
		}
		
		return statsResponse;

	}

	public void monthlyTimeTotal(int month) {
		// TODO Auto-generated method stub

	}

	public void timeAllocationByDay(String projectId) {
		// TODO Auto-generated method stub

	}

}
