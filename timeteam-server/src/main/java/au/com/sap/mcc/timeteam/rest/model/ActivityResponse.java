package au.com.sap.mcc.timeteam.rest.model;

import java.util.ArrayList;
import java.util.List;

public class ActivityResponse extends RestResponse {
	protected List<Activity> activities = new ArrayList<Activity>();

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	

}
