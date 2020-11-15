package de.frittenburger.transcribe.impl;

import software.amazon.awssdk.services.transcribestreaming.model.TranscriptEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import de.frittenburger.transcribe.model.TranscriptMessage;
import software.amazon.awssdk.services.transcribestreaming.model.Alternative;
import software.amazon.awssdk.services.transcribestreaming.model.Result;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponseHandler.Visitor;

public class TranscripterVisitor implements Visitor {

	
	private SimpMessagingTemplate simpMessagingTemplate;

	public TranscripterVisitor(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@Override
	public void visit(TranscriptEvent event) {
		// Visitor.super.visit(event);

		for (Result result : event.transcript().results()) {
			System.out.println(result.resultId());

			for (Alternative a : result.alternatives()) {
				System.out.println(a.transcript());

				TranscriptMessage message = new TranscriptMessage(result.resultId(), a.transcript());

				simpMessagingTemplate.convertAndSend("/topic/transcript", message);

			}
		}

	}
}
