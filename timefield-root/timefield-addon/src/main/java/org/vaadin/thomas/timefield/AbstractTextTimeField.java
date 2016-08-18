package org.vaadin.thomas.timefield;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public abstract class AbstractTextTimeField<T> extends AbstractTimeField<T> {

	private static final long serialVersionUID = -7767159784950039512L;

	protected TextField field;

	public AbstractTextTimeField() {
		field = new TextField();
		field.setNullRepresentation("");
	}

	@Override
	protected Component initContent() {
		return field;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		field.setReadOnly(readOnly);
	}
}
