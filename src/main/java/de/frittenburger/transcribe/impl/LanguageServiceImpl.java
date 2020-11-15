package de.frittenburger.transcribe.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.frittenburger.transcribe.interfaces.LanguageService;
import de.frittenburger.transcribe.model.Language;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;

@Service("LanguageServiceImpl")
@Scope("singleton")
public class LanguageServiceImpl implements LanguageService {

	@Override
	public List<Language> getLanguages() {
		
		List<Language> languages = new ArrayList<>();
		languages.add(new Language(LanguageCode.EN_US.toString(),"Englisch (US)"));
		languages.add(new Language(LanguageCode.EN_GB.toString(),"Englisch (GB)"));
		languages.add(new Language(LanguageCode.ES_US.toString(),"Spanisch"));
		languages.add(new Language(LanguageCode.DE_DE.toString(),"Deutsch"));
		return languages;
		
	}
	
	@Override
	public LanguageCode getLanguageCode(String id) {
		return LanguageCode.fromValue(id);
	}

}
