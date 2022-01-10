package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<Plant> findByTerm(String term) {
        return repository.findByTerm(term);
    }

    public Plant findByName(String name) {
        return repository.findByCommonName(name);
    }

    public Set<String> getAllNames() {
        Set<String> plantNames = new HashSet<>();

        for (Plant plant : repository.findAll()) {
            plantNames.add(plant.getCommonName());
        }

        return plantNames;
    }

    public void delete(Long id) {
       repository.deleteById(id);
    }

    public Plant update(Plant plant) {
        return repository.save(plant);
    }
}
