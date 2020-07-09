package org.circuitdoctor.web.starter;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"org.circuitdoctor.core",
        "org.circuitdoctor.web.controller", "org.circuitdoctor.web.converter","org.circuitdoctor.web.config"})
//@EnableJpaRepositories({"org.circuitdoctor.core.repository"})
@EntityScan({"org.circuitdoctor.core","org.circuitdoctor.web"})
public class StartRestServer {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServer.class, args);
    }
}
