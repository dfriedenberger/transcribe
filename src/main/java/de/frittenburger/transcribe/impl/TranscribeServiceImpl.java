package de.frittenburger.transcribe.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sound.sampled.TargetDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;
import de.frittenburger.transcribe.interfaces.AudioSystemService;
import de.frittenburger.transcribe.interfaces.LanguageService;
import de.frittenburger.transcribe.interfaces.TranscribeService;
import de.frittenburger.transcribe.model.StatusMessage;

@Service("TranscribeService")
@Scope("singleton")
public class TranscribeServiceImpl implements TranscribeService {
	
	private static final Logger logger = LogManager.getLogger(TranscribeServiceImpl.class);
	
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
    private AudioSystemService audioSystemService;
	
	@Autowired
    private LanguageService languageService;


	private final List<TranscriptionJob> jobs = new ArrayList<>();
	
    
	@Override
	public String startTranscription(String deviceId, String languageId) {

		LanguageCode languageCode = languageService.getLanguageCode(languageId);
		TargetDataLine targetDataLine = audioSystemService.getTargetDataLine(deviceId);
		TranscriptionJob job = new TranscriptionJob(targetDataLine, languageCode, simpMessagingTemplate);

		job.start();
		jobs.add(job);
		
		StatusMessage message = new StatusMessage(job.getId(),job.isRunning()?"startet":"start failed");
		
		simpMessagingTemplate.convertAndSend("/topic/status", message);

		return job.getId();
	}


	@Override
	public boolean stopTranscription(String jobId) {
		
		List<TranscriptionJob> selected = jobs.stream().filter(job -> job.getId().equals(jobId)).collect(Collectors.toList());

		switch(selected.size())
		{
		case 0:
			logger.info("No job found");
			return false;
		case 1:
			break;
		default:
			logger.info("many jobs found");
			return false;
		}
		
		TranscriptionJob job = selected.get(0);
		
		job.stop();
		
		StatusMessage message = new StatusMessage(job.getId(),job.isRunning()?"stop failed":"stopped");
		
		simpMessagingTemplate.convertAndSend("/topic/status", message);

		jobs.remove(job);
		
		return true;
		
	}


	@Override
	public void listTranscriptionJobs() {
		
		for(TranscriptionJob job : jobs)
		{
			logger.info(job);
			StatusMessage message = new StatusMessage(job.getId(),job.isRunning()?"running":"done");
			simpMessagingTemplate.convertAndSend("/topic/status", message);
		}
		
	}
    
    
	






	

}