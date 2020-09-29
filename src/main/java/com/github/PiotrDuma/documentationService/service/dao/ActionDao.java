package com.github.PiotrDuma.documentationService.service.dao;

import java.time.LocalDate;
import java.util.List;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Field;




public interface ActionDao extends DaoInterface<Action>{
	
	List<Action> getAll(Field field);
	
	/**
	 * @param sort: if true sort ASC, if false DESC
	 *
	 */
	List<Action> getAll(Field field, boolean sort);

	List<Action> getActionsByUserId(Long userId);
	List<Action> getActionsByDates(Long userId, LocalDate from, LocalDate unitl);
}
