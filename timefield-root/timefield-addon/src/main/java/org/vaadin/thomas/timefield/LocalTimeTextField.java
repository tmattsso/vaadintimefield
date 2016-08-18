package org.vaadin.thomas.timefield;

import java.time.LocalTime;

import com.vaadin.data.util.ObjectProperty;

/**
 * A field for selecting time values.
 * <p>
 * Uses {@link LocalTime} as internal data type.
 *
 * @author Thomas Mattsson / Vaadin Ltd.
 */
public class LocalTimeTextField extends AbstractTextTimeField<LocalTime> {

	private static final long serialVersionUID = 9082508899983202854L;

	private final ObjectProperty<LocalTime> fieldProp = new ObjectProperty<LocalTime>(
			LocalTime.MIN);

	public LocalTimeTextField() {

		super();

		field.setConverter(new LocalTimeConverter(this));
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

			fireValueChange(false);
		});

	}

	public LocalTimeTextField(String string) {
		super();
		setCaption(string);
	}

	@Override
	protected int getHoursInternal() {
		if (fieldProp.getValue() == null) {
			return 0;
		}
		return fieldProp.getValue().getHour();
	}

	@Override
	protected int getMinutesInternal() {
		if (fieldProp.getValue() == null) {
			return 0;
		}
		return fieldProp.getValue().getMinute();
	}

	@Override
	protected int getSecondsInternal() {
		if (fieldProp.getValue() == null) {
			return 0;
		}
		return fieldProp.getValue().getSecond();
	}

	@Override
	protected void setHoursInternal(int val) {
		LocalTime time = fieldProp.getValue();
		if (time == null) {
			time = LocalTime.MIN;
		}
		time = time.withHour(val);
		fieldProp.setValue(time);
	}

	@Override
	protected void setMinutesInternal(int val) {
		LocalTime time = fieldProp.getValue();
		if (time == null) {
			time = LocalTime.MIN;
		}
		time = time.withMinute(val);
		fieldProp.setValue(time);
	}

	@Override
	protected void setSecondsInternal(int val) {
		LocalTime time = fieldProp.getValue();
		if (time == null) {
			time = LocalTime.MIN;
		}
		time = time.withSecond(val);
		fieldProp.setValue(time);
	}

	@Override
	public Class<? extends LocalTime> getType() {
		return LocalTime.class;
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
		return new LocalTimeConverter(this).convertToPresentation(getValue(),
				String.class, getLocale());
	}

}
