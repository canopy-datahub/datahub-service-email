package ex.org.project.emailservice.controllers;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ex.org.project.emailservice.exceptions.EmailRequestCategorizationException;
import ex.org.project.emailservice.models.EmailRequest;
import ex.org.project.emailservice.services.EmailSendingService;
import ex.org.project.emailservice.services.EmailTemplateService;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class LambdaHandler implements Function<SQSEvent, String> {

    private EmailTemplateService templateService;
    private EmailSendingService sendingService;
    private final ObjectMapper mapper;

    public LambdaHandler(EmailTemplateService templateService, EmailSendingService sendingService){
        this.templateService = templateService;
        this.sendingService = sendingService;
        this.mapper = new ObjectMapper();
    }

    /**
     * Processes an SQSEvent to send an email
     * @param event SQSEvent from the Function endpoint
     * @return placeholder success String
     */
    @Override
    public String apply(SQSEvent event) {
        for (SQSEvent.SQSMessage message : event.getRecords()){
            EmailRequest request = messageToEmailRequest(message);
            String template = templateService.parseRequest(request);
            sendingService.sendEmail(request, template);
        }
        return "email sent";
    }

    /**
     * Converts an SQSMessage to an EmailRequest object
     * @param message SQSMessage contained in an SQSEvent
     * @return EmailRequest object that was created from an SQSMessage
     * @throws EmailRequestCategorizationException if the SQSMessage cannot be converted to an EmailRequest
     */
    private EmailRequest messageToEmailRequest(SQSEvent.SQSMessage message){
        try {
            return mapper.readValue(message.getBody(), EmailRequest.class);
        }
        catch (JsonProcessingException e){
            log.error(message.toString());
            throw new EmailRequestCategorizationException("Failed to convert SQS Event to EmailRequest", null);
        }
    }
}
