package io.github.perfmarktop.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.slave")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slave {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
}
