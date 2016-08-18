package org.vaadin.thomas.timefield;

import java.util.Date;

import com.vaadin.data.util.ObjectProperty;

/**
 * A field for selecting time values.
 * <p>
 * Uses {@link Date} as internal data type.
 *
 * @author Thomas Mattsson / Vaadin Ltd.
 */
@SuppressWarnings("deprecation")
public class TimeTextField extends AbstractTextTimeField<Date> {

	private static final long serialVersionUID = 9082508899983202854L;

	private final ObjectProperty<Date> fieldProp = new ObjectProperty<Date>(
			new Date(70, 0, 1, 0, 0, 0));

	public TimeTextField() {

		super();

		field.setConverter(new DateConverter(this));
		field.setPropertyDataSource(fieldProp);
		field.setBuffered(false);
		field.setImmediate(true);

		fieldProp.addValueChangeListener(e -> {
			if (maskInternalValueChange) {
				return;
			}

			maskInternalValueChange = true;
			setValue(fieldProp.getValue(), true);
			maskInternalValueChange = false;
			checkBoundsAndInterval();
		});

	}

	public TimeTextField(String string) {
		this();
		setCaption(string);
	}

	@Override
	protected int getHoursInternal() {
		if (fieldProp.getValue() == null) {
			return 0;
		}
		return fieldProp.getValue().getHours();
	}

	@Override
	protected int getMinutesInternal() {
		if (fieldProp.getValue() == null) {
			return 0;
		}
		return fieldProp.getValue().getMinutes();
	}

	@Override
	protected int getSecondsInternal() {
		if (fieldProp.getValue() == null) {
			return 0;
		}
		return fieldProp.getValue().getSeconds();
	}

	@Override
	protected void setHoursInternal(int val) {

		Date value = fieldProp.getValue();
		if (value == null) {
			value = new Date(70, 0, 1, 0, 0, 0);
		}
		final Date time = new Date(value.getTime());
		time.setHours(val);
		fieldProp.setValue(time);
	}

	@Override
	protected void setMinutesInternal(int val) {
		Date value = fieldProp.getValue();
		if (value == null) {
			value = new Date(70, 0, 1, 0, 0, 0);
		}
		final Date time = new Date(value.getTime());
		time.setMinutes(val);
		fieldProp.setValue(time);
	}

	@Override
	protected void setSecondsInternal(int val) {

		Date value = fieldProp.getValue();
		if (value == null) {
			value = new Date(70, 0, 1, 0, 0, 0);
		}
		final Date time = new Date(value.getTime());
		time.setSeconds(val);
		fieldProp.setValue(time);
	}

	@Override
	public Class<? extends Date> getType() {
		return Date.class;
	}

	@Override
	protected void updateFields() {

		maskInternalValueChange = true;
		checkBoundsAndInterval();
		maskInternalValueChange = false;

		fieldProp.setValue(getValue());
	}

	@Override
	public String getFormattedValue() {
		return new DateConverter(this).convertToPresentation(getValue(),
				String.class, getLocale());
	}

}
