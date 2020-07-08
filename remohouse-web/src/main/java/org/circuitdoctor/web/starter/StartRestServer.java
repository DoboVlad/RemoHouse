package org.circuitdoctor.web.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"org.circuitdoctor.core"})
@ComponentScan({"org.circuitdoctor.web.controller", "org.circuitdoctor.web.converter"})
@SpringBootApplication
public class StartRestServer {
    public static void main(String[] args) {
        SpringApplication.run(org.circuitdoctor.web.starter.StartRestServer.class, args);
    }
}
