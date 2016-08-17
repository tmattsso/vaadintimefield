package org.vaadin.thomas.timefield;

import java.util.Date;

/**
 * A field for selecting time values.
 * <p>
 * Uses {@link Date} as internal data type.
 *
 * @author Thomas Mattsson / Vaadin Ltd.
 */
@SuppressWarnings("deprecation")
public class TimeField extends AbstractDropdownTimeField<Date> {

	private static final long serialVersionUID = 4657188596501444712L;

	public TimeField() {
		super();
	}

	public TimeField(String caption) {
		super(caption);
	}

	@Override
	public Class<? extends Date> getType() {
		return Date.class;
	}

	@Override
	protected void resetValue() {
		setValue(new Date());
	}

	@Override
	protected int getHoursInternal() {
		return getValue().getHours();
	}

	@Override
	protected int getMinutesInternal() {
		return getValue().getMinutes();
	}

	@Override
	protected int getSecondsInternal() {
		return getValue().getSeconds();
	}

	@Override
	protected void setHoursInternal(int val) {
		final Date value = getValue();
		value.setHours(val);
		setValue(value);
	}

	@Override
	protected void setMinutesInternal(int val) {
		final Date value = getValue();
		value.setMinutes(val);
		setValue(value);
	}

	@Override
	protected void setSecondsInternal(int val) {
		final Date value = getValue();
		value.setSeconds(val);
		setValue(value);
	}

}
