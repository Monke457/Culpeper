package com.bwapp.culpeper;

import com.bwapp.culpeper.model.Person;
import com.bwapp.culpeper.service.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataGenerator {

    @Bean
    CommandLineRunner initDatabase(PersonRepository personRepository) {

        return args -> {
            Person p = new Person("Admin", new BCryptPasswordEncoder().encode("Pass123"));

            //personRepository.save(p);
        };
    }
}
