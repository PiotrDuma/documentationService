package com.github.PiotrDuma.documentationService.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Field;

@Repository
public interface ActionRepo extends CrudRepository<Action, Long> {

	public List<Action> findByField(Field field);

	public Action findActionById(Long id);
	
	public List<Action> findByFieldOrderByDateAsc(Field field);
	
	public List<Action> findByFieldOrderByDateDesc(Field field);

	@Query(value = "SELECT a FROM Action a JOIN Field f ON a.field.id=f.id WHERE f.id= ?1")
	public List<Action> findAllByFieldId(Long userId);

	@Query(value = "SELECT a FROM Action a JOIN Field f ON a.field.id=f.id "
			+ "JOIN AppUser u ON f.appUser.id=u.id WHERE u.id= ?1")
	public List<Action> findAllByAppUserId(Long userId);

	@Query(value = "SELECT a FROM Action a JOIN Field f ON a.field.id=f.id "
			+ "JOIN AppUser u ON f.appUser.id=u.id WHERE u.id= ?1 AND a.date BETWEEN ?2 AND ?3 ")

	public List<Action> findAllByAppUserIdSinceDate(Long userId, LocalDate dateFrom, LocalDate dateUntil);
}
