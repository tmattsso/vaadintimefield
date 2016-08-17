package org.vaadin.thomas.timefield;

import java.time.LocalTime;

/**
 * A field for selecting time values.
 * <p>
 * Uses {@link LocalTime} as internal data type.
 *
 * @author Thomas Mattsson / Vaadin Ltd.
 */
public class LocalTimeField extends AbstractDropdownTimeField<LocalTime> {

	private static final long serialVersionUID = 4657188596501444712L;

	public LocalTimeField() {
		super();
	}

	public LocalTimeField(String caption) {
		super(caption);
	}

	@Override
	public Class<? extends LocalTime> getType() {
		return LocalTime.class;
	}

	@Override
	protected void resetValue() {
		setValue(LocalTime.MIN);
	}

	@Override
	protected int getHoursInternal() {
		return getValue().getHour();
	}

	@Override
	protected int getMinutesInternal() {
		return getValue().getMinute();
	}

	@Override
	protected int getSecondsInternal() {
		return getValue().getSecond();
	}

	@Override
	protected void setHoursInternal(int val) {
		LocalTime value = getValue();
		value = value.withHour(val);
		setValue(value, true);
	}

	@Override
	protected void setMinutesInternal(int val) {
		LocalTime value = getValue();
		value = value.withMinute(val);
		setValue(value, true);
	}

	@Override
	protected void setSecondsInternal(int val) {
		LocalTime value = getValue();
		value = value.withSecond(val);
		setValue(value, true);
	}

}
