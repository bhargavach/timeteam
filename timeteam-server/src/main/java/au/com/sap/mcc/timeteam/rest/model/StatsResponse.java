package au.com.sap.mcc.timeteam.rest.model;

import java.util.ArrayList;
import java.util.List;

public class StatsResponse extends RestResponse {

	protected List<DataSet> datasets = new ArrayList<DataSet>();

	public List<DataSet> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet> datasets) {
		this.datasets = datasets;
	}
}
