package com.tweetapp.aws.lambda.dto;

public class AuthenticationRequest {
	
	public String username;
	public String password;
	public AuthenticationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public AuthenticationRequest() {
		super();
	}
	
	
}
