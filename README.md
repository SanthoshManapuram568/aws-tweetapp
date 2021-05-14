# aws-tweetapp
Making the spring-boot-tweet-app an aws console aplication

# Softwares required
AWS SAM<br/>
AWS CLI

# How to run
Open AWS Console and search IAM -> Users -> Security Credentials -> Create Keys -> Download the file<br/>
Open cmd and type -> aws configure <br/>
Enter the credentials that are in the dowloaded file<br/>
Open the project location till the samconfig.toml file is seen<br/>
<h6>Ensure that maven has been installed and JAVA home is set by JDK </h6><br/>
open cmd -> sam build<br/>
Enter the API name : any_name (as user wanted)<br/>
once build is succeed -> type sam deploy -guide<br/>
Enter your option as Y when ever asked <br/>
leave any file as empty and press enter (Optional) / If you have configured then you can enter your file name (Optional)<br/>
Once uploaded successfully -> open aws console -> search API and click on the api name<br/>
click on the methods you want to execute like "Post Tweet","register user".......... -> click test and enter the data as Json<br/>
Once the response is ok , then you can search for DynamoDB -> tables -> tableName -> Items to see your data entered.

# ThankYou 

