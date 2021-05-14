# aws-tweetapp
Making the spring-boot-tweet-app an aws console aplication

#applications required
AWS SAM<br/>
AWS CLI

#How to run
Open AWS Console and search IAM -> Users -> Security Credentials -> Create Keys -> Dowload the file
Open cmd and type -> aws configure 
Enter the credentials that are in the dowloaded file
Open the project location till the samconfig.toml file is seen
<h6>Ensure that maven has been installed and JAVA home is set by JDK </h6>
open cmd -> sam build
Enter the API name : any_name (as user wanted)
once build is succeed -> type sam deploy -guide
Enter your option as Y when ever asked 
leave any file as empty and press enter (Optional) / If you have configured then you can enter your file name (Optional)
Once uploaded successfully -> open aws console -> search API and click on the api name
click on the methods you want to execute like "Post Tweet","register user".......... -> click test and enter the data as Json
Once the response is ok , then you can search for DynamoDB -> tables -> tableName -> Items to see your data entered.

#ThankYou 

