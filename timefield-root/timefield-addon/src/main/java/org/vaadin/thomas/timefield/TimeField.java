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
		final Date date = new Date(0L);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		setValue(date);
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

	@Override
	protected void setInternalValue(int h, int m, int s) {
		final Date date = new Date(0L);
		date.setHours(h);
		date.setMinutes(m);
		date.setSeconds(s);
		setValue(date);
	}

}
