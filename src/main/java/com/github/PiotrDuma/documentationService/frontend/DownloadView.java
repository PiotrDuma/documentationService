package com.github.PiotrDuma.documentationService.frontend;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.download.DownloadService;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "docs", layout = MainLayout.class)
public class DownloadView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	public DownloadView(UserNavigator userNavigator, DownloadService service) {

		getStyle().set("padding-left", "15%").set("padding-right", "15%");
		H3 h3 = new H3("Download files:");
		add(h3);

		Map<String, Anchor> map = service.getDownloadObjects();
		for (String key : map.keySet()) {
			add(map.get(key));
		}
	}
}
