package com.tweetapp.aws.lambda.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.NewPassword;

public class ForgotPasswordLambda {
	
	public APIGatewayProxyResponseEvent forgotPassword(APIGatewayProxyRequestEvent requestGot) 
			throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		NewPassword Password = objectMapper.readValue(requestGot.getBody(), NewPassword.class);
		String username = requestGot.getPathParameters().get("username");
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
		Table table = dynamoDB.getTable(System.getenv("USERS_TABLE"));
		try {

			UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("email", username)
	                .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set password = :val1")
	                .withValueMap(new ValueMap().withString(":val1", Password.newPassword));

	            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

        }
        catch (Exception e) {
            System.err.println("Failed to add new attribute ");
            System.err.println(e.getMessage());
        }
		return new APIGatewayProxyResponseEvent().withStatusCode(200);
	}

}
