package com.draft_law_assessor.service;

import com.google.gson.annotations.SerializedName;

public class DraftModel {

	@SerializedName("text")
	private String text;
	@SerializedName("sentiment")
	private int sentiment;
	@SerializedName("title")
	private String title;
	
	public void setText(String text) {
		this.text = text;
	}
	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public int getSentiment() {
		return sentiment;
	}
	public String getTitle() {
		return title;
	}
	
}
