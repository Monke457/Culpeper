package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Plant;
import com.bwapp.culpeper.model.Use_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UseService {

    @Autowired
    private final UseRepository repository;

    public UseService(UseRepository repository) {
        this.repository = repository;
    }

    public List<Use_> findAll() {
        return repository.findAll();
    }

    public Use_ findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    public List<Use_> findByAilmentContains(String term) {
        return repository.findByAilmentContains(term);
    }

    public List<Use_> findByPlant(String term) {
        return repository.findByPlantContains(term);
    }

    public List<Use_> findByPlantId(Long id) {
        return repository.findByPlantId(id);
    }

    public List<Use_> findByAilmentId(Long id) {
        return repository.findByAilmentId(id);
    }

    public List<Use_> findByPlantTerm(String term) {
        List<Use_> uses = new ArrayList<>(findByPlant(term));
        List<Long> ids = new ArrayList<>();

        //work around for loop not reaching last iteration because list is decreasing...
        for(int i = uses.size()-1; i >= 0; i--) {
            Use_ u = uses.get(i);
            if(ids.contains(u.getPlant().getId())) {
                uses.remove(i);
            }else {
                ids.add(u.getPlant().getId());
            }
        }
        return uses;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Use_ update(Use_ use) {
        return repository.save(use);
    }
}
