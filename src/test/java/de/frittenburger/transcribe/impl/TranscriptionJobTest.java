package de.frittenburger.transcribe.impl;

import static org.junit.Assert.*;

import javax.sound.sampled.TargetDataLine;

import org.junit.Test;

import de.frittenburger.transcribe.interfaces.AudioSystemService;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;

public class TranscriptionJobTest {

	@Test
	public void test() throws InterruptedException {
		
		
		AudioSystemService as = new AudioSystemServiceImpl();
		TargetDataLine dt = as.getTargetDataLine("Microphone Array (Realtek High ");
		
		assertNotNull(dt);
		TranscriptionJob job = new TranscriptionJob(dt, LanguageCode.DE_DE,null);

		job.start();
		
		assertTrue(job.isRunning());
		Thread.sleep(10000);
		job.stop();
		assertFalse(job.isRunning());

	}

}
