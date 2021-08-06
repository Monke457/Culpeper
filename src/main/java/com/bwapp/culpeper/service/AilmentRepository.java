package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Ailment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AilmentRepository  extends JpaRepository<Ailment, Long> {
    List<Ailment> findByAilmentNameContains(String term);
    List<Ailment> findByBodyPartContains(String term);
}
