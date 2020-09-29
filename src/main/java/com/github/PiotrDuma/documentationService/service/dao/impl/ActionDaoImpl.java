package com.github.PiotrDuma.documentationService.service.dao.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.repository.ActionRepo;
import com.github.PiotrDuma.documentationService.service.dao.ActionDao;

@Service
public class ActionDaoImpl implements ActionDao {

	private ActionRepo actionRepo;

	@Autowired
	public ActionDaoImpl(ActionRepo actionRepo) {
		this.actionRepo = actionRepo;
	}

	@Override
	@Transactional
	public Action save(Action action) {
		return actionRepo.save(action);
	}

	@Override
	@Transactional
	public Action update(Long id, Action action) {
		return actionRepo.findById(id).map(elem -> {
			elem.setDetails(action.getDetails());
			elem.setDate(action.getDate());
			elem.setField(action.getField());
			elem.setValue(action.getValue());
			elem.setQuantity(action.getQuantity());
			elem.setNote(action.getNote());
			actionRepo.save(elem);
			return elem;
		}).orElseThrow(() -> new EntityNotFoundException());
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if (actionRepo.existsById(id)) {
			actionRepo.deleteById(id);
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public Optional<Action> get(Long id) {
		return actionRepo.findById(id);
	}

	@Override
	public List<Action> getAll() {
		List<Action> list = new ArrayList<>();
		actionRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public boolean isPreset(Action action) {
		return actionRepo.existsById(action.getId());
	}

	@Override
	public boolean isPreset(Long id) {
		return actionRepo.existsById(id);
	}

	@Override
	public List<Action> getAll(Field field) {
		return actionRepo.findByField(field);
	}

	@Override
	public List<Action> getAll(Field field, boolean sort) {
		if (sort == true) {
			return actionRepo.findByFieldOrderByDateAsc(field);
		}else {
			return actionRepo.findByFieldOrderByDateDesc(field);
		}
	}

	@Override
	public List<Action> getActionsByUserId(Long userId) {
		return actionRepo.findAllByAppUserId(userId);
	}

	@Override
	public List<Action> getActionsByDates(Long userId, LocalDate from, LocalDate unitl) {
		return actionRepo.findAllByAppUserIdSinceDate(userId, from, unitl);
	}

}
