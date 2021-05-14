package com.tweetapp.aws.lambda.api;

import java.util.ArrayList;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.Comment;
import com.tweetapp.aws.lambda.dto.Tweet;

public class CreateTweetLambda {
	
	public APIGatewayProxyResponseEvent createTweet(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		Tweet tweet = objectMapper.readValue(request.getBody(), Tweet.class);
		String username = request.getPathParameters().get("username");
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
		Table table = dynamoDB.getTable(System.getenv("TWEETS_TABLE"));
		String tweetId = UUID.randomUUID().toString();
		Item item = new Item().withPrimaryKey("tweetId",tweetId).withString("firstName", tweet.firstName)
				.withString("lastName", tweet.lastName).withString("username", username)
				.withString("tweetText", tweet.tweetText).withString("tweetDate", tweet.tweetDate)
				.withList("likes",new ArrayList<String>()).withList("comments", new ArrayList<String>());
		PutItemOutcome saved = table.putItem(item);
		
		
		//String outputJson = objectMapper.writeValueAsString(saved.getItem());
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Created");
	}
	
	
}
