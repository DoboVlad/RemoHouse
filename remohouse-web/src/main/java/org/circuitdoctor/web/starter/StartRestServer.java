package org.circuitdoctor.web.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.circuitdoctor.core",
        "org.circuitdoctor.web.controller", "org.circuitdoctor.web.converter","org.circuitdoctor.web.config"})
@EntityScan({"org.circuitdoctor.core","org.circuitdoctor.web"})
public class StartRestServer {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServer.class, args);
    }
}
