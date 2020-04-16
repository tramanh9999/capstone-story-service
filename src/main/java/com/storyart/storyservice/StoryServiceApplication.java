package com.storyart.storyservice;

import com.storyart.storyservice.service.HistoryService;
import com.storyart.storyservice.service.StoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class StoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoryServiceApplication.class, args);
	}
}
