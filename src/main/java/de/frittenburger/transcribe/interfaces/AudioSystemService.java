package de.frittenburger.transcribe.interfaces;

import java.util.List;

import javax.sound.sampled.TargetDataLine;

import de.frittenburger.transcribe.model.InputDevice;

public interface AudioSystemService {

	List<InputDevice> getInputDevices();

	TargetDataLine getTargetDataLine(String id);
	
}
