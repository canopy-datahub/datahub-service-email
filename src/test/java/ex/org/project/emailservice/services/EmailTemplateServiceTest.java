package ex.org.project.emailservice.services;

import ex.org.project.emailservice.exceptions.EmailRequestCategorizationException;
import ex.org.project.emailservice.models.EmailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.StaticApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceTest {

    private TemplateEngine templateEngine;
    private final EmailTemplateService templateService;

    public EmailTemplateServiceTest(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(new StaticApplicationContext());
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateService = new EmailTemplateService(templateEngine);
    }

    @Test
    void parseRequestTest() {
        Map<String, String> props = new HashMap<>();
        props.put("name", "Tester McTestington");
        List<String> cc = new ArrayList<String>();
        EmailRequest mockRequest = new EmailRequest(
                "Test",
                List.of("to@test.com"),
                cc,
                "from@test.com",
                "Subject",
                props
        );
        String template = templateService.parseRequest(mockRequest);
        assertTrue(template.contains("Tester McTestington"));
    }

    @Test
    void parseRequestShouldThrowError() {
        Map<String, String> props = new HashMap<>();
        props.put("name", "Tester McTestington");
        props.put("supportRequestId", "12345");
        List<String> cc = new ArrayList<String>();
        EmailRequest mockRequest = new EmailRequest(
                "invalid type",
                List.of("to@test.com"),
                cc,
                "from@test.com",
                "Subject",
                props
        );
        try {
            String template = templateService.parseRequest(mockRequest);
        } catch (EmailRequestCategorizationException e){
            assertTrue(true);
            return;
        }
        fail("An invalid EmailRequest type should throw an error");
    }
}
