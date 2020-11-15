package de.frittenburger.transcribe.model;

public class TranscriptMessage extends Message {

	private String resultId;
	private String transcript;

	public TranscriptMessage(String resultId, String transcript) {

		super("transcript");
		this.resultId = resultId;
		this.transcript = transcript;
	}

	public String getResultId() {
		return resultId;
	}

	public String getTranscript() {
		return transcript;
	}

	
}
