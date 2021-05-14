package com.tweetapp.aws.lambda.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TweetResponse implements Serializable{
	
	
	public String tweetId;
	public String username;
	public String tweetText;
	public String firstName;
	public String lastName;
	public String tweetDate;
	public Integer likesCount;
	public Integer commentsCount;
	public Boolean likeStatus;
	public List<Comment> comments = new ArrayList<>();
	public TweetResponse(String tweetId, String username, String tweetText, String firstName, String lastName,
			String tweetDate, Integer likesCount, Integer commentsCount, Boolean likeStatus, List<Comment> comments) {
		super();
		this.tweetId = tweetId;
		this.username = username;
		this.tweetText = tweetText;
		this.firstName = firstName;
		this.lastName = lastName;
		this.tweetDate = tweetDate;
		this.likesCount = likesCount;
		this.commentsCount = commentsCount;
		this.likeStatus = likeStatus;
		this.comments = comments;
	}
	public TweetResponse() {
		super();
	}
	
	
}
