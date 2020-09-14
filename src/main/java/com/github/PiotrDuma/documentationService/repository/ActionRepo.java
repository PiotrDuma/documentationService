package com.github.PiotrDuma.documentationService.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Field;

@Repository
public interface ActionRepo extends CrudRepository<Action, Long> {

	public List<Action> findByField(Field field);
	public Action findActionById(Long id);
}
