package ex.org.project.emailservice.models;

import java.util.List;
import java.util.Map;

public record EmailRequest(
        String type,
        List<String> to,
        List<String> cc,
        String from,
        String subject,
        Map<String, String> props
) {}
