package ex.org.project.emailservice.services;

import ex.org.project.emailservice.models.S3Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Slf4j
@Service
public class EmailAttachmentService {

    private final S3Client s3Client;

    EmailAttachmentService(S3Client client){
        this.s3Client = client;
    }

    /**
     * Retrieves a file from s3 based on the object's path
     * @param path relative path to s3 object to download e.g. bucket/path/to/file
     * @return ResponseInputStream<GetObjectResponse> containing the object at the provided path
     */
    public ResponseInputStream<GetObjectResponse> getFile(String path) {
        S3Location location = new S3Location(path);
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(location.getBucket())
                        .key(location.getKey())
                        .build()
        );
    }

}
