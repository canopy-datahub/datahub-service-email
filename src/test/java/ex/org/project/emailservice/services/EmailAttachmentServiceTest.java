package ex.org.project.emailservice.services;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;

@ExtendWith(MockitoExtension.class)
class EmailAttachmentServiceTest {

    @Mock
    private S3Client s3Client;
    @InjectMocks
    private EmailAttachmentService attachmentService;


}