package edu.fra.uas.websitemonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        // Create a new JavaMailSenderImpl instance
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set the host and port for the mail server
        mailSender.setHost("smtp.ionos.de");
        mailSender.setPort(587);
        // Set the username and password for the mail server
        mailSender.setUsername("service@alnaasan.de");
        mailSender.setPassword("B9793atoulB+1and1");

        // Set additional mail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true" );
        props.put("mail.debug", "false");

        // Return the configured mail sender
        return mailSender;
    }
}

