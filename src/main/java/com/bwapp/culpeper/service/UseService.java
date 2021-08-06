package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Use_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UseService {

    @Autowired
    private UseRepository repository;

    public UseService(UseRepository repository) {
        this.repository = repository;
    }

    public List<Use_> findByAilmentName(String term) {
        return repository.findByAilmentNameContains(term);
    }

    public List<Use_> findByAilmentBodyPart(String term) {
        return repository.findByBodyPartContains(term);
    }

    public List<Use_> findByPlantContains(String term) {
        return repository.findByPlantContains(term);
    }

    public List<Use_> findByPlantId(Long id) {
        return repository.findByPlantId(id);
    }

    public List<Use_> findByAilmentTerm(String term) {
        List<Use_> uses = new ArrayList<>();

        if(!term.isBlank()) {
            uses.addAll(findByAilmentName(term));
            uses.addAll(findByAilmentBodyPart(term));
            uses.addAll(findByPlantContains(term));
        }

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
}
