package com.github.PiotrDuma.documentationService.service.balanceCounter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.service.dao.ActionDao;

@Service(value = "balanceCounter")
public class BalanceCounterImpl implements BalanceCounter {

	private final ActionDao actionDao;
	
	@Autowired
	public BalanceCounterImpl(ActionDao actionDao) {
		this.actionDao = actionDao;
	}

	@Override
	public double countBalance(Field field) {
		double counter = 0;
		for(Action a:actionDao.getAll(field)) {
			counter += getBalance(a);
		}
		return counter;
	}

	@Override
	public double countBalance(List<Field> fields) {
		double counter =0;
		for(Field f:fields) {
			counter += countBalance(f);
		}
		return counter;
	}

	@Override
	public double countBalance(AppUser user) {
		List<Field> fields = user.getFields();
		return countBalance(fields);
	}

	
	private double getBalance(Action action) {
		if(action.getType() == Action.ActionType.GATHERING) {
			return action.getValue(); //revenue
		}
		return -action.getValue(); //cost
	}
}
