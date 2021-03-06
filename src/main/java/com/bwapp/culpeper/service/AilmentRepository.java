package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Ailment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AilmentRepository  extends JpaRepository<Ailment, Long> {
    Ailment findByAilmentName(String name);
    List<Ailment> findByAilmentNameContains(String term);
    List<Ailment> findByBodyPartContains(String term);
    boolean existsByAilmentName(String name);
}
