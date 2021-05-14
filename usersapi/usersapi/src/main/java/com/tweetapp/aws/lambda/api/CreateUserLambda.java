package com.tweetapp.aws.lambda.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.UserModel;

public class CreateUserLambda {
	
	public APIGatewayProxyResponseEvent createUser(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		UserModel user = objectMapper.readValue(request.getBody(), UserModel.class);
		
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
		Table table = dynamoDB.getTable(System.getenv("USERS_TABLE"));
		Item item = new Item().withPrimaryKey("email",user.email).withString("firstName", user.firstName)
				.withString("lastName", user.lastName).withString("username", user.username)
				.withString("password", user.password).withString("contactNum", user.contactNum);
		table.putItem(item);
		String outputJson = objectMapper.writeValueAsString(request.getBody());
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
	}

}
