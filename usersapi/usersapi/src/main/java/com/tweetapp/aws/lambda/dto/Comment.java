package com.tweetapp.aws.lambda.dto;

public class Comment {
	
	public String username;
	public String comment;
	public Comment(String username, String comment) {
		super();
		this.username = username;
		this.comment = comment;
	}
	public Comment() {
		super();
	}
	
	
}
