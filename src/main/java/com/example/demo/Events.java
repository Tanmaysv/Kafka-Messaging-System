package com.example.demo;

public class Events {
	private String name;
    private String source;
    private String publishingTime;
    private String description;

    public Events() {

    }

    public Events(String name, String source, String publishingTime, String description) {
        this.name = name;
        this.source = source;
        this.publishingTime = publishingTime;
        this.description = description;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPublishingTime() {
		return publishingTime;
	}

	public void setPublishingTime(String publishingTime) {
		this.publishingTime = publishingTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
    public String toString() {
        return name + ", " + source + ", " + publishingTime + ", " + description;
	}
    
 }
