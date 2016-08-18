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

		return new DateConverter(this).convertToPresentation(getValue(),
				String.class, getLocale());

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
