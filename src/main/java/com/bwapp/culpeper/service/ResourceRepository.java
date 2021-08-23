package com.bwapp.culpeper.service;

import com.bwapp.culpeper.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource findByFileName(String filename);
}
