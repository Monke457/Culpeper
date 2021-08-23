package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Ailment;
import com.bwapp.culpeper.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AilmentService {

    @Autowired
    private final AilmentRepository repository;

    public AilmentService(AilmentRepository repository) {
        this.repository = repository;
    }

    public List<Ailment> findAll() {
        return repository.findAll();
    }

    public Set<Ailment> findAllSet() {
        return new HashSet<>(repository.findAll());
    }

    public Ailment findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    public Set<String> getAllNames() {
        Set<String> ailmentNames = new HashSet<>();

        for (Ailment ailment : repository.findAll()) {
            ailmentNames.add(ailment.getAilmentName());
        }

        return ailmentNames;
    }

    public Ailment findByName(String name) {
        return repository.findByAilmentName(name);
    }

    public List<Ailment> findByAilmentNameContains(String term) {
        return repository.findByAilmentNameContains(term);
    }

    public  List<Ailment> findByBodyPartContains(String term) {
        return repository.findByBodyPartContains(term);
    }

    public List<Ailment> findByTerm(String term) {
        List<Ailment> ailments = new ArrayList<>();

        if(!term.isBlank()) {
            ailments.addAll(findByAilmentNameContains(term));
            ailments.addAll(findByBodyPartContains(term));
        }

        List<Long> ids = new ArrayList<>();

        //work around for loop not reaching last iteration because list is decreasing...
        for(int i = ailments.size()-1; i >= 0; i--) {
            Ailment a = ailments.get(i);
            if(ids.contains(a.getId())) {
                ailments.remove(i);
            }else {
                ids.add(a.getId());
            }
        }

        return ailments;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Ailment update(Ailment ailment) {
        return repository.save(ailment);
    }

    public boolean exists(String name) {
        return repository.existsByAilmentName(name);
    }
}
