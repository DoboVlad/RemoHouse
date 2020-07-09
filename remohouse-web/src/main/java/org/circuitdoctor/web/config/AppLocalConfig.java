package org.circuitdoctor.web.config;

import org.circuitdoctor.core.config.JPAConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({"org.circuitdoctor.core"})
@Import({JPAConfig.class})
@PropertySources({@PropertySource(value = "classpath:local/db.properties"),
})
public class AppLocalConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
