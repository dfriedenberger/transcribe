package de.frittenburger.transcribe.controller;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.frittenburger.transcribe.interfaces.AudioSystemService;
import de.frittenburger.transcribe.interfaces.LanguageService;
import de.frittenburger.transcribe.interfaces.TranscribeService;







@Controller
public class PageController {

 
	private static final Logger logger = LogManager.getLogger(PageController.class);

	@Autowired
    private TranscribeService transcribeService;

	@Autowired
    private AudioSystemService audioSystemService;
	
	@Autowired
    private LanguageService languageService;
	
	@RequestMapping("/")
	public String welcome(@RequestHeader Map<String, String> headers, Map<String, Object> model, HttpServletRequest request) {
		
	   
	    model.put("header", "Transcribe Application Interface");
	    model.put("languages", languageService.getLanguages());
	    model.put("devices", audioSystemService.getInputDevices());
	    
		return "welcome";
	}
	
	
	
	@RequestMapping(value = "/list-jobs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> listJobs(@RequestHeader Map<String, String> headers,HttpServletRequest request) throws IOException {
		
		 transcribeService.listTranscriptionJobs();

		 Map<String,Object> info = new HashMap<>();
		 info.put("state", "OK" );
		 return info;
		
	}
	
	@RequestMapping(value = "/start-transcribe", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> startTranscribe(@RequestHeader Map<String, String> headers,HttpServletRequest request) throws IOException {
		
				 
		 String device = request.getParameter("device");
		 String language = request.getParameter("language");

		 logger.info("Start Transcription for "+device+" with "+language);
		 String jobId = transcribeService.startTranscription(device,language);
	    

		 Map<String,Object> info = new HashMap<>();
		 info.put("state", (jobId != null) ? "OK" : "Failed");
		 info.put("jobId", jobId);

		 return info;
		
	}
	
	@RequestMapping(value = "/stop-transcribe/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> stopTranscribe(@PathVariable(value="id") String jobId,@RequestHeader Map<String, String> headers,HttpServletRequest request) throws IOException {
		
				 
		
		 logger.info("Stop Transcription for "+jobId);
		 boolean stopped = transcribeService.stopTranscription(jobId);

		 Map<String,Object> info = new HashMap<>();
		 info.put("state", stopped ? "OK" : "Failed");
		 return info;
		
	}

	
	
}