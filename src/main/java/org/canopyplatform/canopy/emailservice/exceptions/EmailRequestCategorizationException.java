package org.canopyplatform.canopy.emailservice.exceptions;

import org.canopyplatform.canopy.emailservice.models.EmailRequest;
import lombok.Getter;

@Getter
public class EmailRequestCategorizationException extends RuntimeException{

    private EmailRequest request;

    public EmailRequestCategorizationException(String message, EmailRequest request){
        super(message);
        this.request = request;
    }

}
