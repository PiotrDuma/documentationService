package com.github.PiotrDuma.documentationService.service.BalanceCounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.service.balanceCounter.BalanceCounterImpl;
import com.github.PiotrDuma.documentationService.service.dao.ActionDao;


@ExtendWith(MockitoExtension.class)
public class BalanceCounterTest {
	
	@Mock
	private ActionDao actionDao;
	
	@InjectMocks
	private BalanceCounterImpl counter;

	private List<Action> list;
	private Field field;
	
	@BeforeEach
	public void init() {	
		list = new LinkedList<>();
		field = mock(Field.class);
	}
	
	@Test
	public void testFieldBalanceActionGathering() {
		list.add(new Action(null,"",100,0,null,Action.ActionType.GATHERING,""));
		list.add(new Action(null,"",100,0,null,Action.ActionType.GATHERING,""));
		list.add(new Action(null,"",100,0,null,Action.ActionType.GATHERING,""));
		
		when(actionDao.getAll(Mockito.any())).thenReturn(list);
		
		assertEquals(300, counter.countBalance(field));
	}

	@Test
	public void testFieldBalanceActionSeeding() {
		list.add(new Action(null,"",100,0,null,Action.ActionType.SEEDING,""));
		list.add(new Action(null,"",200,0,null,Action.ActionType.SEEDING,""));
		
		when(actionDao.getAll(Mockito.any())).thenReturn(list);
		
		assertEquals(-300, counter.countBalance(field));
	}
	
	@Test
	public void testFieldBalanceActionMixed() {
		list.add(new Action(null,"",300,0,null,Action.ActionType.SEEDING,""));
		list.add(new Action(null,"",200,0,null,Action.ActionType.TILLAGE,""));
		list.add(new Action(null,"",100,0,null,Action.ActionType.SPRAYING,""));
		list.add(new Action(null,"",1000,0,null,Action.ActionType.GATHERING,""));
		
		when(actionDao.getAll(Mockito.any())).thenReturn(list);
		
		assertEquals(400, counter.countBalance(field));
	}
	
	@Test
	public void testFieldBalanceListOfFields() {
		list.add(new Action(null,"",300,0,null,Action.ActionType.SEEDING,""));
		list.add(new Action(null,"",200,0,null,Action.ActionType.TILLAGE,""));
		list.add(new Action(null,"",100,0,null,Action.ActionType.SPRAYING,""));
		list.add(new Action(null,"",1000,0,null,Action.ActionType.GATHERING,""));
		
		when(actionDao.getAll(Mockito.any())).thenReturn(list);
		
		List<Field> fieldList = Arrays.asList(field,field);
		
		assertEquals(2*400, counter.countBalance(fieldList));
	}
	
	@Test
	public void testFieldBalanceEmptyList() {		
		List<Field> fieldList = new ArrayList<Field>();
		
		assertEquals(0, counter.countBalance(fieldList));
	}
	
	@Test
	public void testFieldBalanceByAppUser() {
		list.add(new Action(null,"",300,0,null,Action.ActionType.SEEDING,""));
		list.add(new Action(null,"",40,0,null,Action.ActionType.TILLAGE,""));
		
		AppUser user = mock(AppUser.class);
		when(user.getFields()).thenReturn(Arrays.asList(field,field));
		when(actionDao.getAll(Mockito.any())).thenReturn(list);
		
		assertEquals(-680, counter.countBalance(user));
	}
}
