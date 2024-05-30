package edu.fra.uas.websitemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebsiteMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteMonitorApplication.class, args);
    }

}
