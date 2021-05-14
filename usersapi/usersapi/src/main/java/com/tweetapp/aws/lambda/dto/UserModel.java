package com.tweetapp.aws.lambda.dto;

public class UserModel {
	
	
	public String username;
	public String firstName;
	public String lastName;
	public String email;
	public String contactNum;
	public String password;
	
	public UserModel() {
		super();
	}
	public UserModel(String username, String firstName, String lastName, String email, String contactNum,
			String password) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.contactNum = contactNum;
		this.password = password;
	}
	
}
