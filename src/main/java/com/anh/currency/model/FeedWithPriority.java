package com.anh.currency.model;

public class FeedWithPriority extends Feed {
	private int priority;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public FeedWithPriority() {
		super();
	}	
}
