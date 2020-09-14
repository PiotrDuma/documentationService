package com.github.PiotrDuma.documentationService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.PiotrDuma.documentationService.model.FieldDetails;

@Repository
public interface FieldDetailsRepo extends CrudRepository<FieldDetails, Long>{

}
