package ex.org.project.emailservice;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import ex.org.project.emailservice.controllers.LambdaHandler;
import ex.org.project.emailservice.services.EmailSendingService;
import ex.org.project.emailservice.services.EmailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.function.Function;

@Slf4j
@SpringBootApplication
@ComponentScan({
    "org.springframework.cloud.function.context",
    "ex.org.project.emailservice.services",
    "ex.org.project.emailservice.config",
    "ex.org.project.emailservice.security"
})
public class EmailServiceApplication {

	@Autowired
	private EmailTemplateService templateService;
	@Autowired
	private EmailSendingService sendingService;

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

	//Lambda function endpoint
	@Bean
	public Function<SQSEvent, String> sendEmail() {
		return new LambdaHandler(templateService, sendingService);
	}

}
