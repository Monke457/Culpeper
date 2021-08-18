package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    @Autowired
    private final PlantRepository repository;

    public PlantService(PlantRepository repository) {
        this.repository = repository;
    }

    public List<Plant> findAll() {
        return repository.findAll();
    }

    public Plant findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }
}
