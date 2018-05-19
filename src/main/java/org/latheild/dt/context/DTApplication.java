package org.latheild.dt.context;

import org.latheild.dt.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({
        "org.latheild.dt.**"
})
@Configuration
@Import({
        WebConfig.class
})
@EnableWebMvc
public class DTApplication {



    public static void main(String[] args) {
        SpringApplication.run(DTApplication.class, args);
    }

}
