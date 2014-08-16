package au.com.sap.mcc.timeteam.rest.model;


public class RestResponse {

	private String errorMessage;
	private Boolean success = true;
		
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
