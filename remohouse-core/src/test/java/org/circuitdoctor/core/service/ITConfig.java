<<<<<<< HEAD:remohouse-core/src/test/java/org/circuitdoctor/ITConfig.java
package org.circuitdoctor;
=======
package org.circuitdoctor.core.service;
>>>>>>> frontend:remohouse-core/src/test/java/org/circuitdoctor/core/service/ITConfig.java

import org.circuitdoctor.core.config.JPAConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration

@ComponentScan(value = "org.circuitdoctor.core",
        excludeFilters = {@ComponentScan.Filter(value = {JPAConfig.class}, type = FilterType.ASSIGNABLE_TYPE)})
@Import({JPAConfigIT.class})
@PropertySources({@PropertySource(value = "classpath:db-h2.properties")})
public class ITConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
