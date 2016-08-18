package org.vaadin.thomas.timefield.demo;

import java.time.LocalTime;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import org.vaadin.thomas.timefield.AbstractTimeField;
import org.vaadin.thomas.timefield.LocalTimeField;
import org.vaadin.thomas.timefield.LocalTimeTextField;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("MyComponent Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		final VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		setContent(content);

		addThings(content);

	}

	private AbstractTimeField<?> createField() {
		return new LocalTimeTextField();
	}

	private void addThings(final VerticalLayout content) {

		final AbstractTimeField<?> f = createField();
		f.setLocale(Locale.FRANCE);
		f.setWidth("200px");
		f.setImmediate(true);
		f.setHours(0);
		content.addComponent(f);

		AbstractTimeField<?> f2 = createField();
		f2.setResolution(Resolution.SECOND);
		f2.setLocale(Locale.US);
		f2.setWidth("200px");
		// f2.setHourMin(1);
		// f2.setHourMax(14);
		content.addComponent(f2);
		f2.addValueChangeListener(e -> {
			final AbstractTimeField<?> field = (AbstractTimeField<?>) e
					.getProperty();
			final LocalTime t = (LocalTime) field.getValue();
			System.out.println(t);
			Notification.show(field.getFormattedValue());
		});

		f2.setPropertyDataSource(f);

		f2 = createField();
		f2.setWidth("200px");
		f2.setResolution(Resolution.MINUTE);
		f2.setMinutes(40);
		f2.setMinuteInterval(30);
		f2.setHourMin(-12);
		f2.setHourMax(3);
		content.addComponent(f2);

		f2 = new LocalTimeField("disabled");
		f2.setWidth("200px");
		f2.setEnabled(false);
		content.addComponent(f2);

		f2 = new LocalTimeField("readonly");
		f2.setWidth("200px");
		f2.setReadOnly(true);
		content.addComponent(f2);
	}

}
