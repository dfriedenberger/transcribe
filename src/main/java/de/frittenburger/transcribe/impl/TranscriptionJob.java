package de.frittenburger.transcribe.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;
import software.amazon.awssdk.services.transcribestreaming.model.MediaEncoding;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionRequest;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponseHandler;

public class TranscriptionJob {

	private static Logger logger = LogManager.getLogger(TranscriptionJob.class);

	private final TargetDataLine targetDataLine;
	private final LanguageCode languageCode;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final String jobId;

    private CompletableFuture<Void> completableFuture = null;


	public TranscriptionJob(TargetDataLine targetDataLine, LanguageCode languageCode, SimpMessagingTemplate simpMessagingTemplate) {
		this.targetDataLine = targetDataLine;
		this.languageCode = languageCode;
		this.simpMessagingTemplate = simpMessagingTemplate;
		jobId = UUID.randomUUID().toString();
	}

	public void start() {
		
			logger.info("Starting transcription job");

			try
			{
				AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
	
				TranscribeStreamingAsyncClient client = TranscribeStreamingAsyncClient.builder()
						.credentialsProvider(ProfileCredentialsProvider.create()).build();
	
				StartStreamTranscriptionRequest request = StartStreamTranscriptionRequest.builder()
						.mediaEncoding(MediaEncoding.PCM).languageCode(languageCode).mediaSampleRateHertz(16_000).build();
	
				targetDataLine.open(format);
				targetDataLine.start();
	
				AudioStreamPublisher publisher = new AudioStreamPublisher(new AudioInputStream(targetDataLine));
	
				StartStreamTranscriptionResponseHandler response = StartStreamTranscriptionResponseHandler.builder()
						.subscriber(new TranscripterVisitor(simpMessagingTemplate)).build();
	
				completableFuture = client.startStreamTranscription(request, publisher, response);
				
			
				logger.info("Transcription job started");
				
			} catch(Exception e)
			{
				logger.error(e);
			}

	}

	
	public boolean isRunning() {
		
		if(completableFuture != null)
		{
			if(!completableFuture.isDone())
				return true;
		}
			
		
		return false;
	}
	public void stop() {
		
		logger.info("Stopping transcription job");
		
		if(completableFuture != null)
		{
			completableFuture.complete(null);
			completableFuture.join();
		}
		
		targetDataLine.stop();
		
		logger.info("Transcription job stopped");


	}

	public String getId() {
		return jobId;
	}

	@Override
	public String toString() {
		return "TranscriptionJob [jobId=" + jobId + " running"+isRunning()+"]";
	}

	

	

}
