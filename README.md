# Email Service

Spring Boot 3 Lambda Function microservice for Canopy. It is running on Java 17 and Spring Cloud 3.

Processes email requests from an SQS queue and sends email notifications via AWS SES.

# Install and Run

## Maven

### Local

This is a Lambda function — either test in the cloud on AWS, set up LocalStack, or containerize it.

### AWS

This service relies on multiple AWS services that need to be configured. These include:
* AWS CLI
  * A profile needs to be configured for running locally
* SES
  * This includes adding any emails you will be sending from, as well as explicitly setting recipients for non-prod instances
  * Production access can be requested and will disable the need for mail address verification
* SQS
  * The queue from which EmailRequest events are retrieved
* S3
  * This is where files for email attachments are to be stored
* Any IAM permissions and profiles for these services

In a specific instance, the only environment variable that needs to be set is:
* spring_profiles_active
    * This should be set to '{environment}'
        * The current environments are dev, test, prod

Once these have been configured:
```
mvn clean package shade:shade
```

### Endpoint

This service is designed to be triggered based on SQS messages in AWS.
