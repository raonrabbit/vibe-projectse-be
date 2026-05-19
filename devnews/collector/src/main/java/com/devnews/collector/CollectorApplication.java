package com.devnews.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.devnews")
@EnableScheduling
public class CollectorApplication {

  public static void main(String[] args) {
    SpringApplication.run(CollectorApplication.class, args);
  }
}
