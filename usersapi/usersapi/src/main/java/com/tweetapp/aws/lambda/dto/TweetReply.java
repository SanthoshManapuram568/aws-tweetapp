package com.tweetapp.aws.lambda.dto;

public class TweetReply {
	
	public String comment;

	public TweetReply(String comment) {
		super();
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "TweetReply []";
	}

	public TweetReply() {
		super();
	}
	
}
