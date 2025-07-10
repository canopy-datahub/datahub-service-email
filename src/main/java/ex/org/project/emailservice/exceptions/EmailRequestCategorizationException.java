package ex.org.project.emailservice.exceptions;

import ex.org.project.emailservice.models.EmailRequest;
import lombok.Getter;

@Getter
public class EmailRequestCategorizationException extends RuntimeException{

    private EmailRequest request;

    public EmailRequestCategorizationException(String message, EmailRequest request){
        super(message);
        this.request = request;
    }

}
