package ex.org.project.emailservice.services;

import ex.org.project.emailservice.exceptions.EmailSendingException;
import ex.org.project.emailservice.models.EmailRequest;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;

@Slf4j
@Service
public class EmailSendingService {

    private final JavaMailSender mailSender;
    private final EmailAttachmentService emailAttachmentService;
    private final Environment env;

    public EmailSendingService(JavaMailSender mailSender, EmailAttachmentService emailAttachmentService,
                               Environment env){
        this.mailSender = mailSender;
        this.emailAttachmentService = emailAttachmentService;
        this.env = env;
    }

    /**
     * Sends an email based on an EmailRequest and a html template containing the content
     * @param request EmailRequest object containing all data needed for a specific email and template
     * @param template HTML template for an EmailRequest as a String
     * @throws EmailSendingException if there is an error in the sending process
     */
    public void sendEmail(EmailRequest request, String template){
        MimeMessage message = mailSender.createMimeMessage();
        setMimeMessageProperties(request, template, message);
        log.info("Sending email with content: " + request);
        mailSender.send(message);
    }

    /**
     * Builds a MimeMessage email based on the data in an EmailRequest and a template
     * @param request EmailRequest object containing all data needed for a specific email and template
     * @param template HTML template for an EmailRequest as a String
     * @param message MimeMessage that is being set
     * @throws EmailSendingException if there is an error in creating the message to be sent
     */
    private void setMimeMessageProperties(EmailRequest  request, String template, MimeMessage message){
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
            addInlineImages(messageHelper);
            if(containsAttachmentUrl(request)){
                String s3FilePath = request.props().get("s3FilePath");
                ResponseInputStream<GetObjectResponse> file = emailAttachmentService.getFile(s3FilePath);
                messageHelper.addAttachment(
                        FilenameUtils.getName(s3FilePath),
                        new ByteArrayResource(IoUtils.toByteArray(file))
                );
            }
            messageHelper.setFrom(request.from());
            messageHelper.setTo(request.to().toArray(new String[request.to().size()]));
            messageHelper.setBcc(request.cc().toArray(new String[request.cc().size()]));
            messageHelper.setSubject(String.format("%s%s", getProfilePrepend(), request.subject()));
            messageHelper.setText(template, true);
        }
        catch (MailException | MessagingException | IOException e){
            throw new EmailSendingException(e.getMessage(), request);
        }
    }

    /**
     * Checks whether an email requires a file attachment
     * @param request EmailRequest to be parsed for attachment url
     * @return true if there is an attachment url, else false
     */
    private boolean containsAttachmentUrl(EmailRequest request){
        return request.props().containsKey("s3FilePath");
    }

    /**
     * Adds available inline images to a Mime Message Helper
     * @param messageHelper
     * @throws MessagingException
     */
    private void addInlineImages(MimeMessageHelper messageHelper) throws MessagingException{
        messageHelper.addInline("copyright", new FileDataSource("images/copyright.png"));
    }

    private String getProfilePrepend() {
        if(env.getActiveProfiles().length == 0) {
            return "Default: ";
        }
        return switch (env.getActiveProfiles()[0]) {
            case "dev" -> "Dev: ";
            case "test" -> "Test: ";
            case "prod" -> "";
            default -> "Default: ";
        };
    }

}
