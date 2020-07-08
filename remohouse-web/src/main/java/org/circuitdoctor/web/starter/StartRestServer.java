package org.circuitdoctor.web.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"org.circuitdoctor.core",
        "org.circuitdoctor.web.controller", "org.circuitdoctor.web.converter"})
@SpringBootApplication(scanBasePackages = {"org.circuitdoctor.core",
        "org.circuitdoctor.web.controller", "org.circuitdoctor.web.converter"})
@EnableJpaRepositories({"org.circuitdoctor.core.repository"})
@EntityScan("org.circuitdoctor.core.domain")
public class StartRestServer {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServer.class, args);
    }
}
