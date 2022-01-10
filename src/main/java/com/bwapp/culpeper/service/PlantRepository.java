package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    Plant findByLatinName(String name);
    Plant findByCommonName(String name);

    @Query("FROM Plant p " +
            "WHERE p.latinName LIKE CONCAT('%', :term, '%') " +
            "OR p.commonName LIKE CONCAT('%', :term, '%') " +
            "OR p.description LIKE CONCAT('%', :term, '%') " +
            "OR p.otherNames LIKE CONCAT('%', :term, '%')")
    List<Plant> findByTerm(String term);
}
