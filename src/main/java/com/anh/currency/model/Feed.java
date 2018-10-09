package com.anh.currency.model;

import java.util.List;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Feed {

	private String description;
	private String pubDate;
	private String title;
	private List<String> priorities;

	public Feed() {
	}

	@Override
	public String toString() {
		return "Feed [description=" + description + ", pubDate=" + pubDate + ", title=" + title + ", priority="
				+ priorities + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getPriorities() {
		return priorities;
	}

	public void setPriorities(List<String> priorities) {
		this.priorities = priorities;
	}

}
