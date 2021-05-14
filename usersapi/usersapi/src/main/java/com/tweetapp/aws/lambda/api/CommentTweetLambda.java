package com.tweetapp.aws.lambda.api;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.Tweet;
import com.tweetapp.aws.lambda.dto.TweetReply;

public class CommentTweetLambda {
	
	public APIGatewayProxyResponseEvent commentTweet(APIGatewayProxyRequestEvent requestGot) 
			throws JsonMappingException, JsonProcessingException {
	
	ObjectMapper objectMapper = new ObjectMapper();
	TweetReply update = objectMapper.readValue(requestGot.getBody(), TweetReply.class);
	String username = requestGot.getPathParameters().get("username");
	String tweetID = requestGot.getPathParameters().get("tweetId");
	DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
	Table table = dynamoDB.getTable(System.getenv("TWEETS_TABLE"));
	try {
		
		GetItemSpec req = new GetItemSpec() 
				   .withPrimaryKey("tweetId",tweetID);
		
		Item item = table.getItem(req);
		Tweet tweet =objectMapper.readValue(item.toJSON().toString(), Tweet.class) ;
		List<String> updatedComments = new ArrayList<>();
		updatedComments.addAll(tweet.comments);
		updatedComments.add(username+"$"+update.comment);
		//tweet.comments.add(username+"$"+update.comment);
		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("tweetId", tweetID)
                .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set comments = :val1")
                .withValueMap(new ValueMap().withList(":val1", updatedComments));

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

    }
    catch (Exception e) {
        System.err.println("Failed to add new attribute ");
        System.err.println(e.getMessage());
    }
	return new APIGatewayProxyResponseEvent().withStatusCode(200);
}
	
}
