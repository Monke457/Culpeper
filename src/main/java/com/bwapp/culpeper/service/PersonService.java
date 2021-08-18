package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Person findByUsername(String s) {
        return repository.findByUsername(s);
    }

}
