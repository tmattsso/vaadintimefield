package org.vaadin.thomas.timefield;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Locale;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.Converter;

public class TimeTextField extends AbstractTextTimeField<LocalTime> {

	private static final long serialVersionUID = 9082508899983202854L;

	private final ObjectProperty<LocalTime> fieldProp = new ObjectProperty<LocalTime>(
			LocalTime.MIN);

	public TimeTextField() {

		super();

		field.setConverter(new Converter<String, LocalTime>() {

			private static final long serialVersionUID = 837077650472730640L;

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

				// TODO am/pm

				try {
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
				return value == null ? null : value.toString();
			}

			@Override
			public Class<LocalTime> getModelType() {
				return LocalTime.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}

		});
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

	public TimeTextField(String string) {
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

}
