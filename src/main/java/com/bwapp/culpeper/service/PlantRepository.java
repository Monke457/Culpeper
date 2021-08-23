package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    Plant findByLatinName(String name);
    Plant findByCommonName(String name);
}
