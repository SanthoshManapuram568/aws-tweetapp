package com.tweetapp.aws.lambda.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.Comment;
import com.tweetapp.aws.lambda.dto.Tweet;
import com.tweetapp.aws.lambda.dto.TweetModel;
import com.tweetapp.aws.lambda.dto.TweetResponse;

public class ReadUserTweetsLambda {
	
	public APIGatewayProxyResponseEvent readUserTweets(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String username = request.getPathParameters().get("username");
		String loggedInUser = request.getHeaders().get("loggedInUser");
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
		
		ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("TWEETS_TABLE")));
		//List<Tweet> tweets = new ArrayList<>();
      // try{
		List<Tweet> tweets = scanResult.getItems().stream().map(item->
			
			 new Tweet(item.get("tweetId").getS(), item.get("username").getS(), item.get("tweetText").getS(),
					item.get("firstName").getS(),item.get("lastName").getS(), item.get("tweetDate").getS(),
					item.get("likes").getSS(),item.get("comments").getSS())
			).collect(Collectors.toList());
		List<TweetResponse> allTweets = new ArrayList<>();
		tweets.forEach(tweet->{
			if(tweet.username.toString().equalsIgnoreCase(username.toString())) {
			List<Comment> comments = new ArrayList<>();
			Integer likesCount = 0;
			Boolean likeStatus = false;
			if(tweet.likes==null) {
				likesCount = 0;
				likeStatus = false;
			}else {
				likesCount = tweet.likes.size();
				likeStatus = tweet.likes.contains(loggedInUser);
			}
			Integer commentsCount = tweet.comments!=null?tweet.comments.size():0;
			if(commentsCount!=0) {
			for(String com : tweet.comments) {
				String[] arrOfStr = com.toString().split("$", 2);
				comments.add(new Comment(arrOfStr[0], arrOfStr[1]));
			}
			}
			allTweets.add(new TweetResponse(tweet.tweetId, tweet.username, tweet.tweetText, tweet.firstName, tweet.lastName, tweet.tweetDate,
					likesCount, commentsCount, likeStatus, comments));
			//allTweets.add(new TweetModel(tweet.tweetId, tweet.username, tweet.tweetText, tweet.firstName, tweet.lastName, tweet.tweetDate, tweet.likes, comments));
			}
		});
		
		String outputJson = objectMapper.writeValueAsString(allTweets);
		
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
       /* }
        catch (Exception e) {
			// TODO: handle exception
        	String outputJson = objectMapper.writeValueAsString(new ArrayList<>());
			return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
        }*/
	}
	
	
}
