package com.tweetapp.aws.lambda.dto;

import java.util.ArrayList;
import java.util.List;


public class Tweet {
	
	
	public String tweetId;
	public String username;
	public String tweetText;
	public String firstName;
	public String lastName;
	public String tweetDate;
	public List<String> likes = new ArrayList<>();
	public List<String> comments = new ArrayList<>();
	public Tweet(String tweetId, String username, String tweetText, String firstName, String lastName, String tweetDate,
			List<String> likes, List<String> comments) {
		super();
		this.tweetId = tweetId;
		this.username = username;
		this.tweetText = tweetText;
		this.firstName = firstName;
		this.lastName = lastName;
		this.tweetDate = tweetDate;
		this.likes = likes;
		this.comments = comments;
	}
	public Tweet() {
		super();
	}
	
	
}
