package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Use_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UseRepository  extends JpaRepository<Use_, Long> {

    @Query("FROM Use_ u " +
            "JOIN u.ailments a " +
            "WHERE a.ailmentName LIKE CONCAT('%', :term, '%') " +
            "OR a.bodyPart LIKE CONCAT('%', :term, '%')")
    List<Use_> findByAilmentContains(@Param("term") String term);

    @Query("FROM Use_ u " +
            "LEFT OUTER JOIN Plant p ON u.plant = p.id " +
            "WHERE p.latinName LIKE CONCAT('%', :term, '%') " +
            "OR p.commonName LIKE CONCAT('%', :term, '%') " +
            "OR p.description LIKE CONCAT('%', :term, '%') " +
            "OR p.otherNames LIKE CONCAT('%', :term, '%')")
    List<Use_> findByPlantContains(@Param("term") String term);

    List<Use_> findByPlantId(Long id);

    @Query("FROM Use_ u " +
            "JOIN u.ailments a " +
            "WHERE a.id = :id ")
    List<Use_> findByAilmentId(@Param("id") Long id);
}
