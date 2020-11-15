package de.frittenburger.transcribe.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Line.Info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.frittenburger.transcribe.interfaces.AudioSystemService;
import de.frittenburger.transcribe.model.InputDevice;

@Service("AudioSystemService")
@Scope("singleton")
public class AudioSystemServiceImpl implements AudioSystemService {

	private static final Logger logger = LogManager.getLogger(TranscribeServiceImpl.class);

	@Override
	public List<InputDevice> getInputDevices() {
		
		List<InputDevice> devices = new ArrayList<>();
		for(Mixer.Info mixerInfo : AudioSystem.getMixerInfo())
		{
			logger.info("MixerInfo: "+mixerInfo.getName());

			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			for(Info targetLine : mixer.getTargetLineInfo())
			{
				logger.info(targetLine.toString());
				try {
					Line line = mixer.getLine(targetLine);
					if(line instanceof TargetDataLine)
					{
						devices.add(new InputDevice(mixerInfo.getName(),mixerInfo.getName()));
					}

				} catch (LineUnavailableException e) {
					logger.error(e);
				}
			}
		}
		
		return devices;
	}

	@Override
	public TargetDataLine getTargetDataLine(String id) {
		for(Mixer.Info mixerInfo : AudioSystem.getMixerInfo())
		{
			logger.info("MixerInfo: "+mixerInfo.getName());

			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			for(Info targetLine : mixer.getTargetLineInfo())
			{
				logger.info(targetLine.toString());
				try {
					Line line = mixer.getLine(targetLine);
					if(line instanceof TargetDataLine)
					{
						if(mixerInfo.getName().equals(id))
							return (TargetDataLine)line;
					}

				} catch (LineUnavailableException e) {
					logger.error(e);
				}
			}
		}
		throw new RuntimeException("Element not found "+id);
	}
}
