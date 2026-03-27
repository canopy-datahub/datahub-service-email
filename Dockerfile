FROM amazoncorretto:17-alpine
EXPOSE 8080

ARG JAR_FILE=target/email-service-0.0.1-SNAPSHOT-aws.jar

#Instruction to copy files from local source to container target
COPY ${JAR_FILE} app.jar

ENTRYPOINT java -jar app.jar

FROM public.ecr.aws/lambda/java:17

# Copy function code and runtime dependencies from Maven layout
COPY target/classes ${LAMBDA_TASK_ROOT}
COPY target/dependency/* ${LAMBDA_TASK_ROOT}/lib/

# Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
CMD [ "com.example.myapp.App::handleRequest" ]