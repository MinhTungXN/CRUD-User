package com.vu.form;

public class SearchForm {

	private String keyword;

	public SearchForm() {
		super();
	}

	public SearchForm(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}