package org.canopyplatform.canopy.emailservice.config;

import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class BeanConfig {
    /*
    Manually creating these beans which are normally created by the io.awspring.cloud libraries.
    For whatever reason, when I converted this application to a lambda function, the beans
    which were being autoconfigured by the cloud libraries were not working correctly.
    Disabling the autoconfig and explicitly creating them here resolved that issue.
     */

    @Bean
    public JavaMailSender javaMailSender(SesClient sesClient) {
        return new SimpleEmailServiceJavaMailSender(sesClient);
    }

    @Bean
    public S3Client s3Client(){
        return S3Client.create();
    }

    @Bean
    public SesClient sesClient(){ return SesClient.create(); }

}
