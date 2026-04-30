package org.canopyplatform.canopy.emailservice.services;

import org.canopyplatform.canopy.emailservice.exceptions.EmailRequestCategorizationException;
import org.canopyplatform.canopy.emailservice.models.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    /**
     * Categorizes an EmailRequest object by type, then retrieves, populates, and returns the html template
     * @param request EmailRequest object to be categorized
     * @return HTML template corresponding to the type of EmailRequest, as a String
     * @throws EmailRequestCategorizationException if the EmailRequest type doesn't match any known templates
     */
    public String parseRequest(EmailRequest request){
        Context context = new Context();
        context.setVariable("request", request);
        return switch (request.type()) {
            case "Support Request Submission" -> templateEngine.process("support-request/support-request-submission", context);
            case "Support Request Assignment" -> templateEngine.process("support-request/support-request-assignment", context);
            case "Support Request Resolved" -> templateEngine.process("support-request/support-request-resolved", context);
            case "Support Request Closed" -> templateEngine.process("support-request/support-request-closed", context);
            case "Study Creation" -> templateEngine.process("study-reg/new-study-creation", context);
            case "Study DCC Metadata" -> templateEngine.process("study-reg/study-dcc-metadata-submission", context);
            case "Study Approval" -> templateEngine.process("study-reg/study-approved-for-release", context);
            case "Submission Confirmation" -> templateEngine.process("data-ingest/submission-confirmation", context);
            case "Submission Processed" -> {
                context.setVariable("rejectedFileNames", splitStringOnSemicolons(request.props().get("rejectedFiles")));
                context.setVariable("approvedFileNames", splitStringOnSemicolons(request.props().get("approvedFiles")));
                yield templateEngine.process("data-ingest/submission-processed", context);
            }
            case "Submission Failure" -> templateEngine.process("data-ingest/submission-failure", context);
            case "SFTP Processed" -> {
                context.setVariable("phsNumbers", splitStringOnSemicolons(request.props().get("phsNumbers")));
                yield templateEngine.process("sftp/sftp-processed", context);
            }
            case "Welcome Email" -> templateEngine.process("users/welcome-email", context);
            case "Upload Portal" -> templateEngine.process("upload-portal/portal-submission", context);
            case "Test" -> templateEngine.process("test", context);
            default -> throw new EmailRequestCategorizationException("Cannot categorize request", request);
        };
    }

    private List<String> splitStringOnSemicolons(String fileNames){
        return Arrays.stream(fileNames.split(";"))
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .sorted()
                .toList();
    }

}
