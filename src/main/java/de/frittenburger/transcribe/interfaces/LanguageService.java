package de.frittenburger.transcribe.interfaces;

import java.util.List;

import de.frittenburger.transcribe.model.Language;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;

public interface LanguageService {

	List<Language>  getLanguages();

	LanguageCode getLanguageCode(String id);

}
