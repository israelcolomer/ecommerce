## Ecommerce Application

Application Assignment

Web service
Create a tiny REST / JSON web service in Java using Spring Boot (RestController) with an API that
supports basic products CRUD:
● Create a new product
● Get a list of all products
● Update a product

The API should also support:
● Placing an order
● Retrieving all orders within a given time period

A product should have a name and some representation of its price.

Each order should be recorded and have a list of products. It should also have the buyer’s e-mail, and the
time the order was placed. The total value of the order should always be calculated, based on the prices
of the products in it.

It should be possible to change the product’s price, but this shouldn’t affect the total value of orders which
have already been placed.

Requirements
● Implement your solution according to the above specification.
● Provide unit tests.
● Document your REST-API.
● Provide a storage solution for persisting the web service’s state.
● Have a way to run the service with its dependencies (database etc) locally. You can use either a
simple script or docker or something else. It’s up to you.





Please run the following script if you want to start the service:
`cd scripts && bash run.sh`

Authentication:

Would definitely go with a stateless solution like JWT. There are plenty of docs online on the topic, but I would highlight:
 - No need to keep state on server reduces memory footprint and garbage collection needs
 - No need for infra solutions like sticky sessions or session sharing solutions to ensure a given client keeps it's 
 session usable no matter where the load balancer would have sent their request
 - Easy to force all client's "session" expiration, i.e. changing the server's secret
 - Pure http solution, based on response codes
 - Easily implemented in java/spring-security, just adding a filter to the chain
 

Service redundancy:

The exact solution will probably vary on the deployment preference, in-house vs cloud. Will focus on cloud, AWS in this case
(although the same can be achieved with different providers)

This would largely depend on which markets we want to target. Assuming my clients are mainly from US and Europe, I would
 have an ECS/EKS cluster running in at least a region within those continents (maybe 2 in the case of US). Within those
regions, I would ensure redundancy by deploying the cluster to at least 2 availability zones.
 
In terms of database redundancy, I would rely on a managed service like RDS, which takes care of region/AZ redundancy and
allows for easy horizontal scalability among other features.


TODOs (non-extensive list):
 - Add JWT based authentication
 - Use authenticated user to restrict product/order crud operations: products only created/updated by admins, 
 only order owner able to update/delete his own orders, etc
 - Add UTs to test http responses from controller layer by mocking service layer and throwing different exceptions
 - Add jib plugin to reduce the image size of each artifact being pushed to docker registry
 
