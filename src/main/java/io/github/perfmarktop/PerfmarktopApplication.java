package io.github.perfmarktop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class PerfmarktopApplication {
    public static void main(String[] args) {
        SpringApplication.run(PerfmarktopApplication.class, args);
    }
}
