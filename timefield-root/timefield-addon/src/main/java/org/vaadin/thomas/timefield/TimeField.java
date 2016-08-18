package org.vaadin.thomas.timefield;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

	@Override
	public String getFormattedValue() {
		if (getValue() == null) {
			return null;
		}

		String format;
		if (!is24HourClock()) {

			switch (getResolution()) {
			case HOUR:
				format = "hh a";
				break;
			case MINUTE:
				format = "hh:mm a";
				break;
			case SECOND:
				format = "hh:mm:ss a";
				break;

			default:
				format = "hh:mm:ss a";
				break;
			}

		} else {
			switch (getResolution()) {
			case HOUR:
				format = "HH";
				break;
			case MINUTE:
				format = "HH:mm";
				break;
			case SECOND:
				format = "HH:mm:ss";
				break;

			default:
				format = "HH:mm:ss";
				break;
			}
		}

		final DateFormat df = new SimpleDateFormat(format);
		return df.format(getValue());
	}

}
