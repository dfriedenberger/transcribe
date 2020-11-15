package de.frittenburger.transcribe.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.frittenburger.transcribe.interfaces.LanguageService;
import de.frittenburger.transcribe.model.Language;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;

public class LanguageServiceImplTest {

	@Test
	public void testGetLanguages() {
		
		LanguageService service = new LanguageServiceImpl();
		
		List<Language> languages = service.getLanguages();
		
		assertEquals(3, languages.size());
		
		Language de = languages.get(2);
		assertEquals("de-DE", de.getId());

	}
	
	
	@Test
	public void testgetLanguageCode() {
		
		LanguageService service = new LanguageServiceImpl();
		
		assertEquals(service.getLanguageCode("de-DE"), LanguageCode.DE_DE);
	}

}
