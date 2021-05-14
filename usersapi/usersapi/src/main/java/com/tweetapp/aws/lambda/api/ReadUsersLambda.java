package com.tweetapp.aws.lambda.api;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.UserModel;

public class ReadUsersLambda {
	
	public APIGatewayProxyResponseEvent readUsers(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
		ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("USERS_TABLE")));
		List<UserModel> users = scanResult.getItems().stream().map(item->
			new UserModel(item.get("username").getS(), item.get("firstName").getS(), item.get("lastName").getS(), item.get("email").getS(),
					item.get("contactNum").getS(), item.get("password").getS())).collect(Collectors.toList());
		
		String outputJson = objectMapper.writeValueAsString(users);
		
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
		
	}
	
}
