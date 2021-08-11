package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByUsername(String s);
}
