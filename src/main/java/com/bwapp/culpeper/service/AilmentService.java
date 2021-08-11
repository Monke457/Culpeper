package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Ailment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Ailment findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
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
}
