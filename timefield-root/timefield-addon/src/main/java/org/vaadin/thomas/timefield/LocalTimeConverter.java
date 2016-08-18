package org.vaadin.thomas.timefield;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class LocalTimeConverter implements Converter<String, LocalTime> {
	private static final long serialVersionUID = 837077650472730640L;
	private final AbstractTimeField<?> field;

	public LocalTimeConverter(AbstractTimeField<?> timeTextField) {
		field = timeTextField;
	}

	@Override
	public LocalTime convertToModel(String value,
			Class<? extends LocalTime> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (value == null) {
			return null;
		}

		int hour = 0;
		int minute = 0;
		int second = 0;

		try {

			Boolean isPm = null;

			value = value.toLowerCase().replaceAll(" ", "");

			if (value.contains("am") || value.contains("a.m.")) {
				isPm = false;
				value = value.replace("am", "").replace("a.m.", "");
			} else if (value.contains("pm") || value.contains("p.m.")) {
				isPm = true;
				value = value.replace("pm", "").replace("p.m.", "");
			}

			if (value.contains(":")) {
				final String[] split = value.split(":");

				if (split.length > 0) {
					final String h = split[0];
					hour = Integer.valueOf(h);
				}
				if (split.length > 1) {
					final String m = split[1];
					minute = Integer.valueOf(m);
				}
				if (split.length > 2) {
					final String s = split[2];
					second = Integer.valueOf(s);
				}
			} else if (value.length() < 7) {

				// try splitting by length (e.g. 0700)
				String sec = null;
				String min = null;
				String h = null;

				switch (value.length()) {
				case 6:
					sec = value.substring(4, 6);
				case 5:
					if (sec == null) {
						sec = value.substring(4, 5);
					}
					second = Integer.valueOf(sec);
				case 4:
					min = value.substring(2, 4);
				case 3:
					if (min == null) {
						min = value.substring(2, 3);
					}
					minute = Integer.valueOf(min);
				case 2:
					h = value.substring(0, 2);
				case 1:
					if (h == null) {
						h = value.substring(0, 1);
					}
					hour = Integer.valueOf(h);

				default:
					break;
				}

			} else {
				// value is fubar'd
				throw new ConversionException();
			}

			if (isPm != null) {

				// need to format to 24h internal val
				if (!isPm && hour == 12) {
					// 12pm == midnight
					hour = 0;
				}

				if (isPm && hour < 12) {
					// 1pm = 13:00
					hour += 12;
				}
			}

			final LocalTime time = LocalTime.MIN.withHour(hour)
					.withMinute(minute).withSecond(second);

			return time;

		} catch (final DateTimeException e) {
			throw new ConversionException(e);
		} catch (final NumberFormatException e) {
			throw new ConversionException(e);
		}
	}

	@Override
	public String convertToPresentation(LocalTime value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (value == null) {
			return null;
		}

		String format;
		if (!field.is24HourClock()) {

			switch (field.getResolution()) {
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
			switch (field.getResolution()) {
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

		final DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		return df.format(value);
	}

	@Override
	public Class<LocalTime> getModelType() {
		return LocalTime.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
}
