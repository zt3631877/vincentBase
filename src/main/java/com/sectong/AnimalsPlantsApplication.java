package com.sectong;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnimalsPlantsApplication {
 
	public static void main(String args[]) {
		SpringApplication.run(AnimalsPlantsApplication.class, args);
	}
 

}
