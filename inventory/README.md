# Product API in Java and Spring Boot

This API queries a DB for products based on Name, Brand, Price and Quantity

Two users are supported - an Admin with full priviledges, and a read-only User. Ideally the credentials for these users 
would be stored in AWS Secrets Manager or similar, for security.

Config files present and further configs for dev/stage/prod environments could be stored in AWS AppConfig.

Included in this repo is the Postman collection for the API calls and a mockup of potential AWS infrastructure.

To make the app easier to work with, I would add unit tests for the endpoints, to make sure changes to Products and
future features don't cause bugs. I would also add tests to make sure that users have the proper privileges, if they are 
expanded.