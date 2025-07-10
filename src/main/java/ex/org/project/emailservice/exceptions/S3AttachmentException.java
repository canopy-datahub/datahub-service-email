package ex.org.project.emailservice.exceptions;

public class S3AttachmentException extends RuntimeException{

    public S3AttachmentException(String message){
        super(message);
    }

}
