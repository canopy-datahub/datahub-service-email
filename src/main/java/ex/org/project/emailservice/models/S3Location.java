package ex.org.project.emailservice.models;

import ex.org.project.emailservice.exceptions.S3AttachmentException;
import lombok.Getter;

@Getter
public class S3Location {

    private String bucket;
    private String key;

    public S3Location(String path){
        if(path == null){
            throw new S3AttachmentException("Cannot parse null path");
        }
        String[] parts = path.split("/", 2);
        if(parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()){
            throw new S3AttachmentException("Cannot parse path: " + path);
        }
        this.bucket = parts[0];
        this.key = parts[1];
    }
}
