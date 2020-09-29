package com.github.PiotrDuma.documentationService.service.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;

@Service
public class DownloadServiceImpl implements DownloadService {

	private final static String PATH = new String("download/");

	private Resource[] resources;

	private final ResourceLoader resourceLoader;

	@Autowired
	public DownloadServiceImpl(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		resources = getAvailableResources();
	}

	@Override
	public Map<String, Anchor> getDownloadObjects() {
		Map<String, Anchor> map = new HashMap<>();
		for (Resource res : resources) {
			map.put(res.getFilename(), downloadLink(res.getFilename()));
		}
		return map;
	}

	@Override
	public Set<String> getResourceNames() {
		return Stream.of(resources).map(e -> e.getFilename()).collect(Collectors.toSet());
	}

	private Resource[] getAvailableResources() {
		String pattern = PATH + "*.*";
		resources = null;
		try {
			resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
//			resources = applicationContext.getResources(pattern); //also works with injected AplicationContext
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return resources;
	}

	private Anchor downloadLink(String filename) {
		return new Anchor(getResource(filename), filename);
	}

	private StreamResource getResource(String filename) {
		return new StreamResource(filename, () -> {
			try {
//				File file = resourceLoader.getResource(PATH+filename).getFile();
				File file = Stream.of(resources).filter(e -> e.getFilename()
						.equals(filename)).findAny().get().getFile();
				return new BufferedInputStream(new FileInputStream(file));
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		});
	}
}
