package com.selivanov;

import com.selivanov.dto.PersonDto;
import com.selivanov.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LearnBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnBaseApplication.class, args);
    }

    @Bean
    CommandLineRunner clr(PersonService personService) {
        return args -> {
            personService.savePerson(new PersonDto(null, "Mike", null));
        };
    }
}
