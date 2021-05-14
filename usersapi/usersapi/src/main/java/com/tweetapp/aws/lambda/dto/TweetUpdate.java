package com.tweetapp.aws.lambda.dto;

public class TweetUpdate {
	public String tweetId;
	public String tweetText;
	public TweetUpdate(String tweetId, String tweetText) {
		super();
		this.tweetId = tweetId;
		this.tweetText = tweetText;
	}
	public TweetUpdate() {
		super();
	}
	
}
