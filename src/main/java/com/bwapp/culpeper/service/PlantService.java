package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    @Autowired
    private PlantRepository repository;

    public PlantService(PlantRepository repository) {
        this.repository = repository;
    }

    public Plant findByLatinName(String name) {
        return repository.findByLatinName(name);
    }
    public Plant findByCommonName(String name) {
        return repository.findByCommonName(name);
    }
    public List<Plant> findByDescription(String term) {
        return repository.findByDescription(term);
    }
}
