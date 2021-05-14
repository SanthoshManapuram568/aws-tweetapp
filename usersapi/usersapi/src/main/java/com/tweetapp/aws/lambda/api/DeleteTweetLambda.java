package com.tweetapp.aws.lambda.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeleteTweetLambda {
	
	public APIGatewayProxyResponseEvent deleteTweet(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String username = request.getPathParameters().get("username");
		String tweetID = request.getHeaders().get("tweetId");
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
		Table table = dynamoDB.getTable(System.getenv("TWEETS_TABLE"));
		 DeleteItemSpec deleteItemSpec = new DeleteItemSpec() 
		            .withPrimaryKey("tweetId", tweetID); 
		 DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
		String outputJson = objectMapper.writeValueAsString(true);
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
	}
	
}
