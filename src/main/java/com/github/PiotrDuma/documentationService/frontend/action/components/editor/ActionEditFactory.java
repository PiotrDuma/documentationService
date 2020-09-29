package com.github.PiotrDuma.documentationService.frontend.action.components.editor;

import com.github.PiotrDuma.documentationService.model.Action;

public class ActionEditFactory {

	public static AbstractActionEditor createActionEdit(Action.ActionType type) {
		AbstractActionEditor editor = new UniqueActionEditor();
		switch (type) {
		case SEEDING:
			editor = new ActionEditorSeeding();
			break;
		case SPRAYING:
			editor = new ActionEditorSpraying();
			break;
		case TILLAGE:
			editor = new ActionEditorTillage();
			break;
		case GATHERING:
			editor = new ActionEditorGathering();
			break;
		default:
			return editor;
		}
		return editor;
	}
	
	public static AbstractActionEditor createActionEdit(Action.ActionType type, Action action) {
		AbstractActionEditor editor = new UniqueActionEditor(action);
		switch (type) {
		case SEEDING:
			editor = new ActionEditorSeeding(action);
			break;
		case SPRAYING:
			editor = new ActionEditorSpraying(action);
			break;
		case TILLAGE:
			editor = new ActionEditorTillage(action);
			break;
		case GATHERING:
			editor = new ActionEditorGathering(action);
			break;
		default:
			return editor;
		}
		return editor;
	}
}
