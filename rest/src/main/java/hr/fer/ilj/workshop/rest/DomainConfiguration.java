package hr.fer.ilj.workshop.rest;

import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hr.fer.ilj.workshop.files.FileLoader;

@Configuration
public class DomainConfiguration {
  @Bean
  public FileLoader loader() {
    return new FileLoader(Path.of(System.getProperty("user.dir")));
  }
}
