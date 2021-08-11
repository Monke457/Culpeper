package com.bwapp.culpeper;

import com.bwapp.culpeper.model.Person;
import com.bwapp.culpeper.service.PersonRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PersonRepository repository) {
        return(args) -> {
            /*
            Logger logger = LoggerFactory.getLogger(getClass());
            Person person = new Person();
            person.setUsername("admin");
            person.setPassword(new BCryptPasswordEncoder().encode("Pass123"));
            repository.save(person);

             */
        };
    }
}
