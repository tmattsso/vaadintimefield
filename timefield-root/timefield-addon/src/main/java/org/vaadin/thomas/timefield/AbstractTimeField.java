package org.vaadin.thomas.timefield;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;

public abstract class AbstractTimeField<T> extends CustomField<T> {

	private static final long serialVersionUID = 3385019798929715725L;

	protected boolean use24HourClock = true;
	private Locale givenLocale = null;
	protected Resolution resolution = Resolution.MINUTE;
	protected int intervalMinutes = 1;
	protected int minHours = 0;
	protected int maxHours = 23;
	protected boolean maskInternalValueChange = false;

	public AbstractTimeField() {
		super();
	}

	/**
	 * Returns hour value in 24-hour format (0-23)
	 */
	public int getHours() {
		return getHoursInternal();
	}

	/**
	 * Set hour in 24-hour format (0-23).
	 *
	 * @param hours
	 * @throws IllegalArgumentException
	 *             If the hour parameter is outside the bounds marked by
	 *             {@link #getHourMin()} and {@link #getHourMax()}
	 */
	public void setHours(int hours) throws IllegalArgumentException {

		if (hours < minHours || hours > maxHours) {
			throw new IllegalArgumentException("Value '" + hours
					+ "' is outside bounds '" + minHours + "' - '" + maxHours
					+ "'");
		}

	}

	public int getMinutes() {
		return getMinutesInternal();
	}

	/**
	 * @param minutes
	 *
	 * @throws IllegalArgumentException
	 *             If the minute parameter is not compatible with
	 *             {@link #getMinuteInterval()}
	 */
	public void setMinutes(int minutes) {
		if (minutes % intervalMinutes != 0) {
			throw new IllegalArgumentException("Value '" + minutes
					+ "' is not compatible with interval '" + intervalMinutes
					+ "'");
		}
	}

	public int getSeconds() {
		return getSecondsInternal();
	}

	public void setSeconds(int seconds) {
	}

	protected abstract int getHoursInternal();

	protected abstract int getMinutesInternal();

	protected abstract int getSecondsInternal();

	protected abstract void setHoursInternal(int val);

	protected abstract void setMinutesInternal(int val);

	protected abstract void setSecondsInternal(int val);

	/**
	 * Sets locale that is used for time format detection. Defaults to browser
	 * locale.
	 */
	@Override
	public void setLocale(Locale l) {
		givenLocale = l;

		final DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT,
				givenLocale);
		final String time = df.format(new Date());

		if (time.contains("am") || time.contains("AM") || time.contains("pm")
				|| time.contains("PM")) {
			use24HourClock = false;
		} else {
			use24HourClock = true;
		}
		updateFields();
	}

	protected abstract void updateFields();

	/**
	 * Only resolutions of HOUR, MIN, SECOND are supported. Default is
	 * DateField.RESOLUTION_MIN.
	 *
	 * @see DateField#setResolution(int)
	 * @param resolution
	 */
	public void setResolution(Resolution resolution) {

		this.resolution = resolution;
		updateFields();
	}

	/**
	 * @see DateField#getResolution()
	 * @return
	 */
	public Resolution getResolution() {
		return resolution;
	}

	@Override
	public void attach() {
		super.attach();

		// if there isn't a given locale at this point, use the one from the
		// browser
		if (givenLocale == null) {
			// we have access to application after attachment
			@SuppressWarnings("deprecation")
			final Locale locale = getUI().getSession().getBrowser().getLocale();
			givenLocale = locale;
		}
	}

	public void set24HourClock(boolean use24hourclock) {
		use24HourClock = use24hourclock;
		updateFields();
	}

	public boolean is24HourClock() {
		return use24HourClock;
	}

	public int getMinuteInterval() {
		return intervalMinutes;
	}

	/**
	 * Set the interval to be used in minute select. Default is 1 minute
	 * intervals.
	 *
	 * @param interval
	 */
	public void setMinuteInterval(int interval) {
		if (interval < 0) {
			interval = 0;
		}
		intervalMinutes = interval;
		updateFields();
	}

	public int getHourMin() {
		return minHours;
	}

	/**
	 * Set the minimum allowed value for the hour, in 24h format. Defaults to 0.
	 * <p>
	 * Changing this setting so that the field has a value outside the new
	 * bounds will lead to the field resetting its value to the first bigger
	 * value that is valid.
	 *
	 */
	public void setHourMin(int minHours) {

		if (minHours < 0) {
			minHours = 0;
		}
		if (minHours > 23) {
			minHours = 23;
		}

		this.minHours = minHours;
		updateFields();
	}

	public int getHourMax() {
		return maxHours;
	}

	/**
	 * Set the maximum allowed value for the hour, in 24h format. Defaults to
	 * 23.
	 * <p>
	 * Changing this setting so that the field has a value outside the new
	 * bounds will lead to the field resetting its value to the first smaller
	 * value that is valid.
	 *
	 */
	public void setHourMax(int maxHours) {

		if (maxHours < 0) {
			maxHours = 0;
		}
		if (maxHours > 23) {
			maxHours = 23;
		}

		this.maxHours = maxHours;
		updateFields();
	}

	protected void checkBoundsAndInterval() {
		// check hour bounds
		if (getHoursInternal() < minHours) {
			// guess
			int compatibleVal = getHoursInternal();
			while (compatibleVal < 24) {
				if (compatibleVal >= minHours) {
					break;
				}
				compatibleVal++;
			}
			if (compatibleVal >= minHours) {
				setHoursInternal(compatibleVal);
			} else {
				// no acceptable hour found. Most likely user is changing
				// bounds, and will fix with another call to the other bound.
			}
		} else if (getHoursInternal() > maxHours) {
			// guess
			int compatibleVal = getHoursInternal();
			while (compatibleVal > 0) {
				if (compatibleVal <= maxHours) {
					break;
				}
				compatibleVal--;
			}
			if (compatibleVal <= maxHours) {
				setHoursInternal(compatibleVal);
			} else {
				// no acceptable hour found. Most likely user is changing
				// bounds, and will fix with another call to the other bound.
			}
		}

		// check minute interval
		if (getMinutesInternal() % intervalMinutes != 0) {
			// guess
			int compatibleVal = getMinutesInternal();
			while (compatibleVal > 0) {
				if (compatibleVal % intervalMinutes == 0) {
					break;
				}
				compatibleVal--;
			}
			setMinutesInternal(compatibleVal);
		}
	}

	@Override
	protected void fireValueChange(boolean repaintIsNotNeeded) {
		if (maskInternalValueChange) {
			return;
		}
		super.fireValueChange(repaintIsNotNeeded);
	}
}