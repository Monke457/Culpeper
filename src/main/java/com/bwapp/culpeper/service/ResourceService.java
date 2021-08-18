package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    public List<Resource> findAll() {
        return repository.findAll();
    }

    public Resource update(Resource r) {
        if(!repository.existsByPath(r.getPath())) {
            return repository.save(r);
        }
        else return null;
    }

    public Resource findByPath(String path) {
        return repository.findByPath(path);
    }

    public void delete(Resource r) {
        repository.delete(r);
    }
}
