package com.tweetapp.aws.lambda.api;

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
import com.tweetapp.aws.lambda.dto.TweetUpdate;

public class UpdateTweetLambda {
	
	public APIGatewayProxyResponseEvent updateTweet(APIGatewayProxyRequestEvent requestGot) 
			throws JsonMappingException, JsonProcessingException {
	
	ObjectMapper objectMapper = new ObjectMapper();
	TweetUpdate update = objectMapper.readValue(requestGot.getBody(), TweetUpdate.class);
	String username = requestGot.getPathParameters().get("username");
	DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
	Table table = dynamoDB.getTable(System.getenv("TWEETS_TABLE"));
	//try {
		
		GetItemSpec req = new GetItemSpec() 
				   .withPrimaryKey("tweetId", update.tweetId);
		
		Item item = table.getItem(req);
		Tweet tweet =objectMapper.readValue(item.toJSON().toString(), Tweet.class) ;
		tweet.tweetText = update.tweetText;
		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("tweetId", update.tweetId)
                .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set tweetText = :val1")
                .withValueMap(new ValueMap().withList(":val1", update.tweetText.toString()));

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("updated");

    /*}
    catch (Exception e) {
        System.err.println("Failed to add new attribute ");
        System.err.println(e.getMessage());
        return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody("something went wrong");
    }*/
	
}
	
}
