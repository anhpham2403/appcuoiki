package com.anh.currency.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Feed {

	private double description;
	private long pubDate;
	private String title;

	public Feed() {
	}

	public Feed(FeedWithPriority priority) {
		description = priority.getDescription();
		pubDate = priority.getPubDate();
		title = priority.getTitle();
	}

	@Override
	public String toString() {
		return "Feed [description=" + description + ", pubDate=" + pubDate + ", title=" + title + "]";
	}

	public double getDescription() {
		return description;
	}

	public void setDescription(double description) {
		this.description = description;
	}

	public long getPubDate() {
		return pubDate;
	}

	public void setPubDate(long pubDate) {
		this.pubDate = pubDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
