package de.frittenburger.transcribe.model;

public class StatusMessage extends Message {

	private String jobId;
	private String status;
	
	public StatusMessage(String jobId, String status) {
		super("status");
		this.jobId = jobId;
		this.status = status;
	}

	public String getJobId() {
		return jobId;
	}

	public String getStatus() {
		return status;
	}

}
