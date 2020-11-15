package de.frittenburger.transcribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TranscribeApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(TranscribeApplication.class, args);
    }
}