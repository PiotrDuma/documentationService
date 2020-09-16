package com.github.PiotrDuma.documentationService.service.download;

import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.html.Anchor;

public interface DownloadService {
	
	Map<String, Anchor> getDownloadObjects();
	Set<String> getResourceNames();
}
