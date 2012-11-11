package com.draft_law_assessor.service;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DraftList {

	@SerializedName("object")
	private List<DraftModel> draftList;

	public void setDraftList(List<DraftModel> draftList) {
		this.draftList = draftList;
	}

	public List<DraftModel> getDraftList() {
		return draftList;
	}
	
}
