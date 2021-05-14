package com.tweetapp.aws.lambda.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
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
import com.tweetapp.aws.lambda.dto.UserModel;

public class ReadTweetLambda {
	
	public APIGatewayProxyResponseEvent readTweetById(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		//AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
		String tweetID = request.getPathParameters().get("tweetId");
		String username = request.getPathParameters().get("username");
		
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
		Table table = dynamoDB.getTable(System.getenv("TWEETS_TABLE"));
		
		//try {
			
			GetItemSpec req = new GetItemSpec() 
					   .withPrimaryKey("tweetId", tweetID);
			
			Item item = table.getItem(req);
			Tweet tweet =objectMapper.readValue(item.toJSON().toString(), Tweet.class) ;
		
				List<Comment> comments = new ArrayList<>();
				Integer likesCount = tweet.likes.size();
				Boolean likeStatus = tweet.likes.contains(username);
				Integer commentsCount = tweet.comments.size();
				for(String com : tweet.comments) {
					String[] arrOfStr = com.split("$",5);
					comments.add(new Comment(arrOfStr[0], arrOfStr[1]));
				}
				TweetResponse resp = new TweetResponse(tweet.tweetId, tweet.username, tweet.tweetText, tweet.firstName,
						tweet.lastName, tweet.tweetDate,
						likesCount, commentsCount, likeStatus, comments);
				String outputJson = objectMapper.writeValueAsString(resp);
				return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
			
		/*	
		}catch (Exception e) {
			// TODO: handle exception
			String outputJson = objectMapper.writeValueAsString(new ArrayList<>());
			return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson);
		}
		*/
	}
	
}
