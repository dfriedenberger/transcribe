package de.frittenburger.transcribe.model;

public class InputDevice {

	private String name;
	private String id;

	public InputDevice(String id,String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public String getId() {
		return id;
	}

}
