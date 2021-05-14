package com.tweetapp.aws.lambda.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.aws.lambda.dto.AuthenticationRequest;
import com.tweetapp.aws.lambda.dto.UserModel;

public class LoginLambda {
	
	public APIGatewayProxyResponseEvent login(APIGatewayProxyRequestEvent request) 
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		//AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
		AuthenticationRequest credentials = objectMapper.readValue(request.getBody(), AuthenticationRequest.class);
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
		Table table = dynamoDB.getTable(System.getenv("USERS_TABLE"));
		
		//try {
			
			GetItemSpec req = new GetItemSpec() 
					   .withPrimaryKey("email", credentials.username);
			
			Item item = table.getItem(req);
			UserModel user =objectMapper.readValue(item.toJSON().toString(), UserModel.class) ;
			if(item.get("password").toString().equalsIgnoreCase(credentials.password.toString())) {
				String output = objectMapper.writeValueAsString(user);
				return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(output);
			}
/*
        }
        catch (Exception e) {
            System.err.println("Failed to add new attribute ");
            return new APIGatewayProxyResponseEvent().withStatusCode(401);
        }
		/*
		List<UserModel> users = scanResult.getItems().stream().map(item->
		new UserModel(item.get("username").getS(), item.get("firstName").getS(), item.get("lastName").getS(), item.get("email").getS(),
				item.get("contactNum").getS(), item.get("password").getS())
				).collect(Collectors.toList());
		
		for(UserModel user : users) {
			if(user.username == credentials.username && user.password == credentials.password) {
				System.out.println("----------------------------------------------------------------------"
						+ "--------------------------------------------------------------------------");
				String outputJson = objectMapper.writeValueAsString(user);
				return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(outputJson); 
			}
		}
	*/
			System.out.println("*****************************************************************************************************");
			return new APIGatewayProxyResponseEvent().withStatusCode(401);
		
		
		
		
	}

}
