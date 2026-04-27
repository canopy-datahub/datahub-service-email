package org.canopyplatform.canopy.emailservice.exceptions;

import org.canopyplatform.canopy.emailservice.models.EmailRequest;
import lombok.Getter;

@Getter
public class EmailSendingException extends RuntimeException{

    private final EmailRequest request;

    public EmailSendingException(String message, EmailRequest request){
        super(message);
        this.request = request;
    }

}
