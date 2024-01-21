# Microservice configuration
Microservices hold in their .yml/.properties (in-file) a version. 
- The version is used by other micro-services for version sensitive operation (for instance, if the resources service version was updated, another service will sync)
- Every microservice stores the version in a database entity
  - The entity in which the version is stored is the MicroserviceConfigurationEntity 
  - It is designed to hold for a key name, a certain value
  - This means it can hold the version, and any other configuration developers see fit. 

When a microservice launches, it checks the database version against the .yml/.properties version.
- If it's greater than, it updates it  and a broadcast should be made through rabbitmq for all subscribers
- If it's less than, it shuts down if the update is required  
- When other instances of that microservice detect that some microservice changed the version (mismatch between the version in their properties file and that in the database)
    - They shut down if the update is required
      -- Confused? Don't forget that there can be many instances of the same microservice (sharing the same database ofcourse) and an instance with an old or new .yml/.properties can be running
    
# Microservice security
## General
Galsie's microservices may receive http requests that require the user to have been authenticated.
- Microservice-common holds a security requests filter which requires the headers of certain uris to hold a user access token for authentication
- The uris which require authentication are stored in each services config file.

## Implementation
users-service and homes-service both use micro-service-common (this common library) for common functionalities.
- The users-service holds the service to handle authentication of the access token given in the request header
- Other services would need to remotely request authentication for the access token from the users-service

To keep a common implementation of the security requests filter (RequiresUserAuthRequestsFilter), the interface UserAuthenticatorService
was used with an authentication method. 
### IMPORTANT
Every service is expected to define a bean in config that returns a UserAuthenticatorService, but constructs either a RemoteUserAuthenticatorService (declared here), or  LocalUserAuthenticatorService (abstract here, must be declared in the service)
- users-service is expected to use its implementation LocalUserAuthenticatorService
- other services are expected to use the RemoteUserAuthenticatorService
 
