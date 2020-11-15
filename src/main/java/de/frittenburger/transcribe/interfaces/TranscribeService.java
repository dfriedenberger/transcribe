package de.frittenburger.transcribe.interfaces;

public interface TranscribeService {


	String startTranscription(String deviceId, String languageId);
	boolean stopTranscription(String jobId);
	void listTranscriptionJobs();

}
