package com.github.PiotrDuma.documentationService.service.balanceCounter;

import java.util.List;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;

public interface BalanceCounter {
	double countBalance(Field field);
	double countBalance(List<Field> fields);
	double countBalance(AppUser user);
}
